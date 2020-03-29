package cn.phlos.paycode.ask.strategy.impl;

import cn.phlos.constant.PayChannelConstant;
import cn.phlos.constant.PayConstant;
import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.mapper.entity.PaymentChannelEntity;
import cn.phlos.paycode.ask.strategy.PayStrategy;
import cn.phlos.paycode.callback.template.AbstractPayCallbackTemplate;
import cn.phlos.paycode.log.AbstractPayment;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.http.HttpClientUtils;
import cn.unionpay.acp.sdk.AcpService;
import cn.unionpay.acp.sdk.LogUtil;
import cn.unionpay.acp.sdk.SDKConfig;
import cn.unionpay.acp.sdk.UnionPayBase;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UnionPayStrategy extends AbstractPayment implements PayStrategy {


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public String toPayHtml(PaymentChannelEntity paymentChannel, PaymentTransacDTO paymentTransacDTO) {
        log.info(">>>>>>>>银联支付组装参数开始<<<<<<<<<<<<");

        Map<String, String> requestData = new HashMap<String, String>();

        /*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
        requestData.put("version", UnionPayBase.version); // 版本号，全渠道默认值
        requestData.put("encoding", UnionPayBase.encoding); // 字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); // 签名方法
        requestData.put("txnType", "01"); // 交易类型 ，01：消费
        requestData.put("txnSubType", "01"); // 交易子类型， 01：自助消费
        requestData.put("bizType", "000201"); // 业务类型，B2C网关支付，手机wap支付
        requestData.put("channelType", "07"); // 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板
        // 08：手机

        /*** 商户接入参数 ***/
        String merchantId = paymentChannel.getMerchantId();
        requestData.put("merId", merchantId); // 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put("accessType", "0"); // 接入类型，0：直连商户
        String paymentId = paymentTransacDTO.getPaymentId();
        // 在微服务电商项目中 订单系统(orderId)   支付系统 支付id
        requestData.put("orderId", paymentId); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put("txnTime", format(paymentTransacDTO.getCreatedTime())); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put("currencyCode", "156"); // 交易币种（境内商户一般是156 人民币）
        Long payAmount = paymentTransacDTO.getPayAmount();
        requestData.put("txnAmt", payAmount + ""); // 交易金额，单位分，不要带小数点
        // requestData.put("reqReserved", "透传字段");
        // //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。

        //商品名称 或者商品描述
        requestData.put("riskRateInfo", "{commodityName=测试商品名称}");

        //
        // 前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        // 如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        // 异步通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知\
        String syncUrl = paymentChannel.getSyncUrl();
        requestData.put("frontUrl", syncUrl);

        // 后台通知地址（需设置为【外网】能访问 http
        // https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        // 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
        // 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可 3.收单后台通知后需要10秒内返回http200或302状态码
        // 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        // 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
        // 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        String asynUrl = paymentChannel.getAsynUrl();
        requestData.put("backUrl", asynUrl);

        // 订单超时时间。
        // 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。
        // 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
        // 此时间建议取支付时的北京时间加15分钟。
        // 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
        requestData.put("payTimeout",
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));

        //////////////////////////////////////////////////
        //
        // 报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
        //
        //////////////////////////////////////////////////

        /** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
        Map<String, String> submitFromData = AcpService.sign(requestData, UnionPayBase.encoding); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, UnionPayBase.encoding); // 生成自动跳转的Html表单

        LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
        // 将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
        return html;
    }

    @Override
    public BaseResponse<JSONObject> refund(PaymentChannelEntity pymentChannel, PaymentTransacDTO paymentTransacDTO) {

        //生成

        Map<String, String> data = new HashMap<String, String>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", UnionPayBase.version);               //版本号
        data.put("encoding", UnionPayBase.encoding);             //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "04");                           //交易类型 04-退货		
        data.put("txnSubType", "00");                        //交易子类型  默认00		
        data.put("bizType", "000201");                       //业务类型 B2C网关支付，手机wap支付	
        data.put("channelType", "07");                       //渠道类型，07-PC，08-手机		

        /***商户接入参数***/
        data.put("merId", pymentChannel.getMerchantId());                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改		
        String paymentId = paymentTransacDTO.getPaymentId();
        data.put("orderId", paymentId);          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        data.put("txnTime", format(paymentTransacDTO.getCreatedTime()));      //订单发送时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
        data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）		
        data.put("txnAmt", paymentTransacDTO.getPayAmount()+"");                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
        //data.put("reqReserved", "透传信息");                  //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。		
        data.put("backUrl", pymentChannel.getAsynUrl());               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知

        /***要调通交易以下字段必须修改***/
        data.put("origQryId", paymentTransacDTO.getTradeNo());      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
        Map<String, String> reqData  = AcpService.sign(data,UnionPayBase.encoding);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getBackRequestUrl();//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl

        Map<String, String> rspData = AcpService.post(reqData,url,UnionPayBase.encoding);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if(rspData.isEmpty()){
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            return null;
        }
        if(!AcpService.validate(rspData, UnionPayBase.encoding)){
            LogUtil.writeErrorLog("验证签名失败");
            return null;
        }

        LogUtil.writeLog("验证签名成功");
        String respCode = rspData.get("respCode");
        if(!("00".equals(respCode) || "03".equals(respCode)|| "04".equals(respCode)|| "05".equals(respCode))){
            //其他应答码为失败请排查原因
            //TODO
            // return setResultError("应答码为失败请排查原因");
        }
        payLog(paymentId,rspData);
        if("00".equals(respCode)){
            //交易已受理，等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
            //TODO
            //记录交易信息

            //更新订单的信息
            examinePaymentTransaction(paymentId,PayConstant.PAY_STATUS_DELETE,null,reqData, PayChannelConstant.YINLIAN_PAY);

        }

        if("03".equals(respCode)|| "04".equals(respCode)|| "05".equals(respCode)){
            //后续需发起交易状态查询交易确定交易状态
            //TODO
        }
        /*if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, UnionPayBase.encoding)){
                LogUtil.writeLog("验证签名成功");
                //String respCode = rspData.get("respCode");
                if("00".equals(respCode)){
                    //交易已受理，等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    //TODO


                }else if("03".equals(respCode)||
                        "04".equals(respCode)||
                        "05".equals(respCode)){
                    //后续需发起交易状态查询交易确定交易状态
                    //TODO
                }else{
                    //其他应答码为失败请排查原因
                    //TODO
                   // return setResultError("应答码为失败请排查原因");
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因
                //return setResultError("验证签名失败");
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            //return setResultError("验证签名失败");
        }*/
       /* //新增退款的字段
        rspData.put("payStatus","refund");
        //调用接口更新交易信息--异步
        String post = HttpClientUtils.doPost(pymentChannel.getSyncUrl(),rspData);*/
       // threadPoolTaskExecutor.execute(new UnionPayStrategy.SynCallbackThread(pymentChannel.getSyncUrl(),rspData));
//        String reqMessage = UnionPayBase.genHtmlResult(reqData);
       //String rspMessage = UnionPayBase.genHtmlResult(rspData);
        //resp.getWriter().write("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
//        return setResultSuccess(PayConstant.YINLIAN_RESULT_SUCCESS);
        return null;
    }

    private String format(Date timeDate) {
        String date = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(timeDate);
        return date;
    }

    @Data
    //生成全参数构造函数
    @AllArgsConstructor
    class SynCallbackThread implements Runnable{

        private String url;
        private Map<String, String> rspData;


        @Override
        public void run() {
            String post = HttpClientUtils.doPost(url, rspData);
            log.info("异步调用银联退款接口{}"+post);
        }
    }

}
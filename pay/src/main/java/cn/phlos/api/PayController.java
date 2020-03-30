package cn.phlos.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.phlos.constant.PayConstant;
import cn.phlos.dto.input.PayCreateTokenDto;
import cn.phlos.dto.out.PaymentTransacDTO;
import cn.phlos.dto.out.PaymentChannelDTO;
import cn.phlos.service.PayContextService;
import cn.phlos.service.PayCreateTokenService;
import cn.phlos.service.PaymentChannelService;
import cn.phlos.service.PaymentTransacInfoService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import cn.phlos.util.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 
 * 
 * 
 * @description: 支付网站
 */
@Controller
public class PayController extends BaseApiService<JSONObject> {
	@Autowired
	private PaymentTransacInfoService payMentTransacInfoService;
	@Autowired
	private PaymentChannelService paymentChannelService;
	@Autowired
	private PayCreateTokenService payCreateTokenService;
	@Autowired
	private PayContextService payContextService;

	@RequestMapping("/pay")
	public String pay(String payToken, Model model) {
		// 1.验证payToken参数
		if (StringUtils.isEmpty(payToken)) {
			setErrorMsg(model, "支付令牌不能为空!");
			return ERROR_500_FTL;
		}
		// 2.使用payToken查询支付信息
		BaseResponse<PaymentTransacDTO> tokenByPayMentTransac = payMentTransacInfoService.tokenByPayMentTransac(payToken);
		if (!isSuccess(tokenByPayMentTransac)) {
			setErrorMsg(model, tokenByPayMentTransac.getMsg());
			return ERROR_500_FTL;
		}
		// 3.查询支付信息
		PaymentTransacDTO data = tokenByPayMentTransac.getData();
		model.addAttribute("data", data);
		// 4.查询渠道信息
		List<PaymentChannelDTO> paymentChanneList = paymentChannelService.selectAll();
		model.addAttribute("paymentChanneList", paymentChanneList);
		model.addAttribute("payToken", payToken);
		return "index";
	}

	@GetMapping("/refund")
	@ResponseBody
	public BaseResponse<JSONObject> refund(String paymentId){
		BaseResponse<JSONObject> refund = payContextService.refund(paymentId);
		if(!isSuccess(refund)){
			return setResultError(refund.getMsg());
		}
		return refund;
	}

	/**
	 * 
	 * @param payToken
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/payHtml")
	public void payHtml(String channelId, String payToken, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		BaseResponse<JSONObject> payHtmlData = payContextService.toPayHtml(channelId, payToken);
		if (isSuccess(payHtmlData)) {
			JSONObject data = payHtmlData.getData();
			String payHtml = data.getString("payHtml");
			response.getWriter().print(payHtml);
		}

	}

	/**
	 * 创建交易的token
	 * @param orderId
	 * @param payAmount
	 * @param userId
	 * @return
	 */
	@GetMapping("/testCreatePayToken")
	public String testCreatePayToken(String orderId, Long payAmount , Long userId){
		PayCreateTokenDto payCreateTokenDto = new PayCreateTokenDto(payAmount,orderId,userId);
		System.out.println("payCreateTokenDto = " + payCreateTokenDto);
		BaseResponse<JSONObject> payToken = payCreateTokenService.createPayToken(payCreateTokenDto);
		if (!isSuccess(payToken)){
			return ERROR_500_FTL;
		}
		JSONObject data = payToken.getData();
		return "redirect:/pay?payToken="+data.get("token");
	}

	/**
	 * 500页面
	 */
	protected static final String ERROR_500_FTL = "500";

	// 接口直接返回true 或者false
	public Boolean isSuccess(BaseResponse<?> baseResp) {
		if (baseResp == null) {
			return false;
		}
		if (baseResp.getCode().equals(Constants.HTTP_RES_CODE_500)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取浏览器信息
	 *
	 * @return
	 */
//	public String webBrowserInfo(HttpServletRequest request) {
//		// 获取浏览器信息
//		Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
//		// 获取浏览器版本号
//		Version version = browser.getVersion(request.getHeader("User-Agent"));
//		String info = browser.getName() + "/" + version.getVersion();
//		return info;
//	}

	public void setErrorMsg(Model model, String errorMsg) {
		model.addAttribute("error", errorMsg);

	}



}

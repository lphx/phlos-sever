package cn.phlos.paycode.callback;

import cn.phlos.paycode.callback.template.AbstractPayCallbackTemplate;
import cn.phlos.paycode.callback.template.factory.TemplateFactory;
import com.alipay.api.AlipayApiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PayAsynCallbackService {
    private static final String UNIONPAYCALLBACK_TEMPLATE = "unionPayCallbackTemplate";
    private static final String ALIPAYCALLBACK_TEMPLATE = "aliPayCallbackTemplate";

    /**
     * 银联异步回调接口执行代码
     *
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping("/unionPayAsynCallback")
    public String unionPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) throws AlipayApiException {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory.getPayCallbackTemplate(UNIONPAYCALLBACK_TEMPLATE);
        //abstractPayCallbackTemplate.getPayParameter(UNIONPAYCALLBACK_TEMPLATE);
        return abstractPayCallbackTemplate.asyncCallBack(req, resp);
    }

    @RequestMapping("/aliPayAsynCallback")
    public String aliPayAsynCallback(HttpServletRequest req, HttpServletResponse resp) throws AlipayApiException {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory.getPayCallbackTemplate(ALIPAYCALLBACK_TEMPLATE);
        //abstractPayCallbackTemplate.getPayParameter(UNIONPAYCALLBACK_TEMPLATE);
        return abstractPayCallbackTemplate.asyncCallBack(req, resp);
    }

   /* @RequestMapping("/unionPayAsynrRefund")
    public String unionPayAsynrRefund(HttpServletRequest req, HttpServletResponse resp) {
        AbstractPayCallbackTemplate abstractPayCallbackTemplate = TemplateFactory.getPayCallbackTemplate(UNIONPAYCALLBACK_TEMPLATE);
        //abstractPayCallbackTemplate.getPayParameter(UNIONPAYCALLBACK_TEMPLATE);
        return abstractPayCallbackTemplate.refundCallBack(req, resp);
    }*/



}
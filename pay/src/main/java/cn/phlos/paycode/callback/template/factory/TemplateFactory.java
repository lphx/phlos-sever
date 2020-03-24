package cn.phlos.paycode.callback.template.factory;

import cn.phlos.paycode.callback.template.AbstractPayCallbackTemplate;
import cn.phlos.util.core.utils.SpringContextUtil;

/**
 * 获取具体实现的模版工厂方案
 */
public class TemplateFactory {

    public static AbstractPayCallbackTemplate getPayCallbackTemplate(String beanName){
        return (AbstractPayCallbackTemplate)SpringContextUtil.getBean(beanName);
    }

}

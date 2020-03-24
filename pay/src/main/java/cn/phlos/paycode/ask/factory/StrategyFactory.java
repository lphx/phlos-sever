package cn.phlos.paycode.ask.factory;

import cn.phlos.paycode.ask.strategy.PayStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化不同的策略行为
 */
public class StrategyFactory {

    private static Map<String, PayStrategy> strategyBean = new ConcurrentHashMap<String, PayStrategy>();

    // 思考几个点：
    public static PayStrategy getPayStrategy(String classAddres) {

       try{
           if (StringUtils.isEmpty(classAddres)){
               return null;
           }
           PayStrategy payStrategy = strategyBean.get(classAddres);
           if ( payStrategy != null){
               return payStrategy;
           }
            //使用Java反射机制初始化子类
           Class<?> className = Class.forName(classAddres);
           // 2.反射机制初始化对象
           PayStrategy payStrategyInstance = (PayStrategy)className.newInstance();
           strategyBean.put(classAddres,payStrategyInstance);
           return payStrategyInstance;
       }catch (Exception e){
            return  null;
       }

    }

}

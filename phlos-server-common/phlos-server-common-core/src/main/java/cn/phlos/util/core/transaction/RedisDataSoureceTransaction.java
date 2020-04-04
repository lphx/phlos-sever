package cn.phlos.util.core.transaction;

import cn.phlos.util.core.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * Redis与 DataSource 事务封装
 *
 * @Author: Penghong Li
 * @Date: Create in 22:27 2020/4/4
 */

@Component
@Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
public class RedisDataSoureceTransaction {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 数据源事务管理器
     */
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 开始事务 采用默认传播行为
     *
     * @return
     */
    public TransactionStatus begin() {
        // 手动begin数据库事务
        // 1.开启数据库的事务 事务传播行为
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
        // 2.开启redis事务
        redisUtil.begin();
        return transaction;
    }

    /**
     * 提交事务
     *
     * @param transactionStatus
     *            事务传播行为
     * @throws Exception
     */
    public void commit(TransactionStatus transactionStatus) throws Exception {
        if (transactionStatus == null) {
            throw new Exception("transactionStatus is null");
        }
        // 支持Redis与数据库事务同时提交
        dataSourceTransactionManager.commit(transactionStatus);
    }

    /**
     * 回滚事务
     *
     * @param transactionStatus
     * @throws Exception
     */
    public void rollback(TransactionStatus transactionStatus) throws Exception {
        if (transactionStatus == null) {
            throw new Exception("transactionStatus is null");
        }
        // 1.回滚数据库事务 redis事务和数据库的事务同时回滚
        dataSourceTransactionManager.rollback(transactionStatus);
        // // 2.回滚redis事务
        redisUtil.discard();
    }
    // 如果redis的值与数据库的值保持不一致话


}

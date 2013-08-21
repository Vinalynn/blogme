package org.vinalynn.wapp.wmblog.service.interceptor;


import com.google.appengine.api.datastore.Transaction;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.vinalynn.wapp.wmblog.util.GoogleDataStoreUtil;

import java.lang.reflect.Method;
import java.util.HashMap;


/**
 * User: caiwm
 * Date: 13-8-21
 * Time: 上午11:36
 */
public class SvTransactionInterceptor implements MethodInterceptor {
    private static Logger log = Logger.getLogger(SvTransactionInterceptor.class);
    private static ThreadLocal<HashMap<String, Object>> tl = new ThreadLocal<HashMap<String, Object>>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Transaction tx = null;
        HashMap<String, Object> tlMap = tl.get();
        String _tx_u_id = String.valueOf(invocation.hashCode());
        try {
            //在服务注入的时候会调用target的toString方法，
            //这个地方对toString方法做次例外处理
            Method method = invocation.getMethod();
            if (!StringUtils.equals("toString", method.getName())) {
                if (null == tlMap) {
                    tlMap = new HashMap<String, Object>();
                    tl.set(tlMap);
                }
                tx = (Transaction) tlMap.get("tx");
                if (null == tx) {
                    tx = GoogleDataStoreUtil.beginTransaction();
                    tlMap.put("tx", tx);
                    //把当前事务打上标识，使用当前线程和invocation的HashCode
                    tlMap.put("tx-u-id", _tx_u_id);
                    log.info("No transaction exists in current thread. " +
                            "Build a new transaction[" + tx.getId() + "]");
                } else {
                    log.info("A transaction[" + tx.getId() + "] exists in current thread, join in immediately");
                }
            }
            Object obj = invocation.proceed();
            if (null != tx && StringUtils.equals(
                    _tx_u_id, String.valueOf(tlMap.get("tx-u-id"))
            )) {
                tx.commit();
                if (log.isInfoEnabled()) {
                    log.info("transaction[" + tx.getId() + "] commited!");
                }
                tl.remove();
            }
            return obj;

        }
        finally {
            if (null != tx && tx.isActive() && StringUtils.equals(
                    _tx_u_id, String.valueOf(tlMap.get("tx-u-id")))) {
                tx.rollback();
                log.error("error appeared, rollback transaction[" + tx.getId() + "]");
            }
        }
    }
}

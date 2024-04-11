package jmu.lsk.utils.thread;

import jmu.lsk.model.common.user.pojos.ApUser;
import jmu.lsk.model.common.wemedia.pojos.WmUser;
import org.apache.commons.lang3.LocaleUtils;

public class AppThreadLocalUtil {
    private final static ThreadLocal<ApUser> AP_USER_THREAD_LOCAL = new ThreadLocal<>();
    /**
     * 添加用户
     * @param apUser
     */
    public static void  setUser(ApUser apUser){
        AP_USER_THREAD_LOCAL.set(apUser);
    }

    /**
     * 获取用户
     */
    public static ApUser getUser(){
        return AP_USER_THREAD_LOCAL.get();
    }

    /**
     * 清理用户
     */
    public static void clear(){
        AP_USER_THREAD_LOCAL.remove();
    }
}

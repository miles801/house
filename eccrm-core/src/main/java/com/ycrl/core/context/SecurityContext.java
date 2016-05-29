package com.ycrl.core.context;

/**
 * 安全上下文
 * Created by Michael on 2014/10/19.
 */
public class SecurityContext {
    /**
     * 登录信息
     */
    private static ThreadLocal<Login> _login = new ThreadLocal<Login>();


    /**
     * 全部移除
     */
    public static void remove() {
        _login.remove();
    }

    public static void set(Login login) {
        _login.set(login);
    }

    public static Login get() {
        Login login = _login.get();
        if (login == null) {
            login = new Login();
        }
        return login;
    }

    public static String getEmpId() {
        return get().getEmpId();
    }

    public static String getEmpCode() {
        return get().getEmpCode();
    }

    public static String getOrgId() {
        return get().getEmpName();
    }

    public static String getEmpName() {
        return get().getEmpName();
    }

    public static String getOrgName() {
        return get().getOrgName();
    }

}

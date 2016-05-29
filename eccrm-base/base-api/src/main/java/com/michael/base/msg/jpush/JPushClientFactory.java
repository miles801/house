package com.michael.base.msg.jpush;

import cn.jpush.api.JPushClient;

/**
 * 获得JPushClient对象的工具类
 *
 * @author Michael
 */
public class JPushClientFactory {
    private static JPushClientFactory ourInstance = new JPushClientFactory();

    public static JPushClientFactory getInstance() {
        return ourInstance;
    }

    private JPushClient jpush = null;

    private JPushClientFactory() {
        jpush = new JPushClient("786458315829782515835f94", "65f98e8c59be7a54be9445f0");
    }

    public JPushClient getJPushClient() {
        return this.jpush;
    }
}

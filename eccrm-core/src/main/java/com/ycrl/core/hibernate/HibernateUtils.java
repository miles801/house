package com.ycrl.core.hibernate;

import com.ycrl.core.SystemContainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.id.Assigned;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.tuple.IdentifierProperty;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

/**
 * @author Michael
 */
public class HibernateUtils {
    /**
     * 将指定实体类的ID属性生成策略变成Assigned
     *
     * @param session Session
     * @param clazz   实体类类型
     */
    public static void setIDAssigned(Session session, Class<?> clazz) {
        SingleTableEntityPersister classMetadata = (SingleTableEntityPersister) session.getSessionFactory().getClassMetadata(clazz);
        IdentifierProperty identifierProperty = classMetadata.getEntityMetamodel().getIdentifierProperty();
        Field field = null;
        try {
            field = identifierProperty.getClass().getDeclaredField("identifierGenerator");
            field.setAccessible(true);
            field.set(identifierProperty, new Assigned());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前环境中的session，并将指定的对象从session中驱逐
     *
     * @param object 要被驱逐的对象
     */
    public static void evict(Object object) {
        Assert.notNull(object, "操作失败!要被“驱逐”的对象不能为空!");
        // 获取session对象
        SessionFactory sessionFactory = (SessionFactory) SystemContainer.getInstance().getBean("sessionFactory");
        Assert.notNull(sessionFactory, "操作失败!在获取sessionFactory对象时失败!");
        Session session = sessionFactory.getCurrentSession();
        Assert.notNull(session, "操作失败!当前的SessionFactory中并没有创建Session对象!");
        // 驱逐
        session.evict(object);
    }
}

package eccrm.base.attachment.utils;

import java.lang.reflect.Method;

/**
 * Created by admin on 2016/4/6.
 */
public class ReflectTest
{
    public static void main(String[] args)
    {

        try
        {
            Class c1 = Class.forName("eccrm.base.attachment.utils.ReflectTest");
            Method[] methods = c1.getDeclaredMethods();
            for (Method m : methods)
            {
                System.out.println(m.getName());
                //invokeMethod(new ReflectTest(), m.getName(), new Object[]{"str", 1});
            }

        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception
    {

        Class ownerClass = owner.getClass();

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++)
        {
            argsClass[i] = args[i].getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(owner, args);
    }

    public static Object invokeMethod(Object owner, String methodName) throws Exception
    {
        Class ownerClass = owner.getClass();

        Method method = ownerClass.getMethod(methodName, null);

        return method.invoke(owner, new Object[]{});
    }

    public int getInt()
    {
        return 1;
    }

    public boolean getBool()
    {
        return false;
    }

    public String getString()
    {
        return "hello word~";
    }

    public static boolean isNulll(Object obj)
    {
        return obj == null || "".equals(obj);
    }
}

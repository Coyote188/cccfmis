package net.modellite.spring;

import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BeanAdapter
{
	private static ApplicationContext	context;
	public static Object getBean(String arg)
	{
		if (context == null)
			return null;
		return context.getBean(arg);
	}
	public static <T> T getBean(String arg, Class<T> iface)
	{
		if (context == null)
			return null;
		Object obj = context.getBean(arg);
		if (obj == null || iface == null)
			return null;
		if (iface.isAssignableFrom(obj.getClass()))
			return iface.cast(obj);
		throw new ClassCastException(obj.getClass().getName() + " Cast to " + iface.getName());
	}
	public static void registerContext(ServletContext servletContext)
	{
		synchronized (servletContext)
		{
			BeanAdapter.context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		}
	}
}

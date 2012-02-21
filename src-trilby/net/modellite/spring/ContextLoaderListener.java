package net.modellite.spring;

import javax.servlet.ServletContextEvent;

public class ContextLoaderListener
	extends org.springframework.web.context.ContextLoaderListener
{
	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		super.contextDestroyed(event);
	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		super.contextInitialized(event);
		BeanAdapter.registerContext(event.getServletContext());
	}
}

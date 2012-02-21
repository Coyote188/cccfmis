package net.modellite.web.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class NativeServlet
	extends javax.servlet.GenericServlet
{
	private static final long	serialVersionUID	= 6912881368176952108L;
	private Properties			initProps;

	@Override
	public void destroy()
	{
		initProps.clear();
		super.destroy();
	}

	public final String getInitProperty(String name)
	{
		return initProps.getProperty(name);
	}

	public final Properties getInitProperties()
	{
		return initProps;
	}

	public final String getInitContextProperty(String name)
	{
		if (name == null || name.trim().isEmpty()) return null;
		ServletContext ctx = getServletContext();
		return ctx.getInitParameter(name);
	}

	public final Object getServletContext(String name)
	{
		if (name == null || name.trim().isEmpty()) return null;
		ServletContext ctx = getServletContext();
		synchronized (ctx)
		{
			return ctx.getAttribute(name);
		}
	}

	public final synchronized void setServletContext(String name, Object value)
	{
		if (name == null || name.trim().isEmpty()) return;
		ServletContext ctx = getServletContext();
		if (value == null)
		{
			ctx.removeAttribute(name);
			return;
		}
		ctx.setAttribute(name, value);
	}

	public final <T> T getServletContext(String name, Class<T> clx)
	{
		if (name == null || name.trim().isEmpty()) return null;
		ServletContext ctx = getServletContext();
		Object val;
		synchronized (ctx)
		{
			val = ctx.getAttribute(name);
		}
		if (val == null) return null;
		if (clx.isAssignableFrom(val.getClass())) return clx.cast(val);
		throw new ClassCastException("" + val.getClass().getCanonicalName() + " can`t cast to " + clx.getCanonicalName());
	}

	@Override
	public final void init(ServletConfig config)
		throws ServletException
	{
		super.init(config);
	}

	@Override
	public final void init()
		throws ServletException
	{
		super.init();
		ServletConfig cfg = getServletConfig();
		initProps = new Properties();
		Enumeration<?> names = cfg.getInitParameterNames();
		Properties props = new Properties();
		while (names.hasMoreElements())
		{
			String name = (String) names.nextElement();
			if (name == null || name.isEmpty()) continue;
			props.put(name, cfg.getInitParameter(name));
		}
		initialize();
	}

	protected abstract void initialize()
		throws ServletException;

	protected abstract void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException;

	@Override
	public final void service(ServletRequest req, ServletResponse resp)
		throws ServletException, IOException
	{
		this.service((HttpServletRequest) req, (HttpServletResponse) resp);
	}
}

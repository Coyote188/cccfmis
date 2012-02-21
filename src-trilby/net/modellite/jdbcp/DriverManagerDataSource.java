package net.modellite.jdbcp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DriverManagerDataSource
	implements javax.sql.DataSource
{
	public DriverManagerDataSource()
	{}
	public DriverManagerDataSource(String url, Properties prop)
	{
		this.prop = prop;
		this.url = url;
	}
	private PrintWriter	logout;
	private int			timeout;
	private Properties	prop;

	private String		url;

	{
		timeout = 0;
		prop = new Properties();
	}

	public void destroy()
		throws SQLException
	{
		;
	}

	@Override
	protected void finalize()
		throws Throwable
	{
		destroy();
		super.finalize();
	}

	@Override
	public Connection getConnection()
		throws SQLException
	{
		return getConnection(prop);
	}

	private Connection getConnection(Properties info)
		throws SQLException
	{
		DriverManager.setLoginTimeout(timeout);
		DriverManager.setLogWriter(logout);
		Connection connection = DriverManager.getConnection(url, info);
		return connection;
	}

	@Override
	public Connection getConnection(String username, String password)
		throws SQLException
	{
		Properties info = new Properties();
		info.put("user", username);
		info.put("password", password);
		return getConnection(info);
	}

	@Override
	public int getLoginTimeout()
		throws SQLException
	{
		return timeout;
	}

	@Override
	public PrintWriter getLogWriter()
		throws SQLException
	{
		return logout;
	}

	public Properties getProp()
	{
		return prop;
	}

	public String getUrl()
	{
		return url;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface)
		throws SQLException
	{
		if (iface == null) return false;
		return iface.isAssignableFrom(this.getClass());
	}

	@Override
	public void setLoginTimeout(int seconds)
		throws SQLException
	{
		timeout = seconds;
	}

	@Override
	public void setLogWriter(PrintWriter out)
		throws SQLException
	{
		logout = out;
	}

	public void setProp(Properties prop)
	{
		this.prop = prop;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	public <T> T unwrap(Class<T> iface)
		throws SQLException
	{
		if (iface == null || !isWrapperFor(iface))
			throw new SQLException("DataSource of type [" + getClass().getName() + "] can only be unwrapped as [javax.sql.DataSource], not as [" + iface.getName());
		return iface.cast(this);
	}
}

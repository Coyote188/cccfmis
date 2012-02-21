package net.modellite.jdbcp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
 
public class Driver
	implements java.sql.Driver
{
	private static final boolean	debug			= true;
	protected static final String	URL_PREFIX		= "jdbcp:";
	protected final int				MAJOR_VERSION	= 1;
	protected final int				MINOR_VERSION	= 0;
	protected static Logger			logger;
	protected static Driver			driver;
	//
	static
	{
		logger = Logger.getAnonymousLogger();
		try
		{
			driver = new Driver();
			DriverManager.registerDriver(driver);
			if (debug) logger.info("Register driver net.modellite.jdbcp.Driver");
		} catch (Exception e)
		{
			logger.severe(e.getMessage());
			if (debug) e.printStackTrace();
		}
	}

	public boolean acceptsURL(String url)
		throws SQLException
	{
		if (null == url) return false;
		return url.startsWith(URL_PREFIX);
	}

	public Connection connect(String url, Properties info)
		throws SQLException
	{
		if (!acceptsURL(url)) return null;
		url = url.replaceFirst(URL_PREFIX, "jdbc:");
		return DriverManager.getConnection(url, info);
	}

	protected void finalize()
		throws Throwable
	{
		DriverManager.deregisterDriver(driver);
		logger = null;
		super.finalize();
	}

	public int getMajorVersion()
	{
		return MAJOR_VERSION;
	}

	public int getMinorVersion()
	{
		return MINOR_VERSION;
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
		throws SQLException
	{
		return new DriverPropertyInfo[0];
	}

	public boolean jdbcCompliant()
	{
		return true;
	}
}

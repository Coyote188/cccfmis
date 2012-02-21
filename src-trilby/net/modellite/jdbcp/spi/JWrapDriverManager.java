package net.modellite.jdbcp.spi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import net.modellite.jdbcp.JWrapDriver;

public class JWrapDriverManager
{
	private static boolean				initialized	= false;
	private static Vector<DriverInfo>	drivers		= new Vector<JWrapDriverManager.DriverInfo>();
	private static java.io.PrintWriter	logWriter;
	static
	{
		if (!initialized) initialize();
	}

	private JWrapDriverManager()
	{}

	public static synchronized void deregisterDriver(Driver driver)
	{
		println("JWrapperManager.deregisterDriver:" + driver);
		int i = 0;
		synchronized (drivers)
		{
			DriverInfo di = null;
			for (; i < drivers.size(); i++)
			{
				di = drivers.elementAt(i);
				if (di.driver == driver)
				{
					drivers.remove(di);
					break;
				}
			}
		}
		if (i >= drivers.size())
		{
			println("    couldn't find driver to unload");
			return;
		}
	}

	private static Class<?> getCallerClass(ClassLoader callerClassLoader, String driverClassName)
	{
		Class<?> callerC = null;
		try
		{
			callerC = Class.forName(driverClassName, true, callerClassLoader);
		} catch (Exception ex)
		{
			callerC = null; // being very careful
		}
		return callerC;
	}

	public synchronized static JWrapDriver getJWrapper(Class<?> local, Class<?> jdbc)
	{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		synchronized (DriverManager.class)
		{
			if (!initialized) initialize();
			println("JWrapperManager.getJWrapper(Loacl:" + local.getCanonicalName() + " Jdbc:" + jdbc.getCanonicalName() + ")");
			for (int i = 0; i < drivers.size(); i++)
			{
				DriverInfo di = drivers.elementAt(i);
				if (getCallerClass(loader, di.driverClassName) != di.driverClass)
				{
					println("\tskipping: " + di);
					continue;
				}
				println("\ttrying " + di);
				if (!di.driver.accepts(local, jdbc)) continue;
				println("getDriver returning " + di);
				return di.driver;
			}
		}
		println("getDriver: no suitable driver");
		return null;
	}

	public static java.io.PrintWriter getLogWriter()
	{
		return logWriter;
	}

	static synchronized void initialize()
	{
		if (initialized) return;
		initialized = true;
		loadInitialDrivers();
	}

	private static void loadInitialDrivers()
	{
		synchronized (drivers)
		{
			JDriverService ds = new JDriverService();
			java.security.AccessController.doPrivileged(ds);
		}
		if (drivers.isEmpty()) return;
	}

	public synchronized static void println(String message)
	{
		if (logWriter != null)
		{
			logWriter.println(message);
			logWriter.flush();
		}
	}

	public static void registerDriver(JWrapDriver driver)
	{
		println("JWrapperManager.registerDriver:" + driver);
		if (!initialized) initialize();
		synchronized (drivers)
		{
			DriverInfo di = null;
			for (int i = 0; i < drivers.size(); i++)
			{
				di = drivers.elementAt(i);
				if (di.driver == driver) return;
			}
			di = new DriverInfo();
			di.driverClass = driver.getClass();
			di.driverClassName = driver.getClass().getName();
			di.driver = driver;
			drivers.addElement(di);
		}
	}

	public static void setLogWriter(OutputStream out)
	{
		if (logWriter != null) logWriter.flush();
		if (out != null) logWriter = new PrintWriter(out);
	}

	private static class DriverInfo
	{
		JWrapDriver	driver;
		Class<?>	driverClass;
		String		driverClassName;

		public String toString()
		{
			return ("driver[className=" + driverClassName + "," + driver + "]");
		}
	}

	private static class JDriverService
		implements java.security.PrivilegedAction<JWrapDriver>
	{
		@Override
		public JWrapDriver run()
		{
			Iterator<?> ds = Service.providers();
			if (ds == null) return null;
			while (ds.hasNext())
				ds.next();
			return null;
		}
	}

	private static class Service
	{
		public static Iterator<?> providers()
		{
			Vector<Class<?>> dres = new Vector<Class<?>>();
			try
			{
				println("JWrapperManager.Service.providers:");
				Enumeration<URL> res = ClassLoader.getSystemResources("META-INF/jservices/" + JWrapDriver.class.getCanonicalName());
				while (res.hasMoreElements())
				{
					byte[] buf = new byte[2048];
					int l = 0;
					InputStream in = res.nextElement().openStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					while ((l = in.read(buf, 0, buf.length)) >= 0)
					{
						if (l > 0) out.write(buf, 0, l);
					}
					in.close();
					String str = new String(out.toByteArray());
					out.close();
					String[] strs = str.split("(\r\n)|(\n)");
					for (String clzz : strs)
					{
						if (clzz.isEmpty()) continue;
						if (clzz.startsWith("#"))
						{
							println("\t" + clzz);
							continue;
						}
						if (clzz.isEmpty()) continue;
						println("\tLoad driver:" + clzz);
						int i = clzz.indexOf("#");
						if (i > 0) clzz = clzz.substring(0, i);
						dres.add(getCallerClass(ClassLoader.getSystemClassLoader(), clzz.trim()));
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return dres.iterator();
		}
	}
}

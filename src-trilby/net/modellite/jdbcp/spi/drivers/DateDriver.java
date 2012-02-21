package net.modellite.jdbcp.spi.drivers;

import net.modellite.jdbcp.JWrapDriver;
import net.modellite.jdbcp.spi.JWrapDriverManager;

public class DateDriver
	implements JWrapDriver
{
	static
	{
		JWrapDriverManager.registerDriver(new DateDriver());
	}

	@Override
	public boolean accepts(Class<?> local, Class<?> jdbc)
	{
		if (local == null || jdbc == null) return false;
		return (local == java.util.Date.class && jdbc == java.sql.Date.class);
	}

	@Override
	public <T> T wrap(Object value, Class<T> clz)
	{
		if (value == null) return null;
		if (clz != null)
		{
			if (clz == java.sql.Date.class)
			{
				return clz.cast(new java.sql.Date(java.util.Date.class.cast(value).getTime()));
			} else if (clz == java.util.Date.class) { return clz.cast(java.util.Date.class.cast(value)); }
		}
		throw new ClassCastException("DateDriver.wrap:" + value.getClass().getCanonicalName());
	}
}

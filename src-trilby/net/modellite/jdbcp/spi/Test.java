package net.modellite.jdbcp.spi;

import java.util.Properties;
import net.modellite.jdbcp.DriverManagerDataSource;
import net.modellite.jdbcp.JEntityAdapter;

public class Test
{
	/**
	 * @param args
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static void main(String[] args)
		throws Exception
	{
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:mysql://localhost:3308/modellite", prop);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(dataSource, "prot_log", "where prot_user=?");
		adapter.load(1);
		
		//adapter.removes(values2);
	}
}

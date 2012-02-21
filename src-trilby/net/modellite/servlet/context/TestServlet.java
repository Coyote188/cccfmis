package net.modellite.servlet.context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import net.modellite.web.servlet.NativeServlet;

public class TestServlet
	extends NativeServlet
{
	private static final long	serialVersionUID	= -3147677648122991752L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		if (source != null)
		{
			try
			{
				JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "protocol_user");
				System.out.println("---原始的-----");
				List<Map<String, Object>> entities = adapter.load();
				//
				for (Map<String, Object> entity : entities)
					System.out.println(entity.get("prot_key") + ":" + entity.get("prot_login_key") + ":" + entity.get("accept_host"));
				Map<String, Object> entity = entities.get(2);
				Map<String, Object> entity2 = entities.get(1);
				entity.put("prot_key", "no444");
				entity.put("prot_login_key", "user3");
				adapter.update(entity);
				//
				System.out.println("---更新后-----");
				List<Map<String, Object>> entities2 = adapter.load();
				for (Map<String, Object> entity1 : entities2)
					System.out.println(entity1.get("prot_key") + ":" + entity1.get("prot_login_key") + ":" + entity1.get("accept_host"));
				//
				entity.put("prot_key", "555555");
				adapter.remove(entity);
				// entity=adapter.newEntity();
				System.out.println("---移除后-----");
				List<Map<String, Object>> entities3 = adapter.load();
				for (Map<String, Object> entity1 : entities3)
					System.out.println(entity1.get("prot_key") + ":" + entity1.get("prot_login_key") + ":" + entity1.get("accept_host"));
				entity.put("prot_key", "no333");
				entity.put("prot_login_key", "国文");
				System.out.println("---提交后-----");
				List<Map<String, Object>> ss = new Vector<Map<String, Object>>();
				entity2.put("prot_login_key", "44444422");
				ss.add(entity);
				ss.add(entity2);
				adapter.commits(ss);
				List<Map<String, Object>> entities4 = adapter.load();
				for (Map<String, Object> entity1 : entities4)
					System.out.println(entity1.get("prot_key") + ":" + entity1.get("prot_login_key") + ":" + entity1.get("accept_host"));
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void initialize()
		throws ServletException
	{
		// TODO Auto-generated method stub
	}
}

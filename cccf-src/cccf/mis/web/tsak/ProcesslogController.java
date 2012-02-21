package cccf.mis.web.tsak;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class ProcesslogController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3736006636018374469L;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		DataSource ds = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		try
		{
			String sioid = (String)Executions.getCurrent().getParameter("sioid");
			System.out.println("sioid" + sioid);
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterBySQL(ds, "select * from processlog_view where sioid = '"+sioid+"'");
			List<Map<String, Object>> logs = adapter.load();
			if(logs!=null && logs.size()>1){
				Executions.getCurrent().setAttribute("lastlog", logs.get(1));
			}
			Executions.getCurrent().setAttribute("logs", logs);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
}

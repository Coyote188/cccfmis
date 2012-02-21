package cccfmis.gcjc.zk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericComposer;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;

public class ApplyGcjcPcApplySp
		extends GenericComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		try
		{// arg.getParent()
			List<?> applys = ApplicationInfoServiceUtil.getfactoryInspection("must");
			// applys.get(0).getProduction().getProductName()
			// System.out.println(applys.size());
			page.setAttribute("applys", applys);
			page.setAttribute("applysel", new ArrayList<ApplicationInfo>());
			JEntityAdapter adapter2 = JAdapterFactry.createJEntityAdapterBySQL(source, "select organization from _factorycheckuser group by organization order by id asc");
			page.setAttribute("fckgruop", adapter2.load());
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_gcjc","spr =?");
			page.setAttribute("gcjcs", adapter.load("已提交"));
			JEntityAdapter adapter3 = JAdapterFactry.createJEntityAdapterByTable(source, "_fckdlect", "pc=?");
			page.setAttribute("fckgruopsl", adapter3.load(-1));
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
}

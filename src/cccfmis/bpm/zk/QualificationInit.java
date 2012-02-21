package cccfmis.bpm.zk;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;
import org.zkoss.zkplus.databind.AnnotateDataBinderInit;

public class QualificationInit extends AnnotateDataBinderInit {
	DataSource source;
	JEntityAdapter adapter;

	public void doAfterCompose(Page page, org.zkoss.zk.ui.Component[] comps) throws Exception {
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		try {
			adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factoryqualification");
			List<Map<String, Object>> list = adapter.load();
			page.setAttribute("qualificationInit", list);
			page.setAttribute("map", new HashMap<String, Object>());
			super.doAfterCompose(page, comps);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}

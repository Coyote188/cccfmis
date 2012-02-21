package cccfmis.bpm.zk;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class FactoryUserEditInit implements Initiator {
	@Override
	public void doAfterCompose(Page arg0) throws Exception {
	}

	@Override
	public boolean doCatch(Throwable arg0) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doFinally() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doInit(Page arg0, Map arg1) throws Exception {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) Executions.getCurrent().getArg();
		// TODO Auto-generated method stub
		arg0.setAttribute("map", map);

		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factoryqualification");
		List<Map<String, Object>> list = adapter.load();
		arg0.setAttribute("qualificationInit", list);
	}

}

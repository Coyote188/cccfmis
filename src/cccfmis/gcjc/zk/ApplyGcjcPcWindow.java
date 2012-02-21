package cccfmis.gcjc.zk;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;

/**
 * @author Administrator
 */
public class ApplyGcjcPcWindow extends GenericComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3941077436683928267L;
	private Component comp;
	private DataSource source;
	private Listbox appList, pclist;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.comp = comp;
		appList = (Listbox) this.comp.getFellow("appList");
		pclist = (Listbox) this.comp.getFellow("pclist");
	}

	public ComponentInfo doBeforeCompose(org.zkoss.zk.ui.Page page, org.zkoss.zk.ui.Component parent, org.zkoss.zk.ui.metainfo.ComponentInfo compInfo) {
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		try {
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "fig_batch", "state='待审批'");
			List batchlist = adapter.load();
			page.setAttribute("batchlist", batchlist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}

	public void onPcSelect(SelectEvent event) {
		BigInteger batchid = (BigInteger) pclist.getSelectedItem().getValue();
		List<ApplicationInfo> list = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where batchid=" + batchid + "");
		if(list.isEmpty())
			return;
		ListModelList model = new ListModelList(list);
		appList.setModel(model);
	}

	public void onPass() throws SQLException {
		BigInteger batchid = (BigInteger) pclist.getSelectedItem().getValue();
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "fig_batch", "id=" + batchid + "");
		Map map = adapter.loadByPrimaryKey(batchid);
		map.put("state", "审批通过");
		adapter.commit(map);
		JEntityAdapter adapter1 = JAdapterFactry.createJEntityAdapterByTable(source, "fig_batch", "state='待审批'");
		List batchlist = adapter1.load();
		ListModelList model = new ListModelList(batchlist);
		pclist.setModel(model);
		if (pclist.getItems().size() > 0) {
			pclist.setSelectedIndex(0);
		}
		else{
			List<ApplicationInfo> list = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where 1!=1");
			ListModelList model2 = new ListModelList(list);
			appList.setModel(model2);
		}
	}
}
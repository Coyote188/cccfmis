package cccfmis.bpm.zk;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class Qualification extends Window {
	private Map<String, Object> map = new HashMap<String, Object>();
	Textbox qcode, qname, qdesc;
	Listbox listbox;
	DataSource source;
	boolean isEdit = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void onCreate() {
		qcode = (Textbox) getFellow("qcode");
		qname = (Textbox) getFellow("qname");
		qdesc = (Textbox) getFellow("qdesc");
		listbox = (Listbox) getFellow("listbox1");
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
	}

	@SuppressWarnings("unchecked")
	public void onSelected(SelectEvent event) {
		if (event.getTarget() instanceof Listbox) {
			Listitem listitem = (Listitem) event.getSelectedItems().toArray()[0];
			map = (Map<String, Object>) listitem.getValue();
			qcode.setValue(map.get("qcode").toString());
			qname.setValue(map.get("qname").toString());
			qdesc.setValue(map.get("qdesc").toString());
			isEdit = true;
		} else {
			System.out.println("-------" + event.getTarget().getClass());
		}
	}

	public void onAdd() {
		map = null;
		qcode.setValue(null);
		qname.setValue(null);
		qdesc.setValue(null);
		listbox.setSelectedItem(null);
		isEdit = false;
	}

	public void onSave() {
		try {
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factoryqualification");
			Map<String, Object> eMap = new HashMap<String, Object>();
			if (isEdit)
				eMap = adapter.loadByPrimaryKey((Integer) map.get("id"));
			eMap.put("id", eMap.get("id"));
			eMap.put("qname", qname.getValue());
			eMap.put("qcode", qcode.getValue());
			eMap.put("qdesc", qdesc.getValue());
			adapter.commit(eMap);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) listbox.getModel();
			list.clear();
			list.addAll(adapter.load());
			onAdd();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void onDel() throws SQLException {
		@SuppressWarnings("unchecked")
		Map<String, Object> dMap = (Map<String, Object>) listbox.getSelectedItem().getValue();
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factoryqualification");
		adapter.remove(dMap);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) listbox.getModel();
		list.clear();
		list.addAll(adapter.load());
		onAdd();
		isEdit = false;
	}
}

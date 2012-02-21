package cccfmis.bpm.zk;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class FactoryCheckUserEdit extends Window {
	@SuppressWarnings("unchecked")
	Map<String, Object> map = (Map<String, Object>) Executions.getCurrent().getArg();
	DataSource source;
	JEntityAdapter adapter;
	int id = 0;
	Bandbox qualification;
	Listbox qlist;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void onCreate() {
		if (map.get("id") != null && map.get("id") != "") {
			id = (Integer) map.get("id");
		}
		qualification = (Bandbox) getFellow("qualification");
		qlist = (Listbox) getFellow("qlist");

		String[] qnames = qualification.getValue().split(",");
		@SuppressWarnings("unchecked")
		List<Listitem> items = qlist.getItems();
		for (int i = 0; i < qnames.length; i++) {
			if (qnames[i] != "")
				for (Listitem item : items) {
					@SuppressWarnings("unchecked")
					Map<String, Object> lMap = (Map<String, Object>) item.getValue();
					if (lMap.get("qname").toString().trim().equals(qnames[i])) {
						item.setSelected(true);
					}
				}
		}
	}

	public void onSave() {
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		Map<String, Object> map = null;
		try {
			Map<String, Object> mapF;
			adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser");

			if (id != 0) {
				mapF = adapter.loadByPrimaryKey(id);
			} else
				mapF = new HashMap<String, Object>();

			mapF.put("name", ((Textbox) getFellow("name")).getValue());
			mapF.put("organization", ((Textbox) getFellow("organization")).getValue());
			mapF.put("sex", ((Radiogroup) getFellow("sex")).getSelectedItem().getLabel());
			mapF.put("qualification", ((Textbox) getFellow("qualification")).getValue());
			mapF.put("telephone", ((Textbox) getFellow("telephone")).getValue());
			mapF.put("adress", ((Textbox) getFellow("adress")).getValue());
			mapF.put("station", ((Radiogroup) getFellow("station")).getSelectedItem().getLabel());
			mapF.put("nature", ((Radiogroup) getFellow("nature")).getSelectedItem().getLabel());
			map = adapter.commit(mapF);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.setAttribute("map", map);
			this.setAttribute("ischange", true);
			this.detach();
		}
	}

	public void onSelected(SelectEvent event) {
		@SuppressWarnings("unchecked")
		Set<Listitem> sitem = event.getSelectedItems();
		Iterator<Listitem> items = sitem.iterator();
		String qname = "";
		while (items.hasNext()) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) items.next().getValue();
			qname = qname + map.get("qname") + ",";
		}
		if (qname != "")
			qname = qname.substring(0, qname.length() - 1);
		qualification.setValue(qname);
	}

}

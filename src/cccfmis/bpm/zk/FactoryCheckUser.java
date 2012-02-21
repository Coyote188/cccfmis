package cccfmis.bpm.zk;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class FactoryCheckUser extends Window {
	DataSource source;
	List<Treerow> selectRow = new ArrayList<Treerow>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void onCreate() {
		loadTree(null);
	}

	private void loadTree(String sql) {
		Tree factorytree = (Tree) getFellow("factorytree");
		factorytree.getTreechildren().getChildren().clear();
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		Menupopup editPopup = (Menupopup) getFellow("editPopup");
		try {
			JEntityAdapter adapter;
			if (sql == null || sql == "")
				adapter = JAdapterFactry.createJEntityAdapterBySQL(source, "select * from _factorycheckuser group by organization order by id asc");
			else
				adapter = JAdapterFactry.createJEntityAdapterBySQL(source, "select * from _factorycheckuser " + sql + " group by organization order by id asc");

			List<Map<String, Object>> list = adapter.load();
			Iterator<Map<String, Object>> listi = list.iterator();
			while (listi.hasNext()) {
				Map<String, Object> pItem = listi.next();
				Treeitem treeitem = new Treeitem();
				treeitem.setAttribute("level", "1");
				treeitem.setValue(pItem.get("organization"));
				treeitem.setContext(editPopup);
				treeitem.setOpen(false);
				Treerow treerow = new Treerow();
				Treecell nameCell = new Treecell(pItem.get("organization").toString());
				Treecell noCell = new Treecell("");
				treerow.setDroppable("true");
				Component comp = null;
				treerow.addForward("onDrop", comp, "onDropItem");
				treerow.appendChild(nameCell);
				treerow.appendChild(noCell);
				treeitem.appendChild(treerow);
				JEntityAdapter cadapter;
				if (sql == null || sql == "")
					cadapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser", " where organization='" + treeitem.getValue() + "'");
				else
					cadapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser", sql + " and organization='" + treeitem.getValue() + "'");
				List<Map<String, Object>> clistitems = cadapter.load();
				Iterator<Map<String, Object>> clistitemsi = clistitems.iterator();
				Treechildren treechildren = new Treechildren();
				while (clistitemsi.hasNext()) {
					Map<String, Object> citem = clistitemsi.next();
					Treeitem ctreeitem = new Treeitem();
					ctreeitem.setAttribute("level", "2");
					ctreeitem.setValue(citem.get("organization"));
					Treerow ctreerow = new Treerow();
					ctreerow.setDraggable("true");
					ctreerow.setId(citem.get("id").toString());
					ctreerow.setAttribute("name", citem.get("name") + " " + citem.get("qualification"));
					Treecell cnameCell = new Treecell(citem.get("name").toString());
					Treecell cnoCell = new Treecell(citem.get("qualification").toString());
					cnameCell.setImage("/image/files/LINGDN.png");
					ctreerow.appendChild(cnameCell);
					ctreerow.appendChild(cnoCell);
					ctreeitem.appendChild(ctreerow);
					ctreeitem.setValue(citem.get("organization"));
					treechildren.appendChild(ctreeitem);
					treeitem.appendChild(treechildren);
				}
				factorytree.getTreechildren().appendChild(treeitem);
			}
		} catch (SQLException e) {
			System.out.println("无法创建连接" + e.getMessage());
		}
	}

	public void onDropItem(ForwardEvent event) throws SQLException, IOException {
		DropEvent evt = (DropEvent) event.getOrigin();
		if (evt.getDragged() instanceof Treerow) {
			if (evt.getTarget() instanceof Vbox) {
				Treerow drow = (Treerow) evt.getDragged();
				selectRow.add(drow);
				drow.setDraggable("false");
				Vbox txt = (Vbox) event.getOrigin().getTarget();
				Checkbox checkbox = new Checkbox();
				checkbox.setLabel(drow.getAttribute("name").toString());
				checkbox.setValue(drow.getId().toString());
				txt.appendChild(checkbox);
			} else if (evt.getTarget() instanceof Treerow) {
				// 拖动行
				Treerow dragrow = (Treerow) evt.getDragged();
				// 目标行
				Treerow droprow = (Treerow) evt.getTarget();
				JEntityAdapter dadapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser");
				Map<String, Object> dmap = dadapter.loadByPrimaryKey(dragrow.getId());
				dmap.put("organization", ((Treeitem) droprow.getParent()).getValue());
				dadapter.commit(dmap);
				Treeitem treeitem = (Treeitem) droprow.getParent();
				treeitem.getTreechildren().appendChild(dragrow.getParent());
				((Treeitem) droprow.getParent()).setOpen(true);
			}
		}
	}

	public void onDelSelect() {
		Vbox txt = (Vbox) getFellow("vbox");
		Checkbox cbx1 = (Checkbox) getFellow("cbx");
		List<Object> list = txt.getChildren();
		for (int i = list.size() - 1; i >= 0; i--) {
			Object pi = list.get(i);
			if (pi instanceof Checkbox) {
				Checkbox check = (Checkbox) pi;
				if (check.isChecked()) {
					Iterator<Treerow> selrows = selectRow.iterator();
					while (selrows.hasNext()) {
						Treerow selrow = selrows.next();
						if (selrow.getId() != null && selrow.getId().toString().equals(check.getValue())) {
							Treerow rrow = (Treerow) getFellow(selrow.getId());
							selectRow.remove(selrow);
							rrow.setDraggable("true");
							txt.removeChild(check);
							break;
						}
					}
				}
			}
		}
		cbx1.setChecked(false);

	}

	public void onChecked() {
		Vbox txt = (Vbox) getFellow("vbox");
		Checkbox cbx = (Checkbox) getFellow("cbx");
		List<Object> list = txt.getChildren();
		Iterator<Object> listi = list.iterator();
		while (listi.hasNext()) {
			Object pi = listi.next();
			if (pi instanceof Checkbox) {
				Checkbox check = (Checkbox) pi;
				check.setChecked(cbx.isChecked());
			}
		}
	}

	public void onSetMaster() throws InterruptedException {
		Vbox txt = (Vbox) getFellow("vbox");
		Checkbox cbx = new Checkbox();
		List<Object> list = txt.getChildren();
		Iterator<Object> listi = list.iterator();
		int i = 0;
		while (listi.hasNext()) {
			Object pi = listi.next();
			if (pi instanceof Checkbox) {
				Checkbox check = (Checkbox) pi;
				cbx = check;
				if (check.isChecked())
					i++;
			}
		}
		if (i == 1) {
			Messagebox.show("您设置为组长的人员是：" + cbx.getLabel());
		} else if (i == 0) {
			Messagebox.show("请选择您要设置组长的人员");
		} else if (i > 1) {
			Messagebox.show("只能设置一人为组长");
		}
	}

	public void onAddOrgUser(ForwardEvent event) throws InterruptedException {
		Tree factorytree = (Tree) getFellow("factorytree");
		Treeitem treeitem = factorytree.getSelectedItem();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", null);
		map.put("organization", treeitem.getValue());
		Window userEdit = (Window) Executions.createComponents("/cccfpage/factory/factoryUserEdit.zul", null, map);
		userEdit.doModal();
		if (userEdit.getAttribute("ischange") != null) {
			if ((Boolean) userEdit.getAttribute("ischange")) {
				@SuppressWarnings("unchecked")
				Map<String, Object> tmap = (Map<String, Object>) userEdit.getAttribute("map");
				Treeitem item = new Treeitem();
				item.setValue(tmap.get("organization"));
				Treerow ctreerow = new Treerow();
				ctreerow.setDraggable("true");
				ctreerow.setId(tmap.get("id").toString());
				ctreerow.setAttribute("name", tmap.get("name") + " " + tmap.get("qualification"));
				Treecell cnameCell = new Treecell(tmap.get("name").toString());
				Treecell cnoCell = new Treecell(tmap.get("qualification").toString());
				cnameCell.setImage("/image/files/LINGDN.png");
				ctreerow.appendChild(cnameCell);
				ctreerow.appendChild(cnoCell);
				item.appendChild(ctreerow);
				item.setValue(tmap.get("organization"));
				if (treeitem.getAttribute("level") != null) {
					if (treeitem.getAttribute("level").toString().equals("2")) {
						item.setAttribute("level", "2");
						treeitem.getParent().appendChild(item);
					} else if (treeitem.getAttribute("level").toString().equals("1")) {
						item.setAttribute("level", "1");
						treeitem.getTreechildren().appendChild(item);
					}
				}
				treeitem.setOpen(true);
			}
		}
	}

	public void onDelOrgUser() throws SQLException {
		Tree factorytree = (Tree) getFellow("factorytree");
		Treeitem treeitem = factorytree.getSelectedItem();
		if (treeitem.getAttribute("level") != null) {
			if (treeitem.getAttribute("level").toString().equals("2")) {
				Treerow row = (Treerow) treeitem.getChildren().get(0);
				JEntityAdapter adapter1 = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser");
				Map<String, Object> map = adapter1.loadByPrimaryKey(row.getId());
				adapter1.remove(map);
				treeitem.getParent().removeChild(treeitem);
			} else if (treeitem.getAttribute("level").toString().equals("1")) {
				JEntityAdapter adapter2 = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser", " where organization='" + treeitem.getValue() + "'");
				List<Map<String, Object>> maps = adapter2.load();
				adapter2.removes(maps);
				factorytree.getTreechildren().removeChild(treeitem);
			}
		}
	}

	public void onAddUser(ForwardEvent event) throws InterruptedException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", null);
		map.put("organization", null);
		Window userEdit = (Window) Executions.createComponents("/cccfpage/factory/factoryUserEdit.zul", null, map);
		userEdit.doModal();
		if (userEdit.getAttribute("ischange") != null) {
			if ((Boolean) userEdit.getAttribute("ischange"))
				loadTree(null);
		}
	}

	@SuppressWarnings("unchecked")
	public void onEditUser() throws SQLException, SuspendNotAllowedException, InterruptedException {
		Tree factorytree = (Tree) getFellow("factorytree");
		Treeitem treeitem = factorytree.getSelectedItem();
		Map<String, Object> map = null;
		Map<String, Object> map1 = null;
		Treerow treerow = null;
		if (treeitem.getAttribute("level") == null || treeitem.getAttribute("level").toString().equals("1"))
			return;
		else if (treeitem.getAttribute("level").toString().equals("2")) {
			treerow = treeitem.getTreerow();
			JEntityAdapter adapter1 = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser");
			map = adapter1.loadByPrimaryKey(treerow.getId());
		}
		Window userEdit = (Window) Executions.createComponents("/cccfpage/factory/factoryUserEdit.zul", null, map);
		userEdit.doModal();
		if (userEdit.getAttribute("ischange") != null) {
			if ((Boolean) userEdit.getAttribute("ischange") && treerow != null) {
				Treecell treecell0 = null;
				Treecell treecell1 = null;
				map1 = (Map<String, Object>) userEdit.getAttribute("map");
				treerow.setAttribute("name", map1.get("name") + " " + map1.get("qualification"));
				treecell0 = (Treecell) treerow.getChildren().get(0);
				treecell1 = (Treecell) treerow.getChildren().get(1);
				treecell0.setLabel(map1.get("name").toString());
				treecell1.setLabel(map1.get("qualification").toString());
			}
		}
	}

	public void onSearchUser() throws WrongValueException, InterruptedException {
		String sql = "";
		Textbox org = (Textbox) getFellow("org");
		Textbox pname = (Textbox) getFellow("pname");
		if (org.getValue() != null && !org.getValue().equals("")) {
			sql = " organization like '%" + org.getValue() + "%' and";
		}
		if (pname.getValue() != null && !pname.getValue().equals("")) {
			sql = sql + " name like '%" + pname.getValue().toString().trim() + "%' ";
		}
		if (sql.trim() != "")
			sql = " where" + sql;
		if (sql.trim().endsWith("and"))
			sql = sql.substring(0, sql.lastIndexOf("and"));
		loadTree(sql);
		selectRow = new ArrayList<Treerow>();
		Vbox txt = (Vbox) getFellow("vbox");
		txt.getChildren().clear();
	}

	public void onManageQual() throws SuspendNotAllowedException, InterruptedException {
		Window qualification = (Window) Executions.createComponents("/cccfpage/factory/factoryQualification.zul", null, null);
		qualification.doModal();
	}
}

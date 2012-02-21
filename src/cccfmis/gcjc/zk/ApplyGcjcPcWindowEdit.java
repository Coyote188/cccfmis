/**
 * 
 */
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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;

/**
 * @author Administrator
 */
public class ApplyGcjcPcWindowEdit
		extends Window
		implements Composer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3941077436683928267L;
	@Override
	public void doAfterCompose(Component arg)
			throws Exception
	{}
	/**
	 * 册除批次
	 * 
	 * @param event
	 * @throws SQLException
	 */
	public void onRemovePC(ForwardEvent event)
			throws SQLException
	{
		Listbox listbox = (Listbox) this.getFellow("pclist");
		Listitem item = listbox.getSelectedItem();
		Object val = item.getValue();
		Listbox listap = (Listbox) this.getFellow("dddd");
		List mdols = (List) listap.getModel();
		if (val != null)
		{
			DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_gcjc", "id=?");
			JEntityAdapter adapter3 = JAdapterFactry.createJEntityAdapterByTable(source, "_fckdlect", "pc=?");
			List<Map<String, Object>> list = adapter.load((Integer) val);
			adapter.removes(list);
			List<Map<String, Object>> list2 = adapter3.load((Integer) val);
			adapter3.removes(list2);
			for (Object obj : mdols)
			{
				ApplicationInfo info = (ApplicationInfo) obj;
				// info.getProductionModel().
				info.setFactoryInspection("must");
				ApplicationInfoServiceUtil.update(info);
			}
		}
		mdols.clear();
		((List) listbox.getModel()).remove(item.getIndex());
		List applys1 = ApplicationInfoServiceUtil.getfactoryInspection("must");
		Listbox listbox1 = (Listbox) this.getFellow("list1");
		((List<?>) listbox1.getModel()).clear();
		((List<?>) listbox1.getModel()).addAll(applys1);
	}
	public void onRemoveSel(ForwardEvent event)
			throws SQLException
	{
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter3 = JAdapterFactry.createJEntityAdapterByTable(source, "_fckdlect");
		Listbox list = (Listbox) this.getFellow("fckgruopsllist");
		Map<String, Object> val = (Map<String, Object>) list.getSelectedItem().getValue();
		// if()
		// System.out.println(val.get("autoid"));
		if (val.containsKey("autoid"))
			adapter3.remove(val);
		((List) list.getModel()).remove(val);
	}
	/**
	 * 批次选择之后
	 * 
	 * @param event
	 * @throws SQLException
	 */
	public void onPcSelect(ForwardEvent event)
			throws SQLException
	{
		List applys1 = ApplicationInfoServiceUtil.getfactoryInspection("must");
		Listbox listbox1 = (Listbox) this.getFellow("list1");
		((List<?>) listbox1.getModel()).clear();
		((List<?>) listbox1.getModel()).addAll(applys1);
		Listbox fckbox = (Listbox) this.getFellow("dddd");
		((List<?>) fckbox.getModel()).clear();
		Listbox listbox = (Listbox) this.getFellow("pclist");
		Listitem item = listbox.getSelectedItem();
		if (item == null || item.getValue() == null)
			return;
		List applysel = ApplicationInfoServiceUtil.getfactoryInspection(item.getValue() + "");
		((List<?>) fckbox.getModel()).addAll(applysel);
		// fckbox.
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter3 = JAdapterFactry.createJEntityAdapterByTable(source, "_fckdlect", "pc=?");
		Listbox fckgruopsllist = (Listbox) this.getFellow("fckgruopsllist");
		((List<?>) fckgruopsllist.getModel()).clear();
		((List<Map<String, Object>>) fckgruopsllist.getModel()).addAll(adapter3.load((Integer) item.getValue()));
	}
	/**
	 * 复制批次
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void onNewPcDrop(ForwardEvent event)
			throws InterruptedException
	{
		try
		{
			DropEvent de = (DropEvent) event.getOrigin();
			Listitem component = (Listitem) de.getDragged();
			Listbox list = (Listbox) this.getFellow("pclist");
			Map<String, Object> ent = (Map<String, Object>) ((List) list.getModel()).get(component.getIndex());
			DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
			JEntityAdapter adapter;
			adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_gcjc");
			Map<String, Object> m = adapter.newEntity();
			for (String key : ent.keySet())
			{
				m.put(key, ent.get(key));
			}
			m.put("id", null);
			((List) list.getModel()).add(m);
			Messagebox.show("-------");
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onGcjcsel(ForwardEvent event)
			throws InterruptedException, SQLException
	{
		Listbox pclist = (Listbox) this.getFellow("pclist");
		Listitem sect = pclist.getSelectedItem();
		if (sect == null)
		{
			// throw new WrongValueException(dbbox, "选择批次后才能添加");
			Messagebox.show("请选择批次.", "提示", Messagebox.OK, Messagebox.QUESTION);
			return;
		}
		if (sect.getValue()==null)
		{
			// throw new WrongValueException(dbbox, "选择批次后才能添加");
			Messagebox.show("请保存批次批次.", "提示", Messagebox.OK, Messagebox.QUESTION);
			return;
		}
		Listbox list = (Listbox) this.getFellow("fckgruopsllist");
		List<Map<String, Object>> sel = (List<Map<String, Object>>) list.getModel();
		DropEvent de = (DropEvent) event.getOrigin();
		Treerow row = (Treerow) de.getDragged();
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		// JEntityAdapter adapter3 = JAdapterFactry.createJEntityAdapterByTable(source, "_fckdlect");
		// Map<String, Object> map = adapter3.newEntity();
		Map<String, Object> user = (Map<String, Object>) ((Treeitem) row.getParent()).getValue();
		for (Map val : sel)
		{
			if (val.get("id").equals(user.get("id")))
				return;
		}
		Map<String, Object> val = (Map<String, Object>) ((Treeitem) row.getParent()).getValue();
		//adapter3.commit(val);
		val.put("pc", sect.getValue());
		sel.add(val);
	}
	public void onCommit(ForwardEvent event)
			throws InterruptedException
	{
		try
		{
			Listbox list = (Listbox) this.getFellow("pclist");
			DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
			List<Listitem> items = list.getItems();
			for (Listitem item : items)
			{
				List<Listcell> cells = item.getChildren();
				for (Listcell cell : cells)
				{
					if (cell.getFirstChild() instanceof InputElement)
						((InputElement) cell.getFirstChild()).getText();
				}
			}
			//
			Listbox listap = (Listbox) this.getFellow("dddd");
			List mdols = (List) listap.getModel();
			// mdols.addAll(ds);
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_gcjc", "");
			if (list.getSelectedItem() != null)
			{
				List<Map<String, Object>> ds = (List<Map<String, Object>>) list.getModel();
				int index = list.getSelectedItem().getIndex();
				ds.get(index).put("spr", "已提交");
				adapter.commit(ds.get(index));
				ds.remove(index);
			}
			Listbox list1 = (Listbox) this.getFellow("list1");
			List mdols1 = (List) list1.getModel();
			for (Object obj : mdols1)
			{
				ApplicationInfo info = (ApplicationInfo) obj;
				info.setFactoryInspection("must");
				ApplicationInfoServiceUtil.update(info);
			}
			Messagebox.show("保存成功.", "提示", Messagebox.OK, Messagebox.INFORMATION);
		} catch (SQLException e)
		{
			Messagebox.show("保存失败." + e.getMessage(), "提示", Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * 保存批次
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void onSave(ForwardEvent event)
			throws InterruptedException
	{
		try
		{
			Listbox list = (Listbox) this.getFellow("pclist");
			DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
			List<Listitem> items = list.getItems();
			for (Listitem item : items)
			{
				List<Listcell> cells = item.getChildren();
				for (Listcell cell : cells)
				{
					if (cell.getFirstChild() instanceof InputElement)
						((InputElement) cell.getFirstChild()).getText();
				}
			}
			//
			Listbox listap = (Listbox) this.getFellow("dddd");
			List mdols = (List) listap.getModel();
			// mdols.addAll(ds);
			if (list.getSelectedItem() != null)
			{
				List<Map<String, Object>> ds = (List<Map<String, Object>>) list.getModel();
				int index = list.getSelectedItem().getIndex();
				// ds.get(index).put("spr", "");
				JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_gcjc");
				adapter.commit(ds.get(index));
				String gid = ((Map<String, Object>) ds.get(index)).get("id") + "";
				for (Object obj : mdols)
				{
					ApplicationInfo info = (ApplicationInfo) obj;
					info.setFactoryInspection(gid);
					ApplicationInfoServiceUtil.update(info);
				}
				Listbox list3 = (Listbox) this.getFellow("fckgruopsllist");
				List sel = (List) list3.getModel();
				JEntityAdapter adapter3 = JAdapterFactry.createJEntityAdapterByTable(source, "_fckdlect");
				adapter3.commits(sel);
			}
			Listbox list1 = (Listbox) this.getFellow("list1");
			List mdols1 = (List) list1.getModel();
			for (Object obj : mdols1)
			{
				ApplicationInfo info = (ApplicationInfo) obj;
				info.setFactoryInspection("must");
				ApplicationInfoServiceUtil.update(info);
			}
			Messagebox.show("保存成功.", "提示", Messagebox.OK, Messagebox.INFORMATION);
		} catch (SQLException e)
		{
			Messagebox.show("保存失败." + e.getMessage(), "提示", Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * 新批次
	 * 
	 * @param event
	 * @throws SQLException
	 */
	public void onNewPC(ForwardEvent event)
			throws SQLException
	{
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_gcjc", "id=?");
		Listbox listbox = (Listbox) this.getFellow("pclist");
		((List) listbox.getModel()).add(adapter.newEntity());
	}
	/**
	 * 添加到批次
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onApplyDrop(ForwardEvent event)
			throws InterruptedException
	{
		Listbox list = (Listbox) this.getFellow("pclist");
		Listitem sect = list.getSelectedItem();
		if (sect == null)
		{
			// throw new WrongValueException(dbbox, "选择批次后才能添加");
			Messagebox.show("请选择批次.", "提示", Messagebox.OK, Messagebox.QUESTION);
			return;
		}
		DropEvent de = (DropEvent) event.getOrigin();
		Listitem component = (Listitem) de.getDragged();
		Listbox lls = (Listbox) component.getParent();
		List<Listitem> items = lls.getItems();
		List Model1 = (List) lls.getModel();
		List Model2 = ((List) ((Listbox) this.getFellow("dddd")).getModel());
		List temp = new ArrayList();
		for (Listitem item : items)
		{
			if (!item.isSelected() && !item.equals(component))
				continue;
			// System.out.println(item.getIndex());
			temp.add(Model1.get(item.getIndex()));
			// Model2.add(Model1.remove(item.getIndex()));
		}
		Model1.removeAll(temp);
		Model2.addAll(temp);
	}
	/**
	 * 从批次移除
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onApplyRemoveDrop(ForwardEvent event)
			throws InterruptedException
	{
		DropEvent de = (DropEvent) event.getOrigin();
		Listitem component = (Listitem) de.getDragged();
		Listbox lls = (Listbox) component.getParent();
		List<Listitem> items = lls.getItems();
		List Model1 = (List) lls.getModel();
		List Model2 = ((List) ((Listbox) this.getFellow("list1")).getModel());
		List temp = new ArrayList();
		for (Listitem item : items)
		{
			if (!item.isSelected() && !item.equals(component))
			{
				continue;
			}
			// System.out.println(item.getIndex());
			temp.add(Model1.get(item.getIndex()));
			// Model2.add(Model1.remove(item.getIndex()));
		}
		Model1.removeAll(temp);
		Model2.addAll(temp);
	}
	public void onTreeNodeOpen(ForwardEvent event)
			throws SQLException
	{
		// Tree fcktree
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		Treeitem selitem = (Treeitem) event.getOrigin().getTarget();
		JEntityAdapter adapter2 = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser", "organization=?");// (source, "select organization from _factorycheckuser group by organization order by id asc");
		this.getPage().setAttribute("fckusers", adapter2.load((String) selitem.getValue()));
		selitem.getTreechildren().getItems().clear();
		Executions.createComponents("/cccfpage/gcjc-user-node.zul", selitem.getTreechildren(), null);
	}
}

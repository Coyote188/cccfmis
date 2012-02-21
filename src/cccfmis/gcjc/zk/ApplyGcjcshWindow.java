/**
 * 
 */
package cccfmis.gcjc.zk;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;

/**
 * 确认
 * @author Administrator
 */
public class ApplyGcjcshWindow
		extends GenericComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3941077436683928267L;
	private Component			comp;
	private DataSource			source;
	private Listbox				appList, pclist, ulist;
	private Vlayout				haselect;
	private String				user				= "";
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		this.comp = comp;
		appList = (Listbox) this.comp.getFellow("appList");
		pclist = (Listbox) this.comp.getFellow("pclist");
		ulist = (Listbox) this.comp.getFellow("userlist");
		haselect = (Vlayout) this.comp.getFellow("haselect");
	}
	public ComponentInfo doBeforeCompose(org.zkoss.zk.ui.Page page, org.zkoss.zk.ui.Component parent, org.zkoss.zk.ui.metainfo.ComponentInfo compInfo)
	{
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		try
		{
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "fig_batch", "state='待确认'");
			List batchlist = adapter.load();
			page.setAttribute("batchlist", batchlist);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onPcSelect(SelectEvent event)
			throws SQLException
	{
		BigInteger batchid = (BigInteger) pclist.getSelectedItem().getValue();
		@SuppressWarnings("unchecked")
		List<ApplicationInfo> list = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where batchid=" + batchid + "");
		ListModelList model = new ListModelList(list);
		appList.setModel(model);
		// 见习见证人员
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser");
		List userlist = adapter.load();
		ListModelList model1 = new ListModelList(userlist);
		ulist.setModel(model1);
	}
	public void onPass()
			throws SQLException
	{
		BigInteger batchid = (BigInteger) pclist.getSelectedItem().getValue();
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "fig_batch", "id=" + batchid + "");
		Map map = adapter.loadByPrimaryKey(batchid);
		map.put("state", "待审批");
		adapter.commit(map);
		JEntityAdapter adapter1 = JAdapterFactry.createJEntityAdapterByTable(source, "fig_batch", "state='待确认'");
		List batchlist = adapter1.load();
		ListModelList model = new ListModelList(batchlist);
		pclist.setModel(model);
		if (pclist.getItems().size() > 0)
		{
			pclist.setSelectedIndex(0);
		} else
		{
			@SuppressWarnings("unchecked")
			List<ApplicationInfo> list = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where 1!=1");
			ListModelList model2 = new ListModelList(list);
			appList.setModel(model2);
		}
	}
	public void onSelectUser(SelectEvent event)
	{
		haselect.getChildren().clear();
		user = "";
		@SuppressWarnings("unchecked")
		Set<Listitem> items = event.getSelectedItems();
		for (Listitem item : items)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) item.getValue();
			Label label = new Label(map.get("name") + " " + map.get("organization"));
			label.setParent(haselect);
			user = user + map.get("name") + ",";
		}
		if (user.endsWith(","))
		{
			user = user.substring(0, user.length() - 1);
		}
	}
	public void onSavePass()
			throws InterruptedException, SQLException
	{
		if (appList.getSelectedItems().size() <= 0)
		{
			Messagebox.show("请在右侧申请列表选择要添加见习见证人员的申请");
		} else
		{
			if (user.equals(""))
			{
				Messagebox.show("请在左侧待添加人员列表选择要添加见习见证人员");
			} else
			{
				System.out.println(user);
				@SuppressWarnings("unchecked")
				Set<Listitem> items = appList.getSelectedItems();
				for (Listitem item : items)
				{
					ApplicationInfo application = (ApplicationInfo) item.getValue();
					System.out.println(application.getApptype());
					application.setShowuser(user);
					ApplicationInfoServiceUtil.saveOrUpdate(application);
				}
				if (pclist.getSelectedItem() != null)
				{
					@SuppressWarnings("unchecked")
					List<ApplicationInfo> list = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where batchid=" + pclist.getSelectedItem().getValue() + "");
					ListModelList model1 = new ListModelList(list);
					appList.setModel(model1);
				}
			}
		}
	}
}

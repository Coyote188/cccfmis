package cccfmis.pinegroup.zk;

import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Window;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;

@SuppressWarnings("unchecked")
public class PineGrioupPzglApply
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8139835181119883430L;
	private Page				page;
	private Window				self;
	private DataSource			source;
	private JEntityAdapter		adapter;
	private Listbox				batchslist, appslist, batchappslist, participantlist;
	private Popup				popup;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		this.page = page;
		return super.doBeforeCompose(page, parent, compInfo);
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		self = (Window) comp;
		super.doAfterCompose(comp);
	}
	public void onCreated(ForwardEvent event)
			throws SQLException
	{
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		adapter = JAdapterFactry.createJEntityAdapterByTable(source, "fig_batch", "state='新建' AND created_id=? ");
		
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		List<Map<String, Object>> batchs = adapter.load(user.getId());
		ListModelList mb = new ListModelList();
		mb.addAll(batchs);
		batchslist.setModel(mb);
		@SuppressWarnings("unchecked")
		List<ApplicationInfo> apps = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where factoryInspection='must' and planner='" + user.getName() + "' and batchid=0");
		 List<?> applys = ApplicationInfoServiceUtil.getfactoryInspection("must");
		ListModelList ma = new ListModelList();
		ma.addAll(apps);
		appslist.setModel(ma);
		JEntityAdapter popadapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheckuser");
		ListModelList mmm = new ListModelList();
		mmm.addAll(popadapter.load());
		participantlist.setModel(mmm);
	}
	public void onNewBatch(ForwardEvent event)
	{
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		Map<String, Object> map = adapter.newEntity();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmm");
		Date date = new Date();
		map.put("batch", df.format(date));
		map.put("batch_name", df.format(date));
		map.put("create_date", new Date());
		map.put("created_id", user.getId());
		map.put("created_name", user.getName());
		map.put("application_count", 0);
		map.put("state", "新建");
		map.put("notes", "");
		try
		{
			adapter.create(map);
			List<Map<String, Object>> batchs = adapter.load(user.getId());
			ListModelList mb = new ListModelList();
			mb.addAll(batchs);
			batchslist.setModel(mb);
		} catch (SQLException e)
		{
			throw new WrongValueException(event.getOrigin().getTarget(), "添加失败:" + e.getMessage());
		}
	}

	public void onDelBatch(ForwardEvent event)
			throws InterruptedException
	{
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		Listitem item = batchslist.getSelectedItem();
		if (item == null)
			throw new WrongValueException(event.getOrigin().getTarget(), "请选择批次");
		if (Messagebox.YES != Messagebox.show("你要删除这个批次吗", "提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION))
			return;
		Map<String, Object> batch = (Map<String, Object>) item.getValue();
		try
		{
			adapter.remove(batch);
			List<ApplicationInfo> apps2 = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where batchid=" + ((BigInteger) batch.get("id")).longValue() + "");
			for (ApplicationInfo inf : apps2)
			{
				inf.setBatchid(0);
				ApplicationInfoServiceUtil.update(inf);
			}
			batchappslist.setModel(new ListModelList());
			List<Map<String, Object>> batchs = adapter.load(user.getId());
			ListModelList mb = new ListModelList();
			mb.addAll(batchs);
			batchslist.setModel(mb);
		} catch (SQLException e)
		{
			throw new WrongValueException(event.getOrigin().getTarget(), "删除失败:" + e.getMessage());
		}
	}
	public void onActivBatch(ForwardEvent event)
			throws InterruptedException
	{
		Listitem item = batchslist.getSelectedItem();
		if (item == null)
			throw new WrongValueException(event.getOrigin().getTarget(), "请选择批次");
		if (Messagebox.YES != Messagebox.show("你要提交这个批次吗", "提示", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION))
			return;
		Map<String, Object> batch = (Map<String, Object>) item.getValue();
		batch.put("state", "待审核");
		try
		{
			UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
			adapter.update(batch);
			List<Map<String, Object>> batchs = adapter.load(user.getId());
			ListModelList mb = new ListModelList();
			mb.addAll(batchs);
			batchslist.setModel(mb);
		} catch (SQLException e)
		{
			throw new WrongValueException(event.getOrigin().getTarget(), "提交失败:" + e.getMessage());
		}
	}
	public void onBatchSelect(ForwardEvent event)
	{
		Listitem item = batchslist.getSelectedItem();
		Map<String, Object> batch = (Map<String, Object>) item.getValue();
		List<ApplicationInfo> apps = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where batchid=" + batch.get("id") + "");
		ListModelList mb = new ListModelList();
		mb.addAll(apps);
		batchappslist.setModel(mb);
	}
	public void onMoveInbatch(ForwardEvent event)
			throws SQLException
	{
		Listitem batitem = batchslist.getSelectedItem();
		if (batitem == null)
			throw new WrongValueException(event.getOrigin().getTarget(), "请选择批次");
		Set<Listitem> set = appslist.getSelectedItems();
		if (set.size() == 0)
			throw new WrongValueException(event.getOrigin().getTarget(), "请选择申请");
		Map<String, Object> batch = (Map<String, Object>) batitem.getValue();
		BigInteger id = (BigInteger) batch.get("id");
		for (Listitem item : set)
		{
			ApplicationInfo inf = (ApplicationInfo) item.getValue();
			inf.setBatchid(id.longValue());
			ApplicationInfoServiceUtil.update(inf);
		}
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		List<ApplicationInfo> apps = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where factoryInspection='must' and planner='" + user.getName() + "' and batchid=0");
		// List<?> applys = ApplicationInfoServiceUtil.getfactoryInspection("must");
		ListModelList ma = new ListModelList();
		ma.addAll(apps);
		appslist.setModel(ma);
		List<ApplicationInfo> apps2 = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where batchid=" + ((BigInteger) batch.get("id")).longValue() + "");
		ListModelList mb = new ListModelList();
		mb.addAll(apps2);
		batchappslist.setModel(mb);
		batch.put("application_count", apps2.size());
		adapter.update(batch);
	}
	public void onMoveOutbatch(ForwardEvent event)
			throws SQLException
	{
		Listitem batitem = batchslist.getSelectedItem();
		if (batitem == null)
			throw new WrongValueException(event.getOrigin().getTarget(), "请选择批次");
		Set<Listitem> set = batchappslist.getSelectedItems();
		if (set.size() == 0)
			throw new WrongValueException(event.getOrigin().getTarget(), "请选择申请");
		Map<String, Object> batch = (Map<String, Object>) batitem.getValue();
		// long id = (Long) batch.get("id");
		for (Listitem item : set)
		{
			ApplicationInfo inf = (ApplicationInfo) item.getValue();
			inf.setBatchid(0);
			ApplicationInfoServiceUtil.update(inf);
		}
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		List<ApplicationInfo> apps = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where factoryInspection='must' and planner='" + user.getName() + "' and batchid=0");
		// List<?> applys = ApplicationInfoServiceUtil.getfactoryInspection("must");
		ListModelList ma = new ListModelList();
		ma.addAll(apps);
		appslist.setModel(ma);
		List<ApplicationInfo> apps2 = ApplicationInfoServiceUtil.findByQuery(" from ApplicationInfo where batchid=" + ((BigInteger) batch.get("id")).longValue() + "");
		ListModelList mb = new ListModelList();
		mb.addAll(apps2);
		batchappslist.setModel(mb);
		batch.put("application_count", apps2.size());
		adapter.update(batch);
	}
	public void onSave(ForwardEvent event)
			throws SQLException, InterruptedException
	{
		Listitem batitem = batchslist.getSelectedItem();
		if (batitem == null)
			throw new WrongValueException(event.getOrigin().getTarget(), "请选择批次");
		Map<String, Object> batch = (Map<String, Object>) batitem.getValue();
		adapter.update(batch);
		Messagebox.show("保存成功", "提示", Messagebox.OK, Messagebox.INFORMATION);
	}
	public void onSaveParticipants(ForwardEvent event)
	{
		if (batchappslist.getSelectedItem() == null)
			return;
		String uss = "";
		for (Object obj : participantlist.getItems())
		{
			Listitem item = (Listitem) obj;
			if (item.isSelected())
			{
				Map<String, Object> e = (Map<String, Object>) item.getValue();
				uss += "," + e.get("name");
			}
		}
		ApplicationInfo inf = (ApplicationInfo) batchappslist.getSelectedItem().getValue();
		inf.setParticipants(uss.length() > 1 ? uss.substring(1) : "");
		ApplicationInfoServiceUtil.update(inf);
		popup.close();
	}
	public void onSelectParticipants(ForwardEvent event)
			throws SQLException
	{		 
		if (batchappslist.getSelectedItem() == null)
		{
			popup.close();
			return;
		}
		if (batchappslist.getSelectedItem() == null)
			return;
		ApplicationInfo inf = (ApplicationInfo) batchappslist.getSelectedItem().getValue();
		String ss = inf.getParticipants();
		if (ss == null)
			ss = "";
		for (Object obj : participantlist.getItems())
		{
			Listitem item = (Listitem) obj;
			Map<String, Object> e = (Map<String, Object>) item.getValue();
			if (ss.indexOf((String) e.get("name")) >= 0)
				item.setSelected(true);
			else
				item.setSelected(false);
		}
	}
}

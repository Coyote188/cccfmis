package cccf.mis.web.tsak.batch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cccf.ma.model.AssignBatch;
import cccf.ma.model.FactoryCheckTask;
import cccf.ma.model.FactoryCheckTaskUser;
import cccf.ma.model.FactoryCheckUser;
import cccf.ma.service.AssignBatchService;

public class BatchPlanController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3193902771109473044L;
	private Map<?, ?>			params				= Executions.getCurrent().getArg();
	private Listbox				task_list;
	private ListModelList		tasks;
	private Listbox				participantlist;
	private Popup				popup;
	private Radiogroup			zzradiogroup;
	private Textbox				approveMemo;
	private Textbox				batch_name;
	private AssignBatchService	svc					= BeanAdapter.getBean("AssignBatchService", AssignBatchService.class);
	private Map					selectItemValue;	
	// 选中申请值
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		// TODO 生成一个批次
		return super.doBeforeCompose(page, parent, compInfo);
	}
	 
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		ListModelList model = new ListModelList();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("values");
		if (list == null)
		{
			task_list.setModel(model);
			return;
		} else
		{}
		model.addAll(list);
		task_list.setModel(model);
		ListModelList mmm = new ListModelList();
		// TODO 添加人员
		// mmm.add('')
		StringBuffer hql = new StringBuffer("select new map(id as id").append(",name as name").append(",organization as organization").append(",sex as sex").append(",qualification as qualification").append(",telephone as telephone")
				.append(",adress as adress").append(",station as station").append(",nature as nature").append(")").append(" from FactoryCheckUser o");
		List fcu_list = svc.getResultList(hql.toString());
		mmm.addAll(fcu_list);
		participantlist.setModel(mmm);
	}
	public void onSubmit()
			throws InterruptedException
	{
		@SuppressWarnings("unchecked")
		List<Listitem> seltedItems = task_list.getItems();
		if (seltedItems.isEmpty())
		{
			Messagebox.show("请选择至少一个申请进行受理分工", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		} else
		{
			AssignBatch batch = new AssignBatch();
			batch.setCheckTasks(new ArrayList<FactoryCheckTask>());
			batch.setBatchName(batch_name.getText()); 
			for (Listitem item : seltedItems)// 迭代选中的Item
			{
				Map<?, ?> tmp = (Map<?, ?>) item.getValue();
				FactoryCheckTask factoryCheckTask = (FactoryCheckTask) tmp.get("factoryCheckTask");
				batch.getCheckTasks().add(factoryCheckTask);
			}
			// TODO 加入批次
			svc.doCreateBatch(batch, UserInfoServiceUtil.getCurrentLoginUser().getId(), "提交", approveMemo.getText());
			Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			// // 操作成功
			self.setAttribute("submits", tasks);
			((Window) self).setVisible(false);
			//
			self.setAttribute("submit", "OK");
			self.setVisible(false);
		}
	}
	public void onSaveParticipants(ForwardEvent event)
	{
		//任务
		FactoryCheckTask factoryCheckTask = (FactoryCheckTask) selectItemValue.get("factoryCheckTask");
		factoryCheckTask.setCheckUsers(new ArrayList<FactoryCheckTaskUser>());
		String uss = "";
		for (Object obj : participantlist.getItems())
		{
			Listitem item = (Listitem) obj;
			if (item.isSelected())
			{
				Map<?, ?> e = (Map<?, ?>) item.getValue();
				uss += "," + e.get("name");
				FactoryCheckTaskUser user = new FactoryCheckTaskUser();
				user.setFactoryCheckUser(new FactoryCheckUser());
				user.getFactoryCheckUser().setId((Integer) e.get("id")); 
				if (e.equals(zzradiogroup.getSelectedItem().getValue()))
				{
					// 组长
					user.setHeadmanSign(1);
				}
				factoryCheckTask.getCheckUsers().add(user);
			}
		}
		// TODO 保存组员 uss
		popup.close();
	}
	public void onSelectParticipants(ForwardEvent event)
			throws SQLException
	{
		Listitem lsitem = ((Listitem) event.getOrigin().getTarget().getParent().getParent());
		popup.open(lsitem);
		selectItemValue = (Map) lsitem.getValue();
		FactoryCheckTask factoryCheckTask = (FactoryCheckTask) selectItemValue.get("factoryCheckTask");
		if (factoryCheckTask == null)
		{
			factoryCheckTask = new FactoryCheckTask();
			selectItemValue.put("factoryCheckTask", factoryCheckTask);
		}
		//factoryCheckTask.setAssignDate(assignDate);
		//factoryCheckTask.setAssignNum(assignNum);//(String) selectItemValue.get("applyno")
		factoryCheckTask.setApplyNo((String) selectItemValue.get("applyno"));
		
		String ss = null;// TODO 取组员
		String zz = "";// TODO 取组长组长
		if (ss == null)
			ss = "";
		int i = 0, index = 0;
		for (Object obj : participantlist.getItems())
		{
			i++;
			Listitem item = (Listitem) obj;
			Map<String, Object> e = (Map<String, Object>) item.getValue();
			if (ss.indexOf((String) e.get("name")) >= 0)
				item.setSelected(true);
			else
				item.setSelected(false);
			if (e.get("name").equals(zz))
			{
				index = i;
			}
		}
		if (index > 0)
			zzradiogroup.setSelectedIndex(index);
		
	}
}

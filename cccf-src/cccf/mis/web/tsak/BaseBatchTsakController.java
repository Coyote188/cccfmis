package cccf.mis.web.tsak;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Vlayout;

public abstract class BaseBatchTsakController
		extends AbsTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6095241973368025491L;
	private Vlayout				includelayout;
	private Map<Object, Object>	args				= new HashMap<Object, Object>();
	private Listbox				tastkListbox;
	// 分页
	private Paging				tastkPaging;
	// 用户信息
	private UserInfo			userinfo;
	// 批次
	private Combobox			batch_no;
	// 批次名称
	private Combobox			batch_name;
	// 批次状态
	private Combobox			batch_status;
	// 流程状态:
	private Combobox			status;
	// 创建人
	private Combobox			created;
	// 时间
	private Datebox				start_batch_date, end_batch_date;
	//
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		if (arg != null)
			args.putAll(arg);
		userinfo = UserInfoServiceUtil.getCurrentLoginUser();
		//
		tastkListbox = (Listbox) Executions.createComponents("/views/TaskList/BatchList.zul", includelayout, args);
		tastkPaging = (Paging) tastkListbox.getFellow("list_paging");
	}
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		Map<String, String> args = getFindArg();
		args.put("uid", userinfo.getId());
		// TODO 批次表
		List<Map<String, Object>> list = BatchTaskUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list != null)
		{
			model.addAll(list);
		}
		getTasksbox().setModel(model);
		tastkPaging.setTotalSize(BatchTaskUtils.getTasksSize(getNodeName(), args));
	}
	protected Map<String, String> getFindArg()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "待办".equals(status.getValue()) ? "0" : "1");
		map.put("batch_no", batch_no.getValue());
		map.put("batch_name", batch_name.getValue());
		map.put("batch_status", batch_status.getValue());
		map.put("created", created.getValue());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		/*if (start_batch_date.getValue() != null)
		{
			Date date = start_batch_date.getValue();
			map.put("start_batch_date", df.format(date));
		}
		if (end_batch_date.getValue() != null)
		{
			Date date = end_batch_date.getValue();
			try
			{
				date = df.parse(df.format(date));
			} catch (ParseException e)
			{}
			date.setTime(date.getTime() + 86400000L - 1);
			map.put("end_batch_date", df.format(date));
		}*/
		return map;
	}
	protected abstract String getNodeName();
	/**
	 * 查询
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onFind(ForwardEvent event)
			throws InterruptedException
	{
		// 条件
		Map<String, String> args = getFindArg();
		args.put("uid", userinfo.getId());
		// TODO 取合同列表
		List<Map<String, Object>> list = BatchTaskUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list == null)
		{
			getTasksbox().setModel(model);
			Messagebox.show("没有查到到任何数据.", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		model.addAll(list);
		getTasksbox().setModel(model);
		tastkPaging.setTotalSize(BatchTaskUtils.getTasksSize(getNodeName(), args));
	}
	/*
	 * (non-Javadoc)
	 * @see cccf.mis.web.tsak.AbsTsakController#getTasksbox()
	 */
	@Override
	protected Listbox getTasksbox()
	{
		return tastkListbox;
	}
}

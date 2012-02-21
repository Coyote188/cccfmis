package cccf.mis.web.tsak;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Vlayout;

public abstract class BaseProductTsakController
		extends AbsTsakController
{
	private static final long	serialVersionUID	= -6095241973368025491L;
	private Vlayout				includelayout;
	private Map<Object, Object>	args				= new HashMap<Object, Object>();
	private Listbox				tastkListbox;
	// 分页
	private Paging				tastkPaging;
	// 用户信息
	private UserInfo			userinfo;
	// 任务号
	private Combobox			no;
	// 企业名称
	private Combobox			enterprise;
	// 产品大类
	private Combobox			product_catalog;
	// 地址
	private Combobox			address;												//
	// 日期
	private Datebox				start_batch_date, end_batch_date;
	//
	// 流程状态:
	private Combobox			status;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		if (arg != null)
			args.putAll(arg);
		userinfo = UserInfoServiceUtil.getCurrentLoginUser();
		//
		tastkListbox = (Listbox) Executions.createComponents("/views/TaskList/BatchProductList.zul", includelayout, args);
		tastkPaging = (Paging) tastkListbox.getFellow("list_paging");
	}
	@Override
	protected Listbox getTasksbox()
	{
		return tastkListbox;
	}
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		Map<String, String> args = getFindArg();
		args.put("usid", userinfo.getId());
		// TODO 取任务数
		List<Map<String, Object>> list = BatchProductUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list != null)
		{
			model.addAll(list);
		}
		getTasksbox().setModel(model);
		// TODO 取任务数数
		tastkPaging.setTotalSize(BatchProductUtils.getTasksSize(getNodeName(), args));
	}
	public void onFind(ForwardEvent event)
			throws InterruptedException
	{
		// 条件
		Map<String, String> args = getFindArg();
		args.put("uid", userinfo.getId());
		// TODO 取任务数
		List<Map<String, Object>> list = BatchProductUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list == null)
		{
			getTasksbox().setModel(model);
			Messagebox.show("没有查到到任何数据.", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		model.addAll(list);
		getTasksbox().setModel(model);
		// TODO 取任务数数
		tastkPaging.setTotalSize(BatchProductUtils.getTasksSize(getNodeName(), args));
	}
	protected Map<String, String> getFindArg()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "待办".equals(status.getValue()) ? "0" : "1");
		map.put("no", no.getValue());
		map.put("enterprise", enterprise.getValue());
		map.put("address", address.getValue());
		map.put("product_catalog", product_catalog.getValue());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (start_batch_date.getValue() != null)
		{
			Date date = start_batch_date.getValue();
			map.put("start_eval_date", df.format(date));
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
			map.put("end_eval_date", df.format(date));
		}
		return map;
	}
	protected abstract String getNodeName();
}

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
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Vlayout;

public abstract class BaseApplyTsakController
		extends AbsTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6095241973368025491L;
	private Vlayout				includelayout;
	private Map<Object, Object>	args				= new HashMap<Object, Object>();
	private Listbox				tastkListbox;
	private Paging				tastkPaging;
	private UserInfo			userinfo;
	// 条件
	// 流程状态
	private Combobox			status;
	// 审请号
	private Combobox			no;
	// 申请企业
	private Combobox			enterprise;
	// 所在地区
	private Combobox			region;
	// 业务类型
	private Combobox			business_type;
	// 产品类型
	private Combobox			product_catalog;
	// 质检中心
	private Combobox			surver_orgname;
	// 质检中心 申请时间
	private Datebox				start_apply_date, end_apply_date;
	/**
	 * 查询参数
	 * 
	 * @return
	 */
	protected Map<String, String> getFindArg()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "待办".equals(status.getValue()) ? "0" : "1");
		map.put("no", no.getValue());
		map.put("enterprise", enterprise.getValue());
		map.put("region", region.getValue());
		map.put("business_type", business_type.getValue());
		map.put("product_catalog", product_catalog.getValue());
		map.put("surver_orgname", surver_orgname.getValue());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (start_apply_date.getValue() != null)
		{
			Date date = start_apply_date.getValue();
			map.put("start_apply_date", df.format(date));
		}
		if (end_apply_date.getValue() != null)
		{
			Date date = end_apply_date.getValue();
			try
			{
				date = df.parse(df.format(date));
			} catch (ParseException e)
			{}
			date.setTime(date.getTime() + 86400000L - 1);
			map.put("end_apply_date", df.format(date));
		}
		return map;
	}
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
		 
		tastkListbox = (Listbox) Executions.createComponents("/views/TaskList/ApplyList.zul", includelayout, args);
		tastkPaging = (Paging) tastkListbox.getFellow("list_paging");
	}
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		Map<String, String> args = getFindArg();
		args.put("uid", userinfo.getId());
		List<Map<String, Object>> list = ApplyTasksUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list != null)
		{
			model.addAll(list);
		}
		getTasksbox().setModel(model);
		tastkPaging.setTotalSize(ApplyTasksUtils.getTasksSize(getNodeName(), args));
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
		List<Map<String, Object>> list = ApplyTasksUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list == null)
		{
			getTasksbox().setModel(model);
			Messagebox.show("没有查到到任何数据.", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		model.addAll(list);
		getTasksbox().setModel(model);
		tastkPaging.setTotalSize(ApplyTasksUtils.getTasksSize(getNodeName(), args));
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

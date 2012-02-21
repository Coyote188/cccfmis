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

public abstract class BaseContractTsakController
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
	// 合同号
	private Combobox			contractno;
	// 申请企业
	private Combobox			enterprise;
	// 地区
	private Combobox			region;
	// 业务类型:
	private Combobox			business_type;
	// 合同状态:
	private Combobox			contract_status;
	// 流程状态:
	private Combobox			status;
	// 合同号
	private Datebox				start_audit_date, end_audit_date;
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
		tastkListbox = (Listbox) Executions.createComponents("/views/TaskList/ContractList.zul", includelayout, args);
		tastkPaging = (Paging) tastkListbox.getFellow("list_paging");
	}
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		Map<String, String> args = getFindArg();
		args.put("uid", userinfo.getId());
		// TODO 取合同列表
		List<Map<String, Object>> list = ContractTasksUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list != null)
		{
			model.addAll(list);
		}
		getTasksbox().setModel(model);
		tastkPaging.setTotalSize(ContractTasksUtils.getTasksSize(getNodeName(), args));
	}
	protected Map<String, String> getFindArg()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "待办".equals(status.getValue()) ? "0" : "1");
		map.put("contractno", contractno.getValue());
		map.put("enterprise", enterprise.getValue());
		map.put("region", region.getValue());
		map.put("business_type", business_type.getValue());
		map.put("contract_status", contract_status.getValue());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (start_audit_date.getValue() != null)
		{
			Date date = start_audit_date.getValue();
			map.put("start_apply_date", df.format(date));
		}
		if (end_audit_date.getValue() != null)
		{
			Date date = end_audit_date.getValue();
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
		List<Map<String, Object>> list = ContractTasksUtils.getTasks(getNodeName(), 0, tastkPaging.getPageSize(), args);
		ListModelList model = new ListModelList();
		if (list == null)
		{
			getTasksbox().setModel(model);
			Messagebox.show("没有查到到任何数据.", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		model.addAll(list);
		getTasksbox().setModel(model);
		tastkPaging.setTotalSize(ContractTasksUtils.getTasksSize(getNodeName(), args));
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

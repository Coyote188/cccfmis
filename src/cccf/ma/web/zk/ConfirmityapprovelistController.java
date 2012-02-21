package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import cccf.ma.function.Functions;

/**
 * @author [JOINYO]
 * @E-mail JOINYO@YEAH.NET
 * @date 创建于2011-5-22下午08:39:35
 * @version 1.0

 * @describe
 * ALL RIGHTS RESERVED,COPYRIGHT(C) FCH LIMITED 2011
 */
public class ConfirmityapprovelistController extends GenericForwardComposer{
	public static final String ON_REFRESH = "onRefreshList";
	private static final long serialVersionUID = 1L;
	private List<Map> tasklist;
	private Map params;
	private Listbox conAppLbx;
	private Component comp;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		this.comp = comp;
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		tasklist=Functions.getCurrentLoginUserTaskList("符合性审核");
		System.out.println(tasklist);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onRefreshList()
	{
		tasklist=Functions.getCurrentLoginUserTaskList("符合性审核");
		conAppLbx.setModel(new ListModelList(tasklist));
	}
	public void onConfirm(ForwardEvent event) throws SuspendNotAllowedException, InterruptedException{
		String applyId=(String) event.getOrigin().getTarget().getAttribute("applyId");
		params=new HashMap();
		params.put("applyId", applyId);
		List<Map> tasks = null;
		for(Map map:tasklist)//寻找任务列表
		{
			if(applyId.equals(map.get("applyId")))
			{
				tasks=(List<Map>) map.get("tasks");
				break;
			}
		}
		params.put("tasks", tasks);
		params.put("isCreateOperate",false);//控制弹出的窗口审批按钮是否创建,true为创建,false为不创建
		params.put("isShowGrid",true);
		Window objWindow=(Window) Executions.createComponents("/SysForm/conformityreview/conformity_review.zul", null, params);
		objWindow.setParent(comp);
		objWindow.doModal();
	}
	public List<Map> getTasklist() {
		return tasklist;
	}

	public void setTasklist(List<Map> tasklist) {
		this.tasklist = tasklist;
	}
}
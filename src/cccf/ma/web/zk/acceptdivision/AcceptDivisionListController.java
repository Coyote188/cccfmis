package cccf.ma.web.zk.acceptdivision;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.modellite.spring.BeanAdapter;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.AcceptDivisionService;
import cccf.ma.service.ApplicationInfoServiceUtil;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

/**
 */
public class AcceptDivisionListController
		extends GenericForwardComposer
{
	private static final long		serialVersionUID	= -3039777696982952204L;
	private List<Map>				tasklist;
	Map								params				= Executions.getCurrent().getArg();
	private AcceptDivisionService	acceptDivisionService;
	private Combobox				reveiwUser;												// 符合性审查人员控件对象
	private Listbox					applylistLbx;											// 申请列表控件对象
	private AnnotateDataBinder		binder;
	private Grid					appoveGrid;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp); 
	    
	}
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		acceptDivisionService = BeanAdapter.getBean("AcceptDivisionService", AcceptDivisionService.class);
		 tasklist = acceptDivisionService.getToAcceptDivisionList();
		System.out.println(tasklist);
		// 初始化符合性审查人列表
		List reviewUserList = UserInfoServiceUtil.getByRoleName("符合性审查人员");
		System.out.println("reviewUserList=" + reviewUserList);
		page.setAttribute("reviewUserList", reviewUserList);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onSubmit()
			throws InterruptedException
	{
		String reuserId = (String) reveiwUser.getSelectedItem().getValue();
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		Set seltedItems = applylistLbx.getSelectedItems();
		if (seltedItems.isEmpty())
		{
			Messagebox.show("请选择至少一个申请进行受理分工", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		} else
		{
			Listitem item = null;
			for (Iterator it = seltedItems.iterator(); it.hasNext();)// 迭代选中的Item
			{
				item = (Listitem) it.next();// 取得选中的Item
				List<Map> tasks = (List<Map>) item.getValue();// 得到Item上存放的Value
				for (Map map : tasks)// 循环value中的任务列表
				{
					Long taskInstanceId = (Long) map.get("taskId");
					String applicationId = (String) map.get("applicationId");
					System.out.println("user=" + reuserId + ";taskInstanceId=" + taskInstanceId + ";applicationId=" + applicationId);
					BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, user.getId(), applicationId, "ApplicationInfo");
					String approveResult = (String) ((Listbox) appoveGrid.getFellow("resultListbox")).getSelectedItem().getValue();
					BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult, reuserId);
					// 更新application
					ApplicationInfo application = ApplicationInfoServiceUtil.getById(applicationId);
					application.setAcceptUser(user);
					application.setReportUser(reuserId);
					ApplicationInfoServiceUtil.update(application);
				}
			}
			Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			tasklist = acceptDivisionService.getToAcceptDivisionList();
			applylistLbx.setModel(new ListModelList(tasklist));
			 
		}
	}
	public List<Map> getTasklist()
	{
		return tasklist;
	}
	public void setTasklist(List<Map> tasklist)
	{
		this.tasklist = tasklist;
	}
}

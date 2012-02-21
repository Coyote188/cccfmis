package cccf.ma.web.zk.factorycheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import cccf.ma.function.Functions;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.ApplicationPublicInfoServiceUtil;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class FilecheckController extends GenericForwardComposer {
	private static final long serialVersionUID = 7215932601101609815L;

	private ApplicationPublicInfo  applicationPublicInfo;
	private Map checkInfo;
	private Map applyInfo;
	private List<TaskInstanceInfo> taskInstanceInfoList;
	private List reviewUserList;
	private TaskInstanceInfo preTaskInstanceInfo;
	Long taskInstanceId;
	private String userId;
	Map params = Executions.getCurrent().getArg();
	ApplicationInfo application;
	Grid appoveGrid;
	Listbox resultListbox;
	Window frmFilecheck;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		userId = params.get("userId").toString();
		if (params.get("taskInstanceId") != null) {
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		
		if (params.get("taskInstanceId") != null) {
			Long taskInstanceId = Long.parseLong(params.get("taskInstanceId")
					.toString());
			setPreTaskInstanceInfo(TaskInstanceInfoServiceUtil.getPreTaskInstanceInfo(taskInstanceId));
		}
		
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		
		setCheckInfo(Functions.query6ByApplyno(application.getSioid())); 
		setApplyInfo(Functions.getProductInfoListByApplyno(application.getSioid()).get(0));
	}
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		if (params.get("rowId") != null) {
			String rowId = params.get("rowId").toString();
			application = ApplicationInfoServiceUtil.getById(rowId);
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	public void onClick$btnFileCheck(){
		Map params = new HashMap();
		
		String applyNo = (String) ApplicationPublicInfoServiceUtil.findByQuery("select id from ApplicationPublicInfo where applyno = '" + application.getSioid() + "'").get(0);
		params.put("applyId", applyNo);
		params.put("isCreateOperate", false);
		params.put("isShowGrid", false);
		Window objWin = (Window) Executions.createComponents("/SysForm/conformityreview/conformity_review.zul", null, params);
	}
	
	public void onSubmit() {
		if (application.getStauts0() == 7) {
			try {
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		if (taskInstanceId > 0) {
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, application.getId(), "ApplicationInfo");
			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			// 结束tasktanc
			BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			try {
				frmFilecheck.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				// 向桌面发送消息以刷新endtasklist
				EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		} else {
			try {
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
		}
	}

	public void setApplicationPublicInfo(ApplicationPublicInfo applicationPublicInfo) {
		this.applicationPublicInfo = applicationPublicInfo;
	}

	public ApplicationPublicInfo getApplicationPublicInfo() {
		return applicationPublicInfo;
	}

	public void setTaskInstanceInfoList(List<TaskInstanceInfo> taskInstanceInfoList) {
		this.taskInstanceInfoList = taskInstanceInfoList;
	}

	public List<TaskInstanceInfo> getTaskInstanceInfoList() {
		return taskInstanceInfoList;
	}

	public void setCheckInfo(Map checkInfo) {
		this.checkInfo = checkInfo;
	}

	public Map getCheckInfo() {
		return checkInfo;
	}

	public void setPreTaskInstanceInfo(TaskInstanceInfo preTaskInstanceInfo) {
		this.preTaskInstanceInfo = preTaskInstanceInfo;
	}

	public TaskInstanceInfo getPreTaskInstanceInfo() {
		return preTaskInstanceInfo;
	}

	public void setApplyInfo(Map applyInfo) {
		this.applyInfo = applyInfo;
	}

	public Map getApplyInfo() {
		return applyInfo;
	}

}

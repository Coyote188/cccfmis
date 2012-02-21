package cccf.ma.web.zk.tech;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.TechnicalEvaluation;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

public class EvaluationArrangementController extends GenericForwardComposer {
	private static final long serialVersionUID = 1153600205967982447L;
	
	private Map params=Executions.getCurrent().getArg();
	private String applyno=null;
	private String rowId,userId;
	private long taskInstanceId;
	
	private Tabpanel cpTbp;
	private Component comp;
	private Grid appoveGrid;
	private Window frmEvaluationArrangement;
	private Listbox lbxReportUserList;
	private Bandbox bdReportUserList;
	
	public ApplicationInfo application;
	private TechnicalEvaluation technicalEvaluation;
	String processId = "0";
	String cdir = "", attachfilePath = "";
	String entityName = "ApplicationInfo";
	String[] counterSignUsers = null;
	String rePortUserId = "";
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.comp=comp;
		rowId=(String) params.get("rowId");
		ApplicationInfo application=(ApplicationInfo) BaseDAOServcieUtil.findById(ApplicationInfo.class,rowId );
		applyno=application.getSioid();
		createCpForm(applyno);
		taskInstanceId=Long.parseLong((String)params.get("taskInstanceId"));
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		userId=UserInfoServiceUtil.getCurrentLoginUser().getId();
		dataInit();
	}
	
	public void createCpForm(String applyno){
		Map tempMap=new HashMap();
		tempMap.put("applyNo", applyno);
		tempMap.put("isMultiple", true);
		Div div=new Div();
		Window objEnWindow=null;
		objEnWindow=(Window) Executions.createComponents("/SysForm/public_viewer/enterprise_info_viewer.zul", null, tempMap);
		objEnWindow.setParent(div);
		objEnWindow=(Window) Executions.createComponents("/SysForm/public_viewer/product_models_viewer.zul", null, tempMap);
		objEnWindow.setParent(div);
		div.setParent(cpTbp);
		
		Listbox lbxProductModels = (Listbox) objEnWindow.getFellow("lbxProductModels");
		List temp = lbxProductModels.getItems();
		for(Iterator it = temp.iterator();it.hasNext();){
			Listitem item = (Listitem) it.next();
			item.setDisabled(true);
		}
	}
	
	public void dataInit(){
		String queryStr = "FROM TechnicalEvaluation WHERE applicationInfo = '" +application.getId()+ "'";
		technicalEvaluation = (TechnicalEvaluation) ApplicationInfoServiceUtil.findByQuery(queryStr).get(0);
	}
	
	
	
	
	public void onSubmit() throws InterruptedException {
		if (application.getStauts0() == 7) {
			try {
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (taskInstanceId > 0) {
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId,
					entityName);
			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			System.out.println(approveResult);
			// 结束tasktanc
			if (approveResult.equals("结论上报") || approveResult.equals("上报结论")) {
				if (counterSignUsers == null || counterSignUsers.length != 3) {
					throw new WrongValueException(bdReportUserList,
							"评定人员必须是三个.");
				} else {
					System.out.println(counterSignUsers[0]
							+ counterSignUsers[1] + counterSignUsers[2]);
					application.setReportUser(rePortUserId);
//					onSave();
					BpmUtil.endTaskToCountSign(taskInstanceId, approveResult,
							counterSignUsers);
					// BpmUtil.endTaskToCountSign(taskInstanceId, approveResult,
					// counterSignUsers);
				}
			} else {
				BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			}
			try {
				// /编号
				frmEvaluationArrangement.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK,
						Messagebox.INFORMATION);
				// 向桌面发送消息以刷新endtasklist
				EventQueues.lookup(userId + "refreshEndTaskListEvent",
						EventQueues.APPLICATION, true).publish(
						new Event("onMsgEventQueue", null, ""));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void onSelect$lbxReportUserList() {
		Set<Listitem> list = lbxReportUserList.getSelectedItems();
		String uName = "";
		rePortUserId = "";
		if (list.size() > 0) {
			counterSignUsers = new String[list.size()];
			int i = 0;
			for (Listitem item : list) {
				UserInfo user = (UserInfo) item.getValue();
				if (user != null && user.getId() != null) {
					uName = user.getName() + "," + uName;
					counterSignUsers[i] = user.getId();
					rePortUserId = user.getId() + "," + rePortUserId;
					i++;
				}
			}
			bdReportUserList.setText(uName);
			System.out.println(">-------->" + uName);
		} else {
			counterSignUsers = null;
			bdReportUserList.setText(null);
		}
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		
		String pid = "";

		if (params.get("rowId") != null) {
			String rowId = params.get("rowId").toString();
			application = ApplicationInfoServiceUtil.getById(rowId);
			if (application != null)
				pid = application.getProduction().getId();
		}
		List reviewUserList = ProductCatalogueInfoServiceUtil.getUserArrayByIdAndType(pid, 0);
		page.setAttribute("reviewUserList", reviewUserList);
		
		return super.doBeforeCompose(page, parent, compInfo);
	}

	public TechnicalEvaluation getTechnicalEvaluation() {
		return technicalEvaluation;
	}

	public void setTechnicalEvaluation(TechnicalEvaluation technicalEvaluation) {
		this.technicalEvaluation = technicalEvaluation;
	}

}

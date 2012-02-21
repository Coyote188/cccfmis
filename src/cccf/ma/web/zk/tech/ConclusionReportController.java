package cccf.ma.web.zk.tech;

import java.util.HashMap;
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
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Radio;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ProductModel;
import cccf.ma.model.TechnicalEvaluation;
import cccf.ma.model.TechnicalEvaluationOpinion;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.TechnicalEvaluationService;

import com.aidi.bpm.model.Approve;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

@SuppressWarnings("unchecked")
public class ConclusionReportController extends GenericForwardComposer {
	private static final long serialVersionUID = 55012959968245685L;

	private Map params=Executions.getCurrent().getArg();
	private String applyno=null;
	private String rowId,userId;
	private long taskInstanceId;
	
	private Tabpanel cpTbp;
	private Component comp;
	private Grid appoveGrid;
	private Window frmConclusionReport;
	private Listbox resultListbox;
	private Textbox opinionTbx;
	Window objEnWindow=null;
	
	public ApplicationInfo application;
	private TechnicalEvaluation technicalEvaluation;
	private TechnicalEvaluationService technicalEvaluationService;
	String processId = "0";
	String cdir = "", attachfilePath = "";
	String entityName = "ApplicationInfo";
	String[] counterSignUsers = null;
	String rePortUserId = "";
	private String operatetype=null;
	
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
		technicalEvaluationService=BeanAdapter.getBean("TechnicalEvaluationService", TechnicalEvaluationService.class);
		dataInit();
	}
	
	public void createCpForm(String applyno){
		Map tempMap=new HashMap();
		tempMap.put("applyNo", applyno);
		tempMap.put("isMultiple", true);
		Div div=new Div();
		
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
		String queryStr = "FROM TechnicalEvaluation WHERE applicationInfo.id = '" +application.getId()+ "'";
		technicalEvaluation = (TechnicalEvaluation) ApplicationInfoServiceUtil.findByQuery(queryStr).get(0);
		System.out.println(technicalEvaluation+technicalEvaluation.getCertificateValid());
	}
	
	public void onCheckOperateType(ForwardEvent event)
	{
		Radio operatetypeRdo=(Radio) event.getOrigin().getTarget();
		operatetype=operatetypeRdo.getLabel();
		if("发证".equals(operatetype))
		{
			Listbox pmLbx=(Listbox) objEnWindow.getFellow("lbxProductModels");
			Set items=pmLbx.getSelectedItems();
			opinionTbx.setValue("符合规则要求，建议发证"+items.size()+"张;");
		}
		else
			opinionTbx.setValue("");
	}
	
	public void onSubmit() throws Exception {
		if(Messagebox.YES != Messagebox.show("是否确认提交表单信息？","操作提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION))
			return;
		TechnicalEvaluationOpinion teEvaOpi=new TechnicalEvaluationOpinion();
		 teEvaOpi.setTechnicalEvaluation(technicalEvaluation);
		 teEvaOpi.setEvaluationPhase("技术初评");
		 teEvaOpi.setOpinionContent(opinionTbx.getValue());
		 teEvaOpi.setMemo(((Textbox)appoveGrid.getFellow("approveMemo")).getValue());
		 teEvaOpi.setEvaluationPersonId(userId);
		 
		 Approve approve =new Approve();
		 approve.setApproveResult(((Listbox)appoveGrid.getFellow("resultListbox")).getSelectedItem().getLabel());
		 approve.setApproveMemo(((Textbox)appoveGrid.getFellow("approveMemo")).getValue());
		 approve.setUserId(userId);
		 technicalEvaluationService.doEvaluation(teEvaOpi, application.getId(), approve);
		 
		 Messagebox.show("信息提交成功!","操作提示",Messagebox.OK,Messagebox.INFORMATION);
		 comp.detach();
		 EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
		 
//		if (taskInstanceId > 0) {
//			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, application.getId(), "ApplicationInfo");
//			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
//			Row row = approveRows.get(0);
//			String approveResult = null;
//			if (resultListbox.getSelectedItem() != null)
//				approveResult = resultListbox.getSelectedItem().getLabel();
//			// 结束tasktanc
//			BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
//			try {
//				frmConclusionReport.detach();
//				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
//				// 向桌面发送消息以刷新endtasklist
//				EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
//			} catch (InterruptedException e) {
//				
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				Messagebox.show("任务结点绑定错误!");
//			} catch (InterruptedException e1) {
//				
//				e1.printStackTrace();
//			}
//		}
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
		
		return super.doBeforeCompose(page, parent, compInfo);
	}

	public TechnicalEvaluation getTechnicalEvaluation() {
		return technicalEvaluation;
	}

	public void setTechnicalEvaluation(TechnicalEvaluation technicalEvaluation) {
		this.technicalEvaluation = technicalEvaluation;
	}

}

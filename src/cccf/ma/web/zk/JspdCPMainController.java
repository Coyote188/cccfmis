package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Radio;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ProductModel;
import cccf.ma.model.TechnicalEvaluation;
import cccf.ma.model.TechnicalEvaluationOpinion;
import cccf.ma.service.TechnicalEvaluationService;

import com.aidi.bpm.model.Approve;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

/**
 * @author [JOINYO]
 * @E-mail JOINYO@YEAH.NET
 * @date 创建于2011-5-22下午09:07:12
 * @version 1.0

 * @describe
 * ALL RIGHTS RESERVED,COPYRIGHT(C) FCH LIMITED 2011
 */
public class JspdCPMainController extends GenericForwardComposer{
	private static final long serialVersionUID = 1L;
	private Map params=Executions.getCurrent().getArg();
	private String applyno=null;
	private Tabpanel cpTbp;
	private Component comp;
	private Grid appoveGrid;
	private String rowId,userId;
	private long taskInstanceId;
	private Window objPmWindow;
	private TechnicalEvaluationService technicalEvaluationService;
	private List<String> modelIds=null;
	private Textbox opinionTbx;
	private Listbox limitLbx,noLbx;
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
		modelIds=new ArrayList<String>();
	}
	public void createCpForm(String applyno)
	{
		Map tempMap=new HashMap();
		tempMap.put("applyNo", applyno);
		tempMap.put("isMultiple", true);
		Div div=new Div();
		Window objEnWindow=null;
		objEnWindow=(Window) Executions.createComponents("/SysForm/public_viewer/enterprise_info_viewer.zul", null, tempMap);
		objEnWindow.setParent(div);
		objPmWindow=(Window) Executions.createComponents("/SysForm/public_viewer/product_models_viewer.zul", null, tempMap);
		objPmWindow.setParent(div);
		div.setParent(cpTbp);
	}
	public void onSubmit() throws InterruptedException
	{
		if((modelIds==null||modelIds.size()==0)&&"发证".equals(operatetype))
		{
			Messagebox.show("请选择评定产品");
			return;
		}
		if(Messagebox.show("是否确认提交表单信息？","操作提示",Messagebox.OK|Messagebox.NO,Messagebox.QUESTION)==Messagebox.OK)
		{
			
			 System.out.println("modelIds="+modelIds);
			 TechnicalEvaluation teEval=new TechnicalEvaluation();
			 teEval.setCertificateValid(limitLbx.getSelectedItem().getLabel());
			 teEval.setEntTollCode(noLbx.getSelectedItem().getLabel());
			 teEval.setOperateType(operatetype);
			 
			 TechnicalEvaluationOpinion teEvaOpi=new TechnicalEvaluationOpinion();
			 teEvaOpi.setTechnicalEvaluation(teEval);
			 teEvaOpi.setEvaluationPhase("技术初评");
			 teEvaOpi.setOpinionContent(opinionTbx.getValue());
			 teEvaOpi.setMemo(((Textbox)appoveGrid.getFellow("approveMemo")).getValue());
			 teEvaOpi.setEvaluationPersonId(userId);
			 
			 Approve approve =new Approve();
			 approve.setApproveResult(((Listbox)appoveGrid.getFellow("resultListbox")).getSelectedItem().getLabel());
			 approve.setApproveMemo(((Textbox)appoveGrid.getFellow("approveMemo")).getValue());
			 approve.setUserId(userId);
			 technicalEvaluationService.doInitializeEvaluation(teEval, modelIds, teEvaOpi, rowId,approve);
			 
			 Messagebox.show("信息提交成功!","操作提示",Messagebox.OK,Messagebox.INFORMATION);
			 comp.detach();
			 EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
		}
	}
	public void onCheckOperateType(ForwardEvent event)
	{
		Radio operatetypeRdo=(Radio) event.getOrigin().getTarget();
		operatetype=operatetypeRdo.getLabel();
		if("发证".equals(operatetype))
		{
			modelIds.clear();
			Listbox pmLbx=(Listbox) objPmWindow.getFellow("lbxProductModels");
			Set items=pmLbx.getSelectedItems();
			int i=0;
			for(Iterator it=items.iterator();it.hasNext();)
			{
				 Listitem item=(Listitem) it.next();
				 modelIds.add((String) item.getValue());
				 ProductModel productModel=(ProductModel) BaseDAOServcieUtil.findById(ProductModel.class, (String) item.getValue());
				 if(productModel.getIsMainModel())
				 {
					 i++;
				 }
			 }
			opinionTbx.setValue("符合规则要求，建议发证"+i+"张;");
		}
		else
			opinionTbx.setValue("");
	}
}

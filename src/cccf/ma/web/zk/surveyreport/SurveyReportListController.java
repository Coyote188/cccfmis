package cccf.ma.web.zk.surveyreport;
  
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.aidi.core.service.BaseDAOServcieUtil;
 

import cccf.ma.common.CommomsController;
import cccf.ma.common.ObjectUtils;
import cccf.ma.model.SurveyReport; 
import cccf.ma.service.SurveyReportService;
 

/**
 * 检验报告 —— 控制类
 */
public class SurveyReportListController  extends GenericForwardComposer { 
	private static final long serialVersionUID = -3039777696982952204L; 
	private Groupbox gb_query;
	//报告状态
	private Checkbox cb_status ;
	private Combobox cbb_status;
	//报告编号
	private Checkbox cb_surveyReportSN; 
	private Textbox tb_surveyReportSN; 
	//企业名称
	private Checkbox cb_enterpriseInfo_name; 
	private Textbox tb_enterpriseInfo_name; 
	//产品名称
	private Checkbox cb_productCatalogueInfo_productName; 
	private Textbox tb_productCatalogueInfo_productName; 
	
    //检验报告列表
	private List<Map> surveyReportList; 
	private Listbox lb_surveyReport;
	private Paging listPaging;
	 
	private Component comp;
	private AnnotateDataBinder binder;
	
	private SurveyReportService surveyReportService;
	
	private StringBuffer select_pageDatas = new StringBuffer("select new map(")
	                              .append("id as id")
	                              .append(",")
	                              .append("surveyReportSN as surveyReportSN")
	                              .append(",")
	                              .append("enterpriseInfo.name as enterpriseInfo_name")
	                              .append(",")
	                              .append("productCatalogueInfo.productName as productCatalogueInfo_productName")
	                              .append(",")
	                              .append("manufactureInfo.name as manufactureInfo_name")
	                              .append(",")
	                              .append("productionEnterpriseInfo.name as productionEnterpriseInfo_name")
	                              .append(",")
	                              .append("surverType as surverType")
	                              .append(",")
	                              .append("surverVerdict as surverVerdict")
	                              .append(",")
	                              .append("organizer as organizer")
	                              .append(",")
	                              .append("organizeTime as organizeTime")
	                              .append(",")
	                              .append("auditor as auditor")
	                              .append(",")
	                              .append("auditTime as auditTime")
	                              .append(",")
	                              .append("surveyFromDate as surveyFromDate")
	                              .append(",")
	                              .append("surveyToDate as surveyToDate")
	                              .append(",")
	                              .append("surverOrgName as surverOrgName")
	                              .append(",")
	                              .append("surverAccording as surverAccording")
	                              .append(",")
	                              .append("surverVerdictContent as surverVerdictContent")
	                              .append(",")
	                              .append("issuingDate as issuingDate")
	                              .append(",")
	                              .append("agentOrg as agentOrg")
	                              .append(",")
	                              .append("samplingOrg as samplingOrg")
	                              .append(",")
	                              .append("status as status")
	                              .append(")");
	private StringBuffer select_pageSize =  new StringBuffer("select count(o)");
	private StringBuffer from_where = new StringBuffer(" from SurveyReport o where 1=1"); 
	  
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		this.comp = comp; 
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		surveyReportService = BeanAdapter.getBean("SurveyReportService", SurveyReportService.class);
		binder.loadAll();
	}
	
	public void onShowCount(ForwardEvent event){  
		Paging listPaging=(Paging) event.getOrigin().getTarget();
		CommomsController.pagingListboxBinder(listPaging, lb_surveyReport, select_pageSize.toString()+from_where ,select_pageDatas.toString()+from_where );  
	} 
	
	//打开添加窗口
	public void onAdd() throws SuspendNotAllowedException, InterruptedException{
		 
		Window win = (Window) Executions.createComponents("/SysForm/SurveyReport/SurveyReport_form.zul",
				null, null); 
	 
        win.addEventListener(Events.ON_CLOSE, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				CommomsController.pagingListboxBinder(listPaging, lb_surveyReport, select_pageSize.toString()+from_where  ,select_pageDatas.toString()+from_where  );  
			}
		});

		win.setClosable(true);
		win.setMaximizable(true);
		win.doModal();
		
		
	}
	public void onRead(ForwardEvent event) throws SuspendNotAllowedException, InterruptedException{
		Button btn=(Button) event.getOrigin().getTarget();
		doSurveyReportRead(btn,true);
	}
	private void doSurveyReportRead(Button btn,boolean isRead) throws SuspendNotAllowedException, InterruptedException{ 
		final Map item = (Map) btn.getAttribute("surveyReportItem");
		if(!isRead){
			item.put("toAudit", false);
		}
		// 获得检验报告中的产品型号
		String hql = "select new map(o.id as id,o.specification as specification,o.characterization as characterization ,isMainModel as isMainModel)" +
				" from ProductModel o" +
				" where o.surveyReport.id='"+item.get("id")+"'";
		List<Map> productModelList = surveyReportService.getResultList(hql);
		 
		//获得检验报告中产品型号的附件
		hql = "select new map(o.productModel.id as productModel_id,o.name as name,o.attachment.name as fileName) from ProductModelAttachment o where o.productModel.surveyReport.id='"+item.get("id")+"'";
	 	  
		List<Map> productModelAttachmentList = surveyReportService.getResultList(hql);
		//关联产品型号附件 
	 	HashMap<String,Map> tmpMap = new HashMap<String,Map>();
	 	for(Map i : productModelList){
	 		tmpMap.put((String) i.get("id"), i);
	 		i.put("productModelAttachmentList", new ArrayList ());
	 	}
	 	for(Map i : productModelAttachmentList){
	 		Map productModel=tmpMap.get((String) i.get("productModel_id"));
	 		List list = (List) productModel.get("productModelAttachmentList");
	 		list.add(i);
	 		productModel.put("productName",item.get("productCatalogueInfo_productName"));
	 	}
	 	
	 	item.put("productModelList",productModelList);  
		Window win = (Window) Executions.createComponents("/SysForm/SurveyReport/SurveyReport_read.zul",
				comp, item); 
		
		 win.addEventListener(Events.ON_CLOSE, new EventListener() { 
				@Override
				public void onEvent(Event event) throws Exception {
					item.put("toAudit", null);
					if(event==null || event.getData()==null)return;
					int iRet = (Integer)event.getData();
					if(iRet!=-1){
						if(iRet==0){
							String id = (String) item.get("id");
				        	SurveyReport bo = (SurveyReport) BaseDAOServcieUtil.findById(SurveyReport.class, id);
				        	bo.setStatus(2);
				        	bo.setAuditor(UserInfoServiceUtil.getCurrentLoginUser().getId());
				        	bo.setAuditTime(ObjectUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				        	BaseDAOServcieUtil.upDate(bo);
				        	item.put("status", 2); 
						}else if(iRet==1){
							String id = (String) item.get("id");
				        	SurveyReport bo = (SurveyReport) BaseDAOServcieUtil.findById(SurveyReport.class, id);
				        	bo.setAuditor(UserInfoServiceUtil.getCurrentLoginUser().getId());
				        	bo.setAuditTime(ObjectUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				        	bo.setStatus(1);
				        	item.put("status", 1);
				        	BaseDAOServcieUtil.upDate(bo);
						}
						CommomsController.pagingListboxBinder(listPaging, lb_surveyReport, select_pageSize.toString()+from_where ,select_pageDatas.toString()+from_where  );  

					}
				}
		 });
	 
		win.setClosable(true);
		win.setMaximizable(true);
		win.doModal();
	}
	
	public void onAudit(ForwardEvent event) throws InterruptedException{
		Button btn=(Button) event.getOrigin().getTarget();
		doSurveyReportRead(btn,false); 
	}
	public void onToRepeatAudit(ForwardEvent event) throws InterruptedException{
		Button btn=(Button) event.getOrigin().getTarget();
		Map item = (Map) btn.getAttribute("surveyReportItem");
		String id = (String) item.get("id");
    	SurveyReport bo = (SurveyReport) BaseDAOServcieUtil.findById(SurveyReport.class, id);
    	bo.setStatus(3);
        bo.setOrganizer(UserInfoServiceUtil.getCurrentLoginUser().getId());
        bo.setOrganizeTime(ObjectUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    	item.put("status", 3);
    	BaseDAOServcieUtil.upDate(bo); 
    	 CommomsController.pagingListboxBinder(listPaging, lb_surveyReport, select_pageSize.toString()+from_where ,select_pageDatas.toString()+from_where );  
    		
	}
	
	public void onQuery()  { 
		StringBuffer where=new StringBuffer(); 
		 if(cb_surveyReportSN.isChecked()){
			 where.append(" and surveyReportSN='"+tb_surveyReportSN.getText()+"'"); 
		 }
		 if(cb_enterpriseInfo_name.isChecked()){
			 where.append(" and enterpriseInfo.name LIKE'"+tb_enterpriseInfo_name.getText()+"%'"); 
		 } 
		 if(cb_productCatalogueInfo_productName.isChecked()){
			 where.append(" and productCatalogueInfo.productName LIKE'"+tb_productCatalogueInfo_productName.getText()+"%'"); 
		 }  
		 if(cb_status.isChecked()){
			 if(cbb_status.getSelectedItem()!=null ){
				Object value =  cbb_status.getSelectedItem().getValue();
		        if( "-1".equals(value)){
		        	 where.append(" and status='"+cbb_status.getSelectedItem().getValue()+"'"); 
		        }
			 } 
		 }
		
	    CommomsController.pagingListboxBinder(listPaging, lb_surveyReport, select_pageSize.toString()+from_where+where  ,select_pageDatas.toString()+from_where+where );  
		 
	}
 

	public List getSurveyReportList() {
		return surveyReportList;
	}

	public void setSurveyReportList(List surveyReportList) {
		this.surveyReportList = surveyReportList;
	}

	 
}

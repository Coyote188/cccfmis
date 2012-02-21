package cccf.mis.web.survey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer; 
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.aidi.core.service.BaseDAOServcieUtil;
 
import cccf.ma.common.ObjectUtils;
import cccf.ma.model.SurveyReport;
import cccf.ma.service.SurveyReportService;

public class SurveyReportController
		extends GenericForwardComposer
{
	private static final long	serialVersionUID	= 296458720895333944L;
	private Listbox				reportbox;
	private Paging				listPaging;
	private StringBuffer		select_pageDatas	= new StringBuffer("select new map(").append("id as id").append(",").append("surveyReportSN as surveyReportSN").append(",").append("enterpriseInfo.name as enterpriseInfo_name").append(",")
															.append("productCatalogueInfo.productName as productCatalogueInfo_productName").append(",").append("manufactureInfo.name as manufactureInfo_name").append(",")
															.append("productionEnterpriseInfo.name as productionEnterpriseInfo_name").append(",").append("surverType as surverType").append(",").append("surverVerdict as surverVerdict").append(",")
															.append("organizer as organizer").append(",").append("organizeTime as organizeTime").append(",").append("auditor as auditor").append(",").append("auditTime as auditTime").append(",")
															.append("surveyFromDate as surveyFromDate").append(",").append("surveyToDate as surveyToDate").append(",").append("surverOrgName as surverOrgName").append(",")
															.append("surverAccording as surverAccording").append(",").append("surverVerdictContent as surverVerdictContent").append(",").append("issuingDate as issuingDate").append(",")
															.append("agentOrg as agentOrg").append(",").append("samplingOrg as samplingOrg").append(",").append("status as status").append(")");
	private StringBuffer		select_pageSize		= new StringBuffer("select count(o)");
	private StringBuffer		from_where			= new StringBuffer(" from SurveyReport o where 1=1");
	private StringBuffer		order_by			= new StringBuffer(" order by organizeTime desc");
	private SurveyReportService	svc;
	private Component comp;
	
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		this.comp = comp;
		listPaging.addForward("onPaging", self, "onPage");
	}
	public void onCreated(ForwardEvent event)
	{
		svc = BeanAdapter.getBean("SurveyReportService", SurveyReportService.class);
		svc.querySingleResult(select_pageSize.toString() + from_where.toString() );
		List<?> datas = svc.queryListForPage(0, listPaging.getPageSize(), select_pageDatas.toString() + from_where.toString() + order_by.toString());
		 
		listPaging.setActivePage(0);
		reportbox.setModel(new ListModelList(datas));
	}
	public void onPage(ForwardEvent event)
	{
		PagingEvent pe = (PagingEvent) event.getOrigin();
		int pgno = pe.getActivePage();// 页数(从零计算)
		int start = pgno * listPaging.getPageSize();
		List<?> datas = svc.queryListForPage(0, listPaging.getPageSize(), select_pageDatas.toString() + from_where.toString() + order_by.toString());
		reportbox.setModel(new ListModelList(  datas ));
	}
	public void onNewReport(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		Window objWindow = (Window) Executions.createComponents("/views/SurveyReport/SurveyReport.zul", null, null);
		objWindow.doModal();
		if ("OK".equals(objWindow.getAttribute("submit")))
		{ 
			objWindow.detach();
			query();
		}
		
	}
	
	private void query(){
		svc.querySingleResult(select_pageSize.toString() + from_where.toString());
		List<?> datas = svc.queryListForPage(0, listPaging.getPageSize(), select_pageDatas.toString() + from_where.toString() + order_by.toString());
		 
		listPaging.setActivePage(0);
		reportbox.setModel(new ListModelList(datas));
	}
	public void onQueryReport(ForwardEvent event){
		query();
	}
	
	
	private void doSurveyReportRead(final Map itemValue,boolean isRead) throws SuspendNotAllowedException, InterruptedException{ 
		 
		if(!isRead){
			itemValue.put("toAudit", true);
		}else{
			itemValue.put("toAudit", false);
		}
		
		 Window win = (Window) Executions.createComponents("/views/SurveyReport/SurveyReport_read.zul", null, itemValue); 
		
		 win.addEventListener(Events.ON_CLOSE, new EventListener() { 
				@Override
				public void onEvent(Event event) throws Exception {
					itemValue.put("toAudit", null);
					if(event==null || event.getData()==null)return;
					int iRet = (Integer)event.getData();
					if(iRet!=-1){
						if(iRet==0){
							String id = (String) itemValue.get("id");
				        	SurveyReport bo = (SurveyReport) BaseDAOServcieUtil.findById(SurveyReport.class, id);
				        	bo.setStatus(2);
				        	bo.setAuditor(UserInfoServiceUtil.getCurrentLoginUser().getId());
				        	bo.setAuditTime(ObjectUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				        	BaseDAOServcieUtil.upDate(bo);
				        	itemValue.put("status", 2); 
						}else if(iRet==1){
							String id = (String) itemValue.get("id");
				        	SurveyReport bo = (SurveyReport) BaseDAOServcieUtil.findById(SurveyReport.class, id);
				        	bo.setAuditor(UserInfoServiceUtil.getCurrentLoginUser().getId());
				        	bo.setAuditTime(ObjectUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				        	bo.setStatus(1);
				        	itemValue.put("status", 1);
				        	BaseDAOServcieUtil.upDate(bo);
						}
						query();
					}
				}
		 });
	 
		win.setClosable(true);
		win.setMaximizable(true);
		win.doModal();
	}
	
	public void onAudit(ForwardEvent event) throws InterruptedException{
		Map itemValue = (Map)reportbox.getSelectedItem().getValue();
		System.out.println(itemValue.getClass().getName());
		doSurveyReportRead(itemValue,false); 
	}
}

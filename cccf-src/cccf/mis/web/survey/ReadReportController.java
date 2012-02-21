package cccf.mis.web.survey;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;

import cccf.ma.service.SurveyReportService;
 

  

public class ReadReportController
		extends GenericForwardComposer
{ 
	private static final long	serialVersionUID	= 8260028457932691461L; 
	private Window readWin;
	private Map params = Executions.getCurrent().getArg();
	private SurveyReportService	svc = BeanAdapter.getBean("SurveyReportService", SurveyReportService.class);
	
 
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{ 
		// 获得检验报告中的产品型号
		String hql = "select new map(o.id as id,o.specification as specification,o.characterization as characterization ,isMainModel as isMainModel)" +
				" from ProductModel o" +
				" where o.surveyReport.id='"+params.get("id")+"'";
		List<Map> productModelList = svc.getResultList(hql);
		 
		//获得检验报告中产品型号的附件
		hql = "select new map(o.productModel.id as productModel_id,o.name as name,o.attachment.name as fileName) from ProductModelAttachment o where o.productModel.surveyReport.id='"+params.get("id")+"'";
	 	  
		List<Map> productModelAttachmentList = svc.getResultList(hql);
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
	 		productModel.put("productName",params.get("productCatalogueInfo_productName"));
	 	}
	 	
	 	params.put("productModelList",productModelList);  
	 	System.out.println(params); 
	 	page.setAttribute("surveyReport", params);
		
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	public void onPass(ForwardEvent event){
		Events.sendEvent(Events.ON_CLOSE, readWin, 1);
	}
	 
	public void onUnPass(ForwardEvent event){
		Events.sendEvent(Events.ON_CLOSE, readWin, 0);
	}
	
	public void onCloseWin(ForwardEvent event){
		Events.sendEvent(Events.ON_CLOSE, readWin, -1);
	}
	 
}

package cccf.mis.web.tsak.evaluation.arrange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import cccf.ma.function.Functions;
import cccf.ma.model.TechnicalEvaluationReportOpinion;
import cccf.ma.service.TechnicalEvaluationArrangeService;

public class ArrangeController
		extends GenericForwardComposer
{
	private Map selectItem;
	
	private Combobox  cb_cp ;
	private Combobox  cb_fp ;
	private Combobox  cb_pd ;
	private Textbox  tb_memo;
	
	TechnicalEvaluationArrangeService svc = BeanAdapter.getBean("TechnicalEvaluationArrangeService", TechnicalEvaluationArrangeService.class);
	
	
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{ 
		selectItem = (Map) Executions.getCurrent().getArg().get("report");  
		String applyno = (String) selectItem.get("applyno");
		
		// 初始化技术评定人员列表
		List<?> reviewUserList = UserInfoServiceUtil.getByRoleName("技术评定人员"); 
		page.setAttribute("reviewUserList", reviewUserList);
		
		/**申请企业信息**/
		StringBuffer hql = new StringBuffer("select new map( ") 
	        .append("o.applyEnterprise.name as name")  // 申请企业
	        .append(",o.applyEnterprise.faxNum as faxNum")      // 专真
	        .append(",o.applyEnterprise.contactPerson as contactPerson")  // 联系人
	        .append(",o.applyEnterprise.telephoneNum as telephoneNum")        // 联系电话
	        .append(",o.applyEnterprise.contactAddress as contactAddress")    //通讯地址
	        .append(",o.applyEnterprise.postalcode as postalcode")              //邮政编码
	        .append(",o.applyEnterprise.nameEn as nameEn")              //企业英文名
			.append(",o.applyEnterprise.contactAddressEn as contactAddressEn")  
			.append(",c.contractNo as contractNo")//企业英文地址 
			.append(")")
			.append(" from ApplicationPublicInfo o,Contract c")
			.append(" where c.applyno=o.applyno and o.applyno='").append(applyno).append("'"); 
	
		Map applyEnt = (Map) svc.querySingleResult(hql.toString());
		page.setAttribute("applyEnt", applyEnt);  //  申请企业信息
		
		/**申请产品信息**/
		List<Map> listProductModel = Functions.getProductInfoListByApplyno(applyno);
		HashMap tmpMap = new HashMap();
		for(Map item : listProductModel){
			item.get("id");
			item.put("checked", false);
			tmpMap.put(item.get("id"), item);
		}
	    hql = new StringBuffer("select new map( ") 
	        .append("o.productModel.id as id")   
			.append(")")
			.append(" from ApplicationInfoProductModel o , ProductModelCertification c")
			.append(" where o.productModel = c.productModel")
			.append(" and c.status=0 ")
			.append(" and o.applicationInfo.sioid='").append(applyno).append("'"); 
	    List<Map> checkedIds = svc.getResultList(hql.toString());
	    for(Map item : checkedIds){
	    	Map pm = (Map)tmpMap.get(item.get("id"));
	    	if(pm!=null){
	    		pm.put("checked", true);
	    	}
	    }
	    page.setAttribute("products", listProductModel);  //  申请企业信息
	    
	    /**技术评定报告 **/
	    hql = new StringBuffer("select DISTINCT new map( ") 
	        .append("r.reportNo as reportNo")   
	        .append(",r.certificateValid as certificateValid")  //证书有效期 
	        .append(",r.entTollCode as entTollCode")  // 企业人数代码
	        .append(",r.operateType as operateType")  // 操作类型 
	        .append(",p.approveMemo as memo")  //建议操作
			.append(")")
			.append(" from TechnicalEvaluationReport r,ProcessLog p,ApplicationInfo a")
			.append(" where a.id=p.boId ") 
			.append(" and r.applyno=a.sioid ")
			.append(" and p.boType='ApplicationInfo' ")
			.append(" and p.taskInstanceName='生成报告'")
			.append(" and p.actorId=r.creater")
			.append(" and r.applyno='").append(applyno).append("'"); 
	     Map  report = (Map) svc.querySingleResult(hql.toString());
	     page.setAttribute("report", report);  //  申请企业信息
	     
	     /**评定内容**/
	     hql = new StringBuffer("select DISTINCT new map( ") 
	        .append("o.phase as evaluationPhase")   //评定阶段 ：报告生成、技术初评、技术复评、技术评定 
	        .append(",o.approveResult as approveResult")    //评定结果
	        .append(",o.evaluationTime as evaluationTime")   //评定时间
	        .append(",o.opinionContent as opinionContent")  // 意见内容
	        .append(",o.memo as memo")  //备   注
	        .append(",u.name as opName")  //评定人
	        .append(",o.sn as sn")   
			.append(")")
			.append(" from TechnicalEvaluationReport r, TechnicalEvaluationReportOpinion o,UserInfo u")
			.append(" where o.technicalEvaluationReport=r") 
			.append(" and o.evaluationPersonId = u.id")
			.append(" and r.applyno='").append(applyno).append("'")
			.append(" order by o.sn"); 
	     List<Map> listOpinion = svc.getResultList(hql.toString());
	     HashMap opinion = new HashMap();
	     for(int i=0;i<4;i++){
	    	 opinion.put("opName_r"+(i+1), "-");
	    	 opinion.put("approveResult_r"+(i+1), "-");
	    	 opinion.put("opinionContent_r"+(i+1), "-");
	    	 opinion.put("memo_r"+(i+1), "-");
	    	 opinion.put("evaluationTime_r"+(i+1), "-");
	     }
	     for(Map item : listOpinion){
	    	String evaluationPhase = (String) item.get("evaluationPhase");
	    	int i=0;
	    	if("生成报告".equals(evaluationPhase)){
	    		i=1;
	    	}else if("初评".equals(evaluationPhase)){
	    		i=2;
	    	}else if("复评".equals(evaluationPhase)){
	    		i=3;
	    	}else if("评定".equals(evaluationPhase)){
	    		i=4;
	    	}
		    
	    	 opinion.put("opName_r"+(i), item.get("opName"));
	    	 opinion.put("approveResult_r"+(i), item.get("approveResult"));
	    	 opinion.put("opinionContent_r"+(i), item.get("opinionContent"));
	    	 opinion.put("memo_r"+(i), item.get("memo"));
	    	 opinion.put("evaluationTime_r"+(i), item.get("evaluationTime"));
		 }
	     page.setAttribute("opinion", opinion);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1305525568355883871L;
	public void onSubmit(ForwardEvent event)
			throws InterruptedException
	{
		
		if(Messagebox.show("是否确认提交表单信息？","操作提示",Messagebox.OK|Messagebox.NO,Messagebox.QUESTION)==Messagebox.OK)
		{ 
				
			//技术评定报告
			 
			ArrayList<TechnicalEvaluationReportOpinion> doTeskUsers = new ArrayList<TechnicalEvaluationReportOpinion>();
			TechnicalEvaluationReportOpinion opinion_cp = new TechnicalEvaluationReportOpinion(); 
			opinion_cp.setPhase("初评");
			opinion_cp.setSn(2);
			opinion_cp.setEvaluationPersonId((String)cb_cp.getSelectedItem().getValue()); 
			doTeskUsers.add(opinion_cp);
			TechnicalEvaluationReportOpinion opinion_fp = new TechnicalEvaluationReportOpinion(); 
			opinion_fp.setPhase("复评");
			opinion_fp.setSn(3);
			opinion_fp.setEvaluationPersonId((String)cb_fp.getSelectedItem().getValue()); 
			doTeskUsers.add(opinion_fp);
			TechnicalEvaluationReportOpinion opinion_pd = new TechnicalEvaluationReportOpinion(); 
			opinion_pd.setPhase("评定");
			opinion_pd.setSn(4);
			opinion_pd.setEvaluationPersonId((String)cb_pd.getSelectedItem().getValue()); 
			doTeskUsers.add(opinion_pd);
		 
			String approveResult = "结论上报";
			String approveMemoValue = tb_memo.getText();
			
			String reportNo = (String)selectItem.get("reportNo");
			String opid = UserInfoServiceUtil.getCurrentLoginUser().getId();
			
			svc.doEvaluationArrange(reportNo, opid, doTeskUsers, approveResult, approveMemoValue);
			 
			self.setAttribute("submit", "OK");
			self.setVisible(false);
		}
	}
}

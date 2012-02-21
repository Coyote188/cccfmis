package cccf.ma.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aidi.core.service.BaseDAOServcieUtil;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.Certification;
import cccf.ma.model.ProcessLog;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductModelCertification;
import cccf.ma.model.TechnicalEvaluation;
import cccf.ma.model.TechnicalEvaluationReport;
import cccf.ma.service.TechnicalEvaluationApprovalService;
import cccf.ma.service.WorkFlowService;

public class TechnicalEvaluationApprovalServiceImpl extends AbstractBaseService
		implements TechnicalEvaluationApprovalService {
	private WorkFlowService workFlowService; 

	@Override
	public void doEvaluationApproval(TechnicalEvaluationReport report,
			String usid, String approveResult, String approveMemo) { 
		StringBuffer hql = new StringBuffer("select o from TechnicalEvaluationReport o")
		        .append(" where o.reportNo ='").append(report.getReportNo()).append("'");  
		TechnicalEvaluationReport entity = (TechnicalEvaluationReport)  querySingleResult(hql.toString());
		if(entity==null){
		   throw new RuntimeException("评定报告找不到！");
		}
		// 证书处理
		if ("通过".equals(approveResult)) {
			  hql = new StringBuffer("select a from TechnicalEvaluationReport o , ApplicationInfo a where a.sioid='")
			                       .append(entity.getApplyno()).append("'"); 
		     List<ApplicationInfo> list =  getResultList(hql.toString());
		     for(ApplicationInfo item : list){
		    	 evaluationApproval(entity ,item ,usid ,approveResult ,approveMemo );
		     }
		}
		
	   //流程处理
	    hql = new StringBuffer("select pl from ApplicationInfo o , ProcessLog pl ,TechnicalEvaluationReport r ")
		        .append(" where pl.boId=o.id and pl.end is null and o.sioid=r.applyno ")
		        .append(" and r.reportNo ='").append(entity.getReportNo()).append("'");
	    List<ProcessLog> pList = getResultList(hql.toString()); 

		for(ProcessLog pItem : pList){ 
			 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
			 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(),usid, processLog, approveResult, approveMemo,null);
	    }  
	}
	
	public void evaluationApproval(TechnicalEvaluationReport report, ApplicationInfo applicationInfo, String auditorId  ,
			String result, String memo) {  
		Long processInstanceId = applicationInfo.getProcessInstanceId();
		// 证书处理
		if ("通过".equals(result)) { 

			Date today = new Date();
			DateFormat df = new SimpleDateFormat("yyyy");
			String year = df.format(today);
			int iYear = new Integer(year);
			if ("三年".equals(report.getCertificateValid())) {
				iYear = iYear + 3;
			} else if ("五年".equals(report.getCertificateValid())) {
				iYear = iYear + 5;
			} else if ("原证书数截止日期".equals(report.getCertificateValid())) {

			}
			df = new SimpleDateFormat("-MM-dd HH:mm:ss");
			String sEndDate = "" + iYear + df.format(today);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sBegin = df.format(today);

			Date validityBeginDate = null;
			Date validityEndDate = null;
			try {
				validityBeginDate = df.parse(sBegin); // 证书有效期开始时间
				validityEndDate = df.parse(sEndDate);// 证书有效期结束时间
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			StringBuffer hql = new StringBuffer("select pc from")
					.append(" ApplicationInfoProductModel am")
					.append(",ProductModelCertification pc ")
					.append("where am.productModel=pc.productModel and pc.status = 0")
					.append(" and am.applicationInfo.id='")
					.append(applicationInfo.getId()).append("'");
			List<ProductModelCertification> list = getResultList(hql.toString());
			// 删除重复
			Map<String, ProductModelCertification> tmpMap = new HashMap<String, ProductModelCertification>();
			for (ProductModelCertification item : list) {
				if (tmpMap.containsKey(item.getProductModel().getId())) {
					getHibernateTemplate().delete(item);
				} else {
					tmpMap.put(item.getProductModel().getId(), item);
				}
			}
			Collection<ProductModelCertification> pmcSet = tmpMap.values();

			Map<String, List<ProductModelCertification>> tmpMap_end = new HashMap<String, List<ProductModelCertification>>();
			for (ProductModelCertification item : pmcSet) {
				String srid = item.getProductModel().getSurveyReport().getId();

				List<ProductModelCertification> pmcList = tmpMap_end.get(srid);
				if (pmcList == null) {
					pmcList = new ArrayList<ProductModelCertification>();
					tmpMap_end.put(srid, pmcList);
				}
				pmcList.add(item);
			}
			Map<String,Certification> certificationMap = new HashMap<String,Certification>();
			for (String srid : tmpMap_end.keySet()) {
				List<ProductModelCertification> pmcList = tmpMap_end.get(srid);
				Certification cert=null;
				for (ProductModelCertification item : pmcList) { 
					if(cert==null){ 
						cert = new Certification();
						cert.setApplyid(applicationInfo.getSioid());
						cert.setAppId(applicationInfo.getApplicationPublic()
								.getApplyEnterprise().getId());
						cert.setAppName(applicationInfo.getApplicationPublic()
								.getApplyEnterprise().getName());
						cert.setAppAdress(applicationInfo
								.getApplicationPublic().getApplyEnterprise()
								.getRegisteredAddress());
						cert.setManufactureId(item.getProductModel().getManufactureInfo().getId());
						cert.setManufactureName(item.getProductModel().getManufactureInfo().getName());
						cert.setManufactureAdress(item.getProductModel().getManufactureInfo().getRegisteredAddress());
						cert.setProductionEnterpriseId(item.getProductModel().getProductionEnterpriseInfo().getId());
						cert.setProductionEnterpriseName(item.getProductModel().getProductionEnterpriseInfo().getName());
						cert.setProductionEnterpriseAdress(item.getProductModel().getProductionEnterpriseInfo().getRegisteredAddress());
						cert.setObservedStandard(item.getProductModel()
								.getProductCatalogueInfo()
								.getObservedStandard());
						cert.setProductModel(item.getProductModel()
								.getSpecification());
						cert.setProductName(item.getProductModel()
								.getProductCatalogueInfo().getProductName());
						cert.setTechnicalRequirement(item.getProductModel()
								.getProductCatalogueInfo()
								.getTechnicalRequirement());

						cert.setValidityBeginDate(validityBeginDate);
						cert.setValidityEndDate(validityEndDate);

						// 强制型认证
					//	if (ProductCatalogueInfo.businessType_strong
					//			.equals(item.getProductModel()
					//					.getProductCatalogueInfo()
					//					.getBusinessType())) {
							ProductCatalogueInfo p = item.getProductModel()
									.getProductCatalogueInfo()
									.getProductParent();
							for (; p.getProductParent() != null;) {
								p = p.getProductParent();
							}
							// 机构代码 08
						 
							cert.setCertNumber(getCertNumberOfStrongCA("08",
									p.getProductNo(), item.getProductModel()
											.getProductCatalogueInfo()
											.getCertificationTypeNo()));
					//	}
 
					    getHibernateTemplate().save(cert);
					} 
					
					item.setCertification(cert);
					item.setStatus(1);
					getHibernateTemplate().merge(item);
				}
			}
		}
    }

	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}
	
	private static int countCertNumber = 0;
	private static String oldCertNumber = null;

	private synchronized static String getCertNumberOfStrongCA(String orgCode,
			String productBigTypeID, String certificatesNo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String no = sdf.format(new Date());
		if (oldCertNumber == null || !oldCertNumber.equals(no)) {
			countCertNumber = 0;
			oldCertNumber = no;
		}
		if (countCertNumber == 0) {
			String hql = "select new map( max( substring(o.certNumber, 11, 6)) as certNumber )"
					+ " from Certification o where substring(o.certNumber, 1, 4) ='"
					+ no + "'";
			List list = BaseDAOServcieUtil.findByQueryString(hql);
			if (list.size() == 1) {
				Map m = (Map) list.get(0);
				String certNumber = (String) m.get("certNumber");
				if (certNumber != null) {
					countCertNumber = new Integer(certNumber);
				}
			}
		}

		countCertNumber++;
		String sn = "000000" + countCertNumber;
		no = no + orgCode + productBigTypeID + certificatesNo
				+ sn.substring(sn.length() - 6, sn.length());
		return no;
	}

}

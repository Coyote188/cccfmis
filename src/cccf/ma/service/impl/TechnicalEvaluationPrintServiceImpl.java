package cccf.ma.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cccf.ma.model.Certification;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.TechnicalEvaluationPrintService;
import cccf.ma.service.WorkFlowService;

public class TechnicalEvaluationPrintServiceImpl extends AbstractBaseService
		implements TechnicalEvaluationPrintService {
	private WorkFlowService workFlowService;  
	@Override
	public void doEvaluationEnd(String reportNo,String usid, String approveResult,
			String approveMemo) {
		 //流程处理
		StringBuffer hql = new StringBuffer("select pl from ApplicationInfo o , ProcessLog pl ,TechnicalEvaluationReport r ")
		        .append(" where pl.boId=o.id and pl.end is null and o.sioid=r.applyno ")
		        .append(" and r.reportNo ='").append(reportNo).append("'");
	    List<ProcessLog> pList = getResultList(hql.toString()); 

		for(ProcessLog pItem : pList){ 
				 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
				 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), usid, processLog, approveResult, approveMemo,null);
	    }  
	}

	@Override
	public List getCertificationList(String reportNo) {
		StringBuffer hql = new StringBuffer("select new map(")
		.append("o.certNumber as number")
		.append(",")
		.append("o.appName as appname")
		.append(",")
		.append("o.appAdress as appadress")
		.append(",")
		.append("o.productionEnterpriseName as productionenterprise")
		.append(",")
		.append("o.productionEnterpriseAdress as productionenterpriseadress")
		.append(",").append("o.productName as productname").append(",")
		.append("o.manufactureName as manufacture").append(",")
		.append("o.productModel as productcatalogue").append(",")
		.append("o.productName as productcatalogueen").append(",")
		.append("o.technicalRequirement as technicalRequirement")
		.append(",")
		.append("o.technicalRequirement as technicalRequirementen")
		.append(",").append("o.observedStandard as observedStandard")
		.append(",").append("o.observedStandard as observedStandarden")
		.append(",")
		.append("o.validityBeginDate as certifaicationfdate")
		.append(",").append("o.validityEndDate as certifaicationldate")
		.append(") from Certification o ,TechnicalEvaluationReport r where r.applyno = o.applyid and r.reportNo='")
		.append(reportNo).append("'");
	 
        return getResultList(hql.toString());
	}

	@Override
	public List getEvaluationReportData(String reportNo) {
		List retList = new ArrayList();
		StringBuffer hql = new StringBuffer(
				"select o from Certification o ,TechnicalEvaluationReport r where r.applyno = o.applyid and r.reportNo='")
		        .append( reportNo).append("'");
		List<Certification> list = getResultList(hql.toString());
		for (Certification cert : list) {
			HashMap fieldsArgs = new HashMap();
			retList.add(fieldsArgs);
			/* 编号 */
			fieldsArgs.put("编号", cert.getCertNumber());
			// 有效期 签发 技评意见 复评意见 初评意见 申请日期
			fieldsArgs.put("有效期", "从" + cert.getValidityBeginDate() + "至"
					+ cert.getValidityEndDate());
			// 检查日期 检查组长 检查组员 检查代码
			fieldsArgs.put("检查日期", "-");
			fieldsArgs.put("检查组长", "-");
			fieldsArgs.put("检查组员", "-");
			fieldsArgs.put("检查代码", "-");
			// application.get
			// 技评意见1
			hql = new StringBuffer("select new map(")
					.append(" o.phase as evaluationPhase")
					.append(",")
					.append(" o.opinionContent as opinionContent")
					.append(",")
					.append(" o.memo as memo")
					.append(",")
					.append(" u.username as evaluationPersonName")
					.append(" ")
					.append(") from TechnicalEvaluationReportOpinion o , UserInfo u")
					.append(" where u.id=o.evaluationPersonId and o.technicalEvaluationReport.reportNo='")
					.append(reportNo).append("'")
					.append(" group by o.sn ");

			List<Map<String, Object>> list2 = getResultList(hql.toString());
			int k = 1;
			for (Map<String, Object> map1 : list2) {
				fieldsArgs.put("技评意见" + k, (String) map1.get("opinionContent"));
				fieldsArgs.put("技评签字" + k,
						"[" + map1.get("evaluationPersonName") + "]");
				k++;
				if (k > 3)
					break;
			}
			//
			/* 申请人 */
			// EnterpriseInfo entinfo = application.getEnterprise();
			fieldsArgs.put("审请人名称", cert.getAppName());
			fieldsArgs.put("申请人地址", cert.getAppAdress());
			/* */
			// 生产厂
			// ProductionEnterpriseInfo prodentInfo =
			// application.getProductionEnterprise();
			fieldsArgs.put("生产企业名称", cert.getProductionEnterpriseName());
			fieldsArgs.put("生产企业地址", cert.getProductionEnterpriseAdress());
			// 制造商
			// ManufactureInfo manInfo = application.getManufacture();
			fieldsArgs.put("制造商名称", cert.getManufactureName());
			fieldsArgs.put("制造商地址", cert.getManufactureAdress());
			/* 产品执行标准 */
			// 产品
			// ProductCatalogueInfo prod = application.getProduction();
			fieldsArgs.put("产品执行标准", cert.getObservedStandard());
			/** 认证规则 **/
			fieldsArgs.put("认证规则", cert.getCaRule());
			/** 产品执行标准 **/
			fieldsArgs.put("体系执行标准", cert.getSystemStandards());
			/** 名称规格型号 **/
			fieldsArgs.put("名称规格型号",
					cert.getProductName() + " " + cert.getProductModel());
			/** 检验报告结论 **/
			// TODO
			hql = new StringBuffer("select DISTINCT new map(")
					.append(" o.productModel.surveyReport.surverVerdict as surverVerdict")
					.append(",")
					.append(" o.productModel.surveyReport.surveyReportSN as surveyReportSN")
					.append(",")
					.append(" o.productModel.surveyReport.surverVerdictContent as surverVerdictContent")
					.append(") from ProductModelCertification o ,ApplicationInfoProductModel a ,TechnicalEvaluationReport r  where ")
					.append(" a.applicationInfo.sioid = r.applyno")
					.append(" o.productModel=a.productModel and r.reportNo'")
					.append(reportNo).append("'");
			Map sr = new HashMap();//(Map) querySingleResult(hql.toString());
			StringBuffer tmpSb = new StringBuffer();
			tmpSb.append("合格");
			/*int surverVerdict = (Integer) sr.get("surverVerdict");
			if (surverVerdict == 1) {
				tmpSb.append("合格");
			} else {
				tmpSb.append("不合格");
			}*/
			tmpSb.append("(检验报告编号: ").append(sr.get("surveyReportSN"))
					.append(") ").append(sr.get("surverVerdictContent"));
			fieldsArgs.put("检验报告结论", tmpSb.toString());
			/** 审核结论 **/
			fieldsArgs.put("审核结论", "推荐质量认证通过");
			/** 备注 **/
			fieldsArgs.put("备注", "[无]");
		}
		return retList;
	}

	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}

}

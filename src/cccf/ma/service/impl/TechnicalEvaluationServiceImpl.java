package cccf.ma.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.taskmgmt.exe.TaskInstance;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.Certification;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductModel;
import cccf.ma.model.ProductModelCertification;
import cccf.ma.model.TechnicalEvaluation;
import cccf.ma.model.TechnicalEvaluationOpinion;
import cccf.ma.service.TechnicalEvaluationService;

import com.aidi.bpm.model.Approve;
import com.aidi.bpm.service.ApproveServiceUtil;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

public class TechnicalEvaluationServiceImpl extends AbstractBaseService
		implements TechnicalEvaluationService {
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

	@Override
	public void doInitializeEvaluation(TechnicalEvaluation te,
			List<String> mdids, TechnicalEvaluationOpinion opinion,
			String applicationInfoID, Approve approve) {
		ApplicationInfo applicationInfo = (ApplicationInfo) getHibernateTemplate()
				.load(ApplicationInfo.class, applicationInfoID);
		te.setApplicationInfo(applicationInfo);
		getHibernateTemplate().save(te);

		opinion.setTechnicalEvaluation(te);
		Date dt = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dt = fordate.parse(dt.toLocaleString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		opinion.setEvaluationTime(dt);
		getHibernateTemplate().save(opinion);

		if (mdids != null) {
			for (String id : mdids) {
				ProductModel pm = (ProductModel) getHibernateTemplate().load(
						ProductModel.class, id);
				ProductModelCertification pmc = new ProductModelCertification();
				pmc.setProductModel(pm);
				getHibernateTemplate().save(pmc);
			}
		}

		doProcess(applicationInfo, approve);
	}

	@Override
	public void doEvaluation(TechnicalEvaluationOpinion opinion,
			String applicationInfoID, Approve approve) {
		StringBuffer hql = new StringBuffer("select o.technicalEvaluation ")
				.append(" from TechnicalEvaluationOpinion o")
				.append(" where o.technicalEvaluation.applicationInfo.id='")
				.append(applicationInfoID).append("'");
		TechnicalEvaluation te = (TechnicalEvaluation) querySingleResult(hql
				.toString());
		opinion.setTechnicalEvaluation(te);
		Date dt = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dt = fordate.parse(dt.toLocaleString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		opinion.setEvaluationTime(dt);
		getHibernateTemplate().save(opinion);

		ApplicationInfo applicationInfo = (ApplicationInfo) getHibernateTemplate()
				.load(ApplicationInfo.class, applicationInfoID);
		doProcess(applicationInfo, approve);

	}

	@Override
	public void doSummaryEvaluation(TechnicalEvaluation te, List<String> mdids,
			TechnicalEvaluationOpinion opinion, String applicationInfoID,
			Approve approve) {
		ApplicationInfo applicationInfo = (ApplicationInfo) getHibernateTemplate()
				.load(ApplicationInfo.class, applicationInfoID);
		te.setApplicationInfo(applicationInfo);
		getHibernateTemplate().update(te);

		Date dt = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dt = fordate.parse(dt.toLocaleString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		opinion.setEvaluationTime(dt);
		getHibernateTemplate().save(opinion);

		StringBuffer hql = new StringBuffer("select pmc ")
				.append(" from ProductModelCertification pmc,ApplicationInfoProductModel apm")
				.append(" where pmc.productModel=apm.productModel")
				.append(" and apm.applicationInfo.id='")
				.append(applicationInfoID).append("'");
		List<ProductModelCertification> list = getResultList(hql.toString());
		for (ProductModelCertification item : list) {
			getHibernateTemplate().delete(item);
		}
		if (mdids != null) {
			for (String id : mdids) {

				ProductModel pm = (ProductModel) getHibernateTemplate().load(
						ProductModel.class, id);
				ProductModelCertification pmc = new ProductModelCertification();
				pmc.setProductModel(pm);
				getHibernateTemplate().save(pmc);
			}
		}
		doProcess(applicationInfo, approve);
	}

	private void doProcess(ApplicationInfo applicationInfo, Approve approve) {

		// 处理流程
		TaskInstance ti = BpmUtil.getCurrentTaskInstance(applicationInfo
				.getProcessInstanceId());

		approve.setTaskInstanceId(ti.getId());
		Date dt = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dt = fordate.parse(dt.toLocaleString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		approve.setApproveDate(dt);
		approve.setRowId(applicationInfo.getId());
		approve.setEntityName("ApplicationInfo");

		ApproveServiceUtil.create(approve);
		BpmUtil.endTaskPreviousActor(ti.getId(), approve.getApproveResult());

	}

	@Override
	public void doResultAudit( String auditorId,
			String result, String memo, String applicationInfoID) { 
		ApplicationInfo applicationInfo = (ApplicationInfo) getHibernateTemplate()
		.load(ApplicationInfo.class, applicationInfoID);
		Long processInstanceId = applicationInfo.getProcessInstanceId();
		// 证书处理
		if ("通过".equals(result)) {
			StringBuffer hql = new StringBuffer(
					"from TechnicalEvaluation where applicationInfo.id='")
					.append(applicationInfoID).append("'");
			TechnicalEvaluation te = (TechnicalEvaluation) querySingleResult(hql
					.toString());

			Date today = new Date();
			DateFormat df = new SimpleDateFormat("yyyy");
			String year = df.format(today);
			int iYear = new Integer(year);
			if ("三年".equals(te.getCertificateValid())) {
				iYear = iYear + 3;
			} else if ("五年".equals(te.getCertificateValid())) {
				iYear = iYear + 5;
			} else if ("原证书数截止日期".equals(te.getCertificateValid())) {

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
			hql = new StringBuffer("select pc from")
					.append(" ApplicationInfoProductModel am")
					.append(",ProductModelCertification pc ")
					.append("where am.productModel=pc.productModel and pc.status = 0")
					.append(" and am.applicationInfo.id='")
					.append(applicationInfoID).append("'");
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
			for (String srid : tmpMap_end.keySet()) {
				List<ProductModelCertification> pmcList = tmpMap_end.get(srid);
				for (ProductModelCertification item : pmcList) {

					if (item.getCertification() == null) {
						Certification cert = new Certification();
						cert.setApplyid(applicationInfo.getId());
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
						item.setCertification(cert);
						item.setStatus(1);
						getHibernateTemplate().merge(item);
					}
				}
			}
		}

		// 处理流程
		TaskInstance ti = BpmUtil.getCurrentTaskInstance(processInstanceId);

		Approve approve = new Approve();
		approve.setTaskInstanceId(ti.getId());
		approve.setApproveResult(result);
		approve.setApproveMemo(memo);
		Date dt = new Date();
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dt = fordate.parse(dt.toLocaleString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		approve.setApproveDate(dt);
		approve.setUserId(auditorId);
		approve.setRowId(applicationInfoID);
		approve.setEntityName("ApplicationInfo");
		ApproveServiceUtil.create(approve); 
	    BpmUtil.endTaskPreviousActor(ti.getId(), approve.getApproveResult());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getEvaluationReportData(String applicationInfoId) {
		List retList = new ArrayList();
		StringBuffer hql = new StringBuffer(
				"select o from Certification o where applyid='").append(
				applicationInfoId).append("'");
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
					.append(" o.evaluationPhase as evaluationPhase")
					.append(",")
					.append(" o.opinionContent as opinionContent")
					.append(",")
					.append(" o.memo as memo")
					.append(",")
					.append(" u.username as evaluationPersonName")
					.append(" ")
					.append(") from TechnicalEvaluationOpinion o , UserInfo u")
					.append(" where u.id=o.evaluationPersonId and o.technicalEvaluation.applicationInfo.id='")
					.append(applicationInfoId).append("'");

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
					.append(") from ProductModelCertification o ,ApplicationInfoProductModel a  where ")
					.append("  o.productModel=a.productModel and a.id='")
					.append(applicationInfoId).append("'");
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

	@Override
	public List getCertificationList(String applicationInfoId) {
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
				.append(") from Certification o where applyid='")
				.append(applicationInfoId).append("'");
		return getResultList(hql.toString());
	}

}

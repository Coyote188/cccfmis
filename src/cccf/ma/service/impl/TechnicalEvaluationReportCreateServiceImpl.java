package cccf.ma.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aidi.core.service.BaseDAOServcieUtil;
 
import cccf.ma.model.ProcessLog;
import cccf.ma.model.ProductModel;
import cccf.ma.model.ProductModelCertification;
import cccf.ma.model.TechnicalEvaluationReport;
import cccf.ma.model.TechnicalEvaluationReportOpinion;
import cccf.ma.service.TechnicalEvaluationReportCreateService;
import cccf.ma.service.WorkFlowService;

public class TechnicalEvaluationReportCreateServiceImpl extends AbstractBaseService
		implements TechnicalEvaluationReportCreateService {
	private WorkFlowService workFlowService;  
	@Override
	public void doReportCreate(TechnicalEvaluationReport report,
			TechnicalEvaluationReportOpinion opinion, List<String> mdids,
			String usid,String approveResult,String approveMemo) {
		if(report.getApplyno() ==null|| report.getApplyno().isEmpty()){
			throw new RuntimeException("申请号不能为空！");
		}
		if(approveResult==null|| approveResult.isEmpty()){
			throw new RuntimeException("流程路径不能为空！");
		}
		report.setStatus(1);
		report.setCreateDate(new Date());
		report.setCreater(usid);
		report.setReportNo(getReportNo());
		getHibernateTemplate().save(report);
		
		opinion.setTechnicalEvaluationReport(report);
		opinion.setEvaluationTime(report.getCreateDate());
		opinion.setEvaluationPersonId(usid);
		opinion.setStatus(1);
		getHibernateTemplate().save(opinion);
		
		//选中产品设置
		if (mdids != null) {
			for (String id : mdids) {
				ProductModel pm = (ProductModel) getHibernateTemplate().load(
						ProductModel.class, id);
				ProductModelCertification pmc = new ProductModelCertification();
				pmc.setProductModel(pm);
				getHibernateTemplate().save(pmc);
			}
		} 
		
		//处理流程
		StringBuffer hql = new StringBuffer("select pl from ApplicationInfo o ,ProcessLog pl")
                    .append(" where pl.boId=o.id and pl.end is null ") 
                    .append(" and o.sioid ='").append(report.getApplyno()).append("'");
		List<ProcessLog> pList = getResultList(hql.toString());
		if(pList.size()>0){
			for(ProcessLog pItem : pList){  
				 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
				 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), usid, processLog, approveResult, approveMemo ,null); 
			}  
		}

	}

	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}
	
	
	/**生成编号***/
	private static int countAppNo = 0;
	private static String oldNo = null;
	private synchronized static String getReportNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String no = "BG" + sdf.format(new Date());
		if(oldNo==null || !oldNo.equals(no)){
			countAppNo=0;
			oldNo = no;
		} 
		if(countAppNo==0){  
			String hql = "select new map( max( substring(o.reportNo, 11, 4)) as reportNo )"
					+ " from TechnicalEvaluationReport o where substring(o.reportNo, 1, 10) ='"
					+ no + "'";
			List list =   BaseDAOServcieUtil.findByQueryString(hql);
			if (list.size() == 1) {
				Map m = (Map) list.get(0);
				String applyno = (String) m.get("reportNo");
				if(applyno!=null){
				   countAppNo = new Integer(applyno); 
				}
			}  
		}
		
		countAppNo++;
		String sn = "0000" + countAppNo;
		no += sn.substring(sn.length() - 4, sn.length());
		return no;
	}
}

package cccf.ma.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.model.AssignBatch; 
import cccf.ma.model.FactoryCheckTask;
import cccf.ma.model.FactoryCheckTaskUser;
import cccf.ma.model.FactoryCheckUser;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.AssignBatchService;
import cccf.ma.service.WorkFlowService;

import com.aidi.core.service.BaseDAOServcieUtil;

public class AssignBatchServiceImpl extends AbstractBaseService implements
		AssignBatchService {
	private WorkFlowService workFlowService;  
	
	@Override
	public void doCreateBatch(AssignBatch batch, String opid,
			String approveResult, String approveMemo) {
		batch.setBatchNo(getNo("PZ"));
		batch.setCreaterId(opid);
		batch.setCreateDate(new Date());
		batch.setStatus(1);
		getHibernateTemplate().save(batch);
		
		StringBuffer appNos = new StringBuffer("''");
		for(FactoryCheckTask task : batch.getCheckTasks()){
			StringBuffer hql = new StringBuffer("from ApplicationPublicInfo where applyno='").append(task.getApplyNo()).append("'");
			ApplicationPublicInfo app = (ApplicationPublicInfo) querySingleResult(hql.toString());
			if(app == null ){
				throw new RuntimeException("申请号为"+task.getApplyNo()+"的申请找不到！");
			}
			task.setTaskNo(getTask());
			task.setApplicationPublicInfo(app);
			task.setAssignBatch(batch);
			task.setBatchNo(batch.getBatchNo());
			appNos.append(",'").append(app.getApplyno()).append("'");
			getHibernateTemplate().save(task);
			
			for(FactoryCheckTaskUser user : task.getCheckUsers()){
				user.setAssignBatchTask(task);
				FactoryCheckUser fcu =(FactoryCheckUser) getHibernateTemplate().load(FactoryCheckUser.class, user.getFactoryCheckUser().getId());
				user.setFactoryCheckUser(fcu);
				
				getHibernateTemplate().save(user);
			} 
		}
		
		//处理流程
		StringBuffer hql = new StringBuffer("select pl from ApplicationInfo o ,ProcessLog pl")
                    .append(" where pl.boId=o.id and pl.end is null ") 
                    .append(" and o.sioid in (").append(appNos).append(")");
		List<ProcessLog> pList = getResultList(hql.toString());
		if(pList.size()>0){
			for(ProcessLog pItem : pList){  
				 ProcessLog processLog = new ProcessLog("ApplicationInfo", pItem.getBoId());
				 workFlowService.doTaskSubmit(pItem.getTaskInstanceId(), opid, processLog, approveResult, approveMemo ,null); 
			}  
		}
	} 
	
	@Override
	public void doCheck(String batchNo,int status, String usid, String approveResult,
			String approveMemo) {
		StringBuffer hql = new StringBuffer("from AssignBatch where batchNo='").append(batchNo).append("'");
		AssignBatch batch = (AssignBatch) querySingleResult(hql.toString());
		batch.setStatus(status);
		getHibernateTemplate().update(batch);
		
		//处理流程
	   hql = new StringBuffer("select DISTINCT pl from ApplicationInfo o ,ProcessLog pl ,FactoryCheckTask ct")
                    .append(" where pl.boId=o.id and pl.end is null ") 
                    .append(" and ct.applyNo = o.sioid")
                    .append(" and ct.assignBatch.id ='").append(batch.getId()).append("'");
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
	
	
	/**生成批次号***/
	
	private static int countTaskNo = 0;
	private static String oldTaskNo = null;
	private synchronized static String getTask() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String no = "SC" + sdf.format(new Date());
		if(oldTaskNo==null || !oldTaskNo.equals(no)){
			countTaskNo=0;
			oldTaskNo = no;
		} 
		if(countTaskNo==0){  
			String hql = "select new map( max( substring(o.taskNo, 11, 4)) as contractNo )"
				+ " from FactoryCheckTask o where substring(o.taskNo, 1, 10) ='"
				+ no + "'";
			
			List list =   BaseDAOServcieUtil.findByQueryString(hql);
			if (list.size() == 1) {
				Map m = (Map) list.get(0);
				String applyno = (String) m.get("contractNo");
				if(applyno!=null){
					countTaskNo = new Integer(applyno); 
				}
			}  
		}
		
		countTaskNo++;
		String sn = "0000" + countTaskNo;
		no += sn.substring(sn.length() - 4, sn.length());
		return no;
	}

	
	/**生成批次号***/
	private static int countAppNo = 0;
	private static String oldNo = null;
	private synchronized static String getNo(String h) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String no = h + sdf.format(new Date());
		if(oldNo==null || !oldNo.equals(no)){
			countAppNo=0;
			oldNo = no;
		} 
		if(countAppNo==0){  
			String hql = "select new map( max( substring(o.batchNo, 11, 4)) as contractNo )"
				+ " from AssignBatch o where substring(o.batchNo, 1, 10) ='"
				+ no + "'";
			List list =   BaseDAOServcieUtil.findByQueryString(hql);
			if (list.size() == 1) {
				Map m = (Map) list.get(0);
				String applyno = (String) m.get("contractNo");
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

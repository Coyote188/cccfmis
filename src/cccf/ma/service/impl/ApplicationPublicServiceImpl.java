package cccf.ma.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ApplicationInfoProductModel;
import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.model.ApplicationPublicInfoAttachment;
import cccf.ma.model.ProcessLog;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductModel;
import cccf.ma.service.ApplicationPublicService;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.WorkFlowService;
import com.aidi.core.service.BaseDAOServcieUtil;
 
 
public class ApplicationPublicServiceImpl extends AbstractBaseService implements ApplicationPublicService {  
	private WorkFlowService workFlowService; 
	private String workFlowFormUrl; 
	
	@Override
	public void doSave(ApplicationPublicInfo bean, List<Map> models,
			List<ApplicationPublicInfoAttachment> attachments) { 
		
		bean.setId(null);
		bean.setApplydate(new Date());
		bean.setApplyno(getApplyNo());  
		getHibernateTemplate().save(bean);
		getHibernateTemplate().flush();
		
		for (ApplicationPublicInfoAttachment att : attachments) {
			if(att.getAttachment()!=null){
				att.getAttachment().persist();
				att.setApplicationPublicInfo(bean);
				getHibernateTemplate().save(att);
				getHibernateTemplate().flush();
			} 
		} 
 
		Map<String, ApplicationInfo> cache = new HashMap<String, ApplicationInfo>();
		for (Map map : models) {
			String productId = (String) map.get("productId");

			ApplicationInfo app = cache.get(productId);
			if (app == null) {
				ProductCatalogueInfo pInfo = ProductCatalogueInfoServiceUtil
						.getById(productId);
				app = new ApplicationInfo();
				app.setProduction(pInfo);
				app.setApplicationPublic(bean);
				app.setEnterprise(bean.getApplyEnterprise());// 为了满足Application对象而设置以下属性
				app.setApplyDate(new Date());
				app.setSioid(bean.getApplyno());
				app.setStauts(0);
				app.setStauts0(-1);
				app.setApptype(bean.getApplyType());
				app.setBusinesstype(bean.getBusinessType());
				getHibernateTemplate().save(app);// 持久化申请对象
				cache.put(productId, app);
			}
			ApplicationInfoProductModel appmodel = new ApplicationInfoProductModel();

			appmodel.setApplicationInfo(app);
			String pmid = (String) map.get("id");
			ProductModel pm =   (ProductModel)getHibernateTemplate().load(ProductModel.class, pmid) ;
			appmodel.setProductModel(pm);

			getHibernateTemplate().save(appmodel);// 持久化产品模型和申请的关系
		}
		bean.setApplications(new ArrayList(cache.values()));
	}

	@Override
	public void doSaveSubmit(ApplicationPublicInfo bean, List<Map> models,
			List<ApplicationPublicInfoAttachment> attachments,String userId) {
		
		List<ApplicationInfo> list=null;
		if(bean.getId()==null){
			doSave(bean,models,attachments);
			list=bean.getApplications();
		}else{
			list = getResultList("from ApplicationInfo where applicationPublic.id='"+bean.getId()+"'");
		} 
		//处理流程
		if(list!=null){
			for(ApplicationInfo item: list){ 
				ProcessLog log = new ProcessLog("ApplicationInfo",item.getId()); 
				item.setProcessInstanceId(workFlowService.doStartProcessAndDoTask(workFlowFormUrl, userId, log, "提交申请" ,null )); 
				item.setStauts(1);
				
			}
		}
	}  
	
	/**生成合同号***/
	private static int countAppNo = 0;
	private static String oldNo = null;
	private synchronized static String getApplyNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String no = "SQ" + sdf.format(new Date());
		if(oldNo==null || !oldNo.equals(no)){
			countAppNo=0;
			oldNo = no;
		} 
		if(countAppNo==0){  
			String hql = "select new map( max( substring(o.applyno, 11, 4)) as applyno )"
					+ " from ApplicationPublicInfo o where substring(o.applyno, 1, 10) ='"
					+ no + "'";
			List list =   BaseDAOServcieUtil.findByQueryString(hql);
			if (list.size() == 1) {
				Map m = (Map) list.get(0);
				String applyno = (String) m.get("applyno");
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

	public void setWorkFlowService(WorkFlowService workFlowService) {
		this.workFlowService = workFlowService;
	}

	public void setWorkFlowFormUrl(String workFlowFormUrl) {
		this.workFlowFormUrl = workFlowFormUrl;
	}

	
	
}

package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import openjframework.service.UserInfoServiceUtil;

import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import cccf.myenum.ActivateStatus;

import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class EnterpriseInfoServiceUtil
{
	private static int defaultStatus = 0; 
    public static Serializable  create(EnterpriseInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       bean.setStatus(defaultStatus);
       EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       return service.create(bean);
    }
    public static void delete(EnterpriseInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(EnterpriseInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(EnterpriseInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       return service.getAll();
       }
    public static EnterpriseInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       return service.getById(id);
       }
     public static EnterpriseInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        EnterpriseInfoService service=(EnterpriseInfoService)ServiceAdapter.getServiceByName("EnterpriseInfoService");
       return service.findByQuery(querystr);
       }
    
    //得到当前企业
    public static EnterpriseInfo getCurrentEnterprise(){
    	UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
    	String querystr = "from EnterpriseInfo e where e.account = '"+ user.getId() +"'";
    	return (EnterpriseInfo) findByQuery(querystr).get(0);
    }
    //得到未激活企业
    
	public static List<EnterpriseInfo> findByInactiveEnterprise(){
    	String querystr = "from EnterpriseInfo e where e.status = '" + ActivateStatus.未激活.ordinal()+"'";
		return findByQuery(querystr);
    }
    /**
     * 根据评定中心人员对产品的权限查找申请该产品激活的注册企业
     */
    public static List<EnterpriseInfo> findInactiveEnterpriseByPermission() {
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		List<EnterpriseInfo> eList = new ArrayList<EnterpriseInfo>();
		
		//根据一个用户得到与之相关的产品列表
		List<ProductCatalogueInfo> pList = Product_User_ListInfoServiceUtil
				.findProductByUser(user);
		
		
		//取出第一个产品，并通过该产品得到与之相关的企业
		for (ProductCatalogueInfo p : pList) {
			//根据一个产品得到与之相关的企业列表
			List<EnterpriseInfo> eTemp = new ArrayList<EnterpriseInfo>();
			eTemp = EnterpriseOwnActivatedProductListInfoServiceUtil
					.findUnactiveEByProduct(p);
			
			//将所得到的企业列表添加到显示的list中
			boolean isExist = false;
			for (EnterpriseInfo e : eTemp) {
				if (eList.isEmpty())
					eList.add(e);
				else {
					for (Iterator it = eList.iterator(); it.hasNext();) {
						if(((EnterpriseInfo) it.next()).getId().equals(e.getId())){
							isExist = true;
							break;
						}
						
					}
					if (!isExist)
						eList.add(e);
				}
			}
		}
		//    	
//    	
//    	if(!Product_User_ListInfoServiceUtil.isUserHasPermissionOfProduct(product, user)){
//    		eList = EnterpriseOwnActivatedProductListInfoServiceUtil.findUnactiveEByProduct(product);
//    	}
		return eList;
    }
    //激活企业
    public static void activeEnterprise(EnterpriseInfo e){
    	e.active();
    	saveOrUpdate(e);
    }
    //拒绝企业激活申请
    public static void refusedEnterprise(EnterpriseInfo e){
    	e.refused();
    	saveOrUpdate(e);
    }
    //通过用户查找企业
    public static EnterpriseInfo findEnterpriseByUser(UserInfo user){
    	String querystr = "from EnterpriseInfo e where e.account = '"+ user.getId() +"'";
    	return (EnterpriseInfo) findByQuery(querystr).get(0);
    }
    
    //验证用户是否存在
    public static boolean isEnterpriseExist(String enterpriseName){
    	String eQuerystr = "from EnterpriseInfo e where e.name = '"+enterpriseName+"'";
    	
    	List eList = findByQuery(eQuerystr);
    	if (eList.isEmpty()) {
			return false;
		}else {
			return true;
		}
    }
	public static void setDefaultStatus(int defaultStatus) {
		EnterpriseInfoServiceUtil.defaultStatus = defaultStatus;
	}
	public static int getDefaultStatus() {
		return defaultStatus;
	}
	//find by location
	public static List<EnterpriseInfo> findByLocation(String location){
		String querystr = "from EnterpriseInfo e where e.location like '%"+location+"%'";
		return findByQuery(querystr);
	}
	//find by state
	public static List<EnterpriseInfo> findByState(StateInfo state){
		String querystr = "from EnterpriseInfo e where e.state = '"+state.getName()+"'";
		return findByQuery(querystr);
	}
	//find by product
	public static List<EnterpriseInfo> findByProduct(ProductCatalogueInfo product){
		String querystr = "select enterprise from EnterpriseOwnActivatedProductListInfo eOAP where eOAP.ProductCatalogueInfo.id = '"+product.getId()+"'";
		return findByQuery(querystr);
	}
	//find by name
	public static List<EnterpriseInfo> findByName(String eName){
		String querystr = "from EnterpriseInfo e where e.name like '%"+eName+"%'";
		List<EnterpriseInfo> eList = findByQuery(querystr);
		return eList == null ? null:eList;
	}
	
}
   
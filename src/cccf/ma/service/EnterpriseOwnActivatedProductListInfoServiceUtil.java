package cccf.ma.service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.model.ProductCatalogueInfo;

import com.aidi.core.spring.CustomerContextHolder;
import com.aidi.core.web.framework.ServiceAdapter;
public class EnterpriseOwnActivatedProductListInfoServiceUtil
{
    public static Serializable  create(EnterpriseOwnActivatedProductListInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       return service.create(bean);
    }
    public static void delete(EnterpriseOwnActivatedProductListInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(EnterpriseOwnActivatedProductListInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(EnterpriseOwnActivatedProductListInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       return service.getAll();
       }
    public static EnterpriseOwnActivatedProductListInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       return service.getById(id);
       }
     public static EnterpriseOwnActivatedProductListInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        EnterpriseOwnActivatedProductListInfoService service=(EnterpriseOwnActivatedProductListInfoService)ServiceAdapter.getServiceByName("EnterpriseOwnActivatedProductListInfoService");
       return service.findByQuery(querystr);
       }
    /**
     * 根据用户得到激活过的产品
     * @param User id
     * @return
     */
    public static List<EnterpriseOwnActivatedProductListInfo> findByActivater(UserInfo user)
    {
    	
    	String qstr="FROM EnterpriseOwnActivatedProductListInfo eoap WHERE activateuser.id='"+user.getId()+"' and eoap.activateStatus=1";
    	List<EnterpriseOwnActivatedProductListInfo> list=findByQuery(qstr);
    	return list;
    }
    /**
     * 取得该企业的已激活产品列表
     * @param enterprise
     * @return
     */
    public static List<EnterpriseOwnActivatedProductListInfo> findByEnterprise(EnterpriseInfo enterprise)
    {
    	
    	String qstr="FROM EnterpriseOwnActivatedProductListInfo eoap WHERE enterprise.id='"+enterprise.getId()+"' and eoap.activateStatus=1";
    	List<EnterpriseOwnActivatedProductListInfo> list=findByQuery(qstr);
    	return list;
    }
    /**
     * 取得当前企业的已激活产品列表
     * @return
     */
    public static List<EnterpriseOwnActivatedProductListInfo> findCurrentEnterprise()
    {
    	EnterpriseInfo enterprise=EnterpriseInfoServiceUtil.findEnterpriseByUser(
 				UserInfoServiceUtil.getCurrentLoginUser());
    	String qstr="FROM EnterpriseOwnActivatedProductListInfo eoap WHERE enterprise.id='"+enterprise.getId()+"' and activateStatus=1";
    	List<EnterpriseOwnActivatedProductListInfo> list=findByQuery(qstr);
    	return list;
    }
    /**
     * 取得当前企业的已激活/未激活的产品列表
     * @return
     */
    public static List<EnterpriseOwnActivatedProductListInfo> findCurrentEnterpriseProduct()
    {
    	EnterpriseInfo enterprise=EnterpriseInfoServiceUtil.findEnterpriseByUser(
 				UserInfoServiceUtil.getCurrentLoginUser());
    	String qstr="FROM EnterpriseOwnActivatedProductListInfo eoap WHERE enterprise.id='"+enterprise.getId()+"'";
    	List<EnterpriseOwnActivatedProductListInfo> list=findByQuery(qstr);
    	return list;
    }
    /**
     * 取得全部未激活的产品申请
     * @return
     */
    public static List<EnterpriseOwnActivatedProductListInfo> findNotActivateAll()
    {
    	
    	String qstr="FROM EnterpriseOwnActivatedProductListInfo eoap WHERE eoap.activateStatus=0 and eoap.enterprise.status=1 order by eoap.applydate asc";
    	List<EnterpriseOwnActivatedProductListInfo> list=findByQuery(qstr);
    	return list;
    }
    /**
     * 给定产品，得到全部与该产品相关的企业
     */
    public static List<EnterpriseInfo> findEByProduct(ProductCatalogueInfo product){
    	List<EnterpriseInfo> eList = new ArrayList<EnterpriseInfo>();
    	String querystr = "FROM EnterpriseOwnActivatedProductListInfo eoap WHERE product.id='"+product.getId()+"'";
    	List<EnterpriseOwnActivatedProductListInfo> eOAPList = findByQuery(querystr);
    	for(EnterpriseOwnActivatedProductListInfo eoap : eOAPList){
    		EnterpriseInfo e = eoap.getEnterprise();
    		eList.add(e);
    	}
    	return eList;
    }
    public static List<EnterpriseInfo> findUnactiveEByProduct(ProductCatalogueInfo product){
    	List<EnterpriseInfo> eList = new ArrayList<EnterpriseInfo>();
    	String querystr = "FROM EnterpriseOwnActivatedProductListInfo eoap WHERE eoap.enterprise.status=0 and product.id='"+product.getId()+"'";
    	List<EnterpriseOwnActivatedProductListInfo> eOAPList = findByQuery(querystr);
    	for(EnterpriseOwnActivatedProductListInfo eoap : eOAPList){
    		EnterpriseInfo e = eoap.getEnterprise();
    		eList.add(e);
    	}
    	return eList;
    }
    /**
     * 
     * @param list
     */
    public static void saveProductOfEnterpriseList(List<EnterpriseOwnActivatedProductListInfo> list){
    	for(EnterpriseOwnActivatedProductListInfo eOAP : list){
    		create(eOAP);
    	}
    }
    public static void saveProductOfEnterpriseList(List<ProductCatalogueInfo> pList,EnterpriseInfo e){
    	if(pList.isEmpty() || null == e){
    		throw new WrongValueException();
    	}
    	List<EnterpriseOwnActivatedProductListInfo> eoapList = new ArrayList<EnterpriseOwnActivatedProductListInfo>();
    	for(ProductCatalogueInfo p : pList){
    		EnterpriseOwnActivatedProductListInfo eOAP = new EnterpriseOwnActivatedProductListInfo();
    		eOAP.setEnterprise(e);
    		eOAP.setProduct(p);
    		eOAP.setActivateStatus(0);
			eOAP.setApplydate(new Date());
			eoapList.add(eOAP);
    	}
    	saveProductOfEnterpriseList(eoapList);
    }
}
   
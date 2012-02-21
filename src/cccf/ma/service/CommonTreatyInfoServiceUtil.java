package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class CommonTreatyInfoServiceUtil
{
    public static Serializable  create(CommonTreatyInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       return service.create(bean);
    }
    public static void delete(CommonTreatyInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(CommonTreatyInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(CommonTreatyInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       return service.getAll();
       }
    public static CommonTreatyInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       return service.getById(id);
       }
     public static CommonTreatyInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        CommonTreatyInfoService service=(CommonTreatyInfoService)ServiceAdapter.getServiceByName("CommonTreatyInfoService");
       return service.findByQuery(querystr);
       }
    /**
     * 根据类型返回最新的一条协议
     * @param type
     * @return
     */
   public static CommonTreatyInfo getNewestofProtocol(int type)
   {
	   String qstr="FROM CommonTreatyInfo  WHERE type="+type+"ORDER BY date desc";
	   List<CommonTreatyInfo> list=findByQuery(qstr);
	   CommonTreatyInfo cott=null;
	   if(list.size()>0)
		   		cott=list.get(0);
	   return cott;
   }

   
}
   
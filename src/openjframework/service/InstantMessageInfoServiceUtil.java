package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class InstantMessageInfoServiceUtil
{
    public static Serializable  create(InstantMessageInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       return service.create(bean);
    }
    public static void delete(InstantMessageInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(InstantMessageInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(InstantMessageInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       return service.getAll();
       }
    public static InstantMessageInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       return service.getById(id);
       }
     public static InstantMessageInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        InstantMessageInfoService service=(InstantMessageInfoService)ServiceAdapter.getServiceByName("InstantMessageInfoService");
       return service.findByQuery(querystr);
       }
    public static void updateMyUnlineMessageBySendUser(UserInfo senderUser)
	{
		String qstr="from InstantMessageInfo where sendUser.id='"+senderUser.getId()+"' and receiveUser.id='"+ UserInfoServiceUtil.getCurrentLoginUser().getId()+"'";
		List<InstantMessageInfo>list=findByQuery(qstr);
		if(list!=null&&list.size()>0)
			for(InstantMessageInfo im:list)
			{
				im.setReadStatus(true);
				update(im);
			}
	}
    public static List<InstantMessageInfo> getMyUnlineMessage(String userid)
    {
    	String qstr="from InstantMessageInfo  where type=0 and readStatus=false and receiveUser.id='"+userid+"'";
    	List<InstantMessageInfo>list=findByQuery(qstr);
    	return list;
    }
}
   
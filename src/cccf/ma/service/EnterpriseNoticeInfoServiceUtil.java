package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import openjframework.service.NoticeInfoServiceUtil;

import java.util.*;
import java.sql.*;
import java.io.Serializable;

import javax.sound.midi.VoiceStatus;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

import cccf.ma.model.*;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;
public class EnterpriseNoticeInfoServiceUtil
{
    public static Serializable  create(EnterpriseNoticeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       return service.create(bean);
    }
    public static void delete(EnterpriseNoticeInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(EnterpriseNoticeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(EnterpriseNoticeInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       return service.getAll();
       }
    public static EnterpriseNoticeInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       return service.getById(id);
       }
     public static EnterpriseNoticeInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        EnterpriseNoticeInfoService service=(EnterpriseNoticeInfoService)ServiceAdapter.getServiceByName("EnterpriseNoticeInfoService");
       return service.findByQuery(querystr);
       }
    public static void sendMsg(EnterpriseInfo receive,NoticeInfo notice){
    	EnterpriseNoticeInfo tempNotice = new EnterpriseNoticeInfo();
    	tempNotice.setEnterpriseUser(receive.getAccount());
    	tempNotice.setNotice(notice);
    	tempNotice.setStatus(0);
    	create(tempNotice);
    	//send msg event
    	EventQueues.lookup(receive.getAccount().getId() + "noticeEventQueue",
				EventQueues.APPLICATION, true).publish(
				new Event("onMsgEventQueue", null, notice));
    }
    public static List<EnterpriseNoticeInfo> findByEnterprise(EnterpriseInfo e){
    	String querystr = "from EnterpriseNoticeInfo enter where enter.enterpriseUser = '"+e.getAccount().getId()+"'  ORDER BY enter.status ASC , enter.notice.sendDate DESC";
    	return findByQuery(querystr);
    }
    public static boolean isEnterpriseHasUnreadNotice(EnterpriseInfo e){
    	String querystr = "from EnterpriseNoticeInfo enter where enter.enterpriseUser = '"+e.getAccount().getId()+"' and enter.status = 0";
    	return findByQuery(querystr).isEmpty()?true:false;
    }
    public static void readMsg(EnterpriseNoticeInfo notice){
    	notice.setStatus(1);
    	update(notice);
    }
    public static List<EnterpriseNoticeInfo> findUnreadNotice(EnterpriseInfo e){
    	String querystr = "from EnterpriseNoticeInfo enter where enter.enterpriseUser = '"+e.getAccount().getId()+"' and enter.status = 0 ORDER BY enter.notice.sendDate DESC";
    	return findByQuery(querystr);
    }
    public static List<EnterpriseNoticeInfo> findReadedNotice(EnterpriseInfo e){
    	String querystr = "from EnterpriseNoticeInfo enter where enter.enterpriseUser = '"+e.getAccount().getId()+"' and enter.status = 1 ORDER BY enter.notice.sendDate DESC";
    	return findByQuery(querystr);
    }
}
   
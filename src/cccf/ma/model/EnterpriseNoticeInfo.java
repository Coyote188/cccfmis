package cccf.ma.model;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class EnterpriseNoticeInfo 
{   
   private String id;      
   private UserInfo enterpriseUser;      
   private NoticeInfo notice;      
   private Integer status;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setEnterpriseUser(UserInfo value)
   {
     this.enterpriseUser=value;
   }
   
   
   public void setNotice(NoticeInfo value)
   {
     this.notice=value;
   }
   
   
   public void setStatus(Integer value)
   {
     this.status=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public UserInfo getEnterpriseUser()
   {
     return enterpriseUser;
   }
   
   
   public NoticeInfo getNotice()
   {
     return notice;
   }
   
   
   public Integer getStatus()
   {
     return status;
   }
   
   public void read(){
	   setStatus(1);
   }
   
   public String getStatusName(){
	   return getStatus() == 0 ? "未读":"已读";
   }
      
}
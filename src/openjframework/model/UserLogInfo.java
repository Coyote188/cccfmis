package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class UserLogInfo 
{   
   private String logId;      
   private Integer logType;      
   private UserInfo user;      
   private java.util.Date logDate;      
   private String logDesc;      
   private String ipAddr;   
   
   public void setLogId(String value)
   {
     this.logId=value;
   }
   
   
   public void setLogType(Integer value)
   {
     this.logType=value;
   }
   
   
   public void setUser(UserInfo value)
   {
     this.user=value;
   }
   
   
   public void setLogDate(java.util.Date value)
   {
     this.logDate=value;
   }
   
   
   public void setLogDesc(String value)
   {
     this.logDesc=value;
   }
   
   
   public void setIpAddr(String value)
   {
     this.ipAddr=value;
   }
   
   
   public String getLogId()
   {
     return logId;
   }
   
   
   public Integer getLogType()
   {
     return logType;
   }
   
   
   public UserInfo getUser()
   {
     return user;
   }
   
   
   public java.util.Date getLogDate()
   {
     return logDate;
   }
   
   
   public String getLogDesc()
   {
     return logDesc;
   }
   
   
   public String getIpAddr()
   {
     return ipAddr;
   }
      
}
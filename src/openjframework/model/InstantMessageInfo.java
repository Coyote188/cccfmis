package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import openjframework.service.UserInfoServiceUtil;

public class InstantMessageInfo 
{   
   private String id;      
   private UserInfo sendUser;      
   private UserInfo receiveUser;      
   private String content;      
   private java.util.Date date;      
   private Integer type;      
   private boolean readStatus;   
   /**
    * 
    * @param receiveUser 接收方
    * @param content 消息内容
    * @param type 消息类型0-离线消息;1-在线消息
    * @param rstatus 阅读状态
    */
   public InstantMessageInfo(UserInfo receiveUser,String content,int type,boolean rstatus)
   {
	   this.sendUser=UserInfoServiceUtil.getCurrentLoginUser();
	   this.receiveUser=receiveUser;
	   this.content=content;
	   this.date=new java.util.Date();
	   this.type=type;
	   this.readStatus=rstatus;
   }
   public InstantMessageInfo()
   {}
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setSendUser(UserInfo value)
   {
     this.sendUser=value;
   }
   
   
   public void setReceiveUser(UserInfo value)
   {
     this.receiveUser=value;
   }
   
   
   public void setContent(String value)
   {
     this.content=value;
   }
   
   
   public void setDate(java.util.Date value)
   {
     this.date=value;
   }
   
   
   public void setType(Integer value)
   {
     this.type=value;
   }
   
   
   public void setReadStatus(boolean value)
   {
     this.readStatus=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public UserInfo getSendUser()
   {
     return sendUser;
   }
   
   
   public UserInfo getReceiveUser()
   {
     return receiveUser;
   }
   
   
   public String getContent()
   {
     return content;
   }
   
   
   public java.util.Date getDate()
   {
     return date;
   }
   
   
   public Integer getType()
   {
     return type;
   }
   
   
   public boolean isReadStatus()
   {
     return readStatus;
   }
      
}
package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import openjframework.*;
import openjframework.model.UserInfo;

import java.io.Serializable;

import openjframework.myenum.MessageEnum;

public class MessageInfo 
{   
   private String id;      
   private String subject; //主题     
   private Integer type;      
   private UserInfo sendUser;      
   private UserInfo receiveUser;      
   private java.util.Date sendDate;      
   private String content; //内容     
   private Integer readStatus;   
   private String TypeName;
   private String readstatusV;
   private String simpleTitle;
   private String messageTitle;
   private Long taskinstanceId;//用于暂存新到达待处理任务ID
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setSubject(String value)
   {
     this.subject=value;
   }
   
   
   public void setType(Integer value)
   {
     this.type=value;
   }
   
   
   public void setSendUser(UserInfo value)
   {
     this.sendUser=value;
   }
   
   
   public void setReceiveUser(UserInfo value)
   {
     this.receiveUser=value;
   }
   
   
   public void setSendDate(java.util.Date value)
   {
     this.sendDate=value;
   }
   
   
   public void setContent(String value)
   {
     this.content=value;
   }
   
   
   public void setReadStatus(Integer value)
   {
     this.readStatus=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getSubject()
   {
     return subject;
   }
   
   
   public Integer getType()
   {
     return type;
   }
   
   
   public UserInfo getSendUser()
   {
     return sendUser;
   }
   
   
   public UserInfo getReceiveUser()
   {
     return receiveUser;
   }
   
   
   public java.util.Date getSendDate()
   {
     return sendDate;
   }
   
   
   public String getContent()
   {
     return content;
   }
   
   
   public Integer getReadStatus()
   {
     return readStatus;
   }
   //消息类型
   public String getTypeName() {
		return MessageEnum.values()[type].toString();
	} 
   //获取阅读状态数值返回文字
   public String getreadstatusV()
   {  Integer Int=this.getReadStatus();
   String str=null;
   if(Int>0){
	   str="已读";
   }
   else{
	   str="未读";
   }
   return str;
   }
   //过长的消息只显示部分
   public String getSimpleTitle()
	{
		if (subject.length() > 30)
			simpleTitle = (new StringBuilder(String.valueOf(subject.substring(0, 30)))).append("......").toString();
		else
			simpleTitle =subject;
		return simpleTitle;
	}
   public String getMessageTitle(){
	   String str="["+sendUser+"]于"+sendDate+"给你发送了"+TypeName;
	   return str;
	  
   }


public Long getTaskinstanceId() {
	return taskinstanceId;
}


public void setTaskinstanceId(Long taskinstanceId) {
	this.taskinstanceId = taskinstanceId;
}
}
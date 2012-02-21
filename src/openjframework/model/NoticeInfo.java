package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import openjframework.myenum.NoticeRating;
import openjframework.myenum.NoticeType;

public class NoticeInfo 
{   
   private String id;      
   private String subject;      
   private String content;      
   private Integer type;      
   private java.util.Date sendDate;      
   private Integer isHaveRead;      
   private String attachment;      
   private Integer rating;
   private UserInfo sendUser;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setSubject(String value)
   {
     this.subject=value;
   }
   
   
   public void setContent(String value)
   {
     this.content=value;
   }
   
   
   public void setType(Integer value)
   {
     this.type=value;
   }
   
   
   public void setSendDate(java.util.Date value)
   {
     this.sendDate=value;
   }
   
   
   public void setIsHaveRead(Integer value)
   {
     this.isHaveRead=value;
   }
   
   
   public void setAttachment(String value)
   {
     this.attachment=value;
   }
   
   
   public void setRating(Integer value)
   {
     this.rating=value;
   }
   
   
   public void setSendUser(UserInfo value)
   {
     this.sendUser=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getSubject()
   {
     return subject;
   }
   
   
   public String getContent()
   {
     return content;
   }
   
   
   public Integer getType()
   {
     return type;
   }
   
   
   public java.util.Date getSendDate()
   {
     return sendDate;
   }
   
   
   public Integer getIsHaveRead()
   {
     return isHaveRead;
   }
   
   
   public String getAttachment()
   {
     return attachment;
   }
   
   
   public Integer getRating()
   {
     return rating;
   }
   
   
   public UserInfo getSendUser()
   {
     return sendUser;
   }
   
   public String getTypeName(int ordinal){
	   return NoticeType.values()[ordinal].toString();
   }
   
   public String getRatingName(int ordinal){
	   return NoticeRating.values()[ordinal].toString();
   }
      
}
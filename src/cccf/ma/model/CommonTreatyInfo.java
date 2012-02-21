package cccf.ma.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import cccf.myenum.ProductEnabledStatus;
import cccf.myenum.TreatyType;

public class CommonTreatyInfo 
{   
   private String id;      
   private String title;      
   private String treaty;      
   private Integer type;  
   private String typeName;
   private java.util.Date date;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setTitle(String value)
   {
     this.title=value;
   }
   
   
   public void setTreaty(String value)
   {
     this.treaty=value;
   }
   
   
   public void setType(Integer value)
   {
     this.type=value;
   }
   
   
   public void setDate(java.util.Date value)
   {
     this.date=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getTitle()
   {
     return title;
   }
   
   
   public String getTreaty()
   {
     return treaty;
   }
   
   
   public Integer getType()
   {
     return type;
   }
   
   
   public java.util.Date getDate()
   {
     return date;
   }
   public String getTypeName()
   {
	   if(type==null)
		   return String.valueOf("-请选择分类-");
	   return TreatyType.values()[type].toString();
   }  
}
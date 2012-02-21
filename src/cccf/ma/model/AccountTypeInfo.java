package cccf.ma.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class AccountTypeInfo 
{   
   private String atypeId;      
   private String name;      
   private String desc;      
   private boolean isCustom;   
   
   public void setAtypeId(String value)
   {
     this.atypeId=value;
   }
   
   
   public void setName(String value)
   {
     this.name=value;
   }
   
   
   public void setDesc(String value)
   {
     this.desc=value;
   }
   
   
   public void setIsCustom(boolean value)
   {
     this.isCustom=value;
   }
   
   
   public String getAtypeId()
   {
     return atypeId;
   }
   
   
   public String getName()
   {
     return name;
   }
   
   
   public String getDesc()
   {
     return desc;
   }
   
   
   public boolean getIsCustom()
   {
     return isCustom;
   }
      
}
package cccf.ma.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class AccountItemInfo 
{   
   private String accountItemId;      
   private AccountTypeInfo accountType;      
   private String name;      
   private String desc;   
   private String unit;
   private float price;
   
   public void setAccountItemId(String value)
   {
     this.accountItemId=value;
   }
   
   
   public void setAccountType(AccountTypeInfo value)
   {
     this.accountType=value;
   }
   
   
   public void setName(String value)
   {
     this.name=value;
   }
   
   
   public void setDesc(String value)
   {
     this.desc=value;
   }
   
   
   public String getAccountItemId()
   {
     return accountItemId;
   }
   
   
   public AccountTypeInfo getAccountType()
   {
     return accountType;
   }
   
   
   public String getName()
   {
     return name;
   }
   
   
   public String getDesc()
   {
     return desc;
   }


public String getUnit() {
	return unit;
}


public void setUnit(String unit) {
	this.unit = unit;
}


public float getPrice() {
	return price;
}


public void setPrice(float price) {
	this.price = price;
}
      
}
package cccf.ma.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class FeeDetailInfo 
{   
   private String feeId;      
   private AccountItemInfo accountItem;      
   private java.util.Date feeDate;      
   private float sum;      
   private ApplicationInfo application;      
   private String casherId;   
   private String unit; 
   private int number;   
   private int number2; 
   
   public void setFeeId(String value)
   {
     this.feeId=value;
   }
   
   
   public void setAccountItem(AccountItemInfo value)
   {
     this.accountItem=value;
   }
   
   
   public void setFeeDate(java.util.Date value)
   {
     this.feeDate=value;
   }
   
   
   public void setSum(float value)
   {
     this.sum=value;
   }
   
   
   public void setApplication(ApplicationInfo value)
   {
     this.application=value;
   }
   
   
   public void setCasherId(String value)
   {
     this.casherId=value;
   }
   
   
   public String getFeeId()
   {
     return feeId;
   }
   
   
   public AccountItemInfo getAccountItem()
   {
     return accountItem;
   }
   
   
   public java.util.Date getFeeDate()
   {
     return feeDate;
   }
   
   
   public float getSum()
   {
     return sum;
   }
   
   
   public ApplicationInfo getApplication()
   {
     return application;
   }
   
   
   public String getCasherId()
   {
     return casherId;
   }


public String getUnit() {
	return unit;
}


public void setUnit(String unit) {
	this.unit = unit;
}


public int getNumber() {
	return number;
}


public void setNumber(int number) {
	this.number = number;
}


public int getNumber2() {
	return number2;
}


public void setNumber2(int number2) {
	this.number2 = number2;
}
      
}
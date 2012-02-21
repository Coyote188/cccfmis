package cccf.ma.model;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import cccf.myenum.ProductPersonType;

public class Product_User_ListInfo 
{   
   private String id;      
   private UserInfo user;      
   private ProductCatalogueInfo product;      
   private Integer type;   
   private String typeName;
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setUser(UserInfo value)
   {
     this.user=value;
   }
   
   
   public void setProduct(ProductCatalogueInfo value)
   {
     this.product=value;
   }
   
   
   public void setType(Integer value)
   {
     this.type=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public UserInfo getUser()
   {
     return user;
   }
   
   
   public ProductCatalogueInfo getProduct()
   {
     return product;
   }
   
   
   public Integer getType()
   {
     return type;
   }
   
   public String getTypeName() {
		return ProductPersonType.values()[type].toString();
	}   
}
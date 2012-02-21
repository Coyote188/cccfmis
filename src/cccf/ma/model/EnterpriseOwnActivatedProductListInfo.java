package cccf.ma.model;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class EnterpriseOwnActivatedProductListInfo 
{   
   private String id;      
   private EnterpriseInfo enterprise;      
   private ProductCatalogueInfo product;      
   private java.util.Date applydate;      
   private java.util.Date activatedate;      
   private Integer activateStatus;      
   private UserInfo activateuser;      
   private String description;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setEnterprise(EnterpriseInfo value)
   {
     this.enterprise=value;
   }
   
   
   public void setProduct(ProductCatalogueInfo value)
   {
     this.product=value;
   }
   
   
   public void setApplydate(java.util.Date value)
   {
     this.applydate=value;
   }
   
   
   public void setActivatedate(java.util.Date value)
   {
     this.activatedate=value;
   }
   
   
   public void setActivateStatus(Integer value)
   {
     this.activateStatus=value;
   }
   
   
   public void setActivateuser(UserInfo value)
   {
     this.activateuser=value;
   }
   
   
   public void setDescription(String value)
   {
     this.description=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public EnterpriseInfo getEnterprise()
   {
     return enterprise;
   }
   
   
   public ProductCatalogueInfo getProduct()
   {
     return product;
   }
   
   
   public java.util.Date getApplydate()
   {
     return applydate;
   }
   
   
   public java.util.Date getActivatedate()
   {
     return activatedate;
   }
   
   
   public Integer getActivateStatus()
   {
     return activateStatus;
   }
   
   
   public UserInfo getActivateuser()
   {
     return activateuser;
   }
   
   
   public String getDescription()
   {
     return description;
   }
      
}
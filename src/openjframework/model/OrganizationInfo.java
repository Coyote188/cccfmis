package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class OrganizationInfo 
{   
   private String id;      
   private String organizationNo;      
   private String organizationName;      
   private String organizationSimp;      
   private String organizationAddr;      
   private boolean checkCenter;      
   private String contacts;      
   private String telephone;      
   private String fax;      
   private String email;      
   private String bank;      
   private String bankAccount;      
   private String accountName;      
   private Integer organizationLevel;      
   private OrganizationInfo organizationParent;      
   private Set organizationChildren;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setOrganizationNo(String value)
   {
     this.organizationNo=value;
   }
   
   
   public void setOrganizationName(String value)
   {
     this.organizationName=value;
   }
   
   
   public void setOrganizationSimp(String value)
   {
     this.organizationSimp=value;
   }
   
   
   public void setOrganizationAddr(String value)
   {
     this.organizationAddr=value;
   }
   
   
   public void setCheckCenter(boolean value)
   {
     this.checkCenter=value;
   }
   
   
   public void setContacts(String value)
   {
     this.contacts=value;
   }
   
   
   public void setTelephone(String value)
   {
     this.telephone=value;
   }
   
   
   public void setFax(String value)
   {
     this.fax=value;
   }
   
   
   public void setEmail(String value)
   {
     this.email=value;
   }
   
   
   public void setBank(String value)
   {
     this.bank=value;
   }
   
   
   public void setBankAccount(String value)
   {
     this.bankAccount=value;
   }
   
   
   public void setAccountName(String value)
   {
     this.accountName=value;
   }
   
   
   public void setOrganizationLevel(Integer value)
   {
     this.organizationLevel=value;
   }
   
   
   public void setOrganizationParent(OrganizationInfo value)
   {
     this.organizationParent=value;
   }
   
   
   public void setOrganizationChildren(Set value)
   {
     this.organizationChildren=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getOrganizationNo()
   {
     return organizationNo;
   }
   
   
   public String getOrganizationName()
   {
     return organizationName;
   }
   
   
   public String getOrganizationSimp()
   {
     return organizationSimp;
   }
   
   
   public String getOrganizationAddr()
   {
     return organizationAddr;
   }
   
   
   public boolean isCheckCenter()
   {
     return checkCenter;
   }
   
   
   public String getContacts()
   {
     return contacts;
   }
   
   
   public String getTelephone()
   {
     return telephone;
   }
   
   
   public String getFax()
   {
     return fax;
   }
   
   
   public String getEmail()
   {
     return email;
   }
   
   
   public String getBank()
   {
     return bank;
   }
   
   
   public String getBankAccount()
   {
     return bankAccount;
   }
   
   
   public String getAccountName()
   {
     return accountName;
   }
   
   
   public Integer getOrganizationLevel()
   {
     return organizationLevel;
   }
   
   
   public OrganizationInfo getOrganizationParent()
   {
     return organizationParent;
   }
   
   
   @SuppressWarnings("unchecked")
   public Set<OrganizationInfo> getOrganizationChildren()
   {
     return organizationChildren;
   }
   
   public void addChild(OrganizationInfo organization) {

       if (organization== null)

               throw new IllegalArgumentException();

       if (organization.getOrganizationParent() != null)

    	   organization.getOrganizationParent().getOrganizationChildren().remove( organization);

       organization.setOrganizationParent(this);

       this.getOrganizationChildren().add(organization);

}
      
}
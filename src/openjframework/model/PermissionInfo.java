package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class PermissionInfo 
{   
   private String id;      
   private ResourceInfo resource;      
   private Set roleList;      
   private Integer operationType;      
   private Integer permissionType;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setResource(ResourceInfo value)
   {
     this.resource=value;
   }
   
   
   public void setRoleList(Set value)
   {
     this.roleList=value;
   }
   
   
   public void setOperationType(Integer value)
   {
     this.operationType=value;
   }
   
   
   public void setPermissionType(Integer value)
   {
     this.permissionType=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public ResourceInfo getResource()
   {
     return resource;
   }
   
   
   public Set getRoleList()
   {
     return roleList;
   }
   
   
   public Integer getOperationType()
   {
     return operationType;
   }
   
   
   public Integer getPermissionType()
   {
     return permissionType;
   }
      
}
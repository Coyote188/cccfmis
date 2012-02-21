package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class ResourceInfo 
{   
   private String id;      
   private String no;      
   private String name;      
   private String resDesc;      
   private ResourceTypeInfo type;      
   private String resourceUrl;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setNo(String value)
   {
     this.no=value;
   }
   
   
   public void setName(String value)
   {
     this.name=value;
   }
   
   
   public void setResDesc(String value)
   {
     this.resDesc=value;
   }
   
   
   public void setType(ResourceTypeInfo value)
   {
     this.type=value;
   }
   
   
   public void setResourceUrl(String value)
   {
     this.resourceUrl=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getNo()
   {
     return no;
   }
   
   
   public String getName()
   {
     return name;
   }
   
   
   public String getResDesc()
   {
     return resDesc;
   }
   
   
   public ResourceTypeInfo getType()
   {
     return type;
   }
   
   
   public String getResourceUrl()
   {
     return resourceUrl;
   }
      
}
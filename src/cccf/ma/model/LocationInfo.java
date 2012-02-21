package cccf.ma.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class LocationInfo 
{   
   private String id;      
   private String name;      
   private LocationInfo parent;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setName(String value)
   {
     this.name=value;
   }
   
   
   public void setParent(LocationInfo value)
   {
     this.parent=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getName()
   {
     return name;
   }
   
   
   public LocationInfo getParent()
   {
     return parent;
   }
      
}
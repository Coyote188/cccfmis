package cccf.ma.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class StateInfo 
{   
   private String id;      
   private String name;   
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setName(String value)
   {
     this.name=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getName()
   {
     return name;
   }
      
}
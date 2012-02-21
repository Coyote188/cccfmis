package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class PoliticalStatusInfo 
{   
   private String no;      
   private String name;   
   
   public void setNo(String value)
   {
     this.no=value;
   }
   
   
   public void setName(String value)
   {
     this.name=value;
   }
   
   
   public String getNo()
   {
     return no;
   }
   
   
   public String getName()
   {
     return name;
   }
      
}
package openjframework.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class RoleInfo 
{   
   private String id;      
   private String name;      
   private Set userList;   
   private boolean isReadonly;
   private String memo;
   
   public void setId(String value)
   {
     this.id=value;
   }
   
   
   public void setName(String value)
   {
     this.name=value;
   }
   
   
   public void setUserList(Set value)
   {
     this.userList=value;
   }
   
   
   public String getId()
   {
     return id;
   }
   
   
   public String getName()
   {
     return name;
   }
   
   
   public Set getUserList()
   {
     return userList;
   }

	
	public void setReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}
	
	
	public boolean isReadonly() {
		return isReadonly;
	}
	
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	public String getMemo() {
		return memo;
	}
      public void setReadonly(){
    	  setReadonly(true);
      }
}
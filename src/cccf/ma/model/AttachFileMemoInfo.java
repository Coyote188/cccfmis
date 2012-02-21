package cccf.ma.model;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class AttachFileMemoInfo 
{   
   private String memoId;      
   private String applicationId;      
   private Long taskInstanceId;      
   private String memo;      
   private String fieldName;      
   private String modelId;   
   
   public void setMemoId(String value)
   {
     this.memoId=value;
   }
   
   
   public void setApplicationId(String value)
   {
     this.applicationId=value;
   }
   
   
   public void setTaskInstanceId(Long value)
   {
     this.taskInstanceId=value;
   }
   
   
   public void setMemo(String value)
   {
     this.memo=value;
   }
   
   
   public void setFieldName(String value)
   {
     this.fieldName=value;
   }
   
   
   public void setModelId(String value)
   {
     this.modelId=value;
   }
   
   
   public String getMemoId()
   {
     return memoId;
   }
   
   
   public String getApplicationId()
   {
     return applicationId;
   }
   
   
   public Long getTaskInstanceId()
   {
     return taskInstanceId;
   }
   
   
   public String getMemo()
   {
     return memo;
   }
   
   
   public String getFieldName()
   {
     return fieldName;
   }
   
   
   public String getModelId()
   {
     return modelId;
   }
      
}
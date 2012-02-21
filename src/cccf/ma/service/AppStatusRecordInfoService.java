package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface AppStatusRecordInfoService
{
     public void delete(AppStatusRecordInfo bean);
     public Serializable create(AppStatusRecordInfo bean);
     public void update(AppStatusRecordInfo bean);
     public void saveOrUpdate(AppStatusRecordInfo bean);
     public AppStatusRecordInfo getById(String id);
     public AppStatusRecordInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
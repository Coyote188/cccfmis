package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface EnterpriseNoticeInfoService
{
     public void delete(EnterpriseNoticeInfo bean);
     public Serializable create(EnterpriseNoticeInfo bean);
     public void update(EnterpriseNoticeInfo bean);
     public void saveOrUpdate(EnterpriseNoticeInfo bean);
     public EnterpriseNoticeInfo getById(String id);
     public EnterpriseNoticeInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface AttachFileMemoInfoService
{
     public void delete(AttachFileMemoInfo bean);
     public Serializable create(AttachFileMemoInfo bean);
     public void update(AttachFileMemoInfo bean);
     public void saveOrUpdate(AttachFileMemoInfo bean);
     public AttachFileMemoInfo getById(String id);
     public AttachFileMemoInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
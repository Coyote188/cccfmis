package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface FeeDetailInfoService
{
     public void delete(FeeDetailInfo bean);
     public Serializable create(FeeDetailInfo bean);
     public void update(FeeDetailInfo bean);
     public void saveOrUpdate(FeeDetailInfo bean);
     public FeeDetailInfo getById(String id);
     public FeeDetailInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
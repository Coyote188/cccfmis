package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface ManufactureInfoService
{
     public void delete(ManufactureInfo bean);
     public Serializable create(ManufactureInfo bean);
     public void update(ManufactureInfo bean);
     public void saveOrUpdate(ManufactureInfo bean);
     public ManufactureInfo getById(String id);
     public ManufactureInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
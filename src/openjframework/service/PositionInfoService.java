package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface PositionInfoService
{
     public void delete(PositionInfo bean);
     public Serializable create(PositionInfo bean);
     public void update(PositionInfo bean);
     public void saveOrUpdate(PositionInfo bean);
     public PositionInfo getById(String id);
     public PositionInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
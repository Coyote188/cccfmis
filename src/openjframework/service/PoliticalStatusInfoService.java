package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface PoliticalStatusInfoService
{
     public void delete(PoliticalStatusInfo bean);
     public Serializable create(PoliticalStatusInfo bean);
     public void update(PoliticalStatusInfo bean);
     public void saveOrUpdate(PoliticalStatusInfo bean);
     public PoliticalStatusInfo getById(String id);
     public PoliticalStatusInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
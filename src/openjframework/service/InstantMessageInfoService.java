package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface InstantMessageInfoService
{
     public void delete(InstantMessageInfo bean);
     public Serializable create(InstantMessageInfo bean);
     public void update(InstantMessageInfo bean);
     public void saveOrUpdate(InstantMessageInfo bean);
     public InstantMessageInfo getById(String id);
     public InstantMessageInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
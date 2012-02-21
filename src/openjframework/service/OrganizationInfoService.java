package openjframework.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import openjframework.model.*;
public interface OrganizationInfoService
{
     public void delete(OrganizationInfo bean);
     public Serializable create(OrganizationInfo bean);
     public void update(OrganizationInfo bean);
     public void saveOrUpdate(OrganizationInfo bean);
     public OrganizationInfo getById(String id);
     public OrganizationInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
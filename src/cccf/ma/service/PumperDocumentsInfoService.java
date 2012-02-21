package cccf.ma.service;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface PumperDocumentsInfoService
{
     public void delete(PumperDocumentsInfo bean);
     public Serializable create(PumperDocumentsInfo bean);
     public void update(PumperDocumentsInfo bean);
     public void saveOrUpdate(PumperDocumentsInfo bean);
     public PumperDocumentsInfo getById(String id);
     public PumperDocumentsInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
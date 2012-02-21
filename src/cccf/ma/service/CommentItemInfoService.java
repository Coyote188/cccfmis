package cccf.ma.service;
import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;
import cccf.ma.model.*;
public interface CommentItemInfoService
{
     public void delete(CommentItemInfo bean);
     public Serializable create(CommentItemInfo bean);
     public void update(CommentItemInfo bean);
     public void saveOrUpdate(CommentItemInfo bean);
     public CommentItemInfo getById(String id);
     public CommentItemInfo getByPrimaryKey(String key);
     public List findByQuery(String querystr);
     public List getAll();
     
     
 }
   
    
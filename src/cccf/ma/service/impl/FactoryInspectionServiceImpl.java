package cccf.ma.service.impl;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.model.FactoryInspection;
import cccf.ma.service.FactoryInspectionService;

import com.aidi.core.service.BaseDAOServcieUtil;

public class FactoryInspectionServiceImpl implements FactoryInspectionService {
	public Serializable create(FactoryInspection bean)
	   {
	   		return BaseDAOServcieUtil.save(bean);
	   	}
	public void delete(FactoryInspection bean)
	   {
	   BaseDAOServcieUtil.remove(bean);}
	public void update(FactoryInspection bean)
	   {
	   BaseDAOServcieUtil.upDate(bean);}
	public FactoryInspection getById(String id)
	   {
	   return (FactoryInspection)BaseDAOServcieUtil.findById(FactoryInspection.class,id);
	   }
	public FactoryInspection getByPrimaryKey(String key)
	   {
	   return (FactoryInspection)BaseDAOServcieUtil.findByPrimaryKey(FactoryInspection.class,key);
	   }
	public List findByQuery(String querystr){
			return BaseDAOServcieUtil.findByQueryString(querystr);
	   }
	public List getAll(){
			return BaseDAOServcieUtil.findByQueryString("from FactoryInspection");
		}
	public void saveOrUpdate(FactoryInspection bean){
			BaseDAOServcieUtil.saveOrUpdata(bean);
		}
}

package cccf.ma.service;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.EnterpriseProductModel;

public interface EnterpriseProductModelService {

	public Serializable create(EnterpriseProductModel bean);

	public void delete(EnterpriseProductModel bean);

	public void update(EnterpriseProductModel bean);

	public EnterpriseProductModel getById(String id);

	public EnterpriseProductModel getByPrimaryKey(String key);

	public List findByQuery(String querystr);

	public List getAll();

	public void saveOrUpdate(EnterpriseProductModel bean);

}
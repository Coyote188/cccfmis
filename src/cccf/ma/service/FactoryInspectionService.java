package cccf.ma.service;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.model.FactoryInspection;

public interface FactoryInspectionService {

	public abstract Serializable create(FactoryInspection bean);

	public abstract void delete(FactoryInspection bean);

	public abstract void update(FactoryInspection bean);

	public abstract FactoryInspection getById(String id);

	public abstract FactoryInspection getByPrimaryKey(String key);

	public abstract List findByQuery(String querystr);

	public abstract List getAll();

	public abstract void saveOrUpdate(FactoryInspection bean);

}
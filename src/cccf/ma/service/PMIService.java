package cccf.ma.service;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.ProductionModelInspection;

public interface PMIService {

	public abstract Serializable create(ProductionModelInspection bean);

	public abstract void delete(ProductionModelInspection bean);

	public abstract void update(ProductionModelInspection bean);

	public abstract ProductionModelInspection getById(String id);

	public abstract ProductionModelInspection getByPrimaryKey(String key);

	public abstract List findByQuery(String querystr);

	public abstract List getAll();

	public abstract void saveOrUpdate(ProductionModelInspection bean);

}
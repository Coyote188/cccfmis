package cccf.ma.service;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.FactoryChecklist;

public interface FactoryCheckService {

	public abstract Serializable create(FactoryChecklist bean);

	public abstract void delete(FactoryChecklist bean);

	public abstract void update(FactoryChecklist bean);

	public abstract FactoryChecklist getById(String id);

	public abstract FactoryChecklist getByPrimaryKey(String key);

	public abstract List findByQuery(String querystr);

	public abstract List getAll();

	public abstract void saveOrUpdate(FactoryChecklist bean);

}
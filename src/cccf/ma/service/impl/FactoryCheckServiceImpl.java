package cccf.ma.service.impl;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.FactoryChecklist;
import cccf.ma.service.FactoryCheckService;

import com.aidi.core.service.BaseDAOServcieUtil;

public class FactoryCheckServiceImpl implements FactoryCheckService {
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#create(cccf.ma.model.FactoryChecklist)
	 */
	public Serializable create(FactoryChecklist bean) {
		return BaseDAOServcieUtil.save(bean);
	}

	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#delete(cccf.ma.model.FactoryChecklist)
	 */
	public void delete(FactoryChecklist bean) {
		BaseDAOServcieUtil.remove(bean);
	}

	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#update(cccf.ma.model.FactoryChecklist)
	 */
	public void update(FactoryChecklist bean) {
		BaseDAOServcieUtil.upDate(bean);
	}

	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#getById(java.lang.String)
	 */
	public FactoryChecklist getById(String id) {
		return (FactoryChecklist) BaseDAOServcieUtil.findById(
				FactoryChecklist.class, id);
	}

	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#getByPrimaryKey(java.lang.String)
	 */
	public FactoryChecklist getByPrimaryKey(String key) {
		return (FactoryChecklist) BaseDAOServcieUtil.findByPrimaryKey(
				FactoryChecklist.class, key);
	}

	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#findByQuery(java.lang.String)
	 */
	public List findByQuery(String querystr) {
		return BaseDAOServcieUtil.findByQueryString(querystr);
	}

	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#getAll()
	 */
	public List getAll() {
		return BaseDAOServcieUtil.findByQueryString("from FactoryChecklist");
	}

	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.FactoryCheckService#saveOrUpdate(cccf.ma.model.FactoryChecklist)
	 */
	public void saveOrUpdate(FactoryChecklist bean) {
		BaseDAOServcieUtil.saveOrUpdata(bean);
	}
}

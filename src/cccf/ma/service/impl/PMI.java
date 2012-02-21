package cccf.ma.service.impl;

import java.io.Serializable;
import java.util.List;

import cccf.ma.model.ProductionModelInspection;
import cccf.ma.service.PMIService;

import com.aidi.core.service.BaseDAOServcieUtil;

public class PMI implements PMIService {
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#create(cccf.ma.model.ProductionModelInspection)
	 */
	public Serializable create(ProductionModelInspection bean)
	   {
	   		return BaseDAOServcieUtil.save(bean);
	   	}
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#delete(cccf.ma.model.ProductionModelInspection)
	 */
	public void delete(ProductionModelInspection bean)
	   {
	   BaseDAOServcieUtil.remove(bean);}
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#update(cccf.ma.model.ProductionModelInspection)
	 */
	public void update(ProductionModelInspection bean)
	   {
	   BaseDAOServcieUtil.upDate(bean);}
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#getById(java.lang.String)
	 */
	public ProductionModelInspection getById(String id)
	   {
	   return (ProductionModelInspection)BaseDAOServcieUtil.findById(ProductionModelInspection.class,id);
	   }
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#getByPrimaryKey(java.lang.String)
	 */
	public ProductionModelInspection getByPrimaryKey(String key)
	   {
	   return (ProductionModelInspection)BaseDAOServcieUtil.findByPrimaryKey(ProductionModelInspection.class,key);
	   }
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#findByQuery(java.lang.String)
	 */
	public List findByQuery(String querystr){
			return BaseDAOServcieUtil.findByQueryString(querystr);
	   }
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#getAll()
	 */
	public List getAll(){
			return BaseDAOServcieUtil.findByQueryString("from ProductionModelInspection");
		}
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.PMIService#saveOrUpdate(cccf.ma.model.ProductionModelInspection)
	 */
	public void saveOrUpdate(ProductionModelInspection bean){
			BaseDAOServcieUtil.saveOrUpdata(bean);
		}
}

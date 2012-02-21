package cccf.ma.service.impl;

import java.io.Serializable;
import java.util.List;
import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.service.EnterpriseProductModelService;

import com.aidi.core.service.BaseDAOServcieUtil;

public class EnterpriseProductModelServiceImpl implements EnterpriseProductModelService {
	/* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#create(cccf.ma.model.EnterpriseProductModel)
	 */
	public Serializable create(EnterpriseProductModel bean)
	   {
	   		return BaseDAOServcieUtil.save(bean);
	   	}
	   
	   /* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#delete(cccf.ma.model.EnterpriseProductModel)
	 */
	public void delete(EnterpriseProductModel bean)
	   {
	   BaseDAOServcieUtil.remove(bean);}
	   
	   /* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#update(cccf.ma.model.EnterpriseProductModel)
	 */
	public void update(EnterpriseProductModel bean)
	   {
	   BaseDAOServcieUtil.upDate(bean);}
	   
	   /* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#getById(java.lang.String)
	 */
	public EnterpriseProductModel getById(String id)
	   {
	   return (EnterpriseProductModel)BaseDAOServcieUtil.findById(EnterpriseProductModel.class,id);
	   }
	   
	   /* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#getByPrimaryKey(java.lang.String)
	 */
	public EnterpriseProductModel getByPrimaryKey(String key)
	   {
	   return (EnterpriseProductModel)BaseDAOServcieUtil.findByPrimaryKey(EnterpriseProductModel.class,key);
	   }
	   
	   /* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#findByQuery(java.lang.String)
	 */
	public List findByQuery(String querystr){
			return BaseDAOServcieUtil.findByQueryString(querystr);
	   }
	   
	   /* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#getAll()
	 */
	public List getAll(){
			return BaseDAOServcieUtil.findByQueryString("from EnterpriseProductModel");
		}
		
	  /* (non-Javadoc)
	 * @see cccf.ma.service.impl.EnterpriseProductModelService#saveOrUpdate(cccf.ma.model.EnterpriseProductModel)
	 */
	public void saveOrUpdate(EnterpriseProductModel bean){
			BaseDAOServcieUtil.saveOrUpdata(bean);
		}
}

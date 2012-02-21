package cccf.ma.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.modellite.spring.BeanAdapter;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import cccf.ma.service.BaseService;

public abstract class AbstractBaseService implements BaseService{
	private HibernateTemplate hibernateTemplate;
	
	 
	
	public int queryForResultSize(String hql){ 
		Object result = querySingleResult(hql);
		if(result==null)return 0;
		Integer i = new Integer(result.toString());
		return i.intValue();
	} 
	public Object querySingleResult(final String hql){
		if(hql==null || hql.trim().length()<1){
			throw new RuntimeException("传入HQL不能为空！");
		}
		return getHibernateTemplate().execute(new HibernateCallback() { 
			@Override
			public Object doInHibernate(Session s) throws HibernateException,
					SQLException {
				Query query = s.createQuery(hql);
                return query.uniqueResult();   
			}
		});
	}
	public List  queryListForPage(final int startPosition,final int maxresult ,final String hql ){
		if(hql==null || hql.trim().length()<1){
			throw new RuntimeException("传入HQL不能为空！");
		}
          List list = getHibernateTemplate().executeFind(new HibernateCallback(){ 
              public Object doInHibernate(Session session)
                      throws HibernateException, SQLException {
                  List list2 = session.createQuery(hql)
                          .setFirstResult(startPosition)
                          .setMaxResults(maxresult)
                          .list();                    
                  return list2;
              }});
          return list; 
	}
	  
	public List getResultList(String hql){
		if(hql==null || hql.trim().length()<1){
			throw new RuntimeException("传入HQL不能为空！");
		}
		return getHibernateTemplate().find(hql);
	}
	public List  queryMultipleResults(List<String> inputs){
		List<Object> results = new ArrayList<Object>();
		for(String hql : inputs){   
			results.add(getResultList(hql));
		} 
		return results;
	}
	public HibernateTemplate getHibernateTemplate() {
		if(hibernateTemplate==null){
			hibernateTemplate= BeanAdapter.getBean("hibernateTemplate",HibernateTemplate.class); 
		}
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}

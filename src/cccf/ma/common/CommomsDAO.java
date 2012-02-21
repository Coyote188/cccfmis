package cccf.ma.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class CommomsDAO {
	 
	
	/** 
     * 分页通用方法
     * @param hql  HQL查询语句
     * @param offset    起始记录下标
     * @param lengh        读取记录数
     * @return            List 结果集
     */
    public static List getListForPage(final String hql, final int offset, final int lengh) {
    	    HibernateTemplate hibernateTemplate = BeanAdapter.getBean("hibernateTemplate",HibernateTemplate.class); 
            List list = hibernateTemplate.executeFind(new HibernateCallback(){

                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    List list2 = session.createQuery(hql)
                            .setFirstResult(offset)
                            .setMaxResults(lengh)
                            .list();                    
                    return list2;
                }});
            return list; 
    }
    
    public static Object querySingleResult( String hql){
    	HibernateTemplate hibernateTemplate = BeanAdapter.getBean("hibernateTemplate",HibernateTemplate.class); 
    	List list = hibernateTemplate.find( hql ); 
    	if(list==null)return null; 
    	 
    	return  list.get(0) ;
    }

 

}

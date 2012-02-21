package cccf.mis.web.tsak;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;

public class CommonFunction {
	/**
	 * 取到无处理人员的  待办任务ID
	 * @param usid
	 * @param taskName
	 * @return
	 */
	public static StringBuffer getBacklogs(String usid,String taskName){
		StringBuffer hql = new StringBuffer(" SELECT DISTINCT ti.id ")
                         .append(" FROM org.jbpm.taskmgmt.exe.PooledActor pooledActor ")
                         .append(" JOIN pooledActor.taskInstances ti ")
                         .append(" WHERE pooledActor.actorId = '").append(usid).append("'")
                         .append(" AND ti.end IS NULL")
		                 .append(" AND ti.isSuspended != TRUE")
		                 .append(" AND ti.isOpen = TRUE")
		                 .append(" AND ti.task.name = '").append(taskName).append("'");
	    JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
	    JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
	    Session session = jbpmContext.getSession();
	    StringBuffer ids = new StringBuffer("(''");
	    try { 
	        Query query = session.createQuery(hql.toString()); 
	        List<?> list = query.list();
	        for(Object o : list){
	        	ids.append(",'").append(o).append("'");
	        }
	     } catch (Exception ex) {
	    	 ex.printStackTrace();
	    	 throw new RuntimeException(ex); 
	     } finally { 
	        jbpmContext.close();
	    }
	    ids.append(")");
		return ids;
	}
}

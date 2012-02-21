package cccf.ma.common;

import java.math.BigInteger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class JbpmUtil {
	 static JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
	 public static TaskInstance getCurrentTaskInstance(Long processInstanceId){
		 Session session =null;
		 TaskInstance ti=null;
		 try{
			 JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();  
			 session = jbpmContext.getSession();
			 
			 Query query = session.createQuery("select ti from org.jbpm.taskmgmt.exe.TaskInstance as ti where ti.processInstance.id = :processInstanceId and ti.end is null ");
			 query.setParameter("processInstanceId", processInstanceId);  
			 ti =(TaskInstance) query.uniqueResult();
		 }finally{
			 session.close();
		 }
		 return ti;
	 }
	 
	 
	 public static long getProcessID(String formURL) {
		 JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();  
		 Session session = jbpmContext.getSession();
		 try{
		 StringBuffer sql = new StringBuffer("SELECT pic.ID_ ")
		               .append(" FROM processform p,jbpm_processdefinition pic ")
		               .append(" WHERE p.processId = pic.ID_")
		               .append(" AND formURL=?")
		               .append(" ORDER BY pic.VERSION_ desc"); 
		 Query q = session.createSQLQuery(sql.toString());
		 q.setParameter(0, formURL);
		 BigInteger bi = (BigInteger)q.list().get(0);
		 return bi.longValue();
		 }finally{
			 session.close();
		 }
	 }
}

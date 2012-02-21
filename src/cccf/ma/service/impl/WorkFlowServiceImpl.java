package cccf.ma.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.db.GraphSession;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import cccf.ma.common.JbpmUtil;
import cccf.ma.model.ProcessLog;
import cccf.ma.service.WorkFlowService;

public class  WorkFlowServiceImpl extends AbstractBaseService implements
		WorkFlowService {
	private JbpmConfiguration jbpmConfiguration = JbpmConfiguration
			.getInstance();

	private SimpleDateFormat fordate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private Date dateToFormat(Date dt) {
		try {
			dt = fordate.parse(dt.toLocaleString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return dt;
	}

	@Override
	public long doStartProcessAndDoTask(String formUrl, String userId,
			List<ProcessLog> variables, String approveResult, String approveMemo) {
		// 取得工作流ID
		String processId = String.valueOf(JbpmUtil.getProcessID(formUrl));
		// 如果流程能够找到
		Long pid = new Long(0);
		if (variables == null || variables.size() == 0) {
			throw new RuntimeException("启动任务，没有关联业务对象！");
		}
		Map vs = new HashMap();
		for (ProcessLog item : variables) {
			vs.put("entityName", item.getBoType());
			vs.put("rowId", item.getBoId());
		}
		if (processId != null && processId != "0") {
			 
			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
			try {
				GraphSession graphSession = jbpmContext.getGraphSession();
				ProcessDefinition processDefinition = graphSession
						.getProcessDefinition(Long.parseLong(processId));
				ProcessInstance processInstance = new ProcessInstance(
						processDefinition);
				pid = processInstance.getId();
				TaskInstance taskInstance = processInstance
						.getTaskMgmtInstance().createStartTaskInstance(); 
				
				if (taskInstance != null) {
					taskInstance.addVariables(vs);
					taskInstance.setVariable("actorId", userId);
					taskInstance.setActorId(userId);
					taskInstance.end();  
 
					for (ProcessLog item : variables) {
						ProcessLog log = new ProcessLog(item.getBoType(),
								item.getBoId(), item.getBoStatus());
						if (log.getBoType() == null
								|| log.getBoType().length() == 0) {
							throw new RuntimeException("业务对象类型不能为空！");
						}
						if (log.getBoId() == null
								|| log.getBoId().length() == 0) {
							throw new RuntimeException("业务对象ID不能为空！");
						}
						 
						log.setActorId(userId);
						log.setApproveMemo(approveMemo);
						log.setApproveResult(approveResult);
						log.setCreaterId(userId);
						log.setCreate(taskInstance.getCreate());

						log.setEnd(taskInstance.getEnd());
						log.setTaskInstanceId(taskInstance.getId());
						log.setTaskInstanceName(taskInstance.getName());
						log.setProcessInstanceId(processInstance.getId());
						getHibernateTemplate().save(log);
					}
				} else {
					ContextInstance contextInstance = processInstance
							.getContextInstance();
					contextInstance.setVariable("vs", vs);
					contextInstance.setVariable("actorId", userId);
					processInstance.signal();
				}

				Collection<TaskInstance> taskInstanceCollection = processInstance
						.getTaskMgmtInstance().getTaskInstances();
				TaskInstance nextTaskInstance = null;
				for (TaskInstance ti : taskInstanceCollection) {
					if (!ti.hasEnded()) {
						nextTaskInstance = ti;
						break;
					}
				}
				if (nextTaskInstance != null) {
					String previousActorId = taskInstance.getActorId();
					nextTaskInstance.setVariableLocally("previousActorId",
							previousActorId);
					nextTaskInstance.setVariableLocally(
							"previousTaskInstanceId",
							Long.valueOf(taskInstance.getId()));
					jbpmContext.save(nextTaskInstance);

					for (ProcessLog item : variables) {
						ProcessLog log = new ProcessLog(item.getBoType(),
								item.getBoId(), item.getBoNextStatus());
						if (log.getBoType() == null
								|| log.getBoType().length() == 0) {
							throw new RuntimeException("业务对象类型不能为空！");
						}
						if (log.getBoId() == null
								|| log.getBoId().length() == 0) {
							throw new RuntimeException("业务对象ID不能为空！");
						} 
						log.setPreviousTaskInstanceId(taskInstance.getId());
						log.setCreaterId(taskInstance.getActorId());// 创建人是前一节点的处理人
						log.setCreate(nextTaskInstance.getCreate());
						log.setTaskInstanceId(nextTaskInstance.getId());
						log.setTaskInstanceName(nextTaskInstance.getName());
						log.setProcessInstanceId(processInstance.getId());
						getHibernateTemplate().save(log);
					}
				}
				jbpmContext.save(processInstance);
			}  catch (Exception e) {
				jbpmContext.setRollbackOnly();
				throw new RuntimeException(e);
			}finally {
				jbpmContext.close();
			}
		}
		return pid;
	}

	@Override
	public void doTaskSubmit(Long taskInstanceId, String userId,
			List<ProcessLog> variables, String approveResult,
			String approveMemo, String accepterId) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		try {
			// 1、结束本任务
			TaskMgmtSession taskMgmtSession = jbpmContext.getTaskMgmtSession();

			TaskInstance taskInstance = (TaskInstance) taskMgmtSession
					.loadTaskInstance(taskInstanceId.longValue());
			if (taskInstance == null) {
				return;
			} 
			taskInstance.setActorId(userId);
			taskInstance.end(approveResult);
			jbpmContext.save(taskInstance);

			List<ProcessLog> logs = getResultList("from ProcessLog where taskInstanceId='"
					+ taskInstance.getId() + "'");

			if (logs != null && logs.size() > 0) {
				for (ProcessLog log : logs) {
					log.setActorId(userId);
					log.setApproveMemo(approveMemo);
					log.setApproveResult(approveResult);
					log.setEnd(dateToFormat(new Date()));
					getHibernateTemplate().update(log);
				}
			}

			// 2、启动下一任务
			ProcessInstance processInstance = taskInstance
					.getTaskMgmtInstance().getProcessInstance();
			TaskInstance nextTaskInstance = null;
			Collection<TaskInstance> taskInstanceCollection = processInstance
					.getTaskMgmtInstance().getTaskInstances();
			for (TaskInstance ti : taskInstanceCollection) {
				if (!ti.hasEnded()) {
					nextTaskInstance = ti;
					nextTaskInstance.setVariableLocally("previousActorId",
							userId);
					nextTaskInstance.setVariableLocally(
							"previousTaskInstanceId",
							Long.valueOf(taskInstance.getId())); 
				}
			}
			if (nextTaskInstance != null) {
				if(accepterId!=null){
					nextTaskInstance.setActorId(accepterId);
				} 
				jbpmContext.save(nextTaskInstance);

				if (variables == null || variables.size() == 0) {
					throw new RuntimeException("启动任务，没有关联业务对象！");
				}

				for (ProcessLog item : variables) {
					ProcessLog log = new ProcessLog(item.getBoType(),
							item.getBoId(), item.getBoNextStatus());
					if (log.getBoType() == null
							|| log.getBoType().length() == 0) {
						throw new RuntimeException("业务对象类型不能为空！");
					}
					if (log.getBoId() == null || log.getBoId().length() == 0) {
						throw new RuntimeException("业务对象ID不能为空！");
					}
					log.setPreviousTaskInstanceId(taskInstance.getId());
					log.setCreaterId(userId); 
					if(accepterId!=null){
						log.setActorId( accepterId);
					}
					log.setCreate(nextTaskInstance.getCreate());
					log.setTaskInstanceId(nextTaskInstance.getId());
					log.setTaskInstanceName(nextTaskInstance.getName());
					log.setProcessInstanceId(processInstance.getId());
 
					getHibernateTemplate().save(log);
				}
			}

		} catch (Exception e) {
			jbpmContext.setRollbackOnly();
			throw new RuntimeException(e);
		} finally {
			jbpmContext.close();
		}
	}

	@Override
	public long doStartProcessAndDoTask(String formUrl, String userId,
			ProcessLog processLog, String approveResult, String approveMemo) {
		List<ProcessLog> variables = new ArrayList<ProcessLog>();
		variables.add(processLog);
		return doStartProcessAndDoTask(formUrl,userId,variables,approveResult,approveMemo);
	}

	@Override
	public void doTaskSubmit(Long taskInstanceId, String userId,
			ProcessLog processLog, String approveResult, String approveMemo,
			String accepterId) {
		List<ProcessLog> variables = new ArrayList<ProcessLog>();
		variables.add(processLog);
		doTaskSubmit(taskInstanceId, userId,variables, approveResult, approveMemo, accepterId);
	}

	@Override
	public List getUnDoTaskIdsByUsid(String usid, String taskNodeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doTaskSubmitToCountSign(Long taskInstanceId, String userId,
			List<ProcessLog> variables, String approveResult,
			String approveMemo, String... counterSignUsers) {
		JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
		try {
			// 1、结束本任务
			TaskMgmtSession taskMgmtSession = jbpmContext.getTaskMgmtSession();

			TaskInstance taskInstance = (TaskInstance) taskMgmtSession
					.loadTaskInstance(taskInstanceId.longValue());
			if (taskInstance == null) {
				return;
			}
			Map vs = new HashMap();
			vs.put("counterSignUsers", counterSignUsers);
			taskInstance.addVariables(vs);
			taskInstance.setActorId(userId);
			taskInstance.end(approveResult);
			jbpmContext.save(taskInstance);

			List<ProcessLog> logs = getResultList("from ProcessLog where taskInstanceId='"
					+ taskInstance.getId() + "'");

			if (logs != null && logs.size() > 0) {
				for (ProcessLog log : logs) {
					log.setActorId(userId);
					log.setApproveMemo(approveMemo);
					log.setApproveResult(approveResult);
					log.setEnd(dateToFormat(new Date()));
					getHibernateTemplate().update(log);
				}
			}

			// 2、启动下一任务
			ProcessInstance processInstance = taskInstance
					.getTaskMgmtInstance().getProcessInstance();
			TaskInstance nextTaskInstance = null;
			Collection<TaskInstance> taskInstanceCollection = processInstance
					.getTaskMgmtInstance().getTaskInstances();
			for (TaskInstance ti : taskInstanceCollection) {
				if (!ti.hasEnded()) {
					nextTaskInstance = ti;
					nextTaskInstance.setVariableLocally("previousActorId",
							userId);
					nextTaskInstance.setVariableLocally(
							"previousTaskInstanceId",
							Long.valueOf(taskInstance.getId()));

					if (counterSignUsers == null
							|| counterSignUsers.length == 0) {
						break;
					}
				}
			}
			if (nextTaskInstance != null) {
				jbpmContext.save(nextTaskInstance);

				if (variables == null || variables.size() == 0) {
					throw new RuntimeException("启动任务，没有关联业务对象！");
				}

				for (ProcessLog item : variables) {
					ProcessLog log = new ProcessLog(item.getBoType(),
							item.getBoId(), item.getBoNextStatus());
					if (log.getBoType() == null
							|| log.getBoType().length() == 0) {
						throw new RuntimeException("业务对象类型不能为空！");
					}
					if (log.getBoId() == null || log.getBoId().length() == 0) {
						throw new RuntimeException("业务对象ID不能为空！");
					}
					log.setPreviousTaskInstanceId(taskInstance.getId());
					log.setCreaterId(userId);
					log.setCreate(nextTaskInstance.getCreate());
					log.setTaskInstanceId(nextTaskInstance.getId());
					log.setTaskInstanceName(nextTaskInstance.getName());
					log.setProcessInstanceId(processInstance.getId());
 
					getHibernateTemplate().save(log);
				}
			}

		} catch (Exception e) {
			jbpmContext.setRollbackOnly();
			throw new RuntimeException(e);
		} finally {
			jbpmContext.close();
		}
		
	}

	@Override
	public void doTaskSubmitToCountSign(Long taskInstanceId, String userId,
			ProcessLog processLog, String approveResult, String approveMemo,
			String... counterSignUsers) {
		List<ProcessLog> variables = new ArrayList<ProcessLog>();
		variables.add(processLog);
		doTaskSubmitToCountSign(taskInstanceId, userId,variables, approveResult, approveMemo, counterSignUsers);
		
	}

}

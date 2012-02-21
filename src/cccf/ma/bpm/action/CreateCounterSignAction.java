package cccf.ma.bpm.action;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import cccf.ma.model.*;
import cccf.ma.service.*;

/**符合性审查(会审),注意任务名称"符合性审查(会审)"须与流程定义中的一致
 * @author 
 *
 */
public class CreateCounterSignAction implements ActionHandler {

	private static final long serialVersionUID = 1L;

	public void execute(ExecutionContext executionContext) throws Exception {

		Token token = executionContext.getToken();
		TaskMgmtInstance tmi = executionContext.getTaskMgmtInstance();
		TaskNode taskNode = (TaskNode) executionContext.getNode();
		Task task = taskNode.getTask("符合性审查(会审)");
		// 从数据库或者ldap 读取会签人设置创建任务实例
		String[] counterSignUsers = (String[]) executionContext
				.getVariable("counterSignUsers");
		if (counterSignUsers != null) {
			for (int i = 0; i < counterSignUsers.length; i++)
				tmi.createTaskInstance(task, token).setActorId(
						counterSignUsers[i]);
		}

	}

}

/**
 * 自动设置受理分工节点的流程执行者,如果applicaion.acceptUser不为null则
 * 将actiorid指定为acceptUser.id，如果为null则不指定actiorid
 * 
 */

package cccf.ma.bpm.handler;

import openjframework.model.*;
import openjframework.service.*;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;

import java.util.*;

/**
 * <a href="IdentityAssignmentHandler.java.html"><b><i>View Source</i></b></a>
 * 
 * @author Charles May
 * 
 */
public class ReviewAssignmentHandler implements AssignmentHandler {

	public void assign(Assignable assignable, ExecutionContext executionContext) {

		ApplicationInfo application = null;
		if (executionContext.getVariable("rowId") != null) {
			String rowId = executionContext.getVariable("rowId").toString();
			application = ApplicationInfoServiceUtil.getByPrimaryKey(rowId);
		}
		if (application != null) {
			if (application.getReveiwUser() != null) {
				try {
					assignable.setActorId(application.getReveiwUser().getId());

				} catch (Exception e) {
					
				}
			}
		}

	}

}
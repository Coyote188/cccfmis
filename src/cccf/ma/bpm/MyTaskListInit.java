package cccf.ma.bpm;

import java.util.*;
import java.text.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.model.*;
import openjframework.service.*;

import org.jbpm.graph.def.ProcessDefinition;
import org.zkoss.zk.ui.*;

import com.aidi.bpm.service.BpmUtil;

import org.jbpm.taskmgmt.exe.*;

public class MyTaskListInit extends
		org.zkoss.zkplus.databind.AnnotateDataBinderInit {

	Map params = Executions.getCurrent().getArg();

	public void doAfterCompose(Page page, Component[] comps) throws Exception {
		
		UserInfo user=UserInfoServiceUtil.getCurrentLoginUser();
		String userId = String.valueOf(user.getId());
		List list = BpmUtil.getMyTaskList(userId);
		

		page.setVariable("taskInstanceList", list);

		super.doAfterCompose(page, comps);
	}

}

package cccf.ma.bpm.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

import com.aidi.bpm.model.Approve;
import com.aidi.bpm.service.ApproveServiceUtil;
import com.aidi.bpm.service.BpmUtil;

import cccf.ma.model.*;
import cccf.ma.service.*;
import openjframework.model.*;
import openjframework.service.*;
import openjframework.bpm.service.*;
import openjframework.bpm.model.*;

public class MessageToApplicantAction implements ActionHandler {

	private static final long serialVersionUID = 1L;

	public void execute(ExecutionContext executionContext) throws Exception {

		String userId = executionContext.getVariable("actorId").toString();
//		String rowId = executionContext.getVariable("rowId").toString();
//		StringBuffer msgContent = new StringBuffer();
//		if(rowId!=null){
//			ApplicationInfo app=ApplicationInfoServiceUtil.getById(rowId);
//			msgContent.append("您的受理申请[])
//		}
		
		if (userId != null) {			
			
			//将消息存入数据库用于离线
			MessageInfo msg=new MessageInfo();
			msg.setReceiveUser(UserInfoServiceUtil.getById(userId));
			msg.setContent(content);
			msg.setSendDate(new Date());
			msg.setReadStatus(0);
			UserInfo user=UserInfoServiceUtil.getCurrentLoginUser();
			msg.setSendUser(user);
			msg.setSubject(content);	
			msg.setType(1);
			MessageInfoServiceUtil.sendMessageToDesktop(msg);
			
		}
	}
	
	private String content;

}

package openjframework.service;

import java.util.*;
import java.util.Date;
import java.io.Serializable;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import openjframework.model.*;
import openjframework.util.CommonDateUtil;
import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class MessageInfoServiceUtil
{
	public static Serializable create(MessageInfo bean)
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		return service.create(bean);
	}
	public static void delete(MessageInfo bean)
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		service.delete(bean);
	}
	public static void saveOrUpdate(MessageInfo bean)
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		service.saveOrUpdate(bean);
	}
	public static void update(MessageInfo bean)
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		service.update(bean);
	}
	public static List getAll()
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		return service.getAll();
	}
	public static MessageInfo getById(String id)
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		return service.getById(id);
	}
	public static MessageInfo getByPrimaryKey(String key)
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		return service.getByPrimaryKey(key);
	}
	public static List findByQuery(String querystr)
	{
		CustomerContextHolder.setCustomerType("dltaxoa");
		MessageInfoService service = (MessageInfoService) ServiceAdapter.getServiceByName("MessageInfoService");
		return service.findByQuery(querystr);
	}
	/**
	 * 发送系统级（type=1)实时消息至客户端桌面
	 * 
	 * @param msg
	 *            消息实体，需要主题、接收者、内容属性
	 */
	public static void sendMessageToDesktop(MessageInfo msg)
	{
		if (msg != null)
		{
			// 将消息存入数据库用于离线
			msg.setReadStatus(0);
			msg.setSendUser(UserInfoServiceUtil.getCurrentLoginUser());
			msg.setSendDate(new Date());
			MessageInfoServiceUtil.create(msg);
			// 发送消息
			EventQueues.lookup(msg.getReceiveUser().getId() + "msgEventQueue", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, msg));
		}
	}
	public static void sendTaskMessageToDesktop(TaskInstance taskInstance)
	{
		StringBuffer msgContent = new StringBuffer();
		String actorId = taskInstance.getActorId();
		if (actorId != null)
		{
			UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
			msgContent.append("【任务名称】" + taskInstance.getName() + "\r【发起时间】" + CommonDateUtil.getSimpleDateStringYMDHm(taskInstance.getCreate()) + "\r【发起人】" + user.getName());
			// 将消息存入数据库用于离线
			MessageInfo msg = new MessageInfo();
			msg.setReceiveUser(UserInfoServiceUtil.getById(actorId));
			msg.setContent(msgContent.toString());
			Date dt = CommonDateUtil.getSimpleToDay();
			msg.setSendDate(dt);
			msg.setReadStatus(0);
			msg.setType(1);
			msg.setSendUser(user);
			String taskName = taskInstance.getName();
			String msgSubject = "您有新的待处理任务：" + taskName;
			msg.setSubject(msgSubject);
			MessageInfoServiceUtil.sendMessageToDesktop(msg);
		}
	}
	/**
	 * @param type
	 *            : 0-unread;1-readed;2-all;
	 * @param user
	 *            : Recipient
	 * @return
	 */
	public static List<MessageInfo> findMessage(int type, UserInfo user)
	{
		// type: 0-unread;1-readed;2-all;
		String querystr = "";
		switch (type)
		{
		case 0:
			querystr = "from MessageInfo m where m.receiveUser = '" + user.getId() + "' and m.readStatus = 0 ORDER BY m.sendDate DESC";
			break;
		case 1:
			querystr = "from MessageInfo m where m.receiveUser = '" + user.getId() + "' and m.readStatus = 1 ORDER BY m.sendDate DESC";
			break;
		default:
			querystr = "from MessageInfo m where m.receiveUser = '" + user.getId() + "' ORDER BY m.readStatus ASC,m.sendDate DESC";
			break;
		}
		return findByQuery(querystr);
	}
	public static void readMsg(MessageInfo msg)
	{
		msg.setReadStatus(1);
		update(msg);
	}
	public static boolean isExistMsg(String content)
	{
		boolean b = false;
		String querystr = "from MessageInfo m where m.content = '" + content + "' ";
		List list = findByQuery(querystr);
		if (list != null)
		{
			if (list.size() > 0)
				b = true;
		}
		return b;
	}
}

package openjframework.web.zk;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import openjframework.InstantMessage.UserListData;
import openjframework.model.RoleInfo;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import openjframework.service.UserLogInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cccf.ma.model.CommonTreatyInfo;
import cccf.ma.service.CommonTreatyInfoServiceUtil;
import cccf.ma.service.EnterpriseInfoServiceUtil;

;
public class LoginController
		extends GenericForwardComposer
{
	private static final long	serialVersionUID	= -3801962286418473844L;
	public static final String	onFillText			= "onFillUsername";
	private Textbox				usernameTbx;
	private Textbox				passwordTbx;
	private Window				loginWin;
	Map							param				= new HashMap();
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}
	public void onClick$registerBtn()
			throws SuspendNotAllowedException, InterruptedException
	{
		CommonTreatyInfo productTreaty = CommonTreatyInfoServiceUtil.getNewestofProtocol(0);
		param.put("cott", productTreaty);
		Window objWindow = (Window) Executions.createComponents("/SysForm/treaty_frame.zul", null, param);
		objWindow.setPosition("center");
		objWindow.doModal();
	}
	/**
	 * 键盘Enter
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onOK$loginWin(KeyEvent event)
			throws InterruptedException
	{
		doLogin();
	}
	/**
	 * 单击登录按钮
	 * 
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$loginBtn(Event event)
			throws InterruptedException
	{
		doLogin();
	}
	public void onFillUsername(Event event)
	{
		String username = (String) event.getData();
		usernameTbx.setValue(username);
	}
	public void onLogin(ForwardEvent event)
			throws InterruptedException
	{
		doLogin();
	}
	/**
	 * 登录
	 * 
	 * @throws InterruptedException
	 */
	public void doLogin()
			throws InterruptedException
	{
		String password_cip = Encrypter.encrypt(passwordTbx.getValue());
		String _userName = usernameTbx.getValue();
		if (password_cip == null || password_cip.isEmpty() || _userName == null || _userName.isEmpty())
		{
			Messagebox.show("请输入用户名、密码!", "提示", Messagebox.YES, Messagebox.INFORMATION);
			return;
		}
		if (UserInfoServiceUtil.isExists(_userName, password_cip))
		{
			String turntoUrl = "/login.zul";
			UserInfo user = UserInfoServiceUtil.getByUserName(_userName);
			int userType = user.getType();
			switch (userType)
			{
			case 0:
			{
				turntoUrl = "/views/Indexs/index_person.zhtml";
				/**************** 分配角色后 ******************/
				/*
				 * Set<RoleInfo> roleList = user.getRoleList(); if (roleList != null) { for (RoleInfo role : roleList) { if (role.getName().equals("中心领导")) turntoUrl = "/SysForm/index_person_leader.zul"; } }
				 */
				login(user);
				break;
			}
			case 1:
			{
				turntoUrl = "/views/Index_enterprise/index_ent.zhtml";
				session.setAttribute("EnterpriseName", EnterpriseInfoServiceUtil.findEnterpriseByUser(user).getName());
				break;
				//
			}
			case 2:
				turntoUrl = "/SysForm/index_systemadministrator.zul";
				break;
			default:
			}
			// session.setAttribute("sex", user.getSex().getName());
			session.setAttribute("userid", user.getId());
			session.setAttribute("name", user.getName());
			session.setAttribute("username", _userName);
			// 写登录日志
			UserLogInfoServiceUtil.insertLoginLog();
			Executions.getCurrent().sendRedirect(turntoUrl);
		} else
		{
			Messagebox.show("用户名或密码错误,请重新登录!", "提示", Messagebox.YES, Messagebox.INFORMATION);
			passwordTbx.setValue("");
		}
	}
	/**
	 * 登录(在线)
	 * 
	 * @param user
	 */
	public void login(UserInfo user)
	{
		String username = user.getUsername();
		// if (sessionScope.containsKey("login_name")) {
		if (UserListData.getInstance().isExistsSelf(user))
		{
			UserListData.getInstance().delUser(user);
		}
		sessionScope.put("login_id", user.getId());
		sessionScope.put("login_name", username);
		// 注册一个事件通道
		EventQueues.lookup(username, EventQueues.APPLICATION, true);
		EventQueues.lookup("chatRoomChannel", EventQueues.APPLICATION, true);
		EventQueues.lookup("chatOne_" + username + "_Channel", EventQueues.APPLICATION, true);
		EventQueues.lookup("userList", EventQueues.APPLICATION, true).publish(new Event(username));
		UserListData.getInstance().addUser(user, 1);
	}
	public void onClick$loseBtn()
	{
		param.put("username", usernameTbx.getText().toString());
		Window objWindow = (Window) Executions.createComponents("/SysForm/password-recovery.zul", null, param);
		try
		{
			objWindow.doModal();
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	/**
	 * 退出(离线)
	 * 
	 * @param user
	 */
	public void onLoginOut(UserInfo user)
	{
		if (sessionScope.containsKey("login_name"))
		{
			String name = (String) sessionScope.get("login_name");
			UserListData.getInstance().delUser(user);
			sessionScope.remove("login_name");
			EventQueues.lookup("userList", EventQueues.APPLICATION, false).publish(new Event("loginOut"));
			EventQueues.lookup("chatRoomChannel", EventQueues.APPLICATION, false).publish(new Event("loginOut"));
		}
	}
}

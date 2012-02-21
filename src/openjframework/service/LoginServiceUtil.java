package openjframework.service;

import org.zkoss.zk.ui.Executions;

public class LoginServiceUtil {

	/**
	 * 验证用户是否登录
	 */
	static public void verification()
	{
		if(UserInfoServiceUtil.getCurrentLoginUser()==null){
			Executions.getCurrent().sendRedirect("/login.zul");
		}
	}
}

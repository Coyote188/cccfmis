package openjframework.web.zk;

import java.io.IOException;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import openjframework.web.zk.mail.EncryptDecryptData;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class PasswrodModifyController extends GenericForwardComposer{
	
	private String username = "";
	private UserInfo user;
	
	private String password,password2;
	
	private Textbox tbxPwd2,tbxPwd1;
	private Window jspPasswordModify;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		getUserInfo(Executions.getCurrent().getParameter("username"));
	}
	
	private void getUserInfo(String username){
		String usernameStr ="";
		try {
			usernameStr = EncryptDecryptData.decodeUri(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setUser(UserInfoServiceUtil.findByUsername(usernameStr));
		setUsername(usernameStr);
		
	}
	
	public void onClick$btnSubmit(){
		tbxPwd1.getValue();
		tbxPwd2.getValue();
		boolean isCorrect = password.equals(password2) ? true : false ;
		if (isCorrect) {
			String str = Encrypter.encrypt(password);
			user.setPassword(str);
			UserInfoServiceUtil.update(user);
		}else {
			throw new WrongValueException(tbxPwd2,"两次输入的密码不一致，请重新输入!");
		}
		jspPasswordModify.detach();
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public UserInfo getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

}

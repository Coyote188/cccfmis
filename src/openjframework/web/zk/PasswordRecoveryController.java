package openjframework.web.zk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import openjframework.web.zk.mail.EncryptDecryptData;
import openjframework.web.zk.mail.MailUtil;

import org.zkforge.bwcaptcha.Captcha;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cccf.ma.service.EnterpriseInfoServiceUtil;

public class PasswordRecoveryController extends GenericForwardComposer {

	private Textbox tbxUsername,capCode;
	private Label lblMailAddress;
	private Button btnCheck, btnSubmit;
	private Window frmPWDRecovery;
	private Captcha cap;
	Map param = Executions.getCurrent().getArg();

	private UserInfo userInfo;
	private String username;
	private String mailAddress;
	private String mailAddressDisply;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		changeCap();
		
		if (!param.get("username").toString().equals("")) {
			setUsername(param.get("username").toString());
			setMailAdd();
		} else {
			btnCheck.setVisible(true);
		}
	}

	private void setMailAdd() {
		if (getUsername().equals(""))
			return;

		userInfo = UserInfoServiceUtil.findByUsername(getUsername());
		if (userInfo != null) {
			String mailadd = "";
			switch (userInfo.getType()) {
			case 0:
				mailadd = userInfo.getProfile().getEmail();
				break;
			case 1:
				mailadd = EnterpriseInfoServiceUtil.findEnterpriseByUser(
						userInfo).getEmailAddress();
				break;
			case 2:
				mailadd = userInfo.getProfile().getEmail();
				break;
			}
			if (mailadd != null && !mailadd.equals("")) {
				setMailAddress(mailadd);
				setMailAddressDisply(mailadd.substring(0, 1)
						+ "******"
						+ mailadd.substring(mailadd.lastIndexOf("@"), mailadd
								.length()));
				btnSubmit.setDisabled(false);
			}
			setMailAddress(mailadd);
			setMailAddressDisply(mailadd);
		}

	}

	public void onClick$btnSubmit() {
		verifyCaptcha();
		
		String EnName = "";
		try {
			EnName = EncryptDecryptData.encodeUri(userInfo.getUsername());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String Url = Executions.getCurrent().getServerName() + ":"
			+ Executions.getCurrent().getServerPort()
			+ "/cccfmis/passwordrecovery.jsp?username="
			+ EnName;
		
		if (mailAddress == null || mailAddress.equals(""))
			throw new WrongValueException(lblMailAddress,
					"该用户尚未添写邮件地址，不能进行该操作!");
		try {
			if (Messagebox.YES == Messagebox.show(
					"申请密码找回，系统将发送修改密码页面链接给您资料中添写的邮箱，请核实邮箱的正确性!", "操作提示",
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION)) {
				if (sendMail(Url))
					try {
						Messagebox.show("随机密码已经发送到您的邮箱中，请及时登录系统进行修改!");
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onClick$btnCheck() {
		tbxUsername.getValue();
		setMailAdd();
		lblMailAddress.setValue(getMailAddress());
	}

	private boolean sendMail(String randomPassword) {
		boolean sent = true;

		MailUtil themail = new MailUtil(mailAddress, randomPassword);

		if (themail.sendout() == false)
			sent = false;
		frmPWDRecovery.detach();
		return sent;
	}
	
	boolean verifyCaptcha() {
		boolean is = false;
		if (!cap.getValue().equalsIgnoreCase(capCode.getValue())) {
			throw new WrongValueException(capCode, "验证码输入错误!");
		} else {
			is = true;
		}
		return is;
	}

	private void changeCap() {
		char[] capChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z' };
		cap.setBgColor(0xffffff);
		cap.setCaptchars(capChars);
		cap.setLineColor(0xccffcc);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public void setMailAddressDisply(String mailAddressDisply) {
		this.mailAddressDisply = mailAddressDisply;
	}

	public String getMailAddressDisply() {
		return mailAddressDisply;
	}

}

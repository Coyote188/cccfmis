package openjframework.web.zk;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ModifyPasswordController extends GenericForwardComposer{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	Window userModifyWindow;
	Popup pass;
	Textbox oldpaTbx;
	Textbox aginpaTbx;
	Textbox newpaTbx;
	UserInfo user;
	Button saveBtn;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		user=UserInfoServiceUtil.getCurrentLoginUser();
	}
	public void onChange$oldpaTbx(ForwardEvent event)
	{
		String p=Encrypter.encrypt(oldpaTbx.getValue());
		if(!p.equals(user.getPassword()))
		{
			 aginpaTbx.setDisabled(true);
			 newpaTbx.setDisabled(true);
			 saveBtn.setDisabled(true);
			 throw new WrongValueException(oldpaTbx,"对不起,旧密码输入错误,请修改后继续!");
			
		}
		else
		{
			 pass.open(oldpaTbx,"end_before");
			 oldpaTbx.setStyle("border: 1px solid #02cc39;");
			 aginpaTbx.setDisabled(false);
			 newpaTbx.setDisabled(false);
			 saveBtn.setDisabled(false);
			 
		}
	}
	
	public void onChange$newpaTbx(Event event)
	{
		onChangeTwoTbx();
	}
	public void onChange$aginpaTbx(Event event)
	{
		onChangeTwoTbx();
	}
	public void onChangeTwoTbx(){
		 if(!aginpaTbx.getValue().toString().equals(newpaTbx.getValue().toString())){
            
             aginpaTbx.setValue("");
             newpaTbx.setValue("");
             saveBtn.setDisabled(true);
             }
    	 else
    	 {
    		 saveBtn.setDisabled(false);
    	 }
	}
	public void onClick$saveBtn(ForwardEvent event)
	{
		validateData();
		user.setPassword(Encrypter.encrypt(aginpaTbx.getValue()));
		try {
			UserInfoServiceUtil.update(user);
			Messagebox.show("密码修改成功!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			userModifyWindow.detach();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onClick$cancelBtn(Event event)
	{
		userModifyWindow.detach();
	}
	void validateData()
	{
	  	newpaTbx.getValue();
	  	aginpaTbx.getValue();
	}
}

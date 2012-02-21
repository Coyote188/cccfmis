package openjframework.web.zk;

import java.util.Map;

import openjframework.model.MessageInfo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;

public class MessageDetailsController extends GenericForwardComposer{
	
	private MessageInfo msg;

	Map param = Executions.getCurrent().getArg();
	private Window frmMsgDetails;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		setMsg((MessageInfo) param.get("msg"));
	}
	
	public void onClick$btnGoBack(){
		Window objWindow = (Window) Executions.createComponents("/SysForm/message-list-viewer.zul", frmMsgDetails.getParent(), null);
		objWindow.doEmbedded();
		frmMsgDetails.detach();
	}

	public void setMsg(MessageInfo msg) {
		this.msg = msg;
	}

	public MessageInfo getMsg() {
		return msg;
	}
	
	

}

package openjframework.web.zk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjframework.model.MessageInfo;
import openjframework.service.MessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class MessageListController extends GenericForwardComposer{

	private List<MessageInfo> msgList;
	
	private Grid grdMsgList;
	private Tab mainTab;
	Map param = new HashMap();
	private Window frmMsgWindow,frmMsgListViewer;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setMsgList(MessageInfoServiceUtil.findMessage(0, UserInfoServiceUtil.getCurrentLoginUser()));
	}
	
	public void onClick$btnRead(ForwardEvent e){
		MessageInfo msg = (MessageInfo) e.getOrigin().getTarget().getAttribute("msg");
		param.put("msg", msg);
		Window objWindow = (Window) Executions.createComponents("/SysForm/message-details.zul", frmMsgWindow, param);
		frmMsgListViewer.detach();
		MessageInfoServiceUtil.readMsg(msg);
	}
	
	public void onClick$btnRemove(ForwardEvent event){
		MessageInfo msg = (MessageInfo) event.getOrigin().getTarget().getAttribute("msg");
		try {
			int reply = Messagebox.show("是否确认删除:" + msg.getSubject() + "?",
					"操作提示", Messagebox.YES | Messagebox.NO,
					Messagebox.EXCLAMATION);
			if (reply == Messagebox.YES) {
				MessageInfoServiceUtil.delete(msg);
				msgList.remove(msg);
			}
			reLoad(3);
			// TODO reload list
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void onClick$tbUnread(){
		reLoad(0);
	}
	public void onClick$tbReaded(){
		reLoad(1);
	}
	public void onClick$tbAll(){
		reLoad(2);
	}

	private void reLoad(int type){
		if (type != 3) {
			setMsgList(MessageInfoServiceUtil.findMessage(type, UserInfoServiceUtil.getCurrentLoginUser()));
		}
		ListModelList model = new ListModelList(getMsgList());
		grdMsgList.setModel(model);
	}

	public void setMsgList(List<MessageInfo> msgList) {
		this.msgList = msgList;
	}


	public List<MessageInfo> getMsgList() {
		return msgList;
	}

}

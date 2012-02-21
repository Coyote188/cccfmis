package openjframework.InstantMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import openjframework.model.InstantMessageInfo;
import openjframework.model.UserInfo;
import openjframework.service.InstantMessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class ChatOneComposer extends GenericForwardComposer {
	private String talkObjectName;
	private Textbox chatOne_text;
	private Div chatOne_viewer;
	private String channelNameOwn;
	private String talkGuys;
	private String talkOtherGuys;
	private UserInfo receive_user;
	private Media media;
	private Toolbarbutton receivefTlb;
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
//		receivefTlb.setVisible(false);
		String username = (String) sessionScope.get("login_name");
		channelNameOwn = "chatOne_" + username + "_Channel";

		receive_user = (UserInfo) pageScope.get("talkObject");
		talkObjectName = receive_user.getUsername();
		// 表示别人发话给我
		talkGuys = talkObjectName + ":" + username;
		// 表示我发话给别人
		talkOtherGuys = username + ":" + talkObjectName;
		HashMap<String, ArrayList<String>> msgHsmap = (HashMap<String, ArrayList<String>>) sessionScope
				.get("HSMsg");
		if (msgHsmap.containsKey(talkObjectName)) {
			for (int i = 0; i < msgHsmap.get(talkObjectName).size(); i++) {
				processMsg(msgHsmap.get(talkObjectName).get(i));
			}
		}
		EventQueues.lookup(channelNameOwn, EventQueues.APPLICATION, false)
				.subscribe(new EventListener() {
					@Override
					public void onEvent(Event evt) throws Exception {
						if (evt.getName().equals(talkGuys)
								|| evt.getName().equals(talkOtherGuys))
							processMsg((String) evt.getData());
					}
				});
		comp.setAttribute(comp.getId(), this, true);

	}
	public void onClick$receivefTlb() throws SuspendNotAllowedException, InterruptedException
	{
		HashMap<String, ArrayList<String>> fileHsmap = (HashMap<String, ArrayList<String>>) sessionScope
		.get("FileHsmap");
		if(fileHsmap==null)
			return;
		ArrayList<String>talkObject_att=fileHsmap.get(talkObjectName);
		if(talkObject_att!=null)
		{
			if(talkObject_att.size()==0)
			{
				Messagebox.show("没有可接收的文件","系统提示",Messagebox.OK,Messagebox.INFORMATION);
				return;
			}
		}
		else
		{
			Messagebox.show("没有可接收的文件","系统提示",Messagebox.OK,Messagebox.INFORMATION);
			return;
		}
		
		pageScope.put("talkObjectName",talkObjectName);
		pageScope.put("att_data",  talkObject_att);
		Window win = (Window) Executions.createComponents(
				"/SysForm/InstantMessage/filedownload.zul", null, pageScope);
		win.setId("win_att" + talkObjectName);
		win.setPosition("center");
		Random r = new Random();
		win.setTop(String.valueOf(r.nextInt(200)) + "px");
		win.setLeft(String.valueOf(r.nextInt(200)) + "px");
		win.setMinheight(20);
		win.setSizable(true);
		win.doModal();
	}
	public void onSend(Event evt) {
		if (chatOne_text.getText() != null && chatOne_text.getText() != "") {
			String channleNameTalk = "chatOne_" + talkObjectName + "_Channel";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH：mm：ss");
			String msg = sessionScope.get("login_name") + ":"
					+ chatOne_text.getValue() + ":" + sdf.format(new Date());
			UserInfo new_receiveUser = UserInfoServiceUtil.getById(receive_user
					.getId());
			if (new_receiveUser.getStatus() == 1) {

				Event e = new Event(talkOtherGuys, null, msg);
				EventQueues.lookup(channelNameOwn, EventQueues.APPLICATION,
						false).publish(e);
				EventQueues.lookup(channleNameTalk, EventQueues.APPLICATION,
						false).publish(e);
			} else {
				Event e = new Event(talkOtherGuys, null, msg);
				EventQueues.lookup(channelNameOwn, EventQueues.APPLICATION,
						false).publish(e);
				saveData(msg, 0, false);
			}
			chatOne_text.setText("");
		} else
			return;

	}

	public void onUpload$uploadFul(UploadEvent event)
			throws InterruptedException, IOException {
		UserInfo new_receiveUser = UserInfoServiceUtil.getById(receive_user
				.getId());
		if (new_receiveUser.getStatus() != 1) {
			Messagebox.show("对方已下线,在线传输文件失败!","系统提示",Messagebox.OK,Messagebox.INFORMATION);
			return;
		}
		media = event.getMedia();
		String attachfilePath = "/attachments/instantmessage/"
				+ sessionScope.get("login_name")
				+ "/"
				+sessionScope.get("login_name")
				+ "_" 
				+ talkObjectName
				+ "/";
		if (ZkFileUtil.uploadFileWithoutMsg(media, attachfilePath,"", "upload")) {
			String channleNameTalk = "chatOne_" + talkObjectName + "_Channel";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH：mm：ss");
			String msgc = sessionScope.get("login_name") + ":" + "您发送的文件<"
					+ media.getName() + ">已上传成功,等待用户接收..." + ":"
					+ sdf.format(new Date());
			String msgo = sessionScope.get("login_name") + ":" + "新给您发送了1个文件<"
					+ media.getName() + ">需要您接收..." + ":" + sdf.format(new Date());
			Event ec = new Event(talkOtherGuys, null, msgc);
			Event eo = new Event(talkOtherGuys, null, msgo);
			EventQueues.lookup(channelNameOwn, EventQueues.APPLICATION, false)
					.publish(ec);
			EventQueues.lookup(channleNameTalk, EventQueues.APPLICATION, false)
					.publish(eo);
			EventQueues.lookup("onFileupload_" + talkObjectName + "_Filedownload",
					EventQueues.APPLICATION, true).publish(
							new Event(talkOtherGuys, null, media.getName()));

		}
	}

	private void processMsg(String msg) {
		String[] str = msg.split(":");
		Div div = new Div();
		new Image("/image/InstantMessage/m1.gif").setParent(div);
		new Label(str[0] + "@" + str[2] + " 说:").setParent(div);
		chatOne_viewer.appendChild(div);
		Label content = new Label(str[1]);
		content.setStyle("color:blue;padding-left:25px");
		chatOne_viewer.appendChild(content);
		Clients.evalJavaScript("sc()");
	}

	@SuppressWarnings("unchecked")
	public void onCloseWin(Event event) {
		((HashMap<String, ArrayList<String>>) sessionScope.get("HSMsg"))
		.remove(talkObjectName);
		((HashMap<String, ArrayList<String>>) sessionScope.get("FileHsmap"))
		.remove(talkObjectName);
		// sessionScope.remove("openWinFlag");
		List data = chatOne_viewer.getChildren();
		if(data.size()>0)
		{
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < data.size(); i++) {
				if (data.get(i) instanceof Label) {
					sb.append(((Label) data.get(i)).getValue());
					sb.append("\n");
				} else {
					if (data.get(i) instanceof Div) {
						Label title = (Label) ((Div) data.get(i)).getChildren()
								.get(1);
						sb.append(title.getValue());
						sb.append("\n");
					}
				}

			}
			saveData(sb.toString(), 1, true);
		}
		
	}

	private void saveData(String msg, int type, boolean rstatus) {
		InstantMessageInfo imi = new InstantMessageInfo(receive_user, msg,
				type, rstatus);
		InstantMessageInfoServiceUtil.create(imi);
	}

}
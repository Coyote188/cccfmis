package openjframework.InstantMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class ChatRoomComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private Div viewerDiv;
	private Textbox contentTbx;
	private AnnotateDataBinder binder;
	private Window chatRoomWin;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		EventQueues.lookup("chatRoomChannel", EventQueues.APPLICATION, true)
				.subscribe(new EventListener() {
					public void onEvent(Event evt) throws Exception {
						if (evt.getName().equalsIgnoreCase("talk")) {
							getMsg((String) evt.getData()).setParent(viewerDiv);
						}
						if (evt.getName().equalsIgnoreCase("welcome")) {
							getWelcomeMsg((String) evt.getData()).setParent(
									viewerDiv);
						}
						binder.loadAll();
					}
				});
	}

	public void onClick$sendBtn(Event event) {
		sendMsg();
	}
	
	public void sendMsg()
	{
		String content = contentTbx.getText();
		contentTbx.setText(" ");
		String userName = (String) sessionScope.get("login_name");
		String msg = userName + ":" + content;
		Event e = new Event("talk", null, msg);
		EventQueues.lookup("chatRoomChannel", EventQueues.APPLICATION, false)
				.publish(e);
	}
	private Vbox getWelcomeMsg(String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Vbox v = new Vbox();
		Label welcome = new Label("*系统消息*："+"【"+name+"】于"+sdf.format(new Date())+"登录评定中心~~ " );
		welcome.setStyle("color:blue;padding-left:8px");
		welcome.setParent(v);
		return v;
	}

	private Vbox getMsg(String msg) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] str = msg.split(":");
		Vbox v = new Vbox();
		Label userMsg = new Label(str[0] + " @" + sdf.format(new Date())
				+ "  :");
		Label contentMsg = new Label(str[1]);
		contentMsg.setStyle("color:red;padding-left:25px");
		userMsg.setParent(v);
		contentMsg.setParent(v);
		return v;
	}

}

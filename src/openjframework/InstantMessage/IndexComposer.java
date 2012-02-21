package openjframework.InstantMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.InstantMessage.UserListData;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Messagebox;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

public class IndexComposer extends GenericForwardComposer {
	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ArrayList<String>> mgsHsmap = new HashMap<String, ArrayList<String>>();

	private HashMap<String, ArrayList<String>> fileHsmap = new HashMap<String, ArrayList<String>>();
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		sessionScope.put("HSMsg", mgsHsmap);
		EventQueues.lookup(
				"chatOne_" + sessionScope.get("login_name") + "_Channel",
				EventQueues.APPLICATION, true).subscribe(new EventListener() {
			@Override
			public void onEvent(Event evt) throws Exception {
				String talkTomeGuys = evt.getName().split(":")[0];
				if (!talkTomeGuys.equalsIgnoreCase((String) sessionScope
						.get("login_name"))) {
					if (!mgsHsmap.containsKey(talkTomeGuys)) {
						ArrayList<String> msgList = new ArrayList<String>();
						msgList.add((String) evt.getData());
						mgsHsmap.put(talkTomeGuys, msgList);
					} else {
						mgsHsmap.get(talkTomeGuys).add((String) evt.getData());
					}
					sessionScope.put("HSMsg", mgsHsmap);
					EventQueues.lookup(
							"chatOne_" + sessionScope.get("login_name")
									+ "_Channel_Notice",
							EventQueues.APPLICATION, true).publish(
							new Event(talkTomeGuys));
				}
			}
		});

		sessionScope.put("FileHsmap", fileHsmap);
		EventQueues.lookup("onFileupload_" + sessionScope.get("login_name")+ "_Filedownload", 
				EventQueues.APPLICATION, true).subscribe(new EventListener() {
					@Override
					public void onEvent(Event evt) throws Exception {
						
//						receivefTlb.setVisible(true);
						String talkTomeGuys = evt.getName().split(":")[0];
						if (!fileHsmap.containsKey(talkTomeGuys)) {
							ArrayList<String> fileList = new ArrayList<String>();
							fileList.add((String) evt.getData());
							fileHsmap.put(talkTomeGuys, fileList);
						} else {
							fileHsmap.get(talkTomeGuys).add(
									(String) evt.getData());
						}
						sessionScope.put("FileHsmap", fileHsmap);
					}
				});

		if (!sessionScope.containsKey("login_name")
				|| sessionScope.get("login_name") == null) {
				return;
		} else {
			String name = (String) sessionScope.get("login_name");
			Event e = new Event("welcome", null, name);
			EventQueues
					.lookup("chatRoomChannel", EventQueues.APPLICATION, true)
					.publish(e);
		}

	}
	
}

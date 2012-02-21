package openjframework.InstantMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class FiledownloadMiniWinComposer extends GenericForwardComposer {

	public List<String> getAttlist() {
		return attlist;
	}

	private static final long serialVersionUID = 1L;
	private AnnotateDataBinder binder;
	private Window filedownloadWin;
	private List<String> attlist;
	private String talkObjectName;
	private List<String> receiveFileList=new ArrayList();
	private String filePath;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		attlist = (List<String>) pageScope.get("att_data");
		talkObjectName = (String) pageScope.get("talkObjectName");
		filePath = "/attachments/instantmessage/" 
					+ talkObjectName 
					+ "/"
					+ talkObjectName 
					+ "_" 
					+ sessionScope.get("login_name")
					+"/";
	}

	public void onClick$receiveTlb(ForwardEvent event)
			throws FileNotFoundException {
		String fileName = (String) event.getOrigin().getTarget().getAttribute(
				"att");
		Filedownload.save(filePath + fileName, null);
		// deleteFile(filePath+fileName);
		attlist.remove(fileName);
		receiveFileList.add(fileName);
		binder.loadAll();
		reSendMsg("文件<"+fileName+">已接收完毕!");
	}

	public void onClick$cancelTlb(ForwardEvent event) {
		String fileName = (String) event.getOrigin().getTarget().getAttribute(
				"att");
		deleteFile(fileName);
		attlist.remove(fileName);
		binder.loadAll();
		reSendMsg("拒绝了<"+fileName+">文件的接收!");
	}
	private void reSendMsg(String data)
	{
		String talkOtherGuys=sessionScope.get("login_name")+ ":" + talkObjectName;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH：mm：ss");
		String msg = sessionScope.get("login_name") + ":"
				+ data + ":" + sdf.format(new Date());
		Event e = new Event(talkOtherGuys, null, msg);
		EventQueues.lookup("chatOne_" + sessionScope.get("login_name") + "_Channel", EventQueues.APPLICATION,
				false).publish(e);
		EventQueues.lookup("chatOne_" + talkObjectName + "_Channel", EventQueues.APPLICATION,
				false).publish(e);
	}
	//删除服务器上对应的文件
	public void deleteFile(String fileName) {
		String dirPath = Executions.getCurrent().getDesktop().getWebApp()
		.getRealPath(filePath);
		File file = new File(dirPath+"/"+fileName);
		file.delete();
	}

	public void onClose() throws InterruptedException {
		if(receiveFileList.size()>0)
		{
			for(String att:receiveFileList)
			{
				deleteFile(att);
			}
			receiveFileList.clear();
		}
		if (attlist.size() > 0)
		{
			if (Messagebox.show("您还有未接收的文件,关闭窗口将放弃接收,是否继续?", "系统提示",
					Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK) {
				
				for(String att:attlist)
				{
					deleteFile(att);
				}
				attlist.clear();
				filedownloadWin.detach();
			}
		}
		else
			filedownloadWin.detach();
		
	}
}

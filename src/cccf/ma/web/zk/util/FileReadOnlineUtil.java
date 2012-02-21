package cccf.ma.web.zk.util;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import openjframework.util.ZkFileUtil;

public class FileReadOnlineUtil {
	/**
	 * 在线查看附件
	 * @param path 文件路径，直接传递 Attachment.path即可
	 */
	public static void onViewFileOnline(String path) {
		String downUrl = ZkFileUtil.getFileUrl(null, path)
				+ "&online=true";
		openFileOnlineWindow(downUrl);
	}
	
	private static void openFileOnlineWindow(String fileUrl) {
		Window objWindow = (Window) Executions.createComponents(
				"/SysForm/attachment-onlinewindow.zul", null, null);
		try {
			Iframe downframe = (Iframe) objWindow.getFellow("downframe");
			if (downframe != null)
				downframe.detach();
			Iframe dframe = new Iframe();
			dframe.setParent(objWindow);
			dframe.setSrc(fileUrl);
			dframe.setId("downframe");
			dframe.setWidth("800px");
			dframe.setHeight("600px");
			System.out.println("out:" + fileUrl);
			try {
				objWindow.doModal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		}

	}
}

package openjframework.web.zk;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import openjframework.model.NoticeInfo;
import openjframework.util.ZkFileUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class MsgEViewer extends GenericForwardComposer {

	private NoticeInfo notice;

	private Window frmNoticeWindow, frmNoticeViewer;
	private Grid grdFileList;

	Map param = Executions.getCurrent().getArg();

	public void onClick$goBack() {
		Window frmNoticeList = (Window) Executions.createComponents(
				"/SysForm/EnterpriseNotice/notice-show-form.zul",
				frmNoticeWindow, null);
		frmNoticeViewer.detach();
		frmNoticeList.doEmbedded();
	}

	private void onDownload(String fileName) {
		try {
			Filedownload.save(notice.getAttachment()
					+ fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		setNotice((NoticeInfo) param.get("notice"));
		init();
	}

	public void setNotice(NoticeInfo notice) {
		this.notice = notice;
	}

	public NoticeInfo getNotice() {
		return notice;
	}

	private class EnterpriseFileEngine implements RowRenderer {
		@Override
		public void render(Row row, Object data) throws Exception {
			final String fileName = (String) data;
			new Label(fileName).setParent(row);
			final Toolbarbutton btnDownload = new Toolbarbutton("下载");
			btnDownload.setStyle("text-Decoration:underline;color:#3300cc");
			btnDownload.addEventListener(Events.ON_CLICK, new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					onDownload(fileName);
				}
			});
			btnDownload.setParent(row);
		}

	}

	private void init() {
		List<String> fileNames = ZkFileUtil.getFileNameList(notice
				.getAttachment());
		if (!fileNames.isEmpty()) {
			ListModel fileListModel = new SimpleListModel(fileNames);
			if (!fileNames.isEmpty()) {
				grdFileList.setRowRenderer(new EnterpriseFileEngine());
				grdFileList.setModel(fileListModel);
			}
		}
	}

}

package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import openjframework.model.NoticeInfo;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Grid;

import cccf.ma.model.EnterpriseNoticeInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.EnterpriseNoticeInfoServiceUtil;

public class NoticeListController extends GenericForwardComposer {

	private List<EnterpriseNoticeInfo> noticeList;

	private Grid grdNoticeList;
	private Window frmNoticeList, frmNoticeWindow;
	Map param = new HashMap();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		setNoticeList(EnterpriseNoticeInfoServiceUtil
				.findUnreadNotice(EnterpriseInfoServiceUtil
						.getCurrentEnterprise()));
		grdNoticeList.setRowRenderer(new NoticeGrdRenderer());
		if (!noticeList.isEmpty()) {
			ListModelList model = new ListModelList(noticeList);
			grdNoticeList.setModel(model);
		}
	}

	public void onClick$tbUnread() {
		setNoticeList(EnterpriseNoticeInfoServiceUtil
				.findUnreadNotice(EnterpriseInfoServiceUtil
						.getCurrentEnterprise()));
		ListModelList model = new ListModelList(noticeList);
		grdNoticeList.setModel(model);
	}

	public void onClick$tbReaded() {
		setNoticeList(EnterpriseNoticeInfoServiceUtil
				.findReadedNotice(EnterpriseInfoServiceUtil
						.getCurrentEnterprise()));
		ListModelList model = new ListModelList(noticeList);
		grdNoticeList.setModel(model);
	}

	public void onClick$tbAll() {
		setNoticeList(EnterpriseNoticeInfoServiceUtil
				.findByEnterprise(EnterpriseInfoServiceUtil
						.getCurrentEnterprise()));
		ListModelList model = new ListModelList(noticeList);
		grdNoticeList.setModel(model);
	}

	@SuppressWarnings("unchecked")
	private void onRead(EnterpriseNoticeInfo notice) {
		EnterpriseNoticeInfoServiceUtil.readMsg(notice);
		param.put("notice", notice.getNotice());
		Window frmNoticeViewer = (Window) Executions.createComponents(
				"/SysForm/EnterpriseNotice/notice-viewer.zul", frmNoticeWindow,
				param);
		frmNoticeList.detach();
		frmNoticeViewer.doEmbedded();
	}

	@SuppressWarnings("unchecked")
	private void onRemove(EnterpriseNoticeInfo notice) {
		try {
			if (Messagebox.YES == Messagebox.show("是否确认删除："
					+ notice.getNotice().getSubject() + " ?", "操作提示",
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION)) {
				EnterpriseNoticeInfoServiceUtil.delete(notice);
				Iterator it = noticeList.iterator();
				while (it.hasNext()) {
					EnterpriseNoticeInfo temp = (EnterpriseNoticeInfo) it
							.next();
					if (temp.getId().equals(notice.getId())) {
						it.remove();
					}
				}
				ListModel model = new SimpleListModel(noticeList);
				grdNoticeList.setModel(model);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void setNoticeList(List<EnterpriseNoticeInfo> noticeList) {
		this.noticeList = noticeList;
	}

	public List<EnterpriseNoticeInfo> getNoticeList() {
		return noticeList;
	}

	private class NoticeGrdRenderer implements RowRenderer {

		@Override
		public void render(Row row, Object data) throws Exception {
			final EnterpriseNoticeInfo notice = (EnterpriseNoticeInfo) data;
			final Image imgMsgStatus = new Image();
			imgMsgStatus.setWidth("20px");
			imgMsgStatus.setHeight("15px");
			if (notice.getStatus() == 0) {
				imgMsgStatus.setSrc("/image/files/mail.png");
			} else {
				imgMsgStatus.setSrc("/image/files/mail-readed.png");
			}
			imgMsgStatus.setParent(row);
			{
				String subject = notice.getNotice().getSubject();
				final Label lbSubject = new Label();
				if (subject.length() >= 10) {
					lbSubject.setValue(subject.substring(0, 10) + "...");
				} else {
					lbSubject.setValue(subject);
				}
				lbSubject.setTooltiptext(subject);
				lbSubject.setParent(row);
			}
			new Label(String.valueOf(notice.getNotice().getSendDate()))
					.setParent(row);
			// new Label(notice.getNotice().getTypeName(
			// notice.getNotice().getType())).setParent(row);
			// new Label(notice.getNotice().getRatingName(
			// notice.getNotice().getRating())).setParent(row);
			final Toolbarbutton btnRead = new Toolbarbutton("阅读");
			btnRead.setStyle("text-Decoration:underline;color:#3300cc");
			btnRead.addEventListener(Events.ON_CLICK, new EventListener() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					onRead(notice);
				}

			});
			// row.appendChild(btnRead);
			final Toolbarbutton btnRemove = new Toolbarbutton("删除");
			btnRemove.setStyle("text-Decoration:underline;color:#3300cc");
			btnRemove.addEventListener(Events.ON_CLICK, new EventListener() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					// TODO Auto-generated method stub
					onRemove(notice);
				}

			});
			// btnRemove.setParent(row);
			final Hbox div = new Hbox();
			div.setParent(row);
			div.appendChild(btnRead);
			div.appendChild(btnRemove);
		}

	}

}

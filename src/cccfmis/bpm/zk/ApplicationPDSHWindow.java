package cccfmis.bpm.zk;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Bandbox;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class ApplicationPDSHWindow extends Window {

	public ApplicationInfo application;
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;
	String cdir = "", attachfilePath = "";
	Long taskInstanceId;
	String entityName = "ApplicationInfo";
	String reportUserid;
	String reportUsers[];
	DataSource source = null;

	public void onCreate() {
		application = (ApplicationInfo) this.getPage().getVariable("inspectionApplication");
		if (params.get("taskInstanceId") != null) {
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);

		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		Grid grid = (Grid) getFellow("ygpg");
		// 加载证书
		try {
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification", " where applyid='" + application.getId() + "'");
			List<Map<String, Object>> list = adapter.load();
			Iterator<Map<String, Object>> listi = list.iterator();
			while (listi.hasNext()) {
				Map<String, Object> map = listi.next();
				Row row = new Row();
				row.setValue(map.get("id"));
				System.out.println(map.get("id"));
				try {
					Label productcatalogue = new Label(map.get("productcatalogue").toString());
					Label number = new Label(map.get("number").toString());
					Label certifaication = new Label("证书");

					row.appendChild(productcatalogue);
					row.appendChild(number);
					row.appendChild(certifaication);

					if (map.get("id") != null) {
						JEntityAdapter oadapter = JAdapterFactry.createJEntityAdapterByTable(source, "_opinion", " where cid=" + Integer.parseInt(map.get("id").toString()) + "");
						List<Map<String, Object>> olist = oadapter.load();
						Iterator<Map<String, Object>> olisti = olist.iterator();
						int i = 1;
						while (olisti.hasNext() && i <= 3) {
							Map<String, Object> omap = olisti.next();
							Column column1 = (Column) getFellow("p" + i);
							UserInfo user = UserInfoServiceUtil.getById(omap.get("uid").toString());
							column1.setLabel("评定人员['" + user.getName() + "']");
							Cell opinion = new Cell();
							Radiogroup rdp = new Radiogroup();
							Radio ty = new Radio("同意");
							ty.setChecked(Boolean.parseBoolean(omap.get("opinion").toString()));
							Radio bty = new Radio("不同意");
							bty.setChecked(!Boolean.parseBoolean(omap.get("opinion").toString()));
							rdp.appendChild(ty);
							rdp.appendChild(bty);
							opinion.appendChild(rdp);
							i++;
							row.appendChild(opinion);
						}
					}
					grid.getRows().appendChild(row);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}

	public void onSubmit() {
		if (application.getStauts0() == 7) {
			try {
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (taskInstanceId > 0) {
			Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");

			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId, entityName);

			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			// 结束tasktanc
			if (approveResult.trim().equals("重新上报")) {
				if (reportUsers == null) {
					Bandbox reportUserList = (Bandbox) getFellow("reportUserList");
					throw new WrongValueException(reportUserList, "上报人员不能为空!");
				} else {
					ApplicationInfoServiceUtil.saveOrUpdate(application);
					BpmUtil.endTaskToCountSign(taskInstanceId, approveResult, reportUsers);
				}
			} else {
				BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			}
			try {
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void onUserSelect() {
		Listbox listbox = (Listbox) getFellow("reportUser");
		Set<Listitem> list = listbox.getSelectedItems();
		Bandbox bandbox = (Bandbox) getFellow("reportUserList");
		String text = "";
		if (list.size() > 0) {
			reportUsers = new String[list.size()];
			int i = 0;
			for (Listitem item : list) {
				UserInfo user = (UserInfo) item.getValue();
				text += user.getName() + ",";
				reportUserid += user.getId() + ",";
				reportUsers[i] = user.getId();
				i++;
			}
			bandbox.setText(text);
			application.setReportUser(reportUserid);
		} else {
			reportUsers = null;
			bandbox.setText(null);
		}
	}

	public void onProcessSelect(Event evt) {
		Listbox listbox = (Listbox) evt.getTarget();
		Listitem item = listbox.getSelectedItem();
		Set<Listitem> list = listbox.getSelectedItems();
		if (item != null) {
			Groupbox gp = (Groupbox) getFellow("userGroupbox");
			if (item.getLabel().trim().equals("重新上报")) {
				gp.setVisible(true);
			} else {
				reportUsers = null;
				gp.setVisible(false);
			}
		}
	}

	public void onChangeStatusList() {
		HashMap params = new HashMap();
		params.put("rowId", application.getId());
		Window objWindow = (Window) Executions.createComponents("appstatusrecord-list.zul", null, params);
		try {

			objWindow.doModal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
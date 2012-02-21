package cccfmis.bpm.zk;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Center;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.web.zk.FactoryInspectionController;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class ApplictionGCJCWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//FactoryInspectionController 返回事件数据
	public static final String ON_GETINSPECTIONDATA = "onGetInspectionData";
	public ApplicationInfo application;
	Map<?, ?> params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;
	String cdir = "", attachfilePath = "";
	Long taskInstanceId;
	String entityName = "ApplicationInfo";
	String[] counterSignUsers;
	DataSource source;
	String AppName = "";
	Textbox Tfilename;
	Textbox Tfiletype;
	Center ctFacInspectionContent;
	Window frmFactoryInspectionForm;

	public void onCreate() {
		application = (ApplicationInfo) this.getPage().getAttribute("inspectionApplication");
		rowId = application.getId();
		if (application != null) {
			if (application.getId() != null) {
				AppName = application.getEnterprise().getName();
			}
		}
		if (params.get("taskInstanceId") != null) {
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);

		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		Listbox listbox = (Listbox) getFellow("fileList");
		try {
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheck", " where Aid='" + application.getId() + "'");
			List<Map<String, Object>> factorycheck = adapter.load();
			Iterator<Map<String, Object>> iterator = factorycheck.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> map = iterator.next();
				Listitem listitem = new Listitem();
				listitem.setValue(map.get("id"));
				listitem.setId(map.get("id").toString());

				Listcell filetype = new Listcell();
				filetype.setLabel(map.get("filetype").toString());

				Listcell filename = new Listcell();
				filename.setLabel(map.get("filename").toString());

				Listcell filepath = new Listcell();
				filepath.setValue(map.get("filepath").toString());
				Toolbarbutton toolshowbtn = new Toolbarbutton("查看文件");
				toolshowbtn.setTooltiptext("点击查看文件");
				Component comp = null;
				toolshowbtn.addForward(Events.ON_CLICK, comp, "onOpenFile", map.get("filepath"));
				filepath.appendChild(toolshowbtn);

				Listcell filedel = new Listcell();
				Toolbarbutton toolbtn = new Toolbarbutton("删除");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", map.get("id"));
				params.put("path", map.get("filepath"));
				toolbtn.addForward(Events.ON_CLICK, comp, "onDel", params);
				filedel.appendChild(toolbtn);

				listitem.appendChild(filetype);
				listitem.appendChild(filename);
				listitem.appendChild(filepath);
				listitem.appendChild(filedel);

				listbox.appendChild(listitem);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

		Tfilename = (Textbox) getFellow("fileName");
		Tfiletype = (Textbox) getFellow("fileType");
		Tfilename.setText(null);
		Tfiletype.setText(null);
		
		ctFacInspectionContent = (Center) this.getFellow("ctFacInspectionContent");
		Map params = new HashMap();
		params.put("application", application);
		Window objWindow = (Window) Executions.createComponents("factory_inspection.zul", ctFacInspectionContent, params);
		objWindow.doEmbedded();
		
		this.addEventListener(ON_GETINSPECTIONDATA, new EventListener(){
			@Override
			public void onEvent(Event ent) throws Exception {
				getInspectionData((Map) ent.getData());
			}
		});
	}
	
	private void getInspectionData(Map arg) {
		application = (ApplicationInfo) arg.get("appfromInspection");
		List<Map<String,Object>> mediaMap = (List<Map<String, Object>>) arg.get("mediaMap");
		
		// application part
		if (application.getStauts0() == 7) {
			try {
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		onSave();
		
		for(Map map : mediaMap){
			System.out.println("<------>event run in application gcjc window" + map.get("filename"));
		}
		
		if (taskInstanceId > 0) {
			Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow(
					"appoveGrid");
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId,
					entityName);
			List<Row> approveRows = (List<Row>) appoveGrid.getRows()
					.getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();

			// 结束tasktanc
			BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			try {
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK,
						Messagebox.INFORMATION);
				EventQueues.lookup(userId + "refreshEndTaskListEvent",
						EventQueues.APPLICATION, true).publish(
						new Event("onMsgEventQueue", null, ""));
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

	public void onOpenFile(ForwardEvent event) throws InterruptedException {
		String fielpath = event.getData().toString();
		Window seefile = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, null);
		Iframe iframe = (Iframe) seefile.getFellow("ifrme");
		iframe.setSrc(fielpath);
		seefile.doModal();
	}

	public void onDel(ForwardEvent event) throws InterruptedException, SQLException {
		Listbox listbox = (Listbox) getFellow("fileList");

		@SuppressWarnings("unchecked")
		Map<String, Object> fileInfo = (Map<String, Object>) event.getData();
		Listitem listitem = (Listitem) getFellow(fileInfo.get("id").toString());
		String filepath = fileInfo.get("path").toString();
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheck");
		try {
			adapter.remove(adapter.loadByPrimaryKey((Integer) listitem.getValue()));
		} catch (Exception e) {
			System.out.println("数据无法删除" + e.getMessage());
		} finally {
			if (filepath != null) {
				File file = new File(this.getDesktop().getWebApp().getRealPath(filepath));
				try {
					file.delete();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				} finally {
					System.out.println("文件" + filepath + "删除成功");
				}
			}
			listbox.getItems().clear();
			onCreate();
		}
	}

	public void onMultiDel() throws SQLException, InterruptedException {
		Listbox fileList = (Listbox) getFellow("fileList");
		Set<Listitem> items = fileList.getSelectedItems();
		if (items.size() <= 0)
			return;
		else if (Messagebox.show("确定要删除选中数据么？", "删除确定", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES) {
			for (Listitem listitem : items) {
				JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheck");
				Map<String, Object> map = adapter.loadByPrimaryKey((Integer) listitem.getValue());
				String path = map.get("filepath").toString();
				try {
					adapter.remove(map);
				} catch (Exception ex) {
					System.out.println("数据无法删除" + ex.getMessage());
				} finally {
					if (path != null) {
						File file = new File(this.getDesktop().getWebApp().getRealPath(path));
						try {
							file.delete();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						} finally {
							System.out.println("文件" + path + "删除成功");
						}
					}
				}
			}
			fileList.getItems().clear();
			onCreate();
		}
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();

	}

	public void onSave() {
		application.setFactoryInspection("over");
		ApplicationInfoServiceUtil.update(application);
		rowId = application.getId();
	}

	public void onSubmit() {
		Events.sendEvent(FactoryInspectionController.ON_FACTORYINSPECTION,
				(Component) ctFacInspectionContent.getChildren().get(0), null);
	}

	public void onFileUP(Map params) {
		//UploadEvent ent = (UploadEvent) event.getOrigin();
		File factory = new File("/Factory");
		if (!factory.exists()) {
			factory.mkdir();
		}
		File perFactory = new File("/Factory/" + AppName);
		if (!perFactory.exists()) {
			perFactory.mkdir();
		}
		Media media = (Media) params.get("file");
		if (media == null)
			return;
		String fileName;
		try {
			if (ZkFileUtil.uploadFile(media, perFactory.getPath())) {
				fileName = media.getName();
				// 文件的真实url 
				String filrUrl = "/Factory/" + AppName + "/" + fileName;
				Map<String, Object> certification = new HashMap<String, Object>();
				certification.put("id", null);
				certification.put("filename", params.get("filename"));//Tfilename.getText());
				certification.put("filepath", filrUrl);
				certification.put("Aid", application.getId());
				certification.put("filetype", params.get("filetype"));//Tfiletype.getText());
				try {
					JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheck");
					adapter.commit(certification);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("数据无法保存" + e.getMessage());
					File file = new File(this.getDesktop().getWebApp().getRealPath(filrUrl));
					if (file.exists())
						file.delete();
				}
			}

		} catch (Exception e) {
			System.out.println("无法上传" + e.getMessage());
		} finally {
			Listbox listbox = (Listbox) getFellow("fileList");
			listbox.getItems().clear();
			onCreate();
		}
	}
}
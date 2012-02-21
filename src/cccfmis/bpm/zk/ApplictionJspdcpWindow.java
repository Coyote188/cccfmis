package cccfmis.bpm.zk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import openjframework.util.ZkFileUtil;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
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
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.model.ProductionModelInfo;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;
import com.itextpdf.text.DocumentException;

public class ApplictionJspdcpWindow
		extends Window
{
	private static final long	serialVersionUID	= -1115949200701980070L;
	public ApplicationInfo		application;
	Map<?, ?>					params				= Executions.getCurrent().getArg();
	String						userId;
	String						processId			= "0";
	String						rowId;
	String						cdir				= "", attachfilePath = "";
	Long						taskInstanceId;
	String						entityName			= "ApplicationInfo";
	String[]					counterSignUsers;
	public void onCreate()
			throws SQLException, WrongValueException, ParseException
	{
		application = (ApplicationInfo) this.getPage().getAttribute("inspectionApplication");
		rowId = application.getId();
		if (params.get("taskInstanceId") != null)
		{
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		/** 加载正在申请中的证书 */
		onladzs();
	}
	/**
	 * 加载/重新加载早请中的证书
	 * 
	 * @throws SQLException
	 * @throws WrongValueException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public void onladzs()
			throws SQLException, WrongValueException, ParseException
	{
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification", "applyid=?");
		System.out.println(application.getId());
		List<Map<String, Object>> list = adapter.load(application.getId());
		System.out.println(list.size());
		Listbox ctlist = (Listbox) this.getFellow("certificatList");
		// ctlist.getChildren().clear();
		for (Map<String, Object> map : list)
		{
			Listitem item = new Listitem();
			item.setValue(map.get("id"));
			/** 编号 */
			Listcell cel1 = new Listcell();
			Textbox idbox = new Textbox();
			idbox.setValue((String) map.get("number"));
			idbox.setHflex("true");
			cel1.appendChild(idbox);
			item.appendChild(cel1);
			/** 开始日期 */
			Listcell cel2 = new Listcell();
			Datebox sdate = new Datebox();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sdate.setValue(df.parse((String) map.get("certifaicationfdate")));
			sdate.setHflex("true");
			cel2.appendChild(sdate);
			item.appendChild(cel2);
			/** 结束日期 */
			Listcell cel3 = new Listcell();
			Datebox edate = new Datebox();
			edate.setValue(df.parse((String) map.get("certifaicationldate")));
			cel3.appendChild(edate);
			item.appendChild(cel3);
			/** 型号 */
			Listcell cel5 = new Listcell("型号");
			List<ProductionModelInfo> alist = new ArrayList<ProductionModelInfo>();
			alist.addAll(application.getProductionModel());
			String lables = (String) map.get("productcatalogue");
			if (lables == null)
				lables = "";
			for (ProductionModelInfo info : alist)
			{
				Checkbox box = new Checkbox(info.getModel());
				if (lables.indexOf(info.getModel()) >= 0)
					box.setChecked(true);
				cel5.appendChild(box);
				box.addForward("onClick", this, "onCertificateCh");
			}
			item.appendChild(cel5);
			/** 产品标准 */
			Listcell cel6 = new Listcell((String) map.get("technicalRequirement"));
			item.appendChild(cel6);
			/** 执行标准 **/
			Listcell cel7 = new Listcell((String) map.get("observedStandard"));
			item.appendChild(cel7);
			/** 类型 */
			item.appendChild(new Listcell("新发证"));
			/** 工具 */
			Listcell cel4 = new Listcell();
			Toolbarbutton delToolbarbutton = new Toolbarbutton("删除");
			delToolbarbutton.addForward("onClick", this, "onDelCertificate");
			cel4.appendChild(delToolbarbutton);
			item.appendChild(cel4);
			/** dd */
			ctlist.appendChild(item);
		}
	}
	@SuppressWarnings("unchecked")
	public void onAddCertificate()
	{
		Listbox ctlist = (Listbox) this.getFellow("certificatList");
		Listitem item = new Listitem();
		/** 编号 */
		Listcell cel1 = new Listcell();
		Textbox idbox = new Textbox();
		idbox.setHflex("true");
		cel1.appendChild(idbox);
		item.appendChild(cel1);
		/** 开始日期 */
		Listcell cel2 = new Listcell();
		Datebox sdate = new Datebox();
		sdate.setHflex("true");
		cel2.appendChild(sdate);
		item.appendChild(cel2);
		/** 结束日期 */
		Listcell cel3 = new Listcell();
		Datebox edate = new Datebox();
		cel3.appendChild(edate);
		item.appendChild(cel3);
		/** 型号 */
		Listcell cel5 = new Listcell("型号");
		List<ProductionModelInfo> alist = new ArrayList<ProductionModelInfo>();
		alist.addAll(application.getProductionModel());
		for (ProductionModelInfo info : alist)
		{
			Checkbox box = new Checkbox(info.getModel());
			cel5.appendChild(box);
			box.addForward("onClick", this, "onCertificateCh");
		}
		item.appendChild(cel5);
		/** 产品标准 */
		Listcell cel6 = new Listcell(application.getProduction().getTechnicalRequirement());
		item.appendChild(cel6);
		/** 执行标准 **/
		Listcell cel7 = new Listcell(application.getProduction().getObservedStandard());
		item.appendChild(cel7);
		/** 类型 */
		item.appendChild(new Listcell("新发证"));
		/** 工具 */
		Listcell cel4 = new Listcell();
		Toolbarbutton delToolbarbutton = new Toolbarbutton("删除");
		delToolbarbutton.addForward("onClick", this, "onDelCertificate");
		cel4.appendChild(delToolbarbutton);
		item.appendChild(cel4);
		//
		ctlist.appendChild(item);
	}
	public void onCertificateCh()
	{}
	public void onDelCertificate()
			throws SQLException
	{
		Listbox ctlist = (Listbox) this.getFellow("certificatList");
		Listitem listitem = ctlist.getSelectedItem();
		try
		{
			String id = ((Textbox) ((Listcell) listitem.getFirstChild()).getFirstChild()).getValue();
			if (Messagebox.YES == Messagebox.show("你真的要删除证书[" + id + "]吗", "问题", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION))
			{
				Listitem item = ctlist.getSelectedItem();
				if (item.getValue() != null)
				{
					DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
					JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification");
					adapter.removeByPrimaryKey((Serializable) item.getValue());
				}
				ctlist.removeItemAt(item.getIndex());
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		//
	}
	/**
	 * Cancel this screen
	 */
	public void onCancel()
	{
		this.detach();
	}
	public void onSeeReport()
			throws SuspendNotAllowedException, InterruptedException, IOException, DocumentException
	{
		String path = getDesktop().getWebApp().getRealPath("/cccfpage/pdffileModel/");
		path = PdfZs.makTempZspdf(application, (Listbox) this.getFellow("certificatList"), path);
		System.out.println(path);
		if (path != null)
		{
			Window wn = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, this.params);
			wn.setTitle("[" + application.getProduction().getProductName() + "]证书");
			Iframe ss = (Iframe) wn.getFellow("ifrme");
			ss.setSrc("/cccfpage/pdffileModel/" + path);
			wn.doModal();
			// <iframe id="ifrme" width="100%" height="100%" src=""></iframe>
		}
	}
	public void onSave()
			throws InterruptedException, SQLException
	{
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification");
		// ApplicationInfoServiceUtil.update(application);
		// 请审号
		// rowId = application.getId();
		// 产品
		ProductCatalogueInfo prod = application.getProduction();
		// 产品ID
		prod.getId();
		// 产品名称
		prod.getProductName();
		// 执行标准
		prod.getObservedStandard();
		// 申请企业
		EnterpriseInfo entinfo = application.getEnterprise();
		entinfo.getId();
		entinfo.getName();
		// 生产厂
		ProductionEnterpriseInfo prodentInfo = application.getProductionEnterprise();
		prodentInfo.getId();
		prodentInfo.getName();
		prodentInfo.getContactAddress();
		prodentInfo.getFacAddressEn();
		// 制造商
		ManufactureInfo manInfo = application.getManufacture();
		manInfo.getId();
		manInfo.getName();
		//
		Listbox ctlist = (Listbox) this.getFellow("certificatList");
		@SuppressWarnings("unchecked")
		List<Listitem> items = ctlist.getItems();
		for (Listitem item : items)
		{
			@SuppressWarnings("unchecked")
			List<Listcell> cells = item.getChildren();
			Textbox textID = (Textbox) cells.get(0).getFirstChild();
			// 证书编号
			String number = textID.getValue();
			// 证书有效期
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Datebox statbox = (Datebox) cells.get(1).getFirstChild();
			Datebox endbox = (Datebox) cells.get(2).getFirstChild();
			Date strdate = statbox.getValue();
			Date enddate = endbox.getValue();
			// 证书单元内容
			@SuppressWarnings("unchecked")
			List<Checkbox> cheboxs = (List<Checkbox>) cells.get(3).getChildren();
			String labes = "";// 型号
			for (Checkbox box : cheboxs)
			{
				if (box.isChecked())
					labes += "," + box.getLabel();
			}
			labes = labes.replaceFirst(",", "");
			//
			Integer v = (Integer) item.getValue();
			Map<String, Object> certification;
			if (v == null)
			{
				certification = adapter.newEntity();
				certification.put("id", null);
			} else
			{
				certification = adapter.loadByPrimaryKey(v);
			}
			//
			certification.put("number", number);
			certification.put("applyid", application.getId());
			certification.put("appname", application.getEnterprise().getName());
			certification.put("appid", application.getEnterprise().getId());
			certification.put("appadress", application.getEnterprise().getContactAddress());
			certification.put("productionenterpriseadress", application.getProductionEnterprise().getContactAddress());
			certification.put("productionenterprise", application.getProductionEnterprise().getName());
			certification.put("productionenterpriseid", application.getProductionEnterprise().getId());
			certification.put("manufactureadress", application.getManufacture().getContactAddress());
			certification.put("manufacture", application.getManufacture().getName());
			certification.put("manufactureid", application.getManufacture().getId());
			certification.put("productcatalogue", labes);
			certification.put("productname", application.getProduction().getProductName());
			certification.put("observedStandard", application.getProduction().getObservedStandard());
			certification.put("technicalRequirement", application.getProduction().getTechnicalRequirement());
			certification.put("certifaicationfdate", df.format(strdate));
			certification.put("certifaicationldate", df.format(enddate));
			certification = adapter.commit(certification);
			item.setValue(certification.get("id"));
		}
	}
	public void onSubmit()
			throws InterruptedException, SQLException
	{
		if (application.getStauts0() == 7)
		{
			try
			{
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		onSave();
		if (taskInstanceId > 0)
		{
			Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId, entityName);
			@SuppressWarnings("unchecked")
			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			// 结束tasktanc
			BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			try
			{
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		} else
		{
			try
			{
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	public void onOpenPumperWindow()
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("application", application);
		params.put("attachfilePath", attachfilePath);
		Window objWindow = (Window) Executions.createComponents("pumperdocuments-view.zul", null, params);
		try
		{
			objWindow.doModal();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	// 实现下载
	public void onFiledown(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		String fieldName = event.getData().toString();
		// 文件的真实url
		String fileUrl = "";
		if (fieldName.equals("businessLisence"))
			fileUrl = application.getBusinessLisence().toString();
		if (fieldName.equals("organizationCode"))
			fileUrl = application.getOrganizationCode();
		if (fieldName.equals("inspectionDeviceList"))
			fileUrl = application.getInspectionDeviceList();
		if (fieldName.equals("layoutGraph"))
			fileUrl = application.getLayoutGraph();
		if (fieldName.equals("brand"))
			fileUrl = application.getBrand();
		if (fieldName.equals("qualityManual"))
			fileUrl = application.getQualityManual();
		if (fieldName.equals("originalCertificate"))
			fileUrl = application.getOriginalCertificate();
		if (fieldName.equals("contractFileUrl"))
			fileUrl = application.getContractFileUrl();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		openFileOnlineWindow(downUrl);
	}
	// 实现下载
	public void onFileListDownload(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		String fileUrl = event.getData().toString();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";;
		openFileOnlineWindow(downUrl);
	}
	public void openFileOnlineWindow(String fileUrl)
	{
		Window objWindow = (Window) Executions.createComponents("attachment-onlinewindow.zul", null, null);
		try
		{
			Iframe downframe = (Iframe) objWindow.getFellow("downframe");
			if (downframe != null)
				downframe.detach();
			Iframe dframe = new Iframe();
			dframe.setParent(objWindow);
			dframe.setSrc(fileUrl);
			dframe.setId("downframe");
			dframe.setWidth("780px");
			dframe.setHeight("600px");
			objWindow.doModal();
		} catch (SuspendNotAllowedException e)
		{
			/** SuspendNotAllowedException */
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			/** InterruptedException */
			e.printStackTrace();
		}
	}
	public void onFileUP(ForwardEvent event)
			throws IOException
	{
		String fieldName = event.getData().toString();
		Media media = null;
		String fileName;
		try
		{
			media = Fileupload.get();
			if (ZkFileUtil.uploadFile(media, attachfilePath))
			{
				fileName = media.getName();
				// 文件的真实url
				String filrUrl = attachfilePath + fileName;
				// 显示文件名的textbox
				Toolbarbutton lbFileName = (Toolbarbutton) this.getFellow("label_" + fieldName);
				lbFileName.setLabel(fileName);
				lbFileName.setContext(filrUrl);
				// lbFileName.setHref("../filedown.jsp?filename="+fileName+"&fileurl="+filrUrl);
				Textbox tbFile = (Textbox) this.getFellow(fieldName);
				tbFile.setValue(filrUrl);
				lbFileName.setContext(filrUrl);
				if (fieldName.equals("contractFileUrl"))
					application.setContractFileUrl(filrUrl);
				Button upButton = (Button) getFellow("up_" + fieldName);
				upButton.setDisabled(true);
				Toolbarbutton tb = (Toolbarbutton) getFellow("del_" + fieldName);
				tb.setVisible(true);
			}
		} catch (InterruptedException e)
		{
			/** InterruptedException */
			e.printStackTrace();
		}
	}
}

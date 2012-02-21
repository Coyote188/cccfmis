package cccfmis.bpm.zk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.rim.itext.PdfFileUtil;
import net.modellite.spring.BeanAdapter;
import openjframework.util.ZkFileUtil;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;
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
import cccf.ma.service.ApplicationInfoServiceUtil;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class ApplictionPdspWindow
		extends Window
{
	private static final long	serialVersionUID	= 7965438026073584787L;
	public ApplicationInfo		application;
	@SuppressWarnings("unchecked")
	Map<String, Object>			params				= Executions.getCurrent().getArg();
	String						userId;
	String						processId			= "0";
	String						rowId;
	String						cdir				= "", attachfilePath = "";
	Long						taskInstanceId;
	String						entityName			= "ApplicationInfo";
	String[]					counterSignUsers;
	public void onCreate()
			throws IOException, SQLException
	{
		application = (ApplicationInfo) this.getPage().getAttribute("inspectionApplication");// .getVariable("inspectionApplication");
		rowId = application.getId();
		if (params.get("taskInstanceId") != null)
		{
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		/*
		 * 加载评定报告
		 */
		loadPdpg();
		/**
		 * 证书
		 */
		loadZs();
	}
	/**
	 * 加载证书
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void loadZs()
			throws IOException, SQLException
	{
		String id = application.getId();
		String path = getDesktop().getWebApp().getRealPath("/cccfpage/pdffileModel/");
		new File(path + "/report/" + rowId).mkdirs();
		PdfZs.makZs(path + "/zsmb.pdf", id, path + "/report/" + rowId + "/zs");
		// zsiframe
		/*
		 * 查看文件
		 */
		Iframe ifm = (Iframe) this.getFellow("zsiframe");
		ifm.setSrc("/cccfpage/pdffileModel/report/" + rowId + "/zs.pdf");
	}
	/**
	 * 加载评定报告
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void loadPdpg()
			throws IOException, SQLException
	{
		/*
		 * 生成评定报靠
		 */
		String path = getDesktop().getWebApp().getRealPath("/cccfpage/pdffileModel/");
		System.out.println(path);
		Map<String, String> fieldsArgs = new HashMap<String, String>();
		/**
		 * TODO 生成评定报告,有多少证书就有多少个,最后合并成一个文件
		 */
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification", "applyid=?");
		JEntityAdapter adapter2 = JAdapterFactry.createJEntityAdapterByTable(source, "_opinion", "aid=? and cid=?");
		List<Map<String, Object>> list = adapter.load(application.getId());
		String trmp = path + "/report/" + rowId;
		(new File(trmp)).mkdirs();
		// filein = new FileInputStream(path + "/pdbgmb.pdf");
		int i = 0;
		List<String> files = new Vector<String>();
		for (i = 0; i < list.size(); i++)
		{
			files.add(path + "/report/" + rowId + "/pdbg" + i + ".pdf");
			// /FileOutputStream fileOut = new FileOutputStream(path + "/report/" + application.getId() + "/pdbg" + i + ".pdf");
			Map<String, Object> map = list.get(i);
			/* 编号 */
			fieldsArgs.put("编号", (String) map.get("number"));
			// 有效期 签发 技评意见 复评意见 初评意见 申请日期
			fieldsArgs.put("有效期", "从" + map.get("certifaicationfdate") + "至" + map.get("certifaicationldate"));
			// 检查日期 检查组长 检查组员 检查代码
			fieldsArgs.put("检查日期", "-");
			fieldsArgs.put("检查组长", "-");
			fieldsArgs.put("检查组员", "-");
			fieldsArgs.put("检查代码", "-");
			//application.get
			//技评意见1
			 List<Map<String, Object>> list2 =adapter2.load(rowId,(Integer)map.get("id"));
			 int k=1;
			 for(Map<String, Object> map1:list2){
				 fieldsArgs.put("技评意见"+k, (String)map1.get("disc"));
				 fieldsArgs.put("技评签字"+k, "["+map1.get("uname")+"]");
				 k++;
				 if(k>3)
					 break;
			 }
			//
			/* 申请人 */
			// EnterpriseInfo entinfo = application.getEnterprise();
			fieldsArgs.put("审请人名称", (String) map.get("appname"));
			fieldsArgs.put("申请人地址", (String) map.get("appadress"));
			/* */
			// 生产厂
			// ProductionEnterpriseInfo prodentInfo = application.getProductionEnterprise();
			fieldsArgs.put("生产企业名称", (String) map.get("productionenterprise"));
			fieldsArgs.put("生产企业地址", (String) map.get("productionenterpriseadress"));
			// 制造商
			// ManufactureInfo manInfo = application.getManufacture();
			fieldsArgs.put("制造商名称", (String) map.get("manufacture"));
			fieldsArgs.put("制造商地址", (String) map.get("manufactureadress"));
			/* 产品执行标准 */
			// 产品
			// ProductCatalogueInfo prod = application.getProduction();
			fieldsArgs.put("产品执行标准", (String) map.get("technicalRequirement"));
			/** 认证规则 **/
			fieldsArgs.put("认证规则", (String) map.get("observedStandard"));
			/** 产品执行标准 **/
			fieldsArgs.put("体系执行标准", (String) map.get("technicalRequirement"));
			/** 名称规格型号 **/
			fieldsArgs.put("名称规格型号", map.get("productname") + " " + map.get("productcatalogue"));
			/** 检验报告结论 **/
			// TODO
			fieldsArgs.put("检验报告结论", "合格(检验报告编号:[--])");
			/** 审核结论 **/
			fieldsArgs.put("审核结论", "推荐质量认证通过");
			/** 备注 **/
			fieldsArgs.put("备注", "[无]");
			/** 生成文件 **/
			PdfFileUtil.simpleMakPdfFile(path + "/pdbgmb.pdf", path + "/report/" + application.getId() + "/pdbg" + i + ".pdf", fieldsArgs);
		}
		String filePath = path + "/report/" + rowId + "/pdbg.pdf";
		PdfFileUtil.simpleCopyFiles(filePath, files.toArray(new String[i]));
		/*
		 * 查看文件
		 */
		Iframe ifm = (Iframe) this.getFellow("pdbgiframe");
		ifm.setSrc("/cccfpage/pdffileModel/report/" + rowId + "/pdbg.pdf");
	}
	/**
	 * Cancel this screen
	 */
	public void onCancel()
	{
		this.detach();
	}
	public void onSave()
	{
		ApplicationInfoServiceUtil.update(application);
		rowId = application.getId();
	}
	public void onSubmit()
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			try
			{
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			try
			{
				objWindow.doModal();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SuspendNotAllowedException e)
		{
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

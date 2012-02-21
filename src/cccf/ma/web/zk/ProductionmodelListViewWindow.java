package cccf.ma.web.zk;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.*;
import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zkplus.databind.BindingListModelSet;
import org.zkoss.zul.*;

import com.aidi.core.zk.*;
import cccf.ma.model.*;
import cccf.ma.service.*;

import openjframework.util.*;

public class ProductionmodelListViewWindow extends Window {

	Long taskInstanceId;
	String processId = "0";
	String userId;
	HashMap params = new HashMap();
	Map appParams = Executions.getCurrent().getArg();

	private ApplicationInfo application;

	public void onCreate() {

		application = (ApplicationInfo) appParams.get("application");

		// 是消防车则增加列
		if (application.getProduction().isFireEngineStatus() && false) {
			Listbox productionModelInfoListbox = (Listbox) this
					.getFellow("productionModelInfoListbox");
			Listheader h1 = (Listheader) productionModelInfoListbox
					.getFellow("model");
			h1.setWidth("90px");

			Listheader h2 = (Listheader) productionModelInfoListbox
					.getFellow("name");
			h2.setWidth("90px");

			Listheader h3 = (Listheader) productionModelInfoListbox
					.getFellow("pccc");
			h3.setVisible(true);

		}

		createRows();

	}

	private void createRows() {
		if (application != null) {
			if (application.getProductionModel() != null) {
				List<ProductionModelInfo> alist = new ArrayList();
				alist.addAll(application.getProductionModel());
				Listbox listbox = (Listbox) this
						.getFellow("productionModelInfoListbox");
				for (ProductionModelInfo m : alist) {
					Listitem item = new Listitem();
					item.setParent(listbox);
					item.setValue(m.getId());
					Listcell cell1 = new Listcell();
					cell1.setParent(item);
					if (m.getModel() != null) {
						cell1.setLabel(m.getModel());
					}

					Listcell cell2 = new Listcell();
					cell2.setParent(item);
					Vbox vbox2 = new Vbox();
					vbox2.setParent(cell2);
					if (m.getName() != null) {
						String name = m.getName();
						if(m.getIsMain()){
							name = m.getName() + "(主型)";
						}
						Label l = new Label(name);
						l.setParent(vbox2);
					}
					Label label2 = new Label("意见");
					label2.setParent(vbox2);

					Listcell cell3 = new Listcell();
					cell3.setParent(item);
					Vbox vbox3 = new Vbox();
					vbox3.setParent(cell3);
					vbox3.setWidth("100%");

//					Toolbarbutton tb3 = new Toolbarbutton();
//					tb3.setLabel("");
//					tb3.setParent(vbox3);
//					if (m.getFullReport() != null && m.getFullReport() != "") {
//						tb3.setLabel("附件");
//						tb3.setStyle("color:#466BAE;");
//						tb3.addForward("onClick", this, "onFileDownload", m
//								.getFullReport());
//
//					}
//					Textbox textbox3 = new Textbox();
//					textbox3.setWidth("150px");
//					textbox3.setParent(vbox3);
//					textbox3.setMultiline(true);
//					textbox3.setRows(2);
//					textbox3.setId(m.getId()+"_memo_fullReport");

					Listcell cell4 = new Listcell();
					cell4.setParent(item);
					Vbox vbox4 = new Vbox();
					vbox4.setParent(cell4);					
					Toolbarbutton tb4 = new Toolbarbutton();
					tb4.setLabel("");
					tb4.setParent(vbox4);
					if (m.getReport() != null && m.getReport() != "") {
						tb4.setLabel("附件");
						tb4.setStyle("color:#466BAE;");
						tb4.addForward("onClick", this, "onFileDownload", m
								.getReport());

					}
					Textbox textbox4 = new Textbox();
					textbox4.setWidth("150px");
					textbox4.setParent(vbox4);
					textbox4.setMultiline(true);
					textbox4.setRows(2);
					textbox4.setId(m.getId()+"_memo_report");

					Listcell cell5 = new Listcell();
					cell5.setParent(item);
					Vbox vbox5 = new Vbox();
					vbox5.setParent(cell5);
					Toolbarbutton tb5 = new Toolbarbutton();
					tb5.setLabel("");
					tb5.setParent(vbox5);
					if (m.getFlowChart() != null && m.getFlowChart() != "") {
						tb5.setLabel("附件");
						tb5.setStyle("color:#466BAE;");
						tb5.addForward("onClick", this, "onFileDownload", m
								.getFlowChart());

					}
					Textbox textbox5 = new Textbox();
					textbox5.setWidth("150px");
					textbox5.setParent(vbox5);
					textbox5.setMultiline(true);
					textbox5.setRows(2);
					textbox5.setId(m.getId()+"_memo_flowChart");

					Listcell cell6 = new Listcell();
					cell6.setParent(item);
					Vbox vbox6 = new Vbox();
					vbox6.setParent(cell6);
					Toolbarbutton tb6 = new Toolbarbutton();
					tb6.setLabel("");
					tb6.setParent(vbox6);
					if (m.getCharacterForm() != null
							&& m.getCharacterForm() != "") {
						tb6.setLabel("附件");
						tb6.setStyle("color:#466BAE;");
						tb6.addForward("onClick", this, "onFileDownload", m
								.getCharacterForm());

					}
					Textbox textbox6 = new Textbox();
					textbox6.setWidth("150px");
					textbox6.setParent(vbox6);
					textbox6.setMultiline(true);
					textbox6.setRows(2);
					textbox6.setId(m.getId()+"_memo_characterForm");

					// here the product images(multiple files)
					org.zkoss.zul.api.Listcell cell8 = new  Listcell();
					cell8.setParent(item);
					Vbox vb8 = new Vbox();
					vb8.setParent(cell8);
					Hbox vbox8 = new Hbox();
					vbox8.setParent(vb8);
					
					if(null != m.getProductImg() && "" != m.getProductImg())
					{
						// here i want to deal with a string substring
						String mutipleFileNames = m.getProductImg();
						String fileNames[] = mutipleFileNames.split("\\|");
						int i = 0;
						for(String fileName : fileNames)
						{
							if(!fileName.isEmpty()){
								++i;
								Toolbarbutton tb = new Toolbarbutton();
								tb.setLabel("附件"+i);
								tb.setParent(vbox8);
								
								tb.setStyle("color:#466BAE;");
								tb.addForward("onClick", this, "onFileDownload", fileName);
							}
						}
					}
					Textbox textbox8 = new Textbox();
					textbox8.setWidth("150px");
					textbox8.setParent(vb8);
					textbox8.setMultiline(true);
					textbox8.setRows(2);
					textbox8.setId(m.getId()+"_memo_productImg");
					/** */
					if (application.getProduction().isFireEngineStatus()) {
						Listcell cell7 = new Listcell();
						cell7.setParent(item);
						Vbox vbox7 = new Vbox();
						vbox7.setParent(cell7);
						Toolbarbutton tb7 = new Toolbarbutton();
						tb7.setLabel("");
						tb7.setParent(vbox7);
						if (m.getPumperCcc() != null && m.getPumperCcc() != "") {
							tb7.setLabel("附件");
							tb7.setStyle("color:#466BAE;");
							tb7.addForward("onClick", this, "onFileDownload", m
									.getPumperCcc());

						}
						Textbox textbox7 = new Textbox();
						textbox7.setWidth("150px");
						textbox7.setParent(vbox7);
						textbox7.setMultiline(true);
						textbox7.setRows(2);
						textbox7.setId(m.getId()+"_memo_pumperCcc");
					}
				}
			}

		}
	}

	public void onCancel() {
		this.detach();
	}

	public void onFileDownload(ForwardEvent event)
			throws FileNotFoundException, UnsupportedEncodingException {
		String fileUrl = event.getData().toString();
		String downUrl = ZkFileUtil.getDownUrl(fileUrl) + "&online=true";
		;
		openFileOnlineWindow(downUrl);

	}

	public void openFileOnlineWindow(String fileUrl) {
		Window objWindow = (Window) Executions.createComponents(
				"attachment-onlinewindow.zul", null, null);
		try {
			Iframe downframe = (Iframe) objWindow.getFellow("downframe");
			if (downframe != null)
				downframe.detach();
			Iframe dframe = new Iframe();
			dframe.setParent(objWindow);
			dframe.setSrc(fileUrl);
			dframe.setId("downframe");
			dframe.setWidth("780px");
			dframe.setHeight("600px");

			try {
				objWindow.doModal();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

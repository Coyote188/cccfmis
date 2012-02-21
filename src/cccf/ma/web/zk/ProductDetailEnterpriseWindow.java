package cccf.ma.web.zk;

import java.util.List;
import java.util.Map;

import openjframework.util.ZkFileUtil;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.EnterpriseInfo;

public class ProductDetailEnterpriseWindow extends Window{
 public EnterpriseInfo enterprise; 
     
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
	 Window window;
	 
	 private List<Object> objList;
	 
	 private Textbox tbxRefusedMsg;

	 private Grid grdFileList,grdMPList;
     
     public void onCreate()
     {
    	 Components.wireVariables(this, this);
    	 enterprise=(EnterpriseInfo)this.getPage().getVariable("enterprise"); 
    	 Components.wireVariables(this, this);
    	 
    	 List<String> fileNames = ZkFileUtil.getFileNameList(enterprise.getCopyOfBusinessLicense());
    	 if (!fileNames.isEmpty()) {
    		 ListModel fileListModel = new SimpleListModel(fileNames);
        	 grdFileList.setRowRenderer(new EnterpriseFileEngine());
        	 grdFileList.setModel(fileListModel);	
		}
     }
     
     private void onViewFileOnline(String path) {
 		String realPath = enterprise.getCopyOfBusinessLicense() + "/"+ path;
 		String downUrl = ZkFileUtil.getFileUrl(enterprise,realPath) + "&online=true";
 		openFileOnlineWindow(downUrl);
 	}
     
     private class EnterpriseFileEngine implements RowRenderer{
 		@Override
 		public void render(Row row, Object data) throws Exception {
 			final String filsName = (String) data;
 			
 			new Label(filsName.substring(0, filsName.indexOf("_"))+" :").setParent(row);
 			
 			final Toolbarbutton btnFile = new Toolbarbutton("附件");
 			btnFile.setStyle("text-Decoration:underline;color:#3300cc");
 			btnFile.addEventListener(Events.ON_CLICK, new EventListener(){
 				@Override
 				public void onEvent(Event arg0) throws Exception {
 					onViewFileOnline(filsName);
 				}
 			});
 			btnFile.setParent(row);
 		}
     	 
      }
 	
 	public void openFileOnlineWindow(String fileUrl){
 		Window objWindow = (Window) Executions.createComponents(
 				"attachment-onlinewindow.zul", null,null);
 		try {
 			Iframe  downframe= (Iframe) objWindow.getFellow("downframe");
 			if(downframe!=null) downframe.detach();
 			Iframe dframe=new Iframe();
 			dframe.setParent(objWindow);
 			dframe.setSrc(fileUrl);
 			dframe.setId("downframe");
 			dframe.setWidth("800px");
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

package cccf.ma.web.zk;
import java.util.*;

import openjframework.service.*;
import openjframework.util.ZkFileUtil;
import openjframework.model.*;
import openjframework.myenum.MessageEnum;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import cccf.ma.service.*;
import cccf.ma.model.*;

public class EnterpriseviewerWindow extends Window{
	
     public EnterpriseInfo enterprise; 
     
     Map params = Executions.getCurrent().getArg();
     String userId;
	 String processId = "0";
	 String rowId;
	 Window window,enterpriseactiveWindow;
	 
	 private List<Object> objList;
	 
	 private Textbox tbxRefusedMsg;

	 private Grid grdFileList,grdMPList;
     
     public void onCreate()
     {
    	 Components.wireVariables(this, this);
    	 enterprise=(EnterpriseInfo)this.getPage().getVariable("enterprise"); 
    	 Components.wireVariables(this, this);
    	 init();
    	 List<Attachment> files = enterprise.getAttachments();
    	 if (!files.isEmpty()) {
    		 ListModel fileListModel = new SimpleListModel(files);
        	 grdFileList.setRowRenderer(new EnterpriseFileEngine());
        	 grdFileList.setModel(fileListModel);	
		}
     }
     
     private void init(){
    	List<ManufactureInfo> mList = ManufactureInfoServiceUtil.getManufactures(enterprise);
     	List<ProductionEnterpriseInfo> pList = ProductionEnterpriseInfoServiceUtil.getProductionEnterprise(enterprise);
     	objList = new ArrayList<Object>();
     	for(Object obj: mList){
     		objList.add(obj);
     	}
     	for (Object obj: pList) {
 			objList.add(obj);
 		}
     	ListModelList model = new ListModelList(objList);
     	grdMPList.setRowRenderer(new GrdRanderer());
     	grdMPList.setModel(model);
     }
     
     public void onRefused() throws InterruptedException{
    	 tbxRefusedMsg.getValue();
    	 int reply = Messagebox.show("是否确认拒绝"+enterprise.getName()+"的激活申请？", "提示", Messagebox.YES
 				| Messagebox.NO, Messagebox.QUESTION);
 		if (reply == Messagebox.YES) {
 			EnterpriseInfoServiceUtil.refusedEnterprise(enterprise);
 			Events.postEvent(new Event(EnterpriseactiveWindow.ON_REFUSED, this
 					.getParent(), enterprise));
 			sendMessage();
 			//无法得到父，刷新不正常！
 			System.out.println(this.getParent());
 			this.detach();
 		} else {
 			return;
 		}
     }
     
     private void sendMessage(){
    	 MessageInfo msg = new MessageInfo();
    	 msg.setSubject("拒绝企业激活申请");
    	 msg.setContent(tbxRefusedMsg.getText().toString());
    	 msg.setReceiveUser(enterprise.getAccount());
    	 msg.setType(MessageEnum.任务消息.ordinal());
    	 MessageInfoServiceUtil.sendMessageToDesktop(msg);
     }
     
     public void onActive() throws InterruptedException {
		int reply = Messagebox.show("是否确认激活该企业", "提示", Messagebox.YES
				| Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES) {
			EnterpriseInfoServiceUtil.activeEnterprise(enterprise);
			if (!objList.isEmpty()) {
				for(Object obj:objList){
					if (obj instanceof ManufactureInfo) {
						ManufactureInfoServiceUtil.active((ManufactureInfo) obj);
					}else {
						ProductionEnterpriseInfoServiceUtil.active((ProductionEnterpriseInfo) obj);
					}
				}
			}
			Events.postEvent(EnterpriseactiveWindow.ON_SENDMSG, enterpriseactiveWindow, enterprise);
			Events.postEvent(new Event(EnterpriseactiveWindow.ON_ACTIVE, this
					.getParent(), enterprise));
			
			this.detach();
		} else {
			return;
		}
	}

     private void onViewFileOnline(String path) {
		String realPath = enterprise.getCopyOfBusinessLicense() + "/"+ path;
		String downUrl = ZkFileUtil.getFileUrl(enterprise,realPath) + "&online=true";
		openFileOnlineWindow(path);
	}
     
	private class EnterpriseFileEngine implements RowRenderer{
		@Override
		public void render(Row row, Object data) throws Exception {
			final Attachment att = (Attachment) data;
			
			new Label(att.getName()).setParent(row);
			
			final Toolbarbutton btnFile = new Toolbarbutton("打开附件");
			btnFile.setStyle("text-Decoration:underline;color:#3300cc");
			btnFile.addEventListener(Events.ON_CLICK, new EventListener(){
				@Override
				public void onEvent(Event arg0) throws Exception {
					onViewFileOnline(att.getPath());
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
	
	private class GrdRanderer implements RowRenderer{

		@Override
		public void render(Row row, Object data) throws Exception {
			final Object obj = data;
			final Label lblType = new Label();
			lblType.setParent(row);
			final Label lblObjName = new Label();
			lblObjName.setParent(row);
			final Label lblObjDetail = new Label();
			lblObjDetail.setParent(row);
			if (obj instanceof ManufactureInfo) {
				lblType.setValue(ObjectType.制造商.toString());
				lblObjName.setValue(((ManufactureInfo) obj).getName());
				lblObjDetail.setValue(((ManufactureInfo) obj).getContactAddress());
			}else {
				lblType.setValue(ObjectType.生产企业.toString());
				lblObjName.setValue(((ProductionEnterpriseInfo) obj).getName());
				lblObjDetail.setValue(((ProductionEnterpriseInfo) obj).getContactAddress());
			}
		}
		
	}
	
	private enum ObjectType{
		生产企业,制造商
	}
}
		
		
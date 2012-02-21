package cccf.ma.web.zk;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cccf.ma.model.*;
import cccf.ma.service.*;

public class AttachFileMemoWindow extends Window {

	Map params = Executions.getCurrent().getArg();
	
	public void onCreate() {
		
		//取得意见
		Map moMap=null;
		if(params.get("taskInstanceId")!=null){
			moMap=AttachFileMemoInfoServiceUtil.getMemoByTaskId(Long.valueOf(params.get("taskInstanceId").toString()));
		}
		
		if(moMap!=null){
			if(params.get("fieldNameArr")!=null){
				String[] fieldNameArr=(String[])params.get("fieldNameArr");
				for(String fname:fieldNameArr){
					if(moMap.get(fname)!=null){
						Textbox tb=(Textbox)getFellow("memo_"+fname);
						tb.setValue(moMap.get(fname).toString());						
					}
				}
			}
			
		}
		

	}

	public void onCancel() {
		this.detach();
	}

}

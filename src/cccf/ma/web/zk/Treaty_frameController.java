package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cccf.ma.model.CommonTreatyInfo;

public class Treaty_frameController extends GenericForwardComposer {
	

	public String getTitle_Win() {
		return title_Win;
	}

	Map params=Executions.getCurrent().getArg();
	CommonTreatyInfo cott; 
	CommonTreatyInfo cott1; 
	Window frameWin;
	
	String title_Win;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		cott=(CommonTreatyInfo) params.get("cott");
		if(cott!=null)
		{
			title_Win=cott.getTypeName();
			openchildWindow();
		}	
		
		
		
	}
	
	public void openchildWindow()
	{
		 Window objWindow = (Window) Executions.createComponents("/SysForm/commontreaty.zul",null,params);
		 objWindow.setParent(frameWin);
		 objWindow.doEmbedded();
	}

}

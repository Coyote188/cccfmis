package cccf.ma.web.zk;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;

import cccf.ma.model.ManufactureInfo;

public class ManuViewerController extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;

	private ManufactureInfo manu;
	Map param = Executions.getCurrent().getArg();
	
	private Window frmManuViewer,frmEnterpriseOuter;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		setManu((ManufactureInfo) param.get("manu"));
	}
	
	public void onClick$btnGoback(){
		frmManuViewer.detach();
		Window objWindow = (Window) Executions.createComponents("manufacture-manage.zul", frmEnterpriseOuter, null);
		objWindow.doEmbedded();
	}
	
	public void onClick$btnModify(){
		frmManuViewer.detach();
		param.put("manu", manu);
		Window objWindow = (Window) Executions.createComponents("manu-edit.zul", frmEnterpriseOuter, param);
		objWindow.doEmbedded();
	}

	public void setManu(ManufactureInfo manu) {
		this.manu = manu;
	}

	public ManufactureInfo getManu() {
		return manu;
	}
	
	
}

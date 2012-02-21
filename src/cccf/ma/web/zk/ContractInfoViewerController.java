package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationPublicInfo;

public class ContractInfoViewerController extends GenericForwardComposer {
	private static final long serialVersionUID = 3074761598206137650L;

	private Map params = Executions.getCurrent().getArg();
	
	private ApplicationPublicInfo applPublicInfo;

	private Div divProductViewer,divApplicationViewer;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		applPublicInfo = (ApplicationPublicInfo) params.get("applPublicInfo");
		Map map = new HashMap();
		map.put("applyNo", applPublicInfo.getApplyno());
		Window objWin = (Window) Executions.createComponents("/SysForm/public_viewer/applyinfo_viewer.zul",
				divApplicationViewer, map);
		Window win = (Window) Executions.createComponents("/SysForm/public_viewer/product_models_viewer.zul", 
				divProductViewer, map);
		objWin.doEmbedded();
		win.doEmbedded();
	}

	public ApplicationPublicInfo getApplPublicInfo() {
		return applPublicInfo;
	}

	public void setApplPublicInfo(ApplicationPublicInfo applPublicInfo) {
		this.applPublicInfo = applPublicInfo;
	}


}

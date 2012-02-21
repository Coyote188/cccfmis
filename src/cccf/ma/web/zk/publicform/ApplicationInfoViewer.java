package cccf.ma.web.zk.publicform;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.function.Functions;

@SuppressWarnings("unchecked")
public class ApplicationInfoViewer extends GenericForwardComposer {
	/**
	 * params.put("applyNo","");
	 */
	private static final long serialVersionUID = 5333288450420175617L;
	private Map appPublicInfo;
	private Map params = Executions.getCurrent().getArg();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		String appNo = (String) params.get("applyNo");
		setAppPublicInfo(Functions.getApplyInfoListByApplyno(appNo));
	}
	
	public void onViewEnterpriseInfo(ForwardEvent ent){
		String applyNo = (String) ent.getOrigin().getTarget().getAttribute("applyno");
		Map map = new HashMap();
		map.put("applyNo", applyNo);
		Window win = (Window) Executions.createComponents("/SysForm/public_viewer/enterprise_details.zul", null, map);
		win.doHighlighted();
	}

	public void onViewDetail(ForwardEvent ent) throws SuspendNotAllowedException, InterruptedException{
		String appNo = ((Toolbarbutton)ent.getOrigin().getTarget()).getLabel();
		Map map = new HashMap();
		map.put("applyNo", appNo);
		Window objWin = (Window) Executions.createComponents("/SysForm/application/application_viewer.zul", null, map);
		objWin.doModal();
	}
	
	public void setAppPublicInfo(Map appPublicInfo) {
		this.appPublicInfo = appPublicInfo;
	}

	public Map getAppPublicInfo() {
		return appPublicInfo;
	}

}

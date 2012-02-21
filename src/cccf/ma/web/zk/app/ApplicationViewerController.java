package cccf.ma.web.zk.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import cccf.ma.function.Functions;
import cccf.ma.web.zk.util.FileReadOnlineUtil;

@SuppressWarnings({"unchecked"})
public class ApplicationViewerController extends GenericForwardComposer {
	private static final long serialVersionUID = 2218506749191447690L;

	private Div divProductModels;
	
	private Map params = Executions.getCurrent().getArg();
	private Map infoForView;
	private List<Map> att;
	private Map applyInfo;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String appNo = (String) params.get("applyNo");
//		infoForView= Functions.getConformityInfo(apID)
		att = Functions.getApplyAttachmentsByApplyno(appNo);
		setApplyInfo(Functions.getApplyInfoListByApplyno(appNo));
		Map map = new HashMap();
		map.put("applyNo", appNo);
		Window objWin = (Window) Executions.createComponents("/SysForm/public_viewer/product_models_viewer.zul",
				divProductModels, map);
		objWin.doEmbedded();
	}
	
	public void onViewFileOnline(ForwardEvent ent){
		String path = (String) ent.getOrigin().getTarget().getAttribute("filepath");
		FileReadOnlineUtil.onViewFileOnline(path);
	}

	public Map getInfoForView() {
		return infoForView;
	}

	public void setInfoForView(Map infoForView) {
		this.infoForView = infoForView;
	}

	public List<Map> getAtt() {
		return att;
	}

	public void setAtt(List<Map> att) {
		this.att = att;
	}

	public void setApplyInfo(Map applyInfo) {
		this.applyInfo = applyInfo;
	}

	public Map getApplyInfo() {
		return applyInfo;
	}

}

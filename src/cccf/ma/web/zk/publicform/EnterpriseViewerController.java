package cccf.ma.web.zk.publicform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mvel.optimizers.impl.refl.ArrayLength;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import cccf.ma.function.Functions;
import cccf.ma.web.zk.util.FileReadOnlineUtil;

public class EnterpriseViewerController extends GenericForwardComposer {
	private static final long serialVersionUID = 7981656471589472385L;

	private Map params = Executions.getCurrent().getArg();
	private Map enterprise;
	private List<Map> attachs;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String appNo = (String) params.get("applyNo");
		setEnterprise(Functions.getApplyEnterpriseInfoByApplyno(appNo));
		attachs = new ArrayList<Map>();
		init();
	}

	private void init(){
		String idStr = (String) enterprise.get("atts");
		if(null == idStr)
			return;
		String[] ids = idStr.split(",");
		for(String id : ids){
			if(null != id && !"".equals(id))
				attachs.add(Functions.getEnterpriseAttByAttId(id));
		}
	}
	
	public void onViewFileOnline(ForwardEvent ent){
		String path = (String) ent.getOrigin().getTarget().getAttribute("att");
		FileReadOnlineUtil.onViewFileOnline(path);
	}

	public Map getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Map enterprise) {
		this.enterprise = enterprise;
	}

	public List<Map> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Map> attachs) {
		this.attachs = attachs;
	}
}

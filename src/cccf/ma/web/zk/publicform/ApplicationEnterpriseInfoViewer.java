package cccf.ma.web.zk.publicform;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Toolbarbutton;

import cccf.ma.function.Functions;

@SuppressWarnings({"unchecked"})
public class ApplicationEnterpriseInfoViewer extends GenericForwardComposer {
	private static final long serialVersionUID = -5309889347792060833L;

	private Map params = Executions.getCurrent().getArg();
	private Map enterpriseInfo;
	
	private Toolbarbutton btnEnterpriseView;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String appNo = (String) params.get("applyNo");
		setEnterpriseInfo(Functions.getApplyEnterpriseInfoByApplyno(appNo));
	}
	
	public void onClick$btnEnterpriseView(){
		String enterpriseName = btnEnterpriseView.getLabel();
		
	}

	public void setEnterpriseInfo(Map enterpriseInfo) {
		this.enterpriseInfo = enterpriseInfo;
	}

	public Map getEnterpriseInfo() {
		return enterpriseInfo;
	}

}

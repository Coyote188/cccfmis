package cccf.ma.web.zk;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;

import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductionEnterpriseInfo;

public class PEViewerController extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;

	private ProductionEnterpriseInfo fac;
	Map param = Executions.getCurrent().getArg();
	
	private Window frmProViewer,frmEnterpriseOuter2;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		setFac((ProductionEnterpriseInfo) param.get("manu"));
	}
	
	public void onClick$btnGoback(){
		frmProViewer.detach();
		Window objWindow = (Window) Executions.createComponents("productionEnterprise-manage.zul", frmEnterpriseOuter2, null);
		objWindow.doEmbedded();
	}
	
	public void onClick$btnModify(){
		frmProViewer.detach();
		param.put("manu", fac);
		Window objWindow = (Window) Executions.createComponents("pe-edit.zul", frmEnterpriseOuter2, param);
		objWindow.doEmbedded();
	}

	public ProductionEnterpriseInfo getFac() {
		return fac;
	}

	public void setFac(ProductionEnterpriseInfo fac) {
		this.fac = fac;
	}
	
	
}

package cccf.ma.web.zk.publicform;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;

import cccf.ma.function.Functions;

@SuppressWarnings("unchecked")
public class ProductModelViewerController extends GenericForwardComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4114682976459153287L;
	
	private Map<String,Object> params = Executions.getCurrent().getArg();
	
	private List<Map> productModels;
	private Map productInfo;
	
	private Listbox lbxProductModels;
	private Popup ppProductInfo;
	private Label applydate, businessType, applyType, applyProduct_name,
			applyEnterprise_name, manufactureInfo_name,
			productionEnterpriseName, productName, specification,
			surverOrgName, observedStandard;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String appNo = (String) params.get("applyNo");
		setProductModels(Functions.getProductInfoListByApplyno(appNo));
	}
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		if (null != params.get("isMultiple")) {
			boolean isMultiple = Boolean.valueOf(params.get("isMultiple").toString());
			page.setAttribute("isMultiple", isMultiple);
		}else{
			page.setAttribute("isMultiple", false);
		}
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	public void onProductView(ForwardEvent ent) {
		Toolbarbutton tbtn = (Toolbarbutton) ent.getOrigin().getTarget();
		String proId = (String) ent.getOrigin().getTarget().getAttribute(
				"productmodel");
		productInfo = (Map) Functions.getProductInfoListByPMID(proId);
		// ppProductInfo.setAttribute("productinfo", productInfo);
		applydate.setValue(productInfo.get("applydate").toString());
		businessType.setValue(productInfo.get("businessType").toString());
		applyType.setValue(productInfo.get("applyType").toString());
		applyProduct_name.setValue(productInfo.get("applyProduct_name").toString());
		applyEnterprise_name.setValue(productInfo.get("applyEnterprise_name").toString());
		manufactureInfo_name.setValue(productInfo.get("manufactureInfo_name").toString());
		productionEnterpriseName.setValue(productInfo.get("productionEnterpriseName").toString());
		productName.setValue(productInfo.get("productName").toString());
		specification.setValue(productInfo.get("specification").toString());
		surverOrgName.setValue(productInfo.get("surverOrgName").toString());
		observedStandard.setValue(productInfo.get("observedStandard").toString());
		ppProductInfo.open(tbtn);
	}
	
	public void setProductModels(List<Map> productModels) {
		this.productModels = productModels;
	}
	public List<Map> getProductModels() {
		return productModels;
	}

}

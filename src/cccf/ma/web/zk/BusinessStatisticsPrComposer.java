package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;

public class BusinessStatisticsPrComposer extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private List<EnterpriseOwnActivatedProductListInfo> eoaplist;
	Map params = Executions.getCurrent().getArg();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		eoaplist=(List<EnterpriseOwnActivatedProductListInfo>) params.get("p_");
		if(eoaplist==null)
			eoaplist=new ArrayList();
	}

	public List<EnterpriseOwnActivatedProductListInfo> getEoaplist() {
		return eoaplist;
	}

	public void setEoaplist(List<EnterpriseOwnActivatedProductListInfo> eoaplist) {
		this.eoaplist = eoaplist;
	}
}

package cccf.mis.web.tsak.contract.approval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import com.aidi.core.service.BaseDAOServcieUtil;
import cccf.mis.web.tsak.BaseContractTsakController;

public class AuditTsakController
		extends BaseContractTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8927966804382034665L;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		getTasksbox().setMultiple(false);
	}
	@Override
	protected String getNodeName()
	{
		return "合同审核";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("contract", item);
		String applyno = (String) item.get("applyno");
		StringBuffer hql = new StringBuffer("select new map(applyno as applyno").append(",applyType as applyType").append(",businessType as businessType").append(",applydate as applydate").append(",productCatalog as productCatalog")
				.append(",contractAgree as contractAgree").append(",contractYear as contractYear").append(",contractMonth as contractMonth").append(",contractApprove as contractApprove").append(")").append(" from ApplicationPublicInfo o")
				.append(" where applyno='").append(applyno).append("'");
		List<?> list = BaseDAOServcieUtil.findByQueryString(hql.toString());
		args.put("apply", list.get(0));
		Window objWindow = (Window) Executions.createComponents("/views/ContractApproval/Audit.zul", null, args);
		objWindow.doModal();
		if ("OK".equals(objWindow.getAttribute("submit")))
		{
			((List<?>) this.getTasksbox().getModel()).remove(this.getTasksbox().getSelectedItem().getValue());
		}
		objWindow.detach();
	}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
			throws SuspendNotAllowedException, InterruptedException
	{
		// TODO Auto-generated method stub
	}
	@Override
	public String getItemButlabel()
	{
		return "审核";
	}
}

package cccf.mis.web.tsak.finance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import com.aidi.core.service.BaseDAOServcieUtil;
import cccf.mis.web.tsak.BaseContractTsakController;

public class FinanceTsakController
		extends BaseContractTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5510357056365924754L;
	@Override
	protected String getNodeName()
	{
		return "财务收费";
	}
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		getTasksbox().setMultiple(false);
	}
	@Override
	public String getItemButlabel()
	{
		return "收费确认";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("contract", item);
		Window objWindow = (Window) Executions.createComponents("/views/Finance/Finance.zul", null, args);
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
}

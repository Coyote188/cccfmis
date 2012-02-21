package cccf.mis.web.tsak.batch.approval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseBatchTsakController;

public class BatchApprovalTaskController
		extends BaseBatchTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2665844214659133645L;
	@Override
	protected String getNodeName()
	{
		return "派组审批";
	}
	@Override
	public String getItemButlabel()
	{
		return "审批";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		Map<String, Object> args = new HashMap<String, Object>();
		if (arg != null)
			args.putAll(arg);
		args.put("batch", item);
		Window win = (Window) Executions.createComponents("/views/BatchApproval/BatchApproval.zul", null, args);
		win.doModal();
		if ("OK".equals(win.getAttribute("submit")))
		{
			((List<?>) getTasksbox().getModel()).remove(item);
		}
		win.detach();
	}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
			throws SuspendNotAllowedException, InterruptedException
	{
		// TODO Auto-generated method stub
	}
}

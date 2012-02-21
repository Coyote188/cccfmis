package cccf.mis.web.tsak.batch.sure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseBatchTsakController;

public class BatchSureTaskController
		extends BaseBatchTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2665844214659133645L;
	@Override
	protected String getNodeName()
	{
		return "派组确认";
	}
	@Override
	public String getItemButlabel()
	{
		return "确认";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		Map<String, Object> args = new HashMap<String, Object>();
		if (arg != null)
			args.putAll(arg);
		args.put("apply", item);
		Window win = (Window) Executions.createComponents("/views/BatchSure/BatchSure.zul", null, args);
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

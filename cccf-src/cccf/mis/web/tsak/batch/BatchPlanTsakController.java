package cccf.mis.web.tsak.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class BatchPlanTsakController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2365414865747130624L;
	@Override
	protected String getNodeName()
	{
		// TODO Auto-generated method stub
		return "派组批次";
	}
	@Override
	public String getItemButlabel()
	{
		return "";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
			throws SuspendNotAllowedException, InterruptedException
	{
		Map<String, Object> args = new HashMap<String, Object>();
		args.putAll(args);
		args.put("values", selectedvalues);
		Window win = (Window) Executions.createComponents("/views/BatchPlan/BatchPlan.zul", null, args);
		// win.setVflex("true");
		// win.setHflex("true");
		win.doModal();
		if ("OK".equals(win.getAttribute("submit")))
		{
			((List) this.getTasksbox().getModel()).remove(this.getTasksbox().getSelectedItem().getValue());
		}
		win.detach();
	}
}

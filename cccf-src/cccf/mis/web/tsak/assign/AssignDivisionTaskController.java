package cccf.mis.web.tsak.assign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class AssignDivisionTaskController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6629028505058581298L;
	@Override
	protected String getNodeName()
	{
		return "派组任务分工";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		super.onInited(event);
		// setItemButVisible(false);
	}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
			throws SuspendNotAllowedException, InterruptedException
	{
		Map<String, Object> args = new HashMap<String, Object>();
		if (arg != null)
			args.putAll(arg);
		args.put("values", selectedvalues);
		Window win = (Window) Executions.createComponents("/views/AssignDivision/AssignDivision.zul", null, args);
		// win.setVflex("true");
		// win.setHflex("true");
		win.doModal();
		List<?> submits = (List<?>) win.getAttribute("submits");
		if (submits != null)
			((List<?>) getTasksbox().getModel()).removeAll(submits);
		win.detach();
	}
	@Override
	public String getItemButlabel()
	{
		return "分工";
	}
}

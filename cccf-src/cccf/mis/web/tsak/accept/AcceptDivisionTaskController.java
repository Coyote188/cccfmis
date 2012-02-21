package cccf.mis.web.tsak.accept;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class AcceptDivisionTaskController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6629028505058581298L;
	@Override
	protected String getNodeName()
	{
		return "受理分工";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
	{
		// TODO Auto-generated method stub
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
			throws SuspendNotAllowedException, InterruptedException
	{
		Map<String, Object> args = new HashMap<String, Object>();
		args.putAll(args);
		args.put("values", selectedvalues);
		Window win = (Window) Executions.createComponents("/views/AcceptDivision/AcceptDivision.zul", null, args);
		// win.setVflex("true");
		// win.setHflex("true");
		win.doModal();
		List<Object> submits = (List<Object>) win.getAttribute("submits");
		if (submits != null)
			((List<Object>) getTasksbox().getModel()).removeAll(submits);
		win.detach();
	}
	@Override
	public String getItemButlabel()
	{
		return "分工";
	}
}

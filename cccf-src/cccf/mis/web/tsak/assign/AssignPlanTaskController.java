package cccf.mis.web.tsak.assign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class AssignPlanTaskController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8435781182835901689L;
	@Override
	protected String getNodeName()
	{
		return "派组检验计划";
	}
	@Override
	public String getItemButlabel()
	{
		return "计划";
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		// 禁用多选
		getTasksbox().setMultiple(false);
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		Map<String, Object> args = new HashMap<String, Object>();
		if (arg != null)
			args.putAll(arg);
		args.put("apply", item);
		Window win = (Window) Executions.createComponents("/views/AssignPlan/AssignPlan.zul", null, args);
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

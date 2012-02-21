package cccf.mis.web.tsak.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import com.aidi.core.service.BaseDAOServcieUtil;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class ReportTaskController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6145374855862830712L;
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		getTasksbox().setMultiple(false);
	}
	@Override
	protected String getNodeName()
	{
		// TODO Auto-generated method stub
		return "生成报告";
	}
	@Override
	public String getItemButlabel()
	{
		return "生成报告";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		 
		args.put("apply", getTasksbox().getSelectedItem().getValue());
		 
		Window objWindow = (Window) Executions.createComponents("/views/EvaluationReport/ReportCreate.zul", null, args);
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

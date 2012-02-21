package cccf.mis.web.tsak.evaluation.arrange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseEvaluationTsakController;

public class ArrangeTsakController
		extends BaseEvaluationTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5289628691789285477L;
	@Override
	protected String getNodeName()
	{
		return "评定安排";
	}
	@Override
	public String getItemButlabel()
	{
		// TODO Auto-generated method stub
		return "评定安排";
	}
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		getTasksbox().setMultiple(false);
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		// 评定报告
		args.put("report", getTasksbox().getSelectedItem().getValue());
		
		Window objWindow = (Window) Executions.createComponents("/views/EvaluationArrange/Arrange.zul", null, args);
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

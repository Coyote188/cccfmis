package cccf.mis.web.tsak.factory.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseProductTsakController;

public class ReviewTsakController
		extends BaseProductTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -526340911321066787L;
	
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
		return "文件审查";
	}
	@Override
	public String getItemButlabel()
	{
		return "审查";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("item", item);
		Window objWindow = (Window) Executions.createComponents("/views/FactoryFileReview/Review.zul", null, args);
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
	{}
}

package cccf.mis.web.tsak.conformity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class ReviewTsakController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3565321891475139373L;
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
		return "符合性审查";
	}
	@SuppressWarnings("rawtypes")
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		System.out.println("item=" + item);
		Map<String, Object> args = new HashMap<String, Object>();
		if (arg != null)
			args.putAll(args);
		args.put("applyId", item.get("applyno"));
		args.put("isShowGrid", true);
		Window objWindow = (Window) Executions.createComponents("/views/ConformityReview/Review.zul", null, args);
		 
		objWindow.doModal();
		if ("OK".equals(objWindow.getAttribute("submit")))
		{
			((List) this.getTasksbox().getModel()).remove(this.getTasksbox().getSelectedItem().getValue());
		}
		objWindow.detach();
	}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
	{}
	@Override
	public String getItemButlabel()
	{
		// TODO Auto-generated method stub
		return "审查";
	}
}

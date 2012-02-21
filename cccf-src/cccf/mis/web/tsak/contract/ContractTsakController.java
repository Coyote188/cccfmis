package cccf.mis.web.tsak.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class ContractTsakController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4402011847493513496L;
	@Override
	protected String getNodeName()
	{
		return "合同制定";
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		getTasksbox().setMultiple(false);
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws UiException, InterruptedException
	{
		System.out.println("---------------");
		@SuppressWarnings("unchecked")
		Map<String, Object> apply = (Map<String, Object>) getTasksbox().getSelectedItem().getValue();
		Map<String, Object> args = new HashMap<String, Object>();
		if (arg != null)
			args.putAll(args);
		args.put("apply", apply);
		Window win = (Window) Executions.createComponents("/views/ContractReview/Review.zul", null, args);
		win.doModal();
		if ("OK".equals(win.getAttribute("submit")))
		{
			((List<?>) getTasksbox().getModel()).remove(getTasksbox().getSelectedItem().getValue());
		}
		win.detach();
		// apply
	}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
	{}
	@Override
	public String getItemButlabel()
	{
		return "合同制定";
	}
}

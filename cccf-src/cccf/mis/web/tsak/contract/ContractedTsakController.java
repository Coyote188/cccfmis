package cccf.mis.web.tsak.contract;

import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import cccf.mis.web.tsak.BaseContractTsakController;

public class ContractedTsakController
		extends BaseContractTsakController
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
	{}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
	{}
	@Override
	public String getItemButlabel()
	{
		return "合同制定";
	}
}

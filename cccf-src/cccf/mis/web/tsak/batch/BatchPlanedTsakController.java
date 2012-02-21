package cccf.mis.web.tsak.batch;

import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import cccf.mis.web.tsak.BaseBatchTsakController;

public class BatchPlanedTsakController
		extends BaseBatchTsakController
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
	{}
}

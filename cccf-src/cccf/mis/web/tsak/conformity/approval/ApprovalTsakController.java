package cccf.mis.web.tsak.conformity.approval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;
import cccf.mis.web.tsak.BaseApplyTsakController;

public class ApprovalTsakController
		extends BaseApplyTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4402011847493513496L;
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		getTasksbox().setMultiple(true);
	}
	@Override
	protected String getNodeName()
	{
		return "符合性审批";
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("applyId", item.get("applyno"));
		params.put("isCreateOperate", true);// 控制弹出的窗口审批按钮是否创建,true为创建,false为不创建
		params.put("isShowGrid", true);
		Window objWindow = (Window) Executions.createComponents("/views/ConformityReview/Review.zul", null, params);
		if ("OK".equals(objWindow.getAttribute("submit")))
		{
			((List<?>) this.getTasksbox().getModel()).remove(this.getTasksbox().getSelectedItem().getValue());
		}
		objWindow.detach();
	}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
	{
		// TODO Auto-generated method stub
	}
	@Override
	public String getItemButlabel()
	{
		return "审批";
	}
}

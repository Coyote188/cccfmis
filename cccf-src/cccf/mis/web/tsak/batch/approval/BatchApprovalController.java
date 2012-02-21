package cccf.mis.web.tsak.batch.approval;

import java.util.Map;

import net.modellite.spring.BeanAdapter;

import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import cccf.ma.service.AssignBatchService;

public class BatchApprovalController
		extends GenericForwardComposer
{
	private Radiogroup rg_approveResult;
	private Textbox  approveMemo;
	private Map<?, ?> params = Executions.getCurrent().getArg();
	private AssignBatchService svc = BeanAdapter.getBean("AssignBatchService", AssignBatchService.class); 
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4331072973327039490L;
	public void onSubmit()
			throws InterruptedException
	{
		Map selectItem = (Map) params.get("batch");
		String batchNo = (String) selectItem.get("batch_no");
		String approveResult = rg_approveResult.getSelectedItem().getValue();
		svc.doCheck(batchNo, 4, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemo.getText());
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
}

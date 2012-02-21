package cccf.mis.web.index;

import java.util.List;
import java.util.Map;
import net.modellite.spring.BeanAdapter;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;
import cccf.ma.service.TechnicalEvaluationReportCreateService;
import cccf.mis.web.tsak.evaluation.EvalQueryUtils;

public class EnterpriseApplyListController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	TechnicalEvaluationReportCreateService	svc					= BeanAdapter.getBean("TechnicalEvaluationReportCreateService", TechnicalEvaluationReportCreateService.class);
	private static final long				serialVersionUID	= 5494581384944260275L;
	public void onOpenApply(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		String applyno = (String) event.getOrigin().getTarget().getAttribute("applyno");
		// TODO getMap...
		Map applyInfo = EvalQueryUtils.getApplyInfo(svc, applyno);
		List appAttachments = EvalQueryUtils.getAppAttachments(svc, applyno);
		List appApprove = EvalQueryUtils.getAttachmentApprove(svc, applyno);
		EvalQueryUtils.buildObjectLinkApprove(appAttachments, appApprove);
		applyInfo.put("attachments", appAttachments);
		List productList = EvalQueryUtils.getProductlList(svc, applyno);
		List productModelList = EvalQueryUtils.getProductModelList(svc, applyno);
		List productModelAttachmentList = EvalQueryUtils.getProductModelAttachmentList(svc, applyno);
		EvalQueryUtils.buildProductLinkProductModel(productList, productModelList);
		EvalQueryUtils.buildProductModelLinkAttachment(productModelList, productModelAttachmentList);
		applyInfo.put("productList", productList);
		Executions.getCurrent().setAttribute("applyInfo", applyInfo);
		Window win = (Window) Executions.createComponents("/views/Index_enterprise/item/Apply_Detail.zul", null, null);
		win.setTitle(" ");
		win.setBorder("normal");
		win.setClosable(true);
		win.setWidth("85%");
		win.setHeight("85%");
		win.doModal();
	}
}

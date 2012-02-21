package cccf.mis.web.pub;

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

public class EvaluationMsgController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 7717760364756909003L;
	TechnicalEvaluationReportCreateService	svc					= BeanAdapter.getBean("TechnicalEvaluationReportCreateService", TechnicalEvaluationReportCreateService.class);
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
		Window win = (Window) Executions.createComponents("/views/public/Apply_Detail_ap.zul", null, null);
		win.setTitle(" ");
		win.setBorder("normal");
		win.setClosable(true);
		win.setWidth("85%");
		win.setHeight("85%");
		win.doModal();
	}
	public void onOpenSurvey(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		String applyno = (String) event.getOrigin().getTarget().getAttribute("survey");
		// TODO getMap...
		Executions.getCurrent().setAttribute("surveyReport", null);
		Window win = (Window) Executions.createComponents("/views/public/SurveyReport_read.zul", null, null);
		win.setTitle(" ");
		win.setBorder("normal");
		win.setClosable(true);
		win.setWidth("85%");
		win.setHeight("85%");
		win.doModal();
	}
}

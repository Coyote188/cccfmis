package cccf.mis.web.tsak.factory.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.modellite.spring.BeanAdapter;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import cccf.ma.service.ConformityReviewService;
import cccf.ma.service.FactoryFileReviewService;
import cccf.ma.service.TechnicalEvaluationReportCreateService;
import cccf.mis.web.pub.OnlinFileController;
import cccf.mis.web.tsak.evaluation.EvalQueryUtils;

public class ReviewController
		extends GenericForwardComposer
{
	private Radiogroup	resultradiogroup;
	private Textbox		approveMemo;
	private Map<?, ?>	params			= Executions.getCurrent().getArg();
	private Map			selectedItem	= (Map) Executions.getCurrent().getArg().get("item");
	private UserInfo	user;
	private String		applyno;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		user = UserInfoServiceUtil.getCurrentLoginUser();
		// TODO Auto-generated method stub
		applyno = (String) selectedItem.get("applyno");
		// TODO 
		TechnicalEvaluationReportCreateService svc = BeanAdapter.getBean("TechnicalEvaluationReportCreateService", TechnicalEvaluationReportCreateService.class);
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
		System.out.println("applyInfo=" + applyInfo);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onSeeFile(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		Toolbarbutton bar = (Toolbarbutton) event.getOrigin().getTarget();
		String file_path = (String) bar.getAttribute("file_path");
		Map<String, String> arg = new HashMap<String, String>();
		arg.put("filepath", file_path);
		Window win = OnlinFileController.createSee(arg);
		win.doModal();
	}
	public void onApprove(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		Toolbarbutton bar = (Toolbarbutton) event.getOrigin().getTarget();
		// 取得附件文件的ID
		String attId = (String) bar.getAttribute("attId");// 取得附件文件的ID
		String file_path = (String) bar.getAttribute("file_path");// 取得附件文件的ID
		String linkId = (String) bar.getAttribute("linkId");// 关系ID
		String type = (String) bar.getAttribute("type");// 类型 :product/apply
		Map<String, String> arg = new HashMap<String, String>();
		arg.put("fileid", attId);
		arg.put("filepath", file_path);
		Window win = OnlinFileController.createApprove(arg);
		win.doModal();
		String test = (String) win.getAttribute("text");
		String val = (String) win.getAttribute("radiog");
		// 保存
		((Label) self.getFellow((String) bar.getAttribute("mome"))).setValue(test);
		((Label) self.getFellow((String) bar.getAttribute("result"))).setValue(val);
		// TODO 保存到库
		ConformityReviewService svc = BeanAdapter.getBean("ConformityReviewService", ConformityReviewService.class);
		if ("apply".equals(type))
		{
			svc.doSaveAppAttApprove(applyno, linkId, user.getId(), user.getName(), val, test);
		} else if ("product".equals(type))
		{
			svc.doSaveProModAttApprove(applyno, linkId, user.getId(), user.getName(), val, test);
		}
		win.detach();
	}
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2037738970428481956L;
	//
	public void onSubmit(ForwardEvent event)
	{
		FactoryFileReviewService svc = BeanAdapter.getBean("FactoryFileReviewService", FactoryFileReviewService.class);
		String taskNo = (String) selectedItem.get("taskNo");
		String approveResult = resultradiogroup.getSelectedItem().getValue();
		svc.doSubmit(taskNo, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemo.getText());
		//
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
}

package cccf.mis.web.tsak.factory.datasend;

import java.util.Map;
import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Textbox;
import cccf.ma.service.FactoryCheckDatasService;
import cccf.ma.service.TechnicalEvaluationReportCreateService;
import cccf.mis.web.tsak.evaluation.EvalQueryUtils;

public class DataSendController
		extends GenericForwardComposer
{
	private Textbox	approveMemo;
	private Map		selectedItem	= (Map) Executions.getCurrent().getArg().get("apply");
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		TechnicalEvaluationReportCreateService svc = BeanAdapter.getBean("TechnicalEvaluationReportCreateService", TechnicalEvaluationReportCreateService.class);
		Map apply = EvalQueryUtils.getApplyInfo(svc, (String) selectedItem.get("applyno"));
		return super.doBeforeCompose(page, parent, compInfo);
	}
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2037738970428481956L;
	//
	public void onSubmit(ForwardEvent event)
	{
		FactoryCheckDatasService svc = BeanAdapter.getBean("FactoryCheckDatasService", FactoryCheckDatasService.class);
		String taskNo = (String) selectedItem.get("taskNo");
		String approveResult = "通过";
		svc.doSubmit(taskNo, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemo.getText());
		//
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
}

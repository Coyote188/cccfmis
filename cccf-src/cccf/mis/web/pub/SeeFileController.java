package cccf.mis.web.pub;

import java.util.HashMap;
import java.util.Map;
import net.modellite.spring.BeanAdapter;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import cccf.ma.service.ConformityReviewService;
import cccf.mis.web.tsak.evaluation.EvalQueryUtils;

public class SeeFileController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7177527055743039005L;
	
	public void onOpenSurvey(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		ConformityReviewService svc = BeanAdapter.getBean("ConformityReviewService", ConformityReviewService.class);
		String srid = (String) event.getOrigin().getTarget().getAttribute("survey");
		// TODO getMap...
		Map param = EvalQueryUtils.getSurveyReport(svc, srid);
		System.out.println("param=" + param);
		Executions.getCurrent().setAttribute("surveyReport", param);
		Window win = (Window) Executions.createComponents("/views/public/SurveyReport_read.zul", null, null);
		win.setTitle(" ");
		win.setBorder("normal");
		win.setClosable(true);
		win.setWidth("85%");
		win.setHeight("85%");
		win.doModal();
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
}

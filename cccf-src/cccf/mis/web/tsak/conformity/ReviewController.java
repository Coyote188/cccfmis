package cccf.mis.web.tsak.conformity;

import java.util.ArrayList;
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
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import cccf.ma.model.ObjectApproveInfo;
import cccf.ma.service.ConformityReviewService;
import cccf.mis.web.pub.OnlinFileController;
import cccf.mis.web.tsak.evaluation.EvalQueryUtils;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

/**
 * @author JOINYO
 * @E-mail JOINYO@YEAH.NET
 * @date 创建于2011-5-19上午12:26:18
 * @version 1.0
 * @describe ALL RIGHTS RESERVED,COPYRIGHT(C) FCH LIMITED 2011
 */
public class ReviewController
		extends GenericForwardComposer
{
	private static final long		serialVersionUID	= 1L;
	private Map<?, ?>				params				= Executions.getCurrent().getArg();
	private Map<?, ?>				applyInfo;
	// 接收传递过来的任务列表
	private ConformityReviewService	svc;
	private Grid					appoveGrid;
	private List<ObjectApproveInfo>	fileApprovelist;
	private UserInfo				user;
	private Window					cofirmWin;
	private String					applyNo;												// 申请号
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		if ((Boolean) params.get("isShowGrid"))
		{
			applyNo = (String) params.get("applyId");
			StringBuffer hql = new StringBuffer("select pl.taskInstanceId from ProcessLog pl,ApplicationInfo a where pl.boType='ApplicationInfo' and pl.end is null ").append(" and pl.boId=a.id").append(" and a.sioid='").append(applyNo).append("'");
			List<?> list = BaseDAOServcieUtil.findByQueryString(hql.toString());
			if (list != null && list.size() > 0)
			{
				Long taskInstanceId = (Long) list.get(0);
				BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
				fileApprovelist = new ArrayList<ObjectApproveInfo>();
			}
		}
	}
	public void onOpenSurvey(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
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
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		user = UserInfoServiceUtil.getCurrentLoginUser();
		applyNo = (String) params.get("applyId");
		// applyInfo = Functions.getConformityInfo(applyNo);// 取得申请信息
		svc = BeanAdapter.getBean("ConformityReviewService", ConformityReviewService.class);
		applyInfo = svc.getApplyDatasByNo(applyNo);
		System.out.println("applyInfo=" + applyInfo);
		Executions.getCurrent().setAttribute("applyInfo", applyInfo);
		// 根据参数设置
		page.setAttribute("isCreateOperate", params.get("isCreateOperate"));
		page.setAttribute("isShowGrid", params.get("isShowGrid"));
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
		if ("apply".equals(type))
		{
			svc.doSaveAppAttApprove(applyNo, linkId, user.getId(), user.getName(), val, test);
		} else if ("product".equals(type))
		{
			svc.doSaveProModAttApprove(applyNo, linkId, user.getId(), user.getName(), val, test);
		}
		win.detach();
	}
	public void onSubmit()
			throws InterruptedException
	{
		if (Messagebox.show("是否确认提交表单信息？", "操作提示", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK)
		{
			List<?> approveRows = appoveGrid.getRows().getChildren();
			Row row = (Row) approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			Row row1 = (Row) approveRows.get(1);
			String approveMemo = ((Textbox) row1.getFellow("approveMemo")).getValue();
			String approveResult = "";
			if (resultListbox.getSelectedItem() != null)
			{
				approveResult = resultListbox.getSelectedItem().getLabel();
			}
			svc.doSubmit(applyNo, approveResult, approveMemo, user.getId(), fileApprovelist);
			Messagebox.show("信息提交成功!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			// Events.postEvent(ConfirmityListController.ON_REFRESH,cofirmWin.getParent(),null);
			cofirmWin.setAttribute("submit", "OK");
			cofirmWin.setVisible(false);
		}
	}
	//
	//
}

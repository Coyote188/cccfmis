package cccf.ma.web.zk;

import java.util.HashMap;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.service.TechnicalEvaluationService;

import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

/**
 * @author [JOINYO]
 * @E-mail JOINYO@YEAH.NET
 * @date 创建于2011-5-23下午11:39:09
 * @version 1.0
 * 
 * @describe ALL RIGHTS RESERVED,COPYRIGHT(C) FCH LIMITED 2011
 */
public class JspdLDQFMainController extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;
	private Map params = Executions.getCurrent().getArg();
	private String applyno = null;
	private String rowId, userId;
	private long taskInstanceId;
	private Grid appoveGrid;

	private Tabpanel cpTbp;
	private Component comp;
	private TechnicalEvaluationService technicalEvaluationService;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.comp = comp;
		rowId = (String) params.get("rowId");
		ApplicationInfo application = (ApplicationInfo) BaseDAOServcieUtil
				.findById(ApplicationInfo.class, rowId);
		applyno = application.getSioid();
		createCpForm(applyno);
		taskInstanceId = Long.parseLong((String) params.get("taskInstanceId"));
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		userId = UserInfoServiceUtil.getCurrentLoginUser().getId();
		technicalEvaluationService = BeanAdapter.getBean(
				"TechnicalEvaluationService", TechnicalEvaluationService.class);

	}

	public void createCpForm(String applyno) {
		Map tempMap = new HashMap();
		tempMap.put("applyNo", applyno);
		tempMap.put("isMultiple", true);
		Div div = new Div();
		Window objEnWindow = null;
		objEnWindow = (Window) Executions.createComponents(
				"/SysForm/public_viewer/enterprise_info_viewer.zul", null,
				tempMap);
		objEnWindow.setParent(div);
		objEnWindow = (Window) Executions.createComponents(
				"/SysForm/public_viewer/product_models_viewer.zul", null,
				tempMap);
		objEnWindow.setParent(div);
		div.setParent(cpTbp);
	}

	public void onSubmit() throws InterruptedException {
		if (Messagebox.show("确认为该申请发证吗？", "操作提示",
				Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK) {
//			BpmUtil.endTaskPreviousActor(taskInstanceId, ((Listbox) appoveGrid.getFellow("resultListbox"))
//					.getSelectedItem().getLabel());
			technicalEvaluationService.doResultAudit(userId,
					((Listbox) appoveGrid.getFellow("resultListbox"))
							.getSelectedItem().getLabel(),
					((Textbox) appoveGrid.getFellow("approveMemo")).getValue(),
					rowId);
			comp.detach();
			Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
		}
	}
}

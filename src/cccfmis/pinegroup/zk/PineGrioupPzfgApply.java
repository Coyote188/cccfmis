package cccfmis.pinegroup.zk;

import java.util.Map;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import cccf.ma.model.ApplicationInfo;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class PineGrioupPzfgApply
		extends GenericForwardComposer
{
	private static final long	serialVersionUID	= 1L;
	// private
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		this.page = page;
		return super.doBeforeCompose(page, parent, compInfo);
	}
	private ApplicationInfo	application;
	@SuppressWarnings("rawtypes")
	private Map				params		= Executions.getCurrent().getArg();
	private String			userId;
	private String			rowId;
	private Long			taskInstanceId;
	private String			entityName	= "ApplicationInfo";
	public void onCreated()
	{
		application = (ApplicationInfo) page.getAttribute("inspectionApplication");
		// application = (ApplicationInfo) page.getVariable("inspectionApplication");
		if (params.get("taskInstanceId") != null)
		{
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		rowId = application.getId();
		userId = UserInfoServiceUtil.getCurrentLoginUser().getId();
		Grid appoveGrid = (Grid) self.getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
	}
	public void onSubmit()
	{
		if (taskInstanceId > 0)
		{
			Grid appoveGrid = (Grid) self.getFellow("appoveGrid");
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId, entityName);
			Listbox resultListbox = (Listbox) self.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
				approveResult = resultListbox.getSelectedItem().getLabel();
			BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
		}
	}
}

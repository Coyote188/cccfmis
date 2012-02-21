package cccf.mis.web.tsak.factory.check;

import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import cccf.ma.service.FactoryCheckDatasCheckService;

public class DataSendController
		extends GenericForwardComposer
{
	private Radiogroup resultradiogroup;
	private Textbox  approveMemo;
	
	private Map selectedItem = (Map) Executions.getCurrent().getArg().get("item");
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2037738970428481956L;
	//
	public void onSubmit(ForwardEvent event)
	{
		FactoryCheckDatasCheckService svc = BeanAdapter.getBean("FactoryCheckDatasCheckService", FactoryCheckDatasCheckService.class);
		String taskNo = (String) selectedItem.get("taskNo");
		String approveResult = resultradiogroup.getSelectedItem().getValue();
		svc.doSubmit(taskNo, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemo.getText());
		//
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
}

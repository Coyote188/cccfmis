package cccf.ma.web.zk;

import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Window;
import cccf.ma.model.CommonTreatyInfo;
import cccf.ma.service.CommonTreatyInfoServiceUtil;

@SuppressWarnings("serial")
public class CommonTreatyController
		extends GenericForwardComposer
{
	public String getTreaty()
	{
		return treaty;
	}
	public String getTitle()
	{
		return title;
	}
	Map					param	= Executions.getCurrent().getArg();
	CommonTreatyInfo	cott;
	Window				commTreatyWin;
	Checkbox			chooseCkb;
	Button				nextBtn;
	Button				cancelBtn;
	String				title;
	String				treaty;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		
		super.doAfterCompose(comp);
		cott = CommonTreatyInfoServiceUtil.getNewestofProtocol(1);		 
		title = cott.getTitle();
		treaty = cott.getTreaty();
	}
	public void onClick$nextBtn(Event event)
			throws SuspendNotAllowedException, InterruptedException
	{
		Window objWindow = null;
		// 0-注册协议;1-产品激活协议
		if (cott.getType() == 0)
		{
			Executions.getCurrent().sendRedirect("enterpriseinfo-edit.zul");
			commTreatyWin.getParent().detach();
		} else
		{
			objWindow = (Window) Executions.createComponents("/SysForm/chooseproductactive.zul", null, null);
			objWindow.setParent(commTreatyWin.getParent());
			commTreatyWin.detach();
			objWindow.doModal();
		}
	}
	public void onClick$cancelBtn(Event event)
	{
		commTreatyWin.detach();
	}
	public void onCheck$chooseCkb(Event event)
	{
		nextBtn.setDisabled(chooseCkb.isChecked() ? false : true);
	}
	public void setTreaty(String treaty)
	{
		this.treaty = treaty;
	}
}

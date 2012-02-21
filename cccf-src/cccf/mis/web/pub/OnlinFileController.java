package cccf.mis.web.pub;

import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class OnlinFileController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7449172898232999998L;
	public static Window createApprove(Map<?, ?> arg)
	{
		Window win = (Window) Executions.createComponents("/views/public/ApproveFile.zul", null, arg);
		return win;
	}
	private Textbox		text;
	private Radiogroup	radiog;
	public void onOk(ForwardEvent event)
	{
		self.setAttribute("text", text.getValue());
		self.setAttribute("radiog", radiog.getSelectedItem().getValue());
		self.setVisible(false);
	}
	 
	public static Window createSee(Map<String, String> arg)
	{
		Window win = (Window) Executions.createComponents("/views/public/OnlinFile.zul", null, arg);
		return win;
	}
}

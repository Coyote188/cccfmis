package cccf.mis.web.pub;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class TestController
		extends GenericForwardComposer
{
	private static final long	serialVersionUID	= 1L;
	public void onPrintWord(ForwardEvent event)
			throws InterruptedException
	{
		Executions.sendRedirect("servlet/PrintServlet?param2=123456");
	}
}

package openjframework.util;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

public class TabPanelRefreshForm{

	public static void refreshForm(Window obj,String url )
	{
		//只针对Tabbox中的窗口
		Tabpanel tp=(Tabpanel)obj.getParent();
		if(tp!=null)
			tp.removeChild(tp.getFirstChild());
		Window newWindow = (Window) Executions.createComponents(
				url, null, null);
		newWindow.setParent(tp);
		newWindow.doEmbedded();
	}
}

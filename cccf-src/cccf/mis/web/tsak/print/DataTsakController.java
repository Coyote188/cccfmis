package cccf.mis.web.tsak.print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.modellite.rim.itext.PdfFileUtil;
import net.modellite.spring.BeanAdapter;
import openjframework.util.ZkFileUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;
import cccf.ma.model.Certification;
import cccf.ma.service.TechnicalEvaluationService;
import cccf.mis.web.tsak.BaseEvaluationTsakController;
import cccfmis.bpm.zk.PdfZs;

public class DataTsakController
		extends BaseEvaluationTsakController
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8390669587416739155L;
	@Override
	protected String getNodeName()
	{
		return "资料打印";
	}
	@Override
	public String getItemButlabel()
	{
		return "打印";
	}
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		getTasksbox().setMultiple(false);
		
	}
	@Override
	protected void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException
	{
		HashMap<String, Object> args = new HashMap<String, Object>();
		// 评定报告
		args.put("report", getTasksbox().getSelectedItem().getValue());
		Window objWindow = (Window) Executions.createComponents("/views/DataPrint/DataPrint.zul", null, args);
		objWindow.doModal();
		if ("OK".equals(objWindow.getAttribute("submit")))
		{
			((List<?>) this.getTasksbox().getModel()).remove(this.getTasksbox().getSelectedItem().getValue());
		}
		objWindow.detach();
	}
	@Override
	protected void doSelectItems(List<Map<String, Object>> selectedvalues)
			throws SuspendNotAllowedException, InterruptedException
	{
		// TODO Auto-generated method stub
	}
}

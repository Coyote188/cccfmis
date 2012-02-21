package cccf.mis.web.tsak.print;

import java.util.List;
import java.util.Map;
import net.modellite.rim.itext.PdfFileUtil;
import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cccf.ma.model.Certification;
import cccf.ma.model.TechnicalEvaluationReport;
import cccf.ma.model.TechnicalEvaluationReportOpinion;
import cccf.ma.service.TechnicalEvaluationPrintService;
import cccf.ma.service.TechnicalEvaluationService;
import cccfmis.bpm.zk.PdfZs;

public class DataController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8027323232748145837L;
	private Map					selectItem;
	private Textbox				approveMemo;
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		selectItem = (Map) Executions.getCurrent().getArg().get("report");
		// 初始化技术评定人员列表
		List<?> reviewUserList = UserInfoServiceUtil.getByRoleName("技术评定人员");
		page.setAttribute("reviewUserList", reviewUserList);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	/**
		 * 
		 */
	public void onSubmit(ForwardEvent event)
			throws InterruptedException
	{
		if (Messagebox.show("是否确认提交表单信息？", "操作提示", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK)
		{
			TechnicalEvaluationPrintService svc = BeanAdapter.getBean("TechnicalEvaluationPrintService", TechnicalEvaluationPrintService.class);
			svc.doEvaluationEnd((String) selectItem.get("reportNo"), UserInfoServiceUtil.getCurrentLoginUser().getId(), "完成", approveMemo.getText());
			self.setAttribute("submit", "OK");
			self.setVisible(false);
		}
	} 
	private List				certificates;
	private List				reports; 
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		
		TechnicalEvaluationPrintService service = BeanAdapter.getBean("TechnicalEvaluationPrintService", TechnicalEvaluationPrintService.class);
		
		String reportNo = (String) selectItem.get("reportNo");
		setCertificates(service.getCertificationList(reportNo));
		setReports(service.getEvaluationReportData(reportNo));
	}
	public void onFileGeneration(ForwardEvent ent)
			throws Exception
	{
		Map map = (Map) ent.getOrigin().getTarget().getAttribute("cert");
		String path = Certification.mkDir(Certification.path);
		PdfZs.makTempZspdf2(map, path);
		Window wn = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, selectItem);
		wn.setTitle("[" + "在线查看" + "]");
		Iframe ss = (Iframe) wn.getFellow("ifrme");
		ss.setSrc(Certification.path + map.get("number") + ".pdf");
		wn.doModal();
	}
	public void onFileGeneration2(ForwardEvent ent)
			throws Exception
	{
		Map map = (Map) ent.getOrigin().getTarget().getAttribute("repo");
		String path = ZkFileUtil.getRealUrl("/filemodels/contract_template.pdf");
		String filePath = Certification.mkDir(Certification.repoPath);
		Map<String, String> fieldsArgs = map;
		String certNo = map.get("编号").toString();
		// String trmp = path + "/report/" + certNo;
		// (new File(trmp)).mkdirs();
		System.out.println(filePath + "------" + path);
		PdfFileUtil.simpleMakPdfFile(path + "/pdbgmb.pdf", filePath + "/" + certNo + ".pdf", fieldsArgs);
		// String file = filePath + "/" + certNo + ".pdf";
		// PdfFileUtil.simpleCopyFiles(filePath,file );
		/*
		 * 查看文件
		 */
		// Iframe ifm = (Iframe) this.getFellow("pdbgiframe");
		// ifm.setSrc("/cccfpage/pdffileModel/report/" + certNo + "/pdbg.pdf");
		Window wn = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, selectItem);
		wn.setTitle("[" + "在线查看" + "]");
		Iframe ss = (Iframe) wn.getFellow("ifrme");
		ss.setSrc(Certification.repoPath + certNo + ".pdf");
		wn.doModal();
	}
	public void setCertificates(List certificates)
	{
		this.certificates = certificates;
	}
	public List getCertificates()
	{
		return certificates;
	}
	public void setReports(List reports)
	{
		this.reports = reports;
	}
	public List getReports()
	{
		return reports;
	}
}

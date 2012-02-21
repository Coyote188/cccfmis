package cccf.ma.web.zk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.rim.itext.PdfFileUtil;
import net.modellite.spring.BeanAdapter;

import openjframework.util.ZkFileUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.itextpdf.text.DocumentException;

import cccf.ma.model.Certification;
import cccf.ma.service.TechnicalEvaluationService;
import cccf.ma.web.zk.util.FileReadOnlineUtil;
import cccfmis.bpm.zk.PdfZs;

@SuppressWarnings("unchecked")
public class TechnicalFilePrinterController extends GenericForwardComposer {
	private static final long serialVersionUID = 2854302815529213241L;

	private Map params = Executions.getCurrent().getArg();
	
	private List certificates;
	private List reports;
	
	TechnicalEvaluationService service;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		service = BeanAdapter.getBean("TechnicalEvaluationService", TechnicalEvaluationService.class);
		super.doAfterCompose(comp);
		String applyId = (String) params.get("applyId");
		setCertificates(service.getCertificationList(applyId));
		setReports(service.getEvaluationReportData(applyId));
	}
	
	public void onFileGeneration(ForwardEvent ent) throws Exception{
		Map map = (Map) ent.getOrigin().getTarget().getAttribute("cert");
		String path = Certification.mkDir(Certification.path);
		PdfZs.makTempZspdf2(map, path);
		Window wn = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, this.params);
		wn.setTitle("[" + "在线查看" + "]");
		Iframe ss = (Iframe) wn.getFellow("ifrme");
		ss.setSrc(Certification.path + map.get("number") +".pdf");
		wn.doModal();
	}
	
	public void onFileGeneration2(ForwardEvent ent) throws Exception{
		Map map = (Map) ent.getOrigin().getTarget().getAttribute("repo");
		String path = ZkFileUtil.getRealUrl("/cccfpage/pdffileModel/");
		String filePath = Certification.mkDir(Certification.repoPath);
		Map<String, String> fieldsArgs = map;
		String certNo = map.get("编号").toString();
//		String trmp = path + "/report/" + certNo;
//		(new File(trmp)).mkdirs();
		System.out.println(filePath +"------" +path);
		PdfFileUtil.simpleMakPdfFile(path + "/pdbgmb.pdf", filePath + "/" + certNo + ".pdf", fieldsArgs);
//		String file = filePath + "/" + certNo + ".pdf";
//		PdfFileUtil.simpleCopyFiles(filePath,file );
		/*
		 * 查看文件
		 */
//		Iframe ifm = (Iframe) this.getFellow("pdbgiframe");
//		ifm.setSrc("/cccfpage/pdffileModel/report/" + certNo + "/pdbg.pdf");
		
		Window wn = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, this.params);
		wn.setTitle("[" + "在线查看" + "]");
		Iframe ss = (Iframe) wn.getFellow("ifrme");
		ss.setSrc(Certification.repoPath + certNo +".pdf");
		wn.doModal();
	}
	
	public void setCertificates(List certificates) {
		this.certificates = certificates;
	}

	public List getCertificates() {
		return certificates;
	}

	public void setReports(List reports) {
		this.reports = reports;
	}

	public List getReports() {
		return reports;
	}
	
}

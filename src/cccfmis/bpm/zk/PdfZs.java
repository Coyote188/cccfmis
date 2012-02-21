package cccfmis.bpm.zk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfZs
{
	/**
	 * 生成证书
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void makZs(String inpath, String applyid, String outPath)
			throws IOException, SQLException
	{
		// InputStream in = new FileInputStream(new File(inpath));
		// File outfile = new File(outPath+".pdf");
		// new File(outfile.getParent()).mkdirs();
		//OutputStream out = new FileOutputStream(outfile);
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification", "applyid=?");
		List<Map<String, Object>> list = adapter.load(applyid);
		int i = 0;
		String[] ins = new String[list.size()];
		for (Map<String, Object> map : list)
		{
			Map<String, String> fieldsArgs = new HashMap<String, String>();
			for (String str : map.keySet())
			{
				fieldsArgs.put(str, map.get(str).toString());
				fieldsArgs.put(str+"en", map.get(str).toString());
			}
			ins[i]=outPath + i + ".paf";
			PdfFileUtil.simpleMakPdfFile(inpath, outPath + i + ".pdf", fieldsArgs);
			i++;
		}
		
		for (int j = 0; j < i; j++)
			ins[j] = outPath + j + ".pdf";
		 
		PdfFileUtil.simpleCopyFiles(outPath+".pdf", ins);
	}
	 
	 
	public static String makTempZspdf(ApplicationInfo application, Listbox ctlist, String path)
			throws IOException, DocumentException
	{
		// 请审号
		String rowId = application.getId();
		// 产品
		ProductCatalogueInfo prod = application.getProduction();
		// 产品ID
		prod.getId();
		// 产品名称
		prod.getProductName();
		// 执行标准
		prod.getObservedStandard();
		//
		// 申请企业
		EnterpriseInfo entinfo = application.getEnterprise();
		entinfo.getId();
		// 生产厂
		ProductionEnterpriseInfo prodentInfo = application.getProductionEnterprise();
		prodentInfo.getId();
		prodentInfo.getName();
		prodentInfo.getContactAddress();
		prodentInfo.getFacAddressEn();
		// 制造商
		ManufactureInfo manInfo = application.getManufacture();
		manInfo.getId();
		manInfo.getName();
		//
		// Listbox ctlist = (Listbox) this.getFellow("certificatList");
		List<String> files = new Vector<String>();
		@SuppressWarnings("unchecked")
		List<Listitem> items = ctlist.getItems();
		int i = 0;
		for (Listitem item : items)
		{
			@SuppressWarnings("unchecked")
			List<Listcell> cells = item.getChildren();
			Textbox textID = (Textbox) cells.get(0).getFirstChild();
			// 证书编号
			String id = textID.getValue();
			// 证书有效期
			Datebox statbox = (Datebox) cells.get(1).getFirstChild();
			Datebox endbox = (Datebox) cells.get(2).getFirstChild();
			Date strdate = statbox.getValue();
			Date enddate = endbox.getValue();
			// 证书单元内容
			@SuppressWarnings("unchecked")
			List<Checkbox> cheboxs = (List<Checkbox>) cells.get(3).getChildren();
			String labes = "";// 型号
			for (Checkbox box : cheboxs)
			{
				if (box.isChecked())
					labes += "," + box.getLabel();
			}
			labes = labes.replaceFirst(",", "");
			// 处理方式
			String type = cells.get(4).getLabel();
			//
			System.out.print(" 证书编号:" + id);
			System.out.print(" 证书有效期:从" + strdate + "到" + enddate);
			System.out.print(" 单元:" + labes);
			System.out.print(" :" + type);
			System.out.println();
			//
			FileOutputStream fos = new FileOutputStream(path + "/temp/" + rowId + i + ".pdf");// 需要生成PDF
			PdfReader reader = new PdfReader(path + "/zsmb.pdf");
			PdfStamper stamp = new PdfStamper(reader, fos);
			/** 填写域 */
			AcroFields fields = stamp.getAcroFields();
			fields.setField("number", id);
			fields.setField("appname", entinfo.getName());
			fields.setField("appadress", "(所在地:" + entinfo.getContactAddress() + ")");
			fields.setField("manufacture", manInfo.getName());
			fields.setField("productionenterprise", prodentInfo.getName());
			fields.setField("productionenterpriseadress", "(所在地:" + prodentInfo.getContactAddress() + ")");
			/**产品*/
			fields.setField("productname", prodentInfo.getName());
			// 产品和规格
			fields.setField("productcatalogue", prod.getProductName() + " " + labes);
			fields.setField("productcatalogueen", prod.getProductName() + " " + labes);
			// 技术要
			fields.setField("technicalRequirement", prod.getTechnicalRequirement());
			fields.setField("technicalRequirementen", prod.getTechnicalRequirement());
			// 执行标准
			fields.setField("observedStandard", prod.getObservedStandard());
			fields.setField("observedStandarden", prod.getObservedStandard());
			// TODO
			 
			DateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
			fields.setField("certifaicationfdate", f.format(strdate));
			fields.setField("certifaicationldate", f.format(enddate));
			f = new SimpleDateFormat("MM/dd/yyyy");
			fields.setField("certifaicationfdateen", f.format(strdate));
			fields.setField("certifaicationldateen", f.format(enddate));
			stamp.setFormFlattening(true);;
			stamp.setFreeTextFlattening(true);
			stamp.close();
			files.add(path + "/temp/" + rowId + i + ".pdf");
			i++;
		}
		if (files.size() == 0)
			return null;
		// FileOutputStream fosmain = ;// 需要生成PDF
		Document doc = new Document(new PdfReader(files.get(0)).getPageSize(1));
		PdfCopy copy = new PdfCopy(doc, new FileOutputStream(path + "/temp/" + rowId + "Zs.pdf"));
		doc.open();
		for (String file : files)
		{
			PdfReader read = new PdfReader(file);
			int n = read.getNumberOfPages();
			for (int j = 1; j <= n; j++)
			{
				doc.newPage();
				PdfImportedPage ipage = copy.getImportedPage(read, j);
				copy.addPage(ipage);
				// copy.get
			}
			read.close();
		}
		doc.close();
		// FileOutputStream fos2 = new FileOutputStream(path + "/temp/" +rowId+ "zsmb.pdf");// 需要生成PDF
		return "/temp/" + rowId + "Zs.pdf";
	}
	
	public static String makTempZspdf2(Map cert, String path)
			throws IOException, DocumentException {
		String filePath = path + "/" + cert.get("number") + ".pdf" ;
		String tempPath = path + "/" + cert.get("number") + "_temp.pdf";
		FileOutputStream fos = new FileOutputStream(tempPath);// 需要生成PDF
		String templatePath = "/cccfpage/pdffileModel";
		
		PdfReader reader = new PdfReader(ZkFileUtil.getRealUrl(templatePath) + "/zsmb.pdf");
		PdfStamper stamp = new PdfStamper(reader, fos);
		
		/** 填写域 */
		AcroFields fields = stamp.getAcroFields();
		fields.setField("number", cert.get("number").toString());
		fields.setField("appname", cert.get("appname").toString());
		fields.setField("appadress", "(所在地:" + cert.get("appadress") + ")");
		fields.setField("manufacture", cert.get("manufacture").toString());
		fields.setField("productionenterprise", cert.get("productionenterprise").toString());
		fields.setField("productionenterpriseadress", "(所在地:" + cert.get("productionenterpriseadress") + ")");
		/** 产品 */
		fields.setField("productname", cert.get("productname").toString());
		// 产品和规格
		fields.setField("productcatalogue", cert.get("productname") + " , " + cert.get("productcatalogue") + ""); // 需要别外一个参数，未知
		fields.setField("productcatalogueen", cert.get("productcatalogueen")
				+ " " + ""); // 需要别外一个参数，未知
		// 技术要
		fields.setField("technicalRequirement", cert
				.get("technicalRequirement").toString());
		fields.setField("technicalRequirementen", cert.get(
				"technicalRequirementen").toString());
		// 执行标准
		fields.setField("observedStandard", cert.get("observedStandard")
				.toString());
		fields.setField("observedStandarden", cert.get("observedStandarden")
				.toString());
		// TODO

		DateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
		fields.setField("certifaicationfdate", f.format(cert.get("certifaicationfdate")));
		fields.setField("certifaicationldate", f.format(cert.get("certifaicationldate")));
		f = new SimpleDateFormat("MM/dd/yyyy");
		fields.setField("certifaicationfdateen", f.format(cert.get("certifaicationfdate")));
		fields.setField("certifaicationldateen", f.format(cert.get("certifaicationldate")));
		stamp.setFormFlattening(true);
		stamp.setFreeTextFlattening(true);
		stamp.close();

		// FileOutputStream fosmain = ;// 需要生成PDF
		Document doc = new Document(new PdfReader(tempPath)
				.getPageSize(1));
		PdfCopy copy = new PdfCopy(doc, new FileOutputStream(filePath));
		doc.open();
		PdfReader read = new PdfReader(tempPath);
		int n = read.getNumberOfPages();
		for (int j = 1; j <= n; j++) {
			doc.newPage();
			PdfImportedPage ipage = copy.getImportedPage(read, j);
			copy.addPage(ipage);
		}
		read.close();
		doc.close();
		File file = new File(path + "\\" + cert.get("number") + "_temp.pdf");
		file.delete();
		return path + "\\" + cert.get("number") + ".pdf";
	}
}

package cccf.ma.pdfutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfUtil
{
	/**
	 * 根据一个databean,处理一个pdf文件，
	 * 
	 * @param templatefile
	 *            //模板文件路径
	 * @param destfile
	 *            //目标文件路径
	 * @param databean
	 *            //数据接口的实现
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void getPdfFile(String templatefile, String destfile, PdfDataBean databean)
			throws IOException, DocumentException
	{
		String TemplatePDF = templatefile;
		// TemplatePDF= Executions.getCurrent().getDesktop().getWebApp()
		// .getRealPath("pdffiles");
		//
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font FontChinese = new Font(bf, 8, Font.NORMAL);
		Font fontChinesesmall = new Font(bf, 6, Font.NORMAL);
		PdfReader reader = new PdfReader(TemplatePDF);
		// destfile
		//int t = destfile.indexOf("attachments");
		// 如果没有目录,建立目录
		String temp = destfile.replace("\\", "/");
		temp = temp.substring(0,temp.lastIndexOf("/"));
		String[] tems=temp.split("/");
		String d=tems[0];
		for(int i=1;i<tems.length;i++){
			 
			d+="/"+tems[i];
			File dir = new File(d);
			
			if(!dir.isDirectory())
				dir.mkdir();
		}
		
		//
		// destfile
		//
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destfile));
		AcroFields form = stamper.getAcroFields();
		// form.setField("name", application.getEnterprise().getName());
		PdfDataBean db = databean;
		List<String> fieldnames = getFieldName(db);
		for (int i = 0; i < fieldnames.size(); i++)
		{
			String tmpname = fieldnames.get(i);
			String value = getFieldValue(tmpname, db);
			form.setFieldProperty(tmpname, "textfont", bf, null);
			form.setField(tmpname, value);
		}
		stamper.setFormFlattening(true);
		stamper.close();
	}
	/**
	 * 根据一组databean，生成一个pdf 生成方法，是将多个pdf合并
	 * 
	 * @param templatefile
	 * @param destfile
	 * @param databean
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void getPdfFile(String templatefile, String destpath, String destfilename, List<PdfDataBean> databean)
			throws IOException, DocumentException
	{
		String filename = destpath + "/" + destfilename;
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, new FileOutputStream(filename));
		document.open();
		for (int i = 0; i < databean.size(); i++)
		{
			String tmppdffile = destpath + "/tmp_" + destfilename;
			getPdfFile(templatefile, tmppdffile, databean.get(i));
			PdfReader reader = new PdfReader(tmppdffile);
			int n = reader.getNumberOfPages();
			for (int j = 1; j <= n; j++)
			{
				document.newPage();
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
		}
		// 删除临时文件
		File file = new File(destpath + "/tmp_" + destfilename);
		if (file.exists())
			file.delete();
		copy.close();
		document.close();
	}
	/**
	 * 根据数据bean得到pdf中要写入的textfield的名字
	 * 
	 * @param db
	 * @return
	 */
	private static List<String> getFieldName(PdfDataBean db)
	{
		List<String> fieldnames = new ArrayList<String>();
		Field[] fields = db.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			String tmpname = fields[i].getName();
			fieldnames.add(tmpname);
		}
		return fieldnames;
	}
	private static String getFieldValue(String fieldname, PdfDataBean db)
	{
		String value = "";
		Method[] methods = db.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			String methodname = methods[i].getName();
			if (methodname.substring(0, 3).toUpperCase().equals("GET") && methodname.substring(3).toUpperCase().equals(fieldname.toUpperCase()))
			{
				Method method = methods[i];
				try
				{
					value = (String) method.invoke(db, new Object[] {});
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return value;
	}
}

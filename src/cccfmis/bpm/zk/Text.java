package cccfmis.bpm.zk;

import java.io.FileOutputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class Text
{
	public static void main(String[] args)
		throws NoSuchAlgorithmException
	{
		System.out.println("Chapter 1 example 1: Hello World");
		// step 1: creation of a document-object
		try
		{
			/** 新文件 */
			Rectangle rectPageSize = new Rectangle(PageSize.A4);// 定义A4页面大小
			// rectPageSize = rectPageSize.rotate();// 加上这句可以实现A4页面的横置
			Document document = new Document(rectPageSize, 50, 50, 50, 50);// 4个参数，设置了页面的4个边距
			// Document document = new Document();
			@SuppressWarnings("unused")
			PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream("D:/Chap0102.pdf"));
			// pdf.
			document.open();
			BaseFont bft = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font ft = new Font(bft, 12, Font.NORMAL);
			Paragraph c = new Paragraph("新文件", ft);
			document.add(c);
			document.close();
			/** 从模板建立 */
			String TemplatePDF = "D:/PdfIn.pdf";// 设置模板路径
			FileOutputStream fos = new FileOutputStream("D:/PdfOut.pdf");// 需要生成PDF
			PdfReader reader = new PdfReader(TemplatePDF);
			//
			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfStamper stamp = new PdfStamper(reader, fos);
			/** 填写域 */
			AcroFields fields = stamp.getAcroFields();
			Map<String, Item> map = fields.getFields();
			for (String name : map.keySet())
				System.out.println(name);
			fields.setField("name1", "我是XXX");
			fields.setField("name2", "我是XXX2");
			fields.setField("sex", "男");
			stamp.setFormFlattening(true);
			stamp.setFreeTextFlattening(true);
			/** 增加内容 */
			PdfContentByte over = stamp.getOverContent(1);
			over.beginText();
			over.setTextMatrix(30, 30);
			/** 中文字体 */
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			over.setFontAndSize(bf, 18);
			over.showText("FFFF");
			over.showTextAligned(Element.ALIGN_LEFT, "[国文]DUPLICATE", 230, 430, 45);
			over.endText();
			/** 增加页 */
			stamp.insertPage(2, PageSize.A4);
			// BaseFont fontChinese = new Font(bfChinese, 12, Font.NORMAL);
			over = stamp.getOverContent(2);
			// 写上内容
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			over.beginText();
			over.setFontAndSize(bfChinese, 24);
			over.showTextAligned(Element.ALIGN_LEFT, "[国文]DUPLICATE", 30, 600, 0);
			over.endText();
			//
			/** 插入水印 */
			Image img = Image.getInstance(new URL("file:D:/WINDOWS XP2.jpg"));
			// img.set
			img.setAbsolutePosition(200, 400);
			PdfContentByte under = stamp.getUnderContent(2);
			under.addImage(img);
			/** 从现有的别的pdf合并 */
			stamp.insertPage(3, PageSize.A4);
			PdfReader reader2 = new PdfReader("D:/Chap0102.pdf");
			under = stamp.getUnderContent(3);
			under.addTemplate(stamp.getImportedPage(reader2, 1), 1, 0, 0, 1, 0, 0);
			stamp.close();
		} catch (Exception ioe)
		{
			ioe.printStackTrace();
			// System.err.println(ioe.getMessage());
		} finally
		{}
		// step 5: we close the document
	}
}

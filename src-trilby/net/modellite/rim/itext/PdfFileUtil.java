package net.modellite.rim.itext;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfFileUtil
{
	public static boolean simpleMakPdfFile(InputStream in, OutputStream out, Map<String, String> fieldsArgs)
			throws IOException
	{
		if (in == null)
			throw new IOException("InputStream not bet null.");
		if (out == null)
			throw new IOException("OutputStream not bet null.");
		PdfReader reader = null;
		PdfStamper stamp = null;
		try
		{
			reader = new PdfReader(in);
			stamp = new PdfStamper(reader, out);
			AcroFields fields = stamp.getAcroFields();
			if (fieldsArgs == null)
				return true;
			for (String key : fieldsArgs.keySet())
			{
				fields.setField(key, fieldsArgs.get(key));
			}
			stamp.setFormFlattening(true);
			stamp.setFreeTextFlattening(true);
			stamp.close();
			return true;
		} catch (DocumentException e)
		{
			throw new IOException(e);
		} finally
		{
			if (reader != null)
				reader.close();
		}
	}
	public static boolean simpleMakPdfFile(String filein, String fileout, Map<String, String> fieldsArgs)
			throws IOException
	{
		if (filein == null)
			throw new IOException("InputStream not bet null.");
		if (fileout == null)
			throw new IOException("OutputStream not bet null.");
		PdfReader reader = null;
		PdfStamper stamp = null;
		try
		{
			reader = new PdfReader(new FileInputStream(filein));
			stamp = new PdfStamper(reader, new FileOutputStream(fileout));
			AcroFields fields = stamp.getAcroFields();
			if (fieldsArgs == null)
				return true;
			for (String key : fieldsArgs.keySet())
				fields.setField(key, fieldsArgs.get(key));
			stamp.setFormFlattening(true);
			stamp.setFreeTextFlattening(true);
			stamp.close();
			return true;
		} catch (DocumentException e)
		{
			throw new IOException(e);
		} finally
		{
			if (reader != null)
				reader.close();
		}
	}
	public static boolean simpleCopyFiles(OutputStream out, InputStream... ins)
			throws IOException
	{
		if (ins.length == 0)
			return false;
		if (out == null)
			throw new IOException("OutputStream not bet null.");
		Document doc = new Document(new PdfReader(ins[0]).getPageSize(1));
		try
		{
			PdfCopy copy = new PdfCopy(doc, out);
			doc.open();
			for (InputStream in : ins)
			{
				PdfReader read = new PdfReader(in);
				int n = read.getNumberOfPages();
				for (int i = 1; i <= n; i++)
				{
					doc.newPage();
					PdfImportedPage ipage = copy.getImportedPage(read, i);
					copy.addPage(ipage);
				}
				read.close();
			}
			doc.close();
			return true;
		} catch (DocumentException e)
		{
			throw new IOException(e);
		}
	}
	public static boolean simpleCopyFiles(String out, String... ins)
			throws IOException
	{
		if (ins.length == 0)
			return false;
		if (out == null)
			throw new IOException("String out not bet null.");
		Document doc = new Document(new PdfReader(ins[0]).getPageSize(1));
		try
		{
			PdfCopy copy = new PdfCopy(doc, new FileOutputStream(out));
			doc.open();
			for (String in : ins)
			{
				PdfReader read = new PdfReader(in);
				int n = read.getNumberOfPages();
				for (int i = 1; i <= n; i++)
				{
					doc.newPage();
					PdfImportedPage ipage = copy.getImportedPage(read, i);
					copy.addPage(ipage);
				}
				read.close();
			}
			doc.close();
			return true;
		} catch (DocumentException e)
		{
			throw new IOException(e);
		}
	}
}

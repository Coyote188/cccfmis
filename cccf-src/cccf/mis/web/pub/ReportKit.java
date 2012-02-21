package cccf.mis.web.pub;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.joni.constants.OPSize;

/**
 * File ：<b>ReportKit.java</b><br/>
 * Note ：报表生成工具类<br />
 */
public class ReportKit
{
	private static ReportKit	report	= null;
	private ReportKit()
	{}
	/** Return Instance */
	public static ReportKit getInstance()
	{
		if (report == null)
		{
			return new ReportKit();
		} else
		{
			return report;
		}
	}
	private String	Encoding	= "UTF-8";
	/**
	 * 生成WORD文档
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param url
	 *            WORD模板URL,
	 * @param name
	 *            生成WORD的名称
	 * @param dataMap
	 *            key替换字段 value替换值
	 * @throws FileNotFoundException
	 *             找不出模板时报错
	 * @throws IOException
	 *             输出时报错
	 */
	public void printWord(HttpServletResponse response, String url, String name, Map<String, String> dataMap)
			throws IOException
	{
		printWord(response.getOutputStream(), url, name, dataMap);
	}
	/**
	 * @param out
	 *            输入流
	 * @param url
	 *            WORD模板URL,
	 * @param name
	 *            生成WORD的名称
	 * @param dataMap
	 *            ARGs
	 */
	public void printWord(OutputStream out, String url, String name, Map<String, String> dataMap)
	{
		// 读取WORD内容
		String doc = FileUtils.getInstance().read(url);
		// 转换对应字段内容
		doc = this.replaceTemplate(doc, dataMap);
		// 设置输出
		PrintWriter writer = new PrintWriter(out);
		// 输出WORD
		writer.println(doc);
		writer.flush();
		writer.close();
	}
	/**
	 * 模板内容替换
	 * 
	 * @param str
	 *            WORD模板内容
	 * @param dataMap
	 *            key为表达式 value为要替换的值
	 * @return 替换内容
	 */
	private String replaceTemplate(String str, Map<String, String> dataMap)
	{
		if (dataMap != null)
		{
			Set<Entry<String, String>> dataSet = dataMap.entrySet();
			Iterator<Entry<String, String>> iterator = dataSet.iterator();
			while (iterator.hasNext())
			{
				Entry<String, String> entry = iterator.next();
				// 过滤数据为NULL的可能
				str = str.replace(filterNull(ReportKit.getFormatName(entry.getKey())), filterNull(entry.getValue()));
			}
		}
		return str;
	}
	/**
	 * 过滤对象是否为null, '', NULL值, null值, &nbsp;，如果 是 return "" , 如果否则返回对象本身
	 * 
	 * @param obj
	 *            过滤对象
	 * @return 对象toString()
	 * @author HF-Winder
	 */
	public static String filterNull(Object obj)
	{
		if (obj == null)
		{
			return "";
		} else if (obj.toString().trim().length() == 0)
		{
			return "";
		} else if ("null".equals(obj.toString().trim()) || "NULL".equals(obj.toString().trim()))
		{
			return "";
		} else if ("&nbsp;".equals(obj.toString().trim()))
		{
			return "";
		} else
		{
			return obj.toString().trim();
		}
	}
	public static InputStream String2InputStream(String str)
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}
	/**
	 * 获得模板替换名称的格式：$[name]，格式一般可以自己定义的，我们这里默认为$[]
	 * 
	 * @param name
	 *            替换名称
	 * @return $[name]
	 */
	public static String getFormatName(String name)
	{
		return (ReportConstant.MONEY_LOWER + ReportConstant.LEFT_BRACKET + name + ReportConstant.RIGHT_BRACKET);
	}
	public String getEncoding()
	{
		return Encoding;
	}
	/** Set Encode */
	public void setEncoding(String encoding)
	{
		Encoding = encoding;
	}
}

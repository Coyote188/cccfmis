package cccf.mis.web.pub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * File ：<b>File.java</b><br/>
 * Note ：文件基本操作类.使用方法：FileUnit.getInstance().XX
 */
public class FileUtils
{
	private static FileUtils	fileutil	= null;
	private FileUtils()
	{}
	/**
	 * @return 文件基本操作类实例
	 */
	public static FileUtils getInstance()
	{
		if (null == fileutil)
		{
			return new FileUtils();
		} else
		{
			return fileutil;
		}
	}
	/**
	 * 文件内容读取
	 * 
	 * @param path
	 *            文件路径
	 * @return String
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public String read(String path)
	{
		StringBuilder buf = new StringBuilder();
		try
		{
			InputStreamReader read = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");
			BufferedReader bufread = new BufferedReader(read);
			String str = "";
			while ((str = bufread.readLine()) != null)
			{
				buf.append(str);
			}
			read.close();
			bufread.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}
}

package openjframework.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import cccf.ma.model.EnterpriseInfo;

public class ZkFileUtil {

	/**
	 * 将media获取取的文件上传至服务器
	 * 
	 * @param media
	 * @param updir服务器上的上传路径
	 *            ，相对URL
	 * @return 上传成功为true
	 * @throws IOException
	 */
	public static boolean uploadFile(Media media, String updir)
			throws IOException {

		String dirPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(updir);

		// 如果路径不存在则创建
		File dirFile = null;
		try {
			dirFile = new File(dirPath);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				boolean creadok = dirFile.mkdirs();
				if (creadok) {
					System.out.println(" ok:创建文件夹成功！ ");
				} else {
					System.out.println(" err:创建文件夹失败！ ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}

		Configuration conf = Executions.getCurrent().getDesktop().getWebApp()
				.getConfiguration();
		// 解决中文问题，如果上传的文件中有中文
		conf.setUploadCharset("UTF-8");

		boolean uploadValid = false;
		if (media != null) {

			// 限制大小
			int maxSize = 10 * 1024 * 1024, fileSize = 0;

			if (media.isBinary()) {
				byte[] uploadedfile = IOUtils
						.toByteArray(media.getStreamData());
				fileSize = uploadedfile.length;
			} else {
				fileSize = media.getStringData().getBytes().length;
			}
			if (fileSize > maxSize) {
				try {
					Messagebox.show("文件不能超过"
							+ String.valueOf(maxSize / (1024 * 1024)) + "MB");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}

			String fileName = media.getName();
			// Reader r = media.getReaderData();
			// InputStream ins = new
			// ByteArrayInputStream(media.getStringData().getBytes());
			InputStream ins;
			if (media.isBinary()) {
				ins = media.getStreamData();
			} else {
				ins = new ByteArrayInputStream(media.getStringData().getBytes());
			}

			File f = new File(dirPath + "/" + fileName);

			if (!f.exists()) {
				{
					f.createNewFile();
				}
				OutputStream out = new FileOutputStream(f);
				byte[] buf = new byte[102400];
				int len;
				while ((len = ins.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				ins.close();

				try {
					Messagebox.show("上传成功!", "提示", Messagebox.OK,
							Messagebox.INFORMATION);
					uploadValid = true;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				try {
					Messagebox.show("你上传的文件名也存在，请重命名后再上传!", "提示",
							Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return uploadValid;
	}

	// 实现多个文件打包
	public static void attachmentBale(String originalFileNameStr,
			String zipFileName) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = Executions.getCurrent().getDesktop().getWebApp()
				.getConfiguration();
		// 解决中文问题，如果上传的文件中有中文
		conf.setUploadCharset("UTF-8");
		byte[] buffer = new byte[1024];

		String strZipName = zipFileName + ".zip";

		Object contextPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath("/upload/workTask");

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				contextPath + "/" + strZipName));
		List<File> filelist = new ArrayList();

		String[] newStr = originalFileNameStr.split(";");
		for (int i = newStr.length - 1; i >= 0; i--) {
			File file = new File(contextPath + "/" + newStr[i]);
			filelist.add(file);
		}

		// 读入需要下载的文件的内容，打包到zip文件
		for (int i = 0; i < filelist.size(); i++) {

			FileInputStream fis = new FileInputStream(filelist.get(i));

			out.putNextEntry(new ZipEntry(filelist.get(i).getName()));

			int len;

			while ((len = fis.read(buffer)) > 0) {

				out.write(buffer, 0, len);

			}

			out.closeEntry();

			fis.close();
			// //删除除原文件
			// if(filelist.get(i).exists()&&filelist.get(i).isFile())
			// filelist.get(i).delete();
			//
		}
		out.close();

	}

	public static String getDownUrl(String fileUrl) {
		final Execution exec = Executions.getCurrent();
		HttpServletRequest req = (HttpServletRequest) exec.getNativeRequest();
		HttpSession session = req.getSession();

		String downUrl = "../filedown.jsp;jsessionid=" + session.getId()
				+ "?filename="
				+ fileUrl.substring(fileUrl.lastIndexOf("/") + 1) + "&fileurl="
				+ fileUrl;
		return downUrl;
	}

	public static List<String> getFileNameList(String path) {
		String pathString = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(path);
		File file = new File(pathString);
		List<String> fileList = new ArrayList<String>();
		if (file.list() != null)
			for (String fileName : file.list()) {
				fileList.add(fileName);
			}
		return fileList;
	}

	public static String getRealUrl(String url) {
		Configuration conf = Executions.getCurrent().getDesktop().getWebApp()
				.getConfiguration();
		// 解决中文问题，如果上传的文件中有中文
		conf.setUploadCharset("UTF-8");
		return Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(url);
	}

	public static String getFileUrl(EnterpriseInfo e, String fileUrl) {
		final Execution exec = Executions.getCurrent();
		HttpServletRequest req = (HttpServletRequest) exec.getNativeRequest();
		HttpSession session = req.getSession();

		String downUrl = "../filedown.jsp;jsessionid=" + session.getId()
				+ "?filename="
				+ fileUrl.substring(fileUrl.lastIndexOf("/") + 1) + "&fileurl="
				+ fileUrl;
		return downUrl;
	}

	public static void removeFile(String fileUrl) {
		String dirPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(fileUrl);
		File file = new File(dirPath);
		file.delete();
		System.out.println("删除成功！" + dirPath);
	}

	public static boolean uploadFileWithoutMsg(Media media, String updir,
			String fileName, String type) throws IOException {

		String dirPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(updir);

		// 如果路径不存在则创建
		File dirFile = null;
		try {
			dirFile = new File(dirPath);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				dirFile.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}

		Configuration conf = Executions.getCurrent().getDesktop().getWebApp()
				.getConfiguration();
		// 解决中文问题，如果上传的文件中有中文
		conf.setUploadCharset("UTF-8");

		boolean uploadValid = false;
		if (media != null) {

			// 限制大小
			int maxSize = 10 * 1024 * 1024, fileSize = 0;

			if (media.isBinary()) {
				byte[] uploadedfile = IOUtils
						.toByteArray(media.getStreamData());
				fileSize = uploadedfile.length;
			} else {
				fileSize = media.getStringData().getBytes().length;
			}
			if (fileSize > maxSize) {
				try {
					Messagebox.show("文件不能超过"
							+ String.valueOf(maxSize / (1024 * 1024)) + "MB");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}
//			String fileName = prefixName + media.getName(); .
//			String fileName = updir.substring(updir.lastIndexOf("/") , updir.length());
			InputStream ins;
			if (media.isBinary()) {
				ins = media.getStreamData();
			} else {
				ins = new ByteArrayInputStream(media.getStringData().getBytes());
			}

			File f = new File(dirPath + "/" + fileName);

			if (!f.exists()) {
				{
					f.createNewFile();
				}
				OutputStream out = new FileOutputStream(f);
				byte[] buf = new byte[102400];
				int len;
				while ((len = ins.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				ins.close();
				if (type.equals("modify")) {
					try {
						Messagebox.show("修改成功!", "提示", Messagebox.OK,
								Messagebox.INFORMATION);
						uploadValid = true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					uploadValid = true;
				}
			}
		}
		return uploadValid;
	}

	/**
	 * 
	 * @param dirPath
	 * @param fileNameStr
	 *            文件名串,以";"最为分割符,如果为空,则打包下载dirPath目录下所有文件
	 * @throws IOException
	 */
	public static void moreFileBaleDown(String dirPath, String fileNameStr)
			throws IOException {
		Configuration conf = Executions.getCurrent().getDesktop().getWebApp()
				.getConfiguration();
		byte[] buffer = new byte[1024];
		String strZipName = Executions.getCurrent().getLocalName()
				+ Executions.getCurrent().getLocalAddr() + ".zip";
		System.out.println(strZipName);
		Object contextPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(dirPath);

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				contextPath + "/" + strZipName));
		List<File> filelist = new ArrayList();
		if (fileNameStr.equals("") || fileNameStr.length() == 0) {
			List<String> allFileNameStr = getFileNameList(dirPath);
			for (String filename : allFileNameStr) {
				File file = new File(contextPath + "/" + filename);
				filelist.add(file);
			}
		} else {
			String[] newStr = fileNameStr.split(";");
			for (int i = newStr.length - 1; i >= 0; i--) {
				File file = new File(contextPath + "/" + newStr[i]);
				filelist.add(file);
			}

		}

		// 读入需要下载的文件的内容，打包到zip文件
		for (int i = 0; i < filelist.size(); i++) {

			FileInputStream fis = new FileInputStream(filelist.get(i));

			out.putNextEntry(new ZipEntry(filelist.get(i).getName()));

			int len;

			while ((len = fis.read(buffer)) > 0) {

				out.write(buffer, 0, len);

			}

			out.closeEntry();

			fis.close();
			// 删除除原文件
			filelist.get(i).delete();

		}
		out.close();
		Filedownload.save(strZipName, null);
		File file = new File(strZipName);
		file.delete();

	}

}

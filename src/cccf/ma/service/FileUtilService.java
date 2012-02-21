package cccf.ma.service;

import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.springframework.web.multipart.MultipartFile;
import org.zkoss.image.Image;
import org.zkoss.image.encoder.ImageEncoder;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;


public class FileUtilService {
	private String filePath ;
//	private RenderedImage img;
	public String uploadFile(String path, InputStream is) throws IOException {
		
		boolean isDone = false;
		File dirFile = null;
//		// test getting root path
//		 String rootPath = FileUtilImpl.class.get
//		
//		
//			System.out.println();
			filePath = "f:/enterpriseinfo/"+path+"/";
			dirFile = new File(filePath);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				isDone = dirFile.mkdirs();
			}
	
		if(isDone){
			DataInputStream din = new DataInputStream(new BufferedInputStream(is));
			DataOutputStream dOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filePath+path+".jpg")));
			int i=din.available();
			byte[] b=new byte[i];
			din.readFully(b);
			dOut.write(b);
			dOut.flush();
			din.close();
			dOut.close();
		}		
		System.out.println("has clicked!"+filePath);
		return filePath;
	}
	
	public Image getFile(String path, String name) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(path
				+ name + ".jpg"));
		RenderedImage image = ImageIO.read(is);
		name = name + ".jpg";
		return org.zkoss.image.Images.encode("foo.jpeg", image);
	}
	
	public Image getFile(String path) throws IOException{
		String dirPath = Executions.getCurrent().getDesktop().getWebApp()
		.getRealPath(path);
		InputStream is = new BufferedInputStream(new FileInputStream(dirPath));
		RenderedImage image = ImageIO.read(is);
		return org.zkoss.image.Images.encode("foo.jpeg", image);
		
	}

	public boolean deleteFile(String path, MultipartFile file) {
		return false;
	}
}

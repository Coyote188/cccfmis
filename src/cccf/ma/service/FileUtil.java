package cccf.ma.service;


import java.io.IOException;
import java.io.InputStream;

import org.zkoss.image.Image;

import com.aidi.core.web.framework.ServiceAdapter;

public class FileUtil {
	public static String save(String path,InputStream is) throws IOException {
		FileUtilService service = (FileUtilService) ServiceAdapter
				.getServiceByName("FileUtilService");
		return service.uploadFile(path, is);
	}
	
	public static Image getImage(String path, String name) throws IOException{
		FileUtilService service = (FileUtilService) ServiceAdapter.getServiceByName("FileUtilService");
		return service.getFile(path, name);
	}
	
	public static Image getImage(String path) throws IOException {
		FileUtilService service = (FileUtilService) ServiceAdapter.getServiceByName("FileUtilService");
		return service.getFile(path);
	}
}

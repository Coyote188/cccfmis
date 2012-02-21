package cccf.ma.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zul.Messagebox;

import com.aidi.core.service.BaseDAOServcieUtil;

public class Attachment {
	private String id; 
	private String path; //路径
	private String name; //文件名
	private Media attfile;
	private List<Attachment> attachments; //附件
	
	public void addAtt(Attachment attachment){
		getAttachments().add(attachment);
	}
	
	public Attachment(){}
	
	public String getFilePath(){
		return getPath().substring(0,getPath().lastIndexOf("/"));
	}
	
	public Attachment(String path, String name, Media attfile) {
		this.path = path;
		this.name = name;
		this.attfile = attfile;
	}

	public Serializable save(){
		upload();
		return BaseDAOServcieUtil.save(this);
	}
	
	public void remove(){
		removeFile();
		BaseDAOServcieUtil.remove(this);
	}
	
	public void updata() {
		String filePath = getPath().substring(0,getPath().lastIndexOf("/"));
		String fileName = getPath().substring(getPath().lastIndexOf("/"));
		try {
			uploadFile(getAttfile(),filePath, fileName);
			BaseDAOServcieUtil.upDate(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void persist(){
		upload();
		BaseDAOServcieUtil.saveOrUpdata(this);
	}
	
	public void removeFile() {
		String dirPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(getPath()); //得到文件的系统路径
		File file = new File(dirPath);
		file.delete();
	}
	
	private void upload(){
//		String filetype = getAttfile().getName().substring(getAttfile().getName().lastIndexOf("."), getAttfile().getName().length());
		int index = this.attfile.getName().lastIndexOf(".");
		String fileName = UUID.randomUUID().toString();
		if(index!=-1){
			fileName = fileName +this.attfile.getName().substring(index); 
		}else{
			fileName = fileName +this.attfile.getName() ;
		}
		 
		try {
			uploadFile(getAttfile(),getPath(), fileName);
			setPath(getPath() + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传文件
	 * @param media 媒体文件
	 * @param updir 路径: 文件保存路径/文件名,文件名为uuid类型
	 * @param fileName 显示用文件名
	 * @return
	 * @throws IOException
	 */
	private boolean uploadFile(Media media, String updir,String fileName ) throws IOException {

		String dirPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(updir + "/");

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
				byte[] uploadedfile = IOUtils.toByteArray(media.getStreamData());
				fileSize = uploadedfile.length;
			} else {
				fileSize = media.getStringData().getBytes().length;
			}
			if (fileSize > maxSize) {
				try {
					Messagebox.show("文件不能超过" + String.valueOf(maxSize / (1024 * 1024)) + "MB");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}
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
			}
		}
		return uploadValid;
	}
	

	/**
	 * getter & setter
	 * @return
	 */
	public String getId() {
		return id;
	}
	public String getPath() {
		return path;
	}
	public String getName() {
		return name;
	}
	public List<Attachment> getAttachments() {
		return attachments;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param path e.g. /folder
	 */
	public void setPath(String path) {
		this.path = path;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public void setAttfile(Media attfile) {
		this.attfile = attfile;
	}

	public Media getAttfile() {
		return attfile;
	}
}

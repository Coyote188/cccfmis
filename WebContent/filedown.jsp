<%@ page language="java" pageEncoding="UTF-8"%><%
	String filedownload = request.getParameter("fileurl");//即将下载的文件的相对路径
	String filedisplay = request.getParameter("filename");//下载文件时显示的文件保存名称
	filedisplay = new String(filedisplay.getBytes("ISO-8859-1"));
	filedownload = new String(filedownload.getBytes("ISO-8859-1"));
	
	String online=request.getParameter("online");
	
	boolean isOnLine =false;
	if(online!=null){
		if(online.equals("true")) isOnLine=true;
	}
	
	String filePath=application.getRealPath(filedownload);

	java.io.File f = new java.io.File(filePath);
	if (!f.exists()) {
		response.sendError(404, "File not found!");
		return;
	}
	java.io.BufferedInputStream br = new java.io.BufferedInputStream(
			new java.io.FileInputStream(f));
	byte[] buf = new byte[1024];
	int len = 0;

	response.reset(); //非常重要
	if (isOnLine) { //在线打开方式
		java.net.URL u = new java.net.URL("file:///" + filedownload);
		response.setContentType(u.openConnection().getContentType());
		response.setHeader("Content-Disposition", "inline; filename="
				+ java.net.URLEncoder.encode(filedisplay,"UTF-8"));
		// 文件名应该编码成UTF-8 
	} else { //纯下载方式
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + java.net.URLEncoder.encode(filedisplay,"UTF-8"));
	}
	java.io.OutputStream outl = response.getOutputStream();
	while ((len = br.read(buf)) > 0)
		outl.write(buf, 0, len);
	br.close();
	outl.close();
%>
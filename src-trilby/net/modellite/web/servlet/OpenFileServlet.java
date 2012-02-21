package net.modellite.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.modellite.spring.BeanAdapter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class OpenFileServlet
		extends NativeServlet
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2624105209636981762L;
	@Override
	protected void initialize()
			throws ServletException
	{}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		final String file_id = req.getParameter("fileid");
		System.out.println("file_id:" + file_id);
		if (file_id == null || file_id.isEmpty())
		{
			resp.sendError(404, "file_id:" + file_id);
			return;
		}
		HibernateTemplate template = BeanAdapter.getBean("hibernateTemplate", HibernateTemplate.class);
		List<?> list = template.executeFind(new HibernateCallback()
		{
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException
			{
				Query query = session.createQuery("select new map(o.name as filename,o.path as uri) from Attachment o where o.id='" + file_id + "'");
				return query.list();
			}
		});
		if (list.isEmpty())
		{
			resp.sendError(404, "没有找到文件.");
			return;
		}
		Map<?, ?> val = (Map<?, ?>) list.get(0);
		String name = (String) val.get("filename");
		String uri = (String) val.get("uri");
		File file = new File(getServletContext().getRealPath(uri));
		if (!file.exists() || !file.canRead())
		{
			resp.sendError(500, "文件产存在,或不可读.");
			return;
		}
		FileInputStream inputStream = new FileInputStream(file);
		resp.reset();
		int p = uri.lastIndexOf(".");
		String ext = uri.substring(p);
		String mimeType=null;
		System.out.println("::"+ext);
		if (p >= 0)
		{
			mimeType = getServletContext().getMimeType(ext);
		}
		if (mimeType == null)
		{
			mimeType = "application/octet-stream";
		}
		System.out.println("mimeType" + mimeType);
		resp.setContentType(mimeType);		
		//resp.setHeader("Content-disposition", "attachment inline;filename=\"" + java.net.URLEncoder.encode(name,"UTF-8") + "\"");
		resp.setHeader("Content-disposition", "inline;filename=\"" + new String((name).getBytes("GB2312"), "ISO8859_1") + "\";");
		ServletOutputStream outputStream = resp.getOutputStream();
		BufferedInputStream bufinput = new BufferedInputStream(inputStream);
		BufferedOutputStream bufout = new BufferedOutputStream(outputStream);
		byte buf[] = new byte[2048];
		int l = 0;
		while ((l = bufinput.read(buf, 0, 2048)) > 0)
		{
			bufout.write(buf, 0, l);
		}
		bufout.flush();
		bufout.close();
		bufinput.close();
	}
}

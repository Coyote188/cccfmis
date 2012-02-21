package openjframework.web.zk.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import openjframework.web.zk.Encrypter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


@SuppressWarnings("unused")
public class MailUtil {

	private MimeMessage mimeMsg; // MIME邮件对象
	private Session session; // 邮件会话对象
	private Properties props; // 系统属性
	private boolean needAuth = false; // SMTP是否需要认证
	private String username = ""; // SMTP认证用户名和密码
	private String password = "";
	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

	// mail content
	private String content;
	private String subject;
	private String to;
	private String randomPassword;

	/**
	 * 使用XML文件配置邮件服务，其中包括用户名、密码、发送邮箱地址、smtp服务器、邮件主题、邮件内容。
	 */
	public MailUtil(String to,String r) {
		setRandomPassword(r);
		loadConfig();
		setTo(to);
	}

	public MailUtil(String smtp) {
		setSmtpHost(smtp);
		createMimeMessage();
	}
	
	public MailUtil(){
		
	}

	// 采用配置文件实现对邮箱的原始配置
	public void loadConfig() {
		String username = "";
		String password = "";
		String address = "";
		String hostName = "";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			DocumentBuilder documentBuilder = builderFactory
					.newDocumentBuilder();
			InputStream iStream = MailUtil.class
					.getResourceAsStream("mail-config.xml");
			Document doc = documentBuilder.parse(iStream);
			Element root = doc.getDocumentElement(); // get root
			NodeList mailconfig = root.getChildNodes(); // get root child

			Node mailInfo = mailconfig.item(1);

			if (mailInfo != null) {
				for (Node node = mailInfo.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						if (node.getNodeName().equals("username")) {
							username = node.getTextContent();
						} else if (node.getNodeName().equals("password")) {
							password = node.getTextContent();
						} else if (node.getNodeName().equals("address")) {
							address = node.getTextContent();
						} else if(node.getNodeName().equals("hostName")){
							hostName = node.getTextContent();
						} else if (node.getNodeName().equals("subject")) {
							this.subject = node.getTextContent();
						} else if (node.getNodeName().equals("content")) {
							this.content = node.getTextContent();
						}
					}
				}
			}
			
			
			setSmtpHost(hostName);
			createMimeMessage();
			setNeedAuth(true);
			setSubject();
			setBody(getContent()+""+randomPassword);
			setTo(to);
			setFrom(address);
			setNamePass(username, password);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSmtpHost(String hostName) {
		if (props == null)
			props = System.getProperties(); // 获得系统属性对象
		props.put("mail.smtp.host", hostName); // 设置SMTP主机
	}

	// 配置ssl
	public void setSSL() {
		// props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		// props.setProperty("mail.smtp.socketFactory.fallback", "false");
		// props.setProperty("mail.smtp.port", "25");
		// props.setProperty("mail.smtp.socketFactory.port", "25");
	}

	public boolean createMimeMessage() {
		try {
			session = Session.getDefaultInstance(props, null); // 获得邮件会话对象
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();
			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setNeedAuth(boolean need) {
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	public void setNamePass(String name, String pass) {
		if(props == null)
			props = System.getProperties();
		props.put("username", name);
		props.put("password", pass);
	}

	public boolean setSubject(String mailSubject) {
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * set the mail subject with xml
	 * @return boolean
	 */
	public boolean setSubject() {
		try {
			mimeMsg.setSubject(getSubject());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=GB2312");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addFileAffix(String filename) {
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(fileds.getName());
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setFrom(String from) {
		try {
			mimeMsg.setFrom(new InternetAddress(from)); // 设置发信人
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setTo(String to) {
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC,
					(Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean sendout() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			Session mailSession = Session.getInstance(props, null);
			mailSession.setDebug(true);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), props
					.getProperty("username"), props.getProperty("password"));

			transport.sendMessage(mimeMsg, mimeMsg
					.getRecipients(Message.RecipientType.TO));
			// transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
			// transport.send(mimeMsg);
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// public static void main(String[] args) {
	// String pwd = UUID.randomUUID().toString();
	// String randomPassword = pwd.substring(3, 10);
	// Encrypter.encrypt(randomPassword);
	// String mailbody = "您本次找回操作产生的随便密码为：" + randomPassword;
	// MailUtil themail = new MailUtil("smtp.sina.cn");
	// themail.setNeedAuth(true);
	// themail.setSocket();
	// if (themail.setSubject("标题") == false)
	// return;
	// if (themail.setBody(mailbody) == false)
	// return;
	// if (themail.setTo("ligang.cui@gmail.com") == false)
	// return;
	// if (themail.setFrom("cccfmis@sina.cn") == false)
	// return;
	// themail.setNamePass("cccfmis", "111111");
	// if (themail.sendout() == false)
	// return;
	// }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		setBody(content);
	}

	public String getSubject() {
		return subject;
	}

	public String getTo() {
		return to;
	}

	public String getRandomPassword() {
		return randomPassword;
	}

	public void setRandomPassword(String randomPassword) {
		this.randomPassword = randomPassword;
	}

}

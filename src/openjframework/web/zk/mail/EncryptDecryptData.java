package openjframework.web.zk.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EncryptDecryptData {
	public final static String decodeUri(String encodedUrl) throws IOException {
		String[] url = encodedUrl.replace("|", ",").split(",");
		byte[] urlByte = new byte[url.length];
		for (int i = 0; i < url.length; i++) {
			System.out.println(url[i]);
		}

		for (int i = 0; i < url.length; i++) {
			if (!url[i].equals(""))
				urlByte[i] = Byte.parseByte(url[i]);
		}
		ByteArrayInputStream bin = new ByteArrayInputStream(urlByte);
		DataInputStream din = new DataInputStream(bin);
		String oriStr = din.readUTF();
		din.close();
		bin.close();
		return oriStr;
	}

	public final static String encodeUri(String url) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		dout.writeUTF(url);
		byte[] byteUrl = bout.toByteArray();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < byteUrl.length; i++) {
			buf.append(byteUrl[i] + "|");
		}
		dout.close();
		bout.close();
		return buf.toString();
	}
}
package openjframework.util;
import java.io.FileInputStream;   
import java.io.FileWriter;   
import java.io.IOException;   
import java.io.InputStream;   
import java.io.PrintWriter;   
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;   
import java.util.Iterator;   
import java.util.List;
import java.util.Map;   

import org.zkoss.zk.ui.Executions;

import cccf.ma.model.CommentItemInfo;
import cccf.ma.service.CommentItemInfoServiceUtil;
  
/**  
 * 动态更改word文档  
 * @author Eason  
 *  
 */  
public class ReportToWord {   
  
    /**  
     * 替换动态文档  
     * @param content  
     * @param markersign  
     * @param replacecontent  
     * @return  
     * @throws UnsupportedEncodingException 
     */  
    public String replaceStr(String content, String markersign,   
            String replacecontent) throws UnsupportedEncodingException {   
        //String rc = strToRtf(replacecontent);   
        String rc = encode2HtmlUnicode(replacecontent);   
        String target = "";   
        markersign = "$" + markersign + "$";   
        target = content.replace(markersign, rc);   
        return target;   
    }   
       
    /**  
     * 把给定的str转换为10进制表示的unicode  
     * 目前只是用于mht模板的转码  
     * @param str  
     * @return  
     * @throws UnsupportedEncodingException 
     */  
    public String encode2HtmlUnicode(String str) throws UnsupportedEncodingException {   
       
    if(str == null) return "";   
    byte b[] = str.getBytes("UTF-8");  
    StringBuilder sb = new StringBuilder(str.length() * 2);   
    for (int i = 0; i < str.length(); i++) {   
        sb.append(encode2HtmlUnicode(str.charAt(i)));   
    }   
    return sb.toString();   
    }   
       
    public String encode2HtmlUnicode(char character) {   
    if (character > 255) {   
        return "&#" + (character & 0xffff) + ";";   
    } else {   
        return String.valueOf(character);   
    }   
    }   
       
       
  
    /**  
     * 读取和输出文件  
     * @param inputPath  
     * @param outPath  
     * @param data  
     */  
    public void rgModel(String inputPath, String outPath, Map<String, String> data) {   
        /* 字节形式读取模板文件内容,将结果转为字符串 */  
        String sourname = inputPath;   
        String sourcecontent = "";   
        InputStream ins = null;   
        try {   
            ins = new FileInputStream(sourname);   
            byte[] b = new byte[1638400];// 提高对文件的读取速度，特别是对于1M以上的文件   
            if (ins == null) {   
                System.out.println("源模板文件不存在");   
            }   
            int bytesRead = 0;   
            while (true) {   
                bytesRead = ins.read(b, 0, 1638400);   
                if (bytesRead == -1) {   
                    System.out.println("读取模板文件结束");   
                    break;   
                }   
                sourcecontent += new String(b, 0, bytesRead);   
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
           
        /* 修改变化部分 */  
        String targetcontent = "";   
        String oldText = "";   
        Object newValue;   
        /* 结果输出保存到文件 */  
        try {   
            Iterator<String> keys = data.keySet().iterator();   
            int keysfirst = 0;   
            while (keys.hasNext()) {   
                oldText = (String) keys.next();   
                newValue = data.get(oldText);   
                String newText = (String) newValue;   
                if (keysfirst == 0) {   
                    targetcontent = replaceStr(sourcecontent, oldText, newText);   
                    keysfirst = 1;   
                } else {   
                    targetcontent = replaceStr(targetcontent, oldText, newText);   
                    keysfirst = 1;   
                }   
            }   
  
            FileWriter fw = new FileWriter(outPath, true);   
            PrintWriter out = new PrintWriter(fw);   
            if (targetcontent.equals("") || targetcontent == "") {   
                out.println(sourcecontent);   
            } else {   
                out.println(targetcontent);   
            }   
            out.close();   
            fw.close();   
            System.out.println(outPath + " 生成文件成功");   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }   
       
    public static boolean wordReport(){   
        ReportToWord oRTF = new ReportToWord();   
  
        // *****************************************   
        // 利用HashMap读取数据库中的数据   
        HashMap<String, String> map = new HashMap<String, String>();   
        String word ="";
		Integer i = 0;
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("HHmmss");
		String time = sdf.format(new Date());
		String dirPath = Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath("/wordfiles/templent/templent.mht");
		String dirPaths = Executions.getCurrent().getDesktop().getWebApp()
		.getRealPath("/wordfiles");
		List<CommentItemInfo> list = CommentItemInfoServiceUtil.getAll();
		String br="<P style=LINE-HEIGHT: 0pt; MARGIN: 0cm 0cm 0pt class=MsoNormal>";
		for (CommentItemInfo w : list) {
			i += 1;
			word+=br+ i.toString() + "、" + w.getContent() + "";
		}
		map.put("word", word);   
        map.put("number", time.toString());   
        map.put("title", "系统测试");   
        map.put("xh", "II-2000001");    
        // ******************************************   
        oRTF.rgModel("d:/templent.mht", "d:/"+time.toString()+".doc", map);  
      // oRTF.rgModel(dirPath, dirPaths+"/"+time.toString()+".doc", map);   
       return true;
    }   
}  
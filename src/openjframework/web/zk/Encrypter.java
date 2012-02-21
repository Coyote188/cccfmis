package openjframework.web.zk;

import java.security.MessageDigest; 
import java.security.Security; 
import java.security.NoSuchAlgorithmException; 
import java.security.Provider; 

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 

/** *//** 
* <p> 
* 加密类，里面封装的是MD5的处理机制 <br> 
* 
* @author 
* @since  */ 
public class Encrypter 
{ 

    private static String DEFAULT_JCE = "com.sun.crypto.provider.SunJCE"; 
    private static String IBM_JCE = "com.ibm.crypto.provider.IBMJCE"; 
    protected static final Log log = LogFactory.getLog(Encrypter.class); 
    /** *//** 
     * 初始化系统加密算法提供者 
     */ 
    static 
    { 
        
        try 
        { 
            Security.addProvider((Provider)Class.forName(DEFAULT_JCE).newInstance()); 
        } 
        catch (Exception e) 
        { 
            log.info(e); 
            try 
            { 
                Security.addProvider((Provider)Class.forName(IBM_JCE).newInstance()); 
            } 
            catch (Exception ex) 
            { 
                log.info(ex); 
            } 
        } 
    } 

    /** *//** 
     * get hex string 
     * 
     * @param x 
     * @return 
     */ 
    private static String hexDigit(byte x) 
   { 
        StringBuffer sb = new StringBuffer(); 
        char c; 
        // First nibble 
        c = (char) ((x >> 4) & 0xf); 
        if (c > 9) 
       { 
            c = (char) ((c - 10) + 'a'); 
        } 
        else 
        { 
            c = (char) (c + '0'); 
        } 
        sb.append(c); 
        // Second nibble 
        c = (char) (x & 0xf); 
        if (c > 9) 
        { 
            c = (char) ((c - 10) + 'a'); 
        } 
        else 
        { 
            c = (char) (c + '0'); 
        } 
        sb.append(c); 
        return sb.toString(); 
    } 

    /** *//** 
     * 加密 
     * 
     * @param content 
     *            加密内容 
     * @return 加密串 
     */ 
    public static String encrypt(String content) 
    { 
        try 
       { 
            MessageDigest algorithm = null; 
            algorithm = MessageDigest.getInstance("MD5"); 
            algorithm.reset(); 
            if (content != null) 
            { 
                algorithm.reset(); 
                algorithm.update(content.getBytes()); 
                byte digest[] = algorithm.digest(); 
                StringBuffer hexString = new StringBuffer(); 
                int digestLength = digest.length; 
                for (int i = 0; i < digestLength; i++) 
                { 
                    hexString.append(hexDigit(digest[i])); 
                } 
                return hexString.toString(); 
            } 
            else 
           { 
                return ""; 
            } 
        } 
        catch (NoSuchAlgorithmException ex) 
        { 
            //加密过程中出现异常，采用原始的的内容串 
            return content; 
        } 
    } 
    /************以下为测试加密结果************/
//   public static void main(String[] args){ //注意去掉空格
//       System.out.print(Encrypter.encrypt("d333333")+"\n");
//    } 
} 



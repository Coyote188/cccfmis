package openjframework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 此类用于封装公用的工具方法
 * 
 * @author 羊海潮
 * 
 */
public class CommonDateUtil {

	/**
	 * 得到yyyy-MM-dd HH:mm:ss的系统当前日期
	 * 
	 * @return
	 */
	public static Date getSimpleToDay() {
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = new Date();
		try {
			dt = fordate.parse(dt.toLocaleString());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return dt;
	}

	/**将日期转换成yyyy-MM-dd HH:mm字符串
	 * @param dt
	 * @return
	 */
	public static String getSimpleDateStringYMDHm(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str1 = sdf.format(dt);
		return str1;
	}
	
	/**将日期转换成yyyy-MM-dd HH:mm:ss字符串
	 * @param dt
	 * @return
	 */
	public static String getSimpleDateStringYMDHms(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str1 = sdf.format(dt);
		return str1;
	}
	
	/**将日期转换成yyyy-MM-dd字符串
	 * @param dt
	 * @return
	 */
	public static String getSimpleDateStringYMD(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str1 = sdf.format(dt);
		return str1;
	}
	
	/**将YYYY-MM-dd字符串转为Date
	 * @param ymd
	 * @return
	 */
	public static Date StringYMDtoDate(String ymd){
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");  
		Date date=null;
		try {
			date = format1.parse(ymd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return date;


	}
}

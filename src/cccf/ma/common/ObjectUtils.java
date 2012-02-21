package cccf.ma.common;

import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat; 
import java.sql.Timestamp;
@SuppressWarnings("unchecked")
public class ObjectUtils {
	/**
	 * 判断 c1 是否是 c2的父类或接口
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static boolean isSubclassOf(Class c1, Class c2) {
		try {
			c2.asSubclass(c1);
			try {
				c1.asSubclass(c2);
				return false;
			} catch (ClassCastException cce) {
				return true;
			}
		} catch (ClassCastException cce) {
			return false;
		}
	}

	public static boolean isEquals(Object a, Object b) {
		if (a == b)
			return true;
		return a == null ? b.equals(a) : a.equals(b);
	}

	public static int getInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception ex) {
			return 0;
		}
	}

	public static double getdouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception ex) {
			return 0;
		}
	}

	public static long getlong(String str) {
		try {
			return Long.parseLong(str);
		} catch (Exception ex) {
			return 0;
		}
	}

	public static float getfloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (Exception ex) {
			return 0;
		}

	}

	public static boolean getboolean(String str) {
		try {
			return Boolean.getBoolean(str);
		} catch (Exception ex) {
			return false;
		}

	}

	public static Boolean getBoolean(String str) {
		try {
			return Boolean.valueOf(str);
		} catch (Exception ex) {
			return null;
		}

	}

	public static String getString(String str) {
		if (str == null) {
			return null;
		}
		return str.trim();
	}

	public static Integer getInteger(String str) {
		try {
			if (str == null || str.equals("")) {
				return null;
			}

			return new Integer(str);
		} catch (Exception ex) {
			return null;
		}
	}

	public static Float getFloat(String str) {
		try {
			if (str == null || str.equals("")) {
				return null;
			}

			return new Float(str);
		} catch (Exception ex) {
			return null;
		}
	}

	public static Long getLong(String str) {
		try {
			if (str == null || str.equals("")) {
				return null;
			}

			return new Long(str);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 
	 * @param str
	 * @param strFormat
	 *            yyyy-MM-dd
	 * @return
	 */
	public static java.sql.Date getSqlDate(String str, String strFormat) {
		try {
			if (str == null || str.equals("")) {
				return null;
			}
			DateFormat df = new java.text.SimpleDateFormat(strFormat);
			java.util.Date tempDate = df.parse(str);
			return new java.sql.Date(tempDate.getTime());
		} catch (ParseException ex) {
			return null;
		}
	}
	
	
	public static void main(String[] args){
		System.out.println(getUtilDate("2011041211110.222","yyyyMMddHHmmss.SSS"));
		 
		System.out.println(strDateToFormat("2011041211110.222","yyyyMMddHHmmss.SSS","yyyyMMddHHmmss.SSS"));
		
		System.out.println(getLong("20110412111108"));
		
	}
	
    public static String strDateToFormat(String str, String strFormat,  String retFormat){
    	return dateToString(getUtilDate(str,strFormat),retFormat);
    }
	
	public static String dateToString( java.util.Date dt,  String retFormat){ 
        DateFormat df = new SimpleDateFormat(retFormat); 
        return df.format(dt); 
	}

	/**
	 * @param str
	 * @param strFormat
	 *            yyyy-MM-dd
	 * @return
	 */
	public static java.util.Date getUtilDate(String str, String strFormat) {
		try {

			if (str == null || str.trim().equals("")) {
				return null;
			}
			DateFormat df = new java.text.SimpleDateFormat(strFormat);
			java.util.Date tempDate = df.parse(str);
			return tempDate;
		} catch (ParseException ex) {
			return null;
		}
	}

	public static Double getDouble(String str) {
		try {
			if (str == null || str.equals("")) {
				return null;
			}
			return new Double(str);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	/**
	 * 
	 * @param str
	 * @param strFormat
	 *            "yyyy-MM-dd"
	 * @return
	 */
	public static Timestamp getTimestamp(String str, String strFormat) {
		try {
			if (str == null || str.equals("")) {
				return null;
			}
			if (str.length() == 10) {
				DateFormat df = new java.text.SimpleDateFormat(strFormat);// DateFormat.getDateInstance(DateFormat.MEDIUM,
				// java.util.Locale.CHINA);
				java.util.Date tempDate = df.parse(str);
				return new java.sql.Timestamp(tempDate.getTime());
			}
			DateFormat df = DateFormat.getDateTimeInstance();
			java.util.Date tempDate = df.parse(str);
			return new java.sql.Timestamp(tempDate.getTime());
		} catch (ParseException ex) {
			return null;
		}
	}
}

package cccf.ma.web.zk;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Textbox;

public class InputValidateUtil {

	public static final int LENGTH_NAME = 20;
	public static final int LENGTH_ENTERPRISENAME = 50;
	public static final int LENGTH_USERNAME = 15;
	public static final int LENGTH_PASSWORD = 18;
	public static final int LENGTH_VARCHAR = 255;
	public static final int LENGTH_CONTENT = 4096;
	public static final int LENGTH_SUBJECT = 255;
	public static final int LENGTH_NOTICE = 4096;
	
	/**
	 * 该方法用于验证数据的长度是否超过规定长度，若超过刚对相应的组件抛出异常
	 * @param length 参数的最大长度
	 * @param tbx 承载参数的 Textbox 组件
	 */
	public static void validate(int length,Textbox tbx){
		if (tbx.getText().length() > length)
			throw new WrongValueException(tbx ,"您输入的数据过长，请重新输入!");
		else 
			return;
	}
	
	/**
	 * 用于在没有组件时对数据进行验证
	 * @param corrent 实际长度
	 * @param provisions 规定长度
	 */
	public static void validate(int corrent ,int provisions){
		if(corrent > provisions)
			throw new WrongValueException("您输入的数据过长，请重新输入!");
		else 
			return;
	}
	
	public static void validate(int length,Component comp){
		
	}
	
}

package cccf.myenum;

import cccf.ma.service.EnterpriseInfoServiceUtil;

public class ApplicationStatusUtil {

	// 修改状态
	public static boolean isEditEnabled(int status,int status0){
		boolean isEnabled=false;		
		//暂存及撤消为可编辑
		if (status== 0||status0==4) {
			isEnabled=true;
		}else {
			isEnabled=false;
		}
		
		//企业未激活
		if(EnterpriseInfoServiceUtil.getCurrentEnterprise().getStatus()==0)
		{
			isEnabled=false;
		}
		
		return isEnabled;
	}
}

package cccf.ma.web.zk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import com.aidi.bpm.service.BpmUtil;

import cccf.ma.bpm.MyTaskInstanceInfo;
import cccf.ma.bpm.MyTaskInstanceInfoUtil;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;

public class BusinessStatisticsComposer extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;
	private Combobox bstypeCbx;
	private Button queryBtn;
	private Radiogroup attRad;
	private int bstype;
	private Datebox startDbx,endDbx;
	private Div resultDiv,pDiv,eDiv,mDiv,aDiv,awDiv,aeDiv;
	private Label countP,countE,countM,countAw,countAe;
	
	int p_=0,e_=0,m_=0,aw_=0,ae_=0;
	boolean isP_=false,isE_=false,isM_=false,isA_=false,isAw_=false,isAe_=false;
	Map params =new HashMap();
	UserInfo user;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		user=UserInfoServiceUtil.getCurrentLoginUser();
	}
	public void onSelect$bstypeCbx()
	{
		queryBtn.setDisabled(false);
		if(bstypeCbx.getSelectedItem().getValue().equals("#4"))
		{
			attRad.setVisible(true);
		}
		else
			attRad.setVisible(false);
		bstype=bstypeCbx.getSelectedIndex();
		
	}
	
	public void onClick$queryBtn() throws ParseException
	{
		String qstr = "";
		String url;//定义显示结果的页面地址
		String before,end;//定义查询时间范围的边界
		if(startDbx.getText()!=null&&startDbx.getText()!="")
		{
			before=startDbx.getText();
		}
		else
			before="1900-01-01";
		
		if(endDbx.getText()!=null&&endDbx.getText()!="")
		{
			end=endDbx.getText();
		}
		else
			end="9999-12-31";
		
		//switch业务类型
		switch(bstype)
		{
			case 0:
			{
				qstr="FROM EnterpriseOwnActivatedProductListInfo  WHERE activateuser.id='"+user.getId()+"'";
				if((startDbx.getText()!=null&&startDbx.getText()!="")||(endDbx.getText()!=null&&endDbx.getText()!=""))
					qstr+=" and activatedate > '"+before +"' and activatedate <'"+end+"'";
				List<EnterpriseOwnActivatedProductListInfo> result=EnterpriseOwnActivatedProductListInfoServiceUtil.findByQuery(qstr);
				if(result!=null)
					p_=result.size();
				isP_=true;
				displayStatistics();
				params.clear();
				params.put("p_", result);
				displayQueryResult("/SysForm/businessStatistics_productActivate.zul",params);
			}break;
			
			case 3:
			{
				isA_=true;
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				Date beforeDate=format.parse(before);
				Date endDate=format.parse(end);
				//待办事物
				if(attRad.getSelectedIndex()==0)
				{
					List list = BpmUtil.getMyTaskList(String.valueOf(user.getId()));
					
					if((startDbx.getText()!=null&&startDbx.getText()!="")||(endDbx.getText()!=null&&endDbx.getText()!=""))
					{
						for(Iterator it=list.iterator();it.hasNext();)
						{
							TaskInstance taskIns=(TaskInstance) it.next();
							//如果任务时间在结束时间之后或者在开始时间之前则移除
							if(taskIns.getCreate().after(endDate)||taskIns.getCreate().before(beforeDate))
							{
								it.remove();
							}
						}
					}
					if(list!=null)
						aw_=list.size();
					isAw_=true;
					displayStatistics();
					params.clear();
					params.put("tasklist", list);
					displayQueryResult("/SysForm/businessStatistics_mytask_list.zul",params);
				}
				//已办事物
				if(attRad.getSelectedIndex()==1)
				{
					List taskInstanceInfoList =MyTaskInstanceInfoUtil.getTaskInstanceInfoListByUserId(user.getId());
					if((startDbx.getText()!=null&&startDbx.getText()!="")||(endDbx.getText()!=null&&endDbx.getText()!=""))
					{
						for(Iterator it=taskInstanceInfoList.iterator();it.hasNext();)
						{
							MyTaskInstanceInfo taskInsInfo=(MyTaskInstanceInfo) it.next();
							//如果任务时间在结束时间之后或者在开始时间之前则移除
							if(taskInsInfo.getApproveDate().after(endDate)||taskInsInfo.getApproveDate().before(beforeDate))
							{
								it.remove();
							}
						}
					}
					if(taskInstanceInfoList!=null)
						ae_=taskInstanceInfoList.size();
					isAe_=true;
					displayStatistics();
					params.clear();
					params.put("endtasklist", taskInstanceInfoList);
					displayQueryResult("/SysForm/businessStatistics_myendtask_list.zul",params);
				}
				
			}break;
		}
		
	}
	void displayStatistics()
	{
		if(isP_==true)
		{
			pDiv.setVisible(isP_);
			countP.setValue(String.valueOf(p_));
		}
		if(isE_==true)
		{
			eDiv.setVisible(isE_);
			countE.setValue(String.valueOf(e_));
		}
		if(isM_==true)
		{
			mDiv.setVisible(isM_);
			countM.setValue(String.valueOf(m_));
		}
		if(isA_==true)
		{
			aDiv.setVisible(isA_);
			if(isAw_==true)
			{
				awDiv.setVisible(isAw_);
				countAw.setValue(String.valueOf(aw_));
			}
			if(isAe_==true)
			{
				aeDiv.setVisible(isAe_);
				countAe.setValue(String.valueOf(ae_));
			}
			
		}
	}
	public void displayQueryResult(String url,Map params)
	{
		if(resultDiv.getFirstChild()!=null)
			resultDiv.getFirstChild().detach();
		Window objWindow=(Window)Executions.createComponents(url, null, params);
		objWindow.setWidth("100%");
		objWindow.setParent(resultDiv);
		objWindow.setContentStyle("overflow:auto;");
		objWindow.doEmbedded();
	}
}

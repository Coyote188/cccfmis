package cccf.ma.web.zk;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import openjframework.service.*;
import openjframework.model.*;

import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.InputEvent;

import com.aidi.bpm.service.*;
import com.aidi.core.spring.*;
import com.aidi.core.zk.*;
import cccf.ma.service.*;
import cccf.ma.model.*;
import cccf.myenum.*;

public class AppstatusRecordApplyWindow extends Window {

	public AppStatusRecordInfo appStatusRecord;
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = null;
	String rowId;
	ApplicationInfo application;
	Combobox changeTypeCombo;

	public void onCreate() {
		application = (ApplicationInfo) params.get("application");

		appStatusRecord = (AppStatusRecordInfo) this.getPage().getVariable(
				"appStatusRecord");

		processId = (String) params.get("processId");
		userId = (String) params.get("userId");
		if (processId == null || processId.trim().length() == 0) {
			UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
			userId = String.valueOf(user.getId());
			// 取得工作流ID
			processId = String.valueOf(ProcessFormServiceUtil
					.getProcessID("appstatusrecord-apply.zul"));
		}

		changeTypeCombo = (Combobox) this.getFellow("changeType");

		if (application.getStauts0() == 4 || application.getStauts0() == 5) {
			Comboitem item = new Comboitem();
			item.setValue(2);
			item.setLabel(ApplicationChangeStatus.values()[2].toString());
			changeTypeCombo.appendChild(item);
		} else {
			for (int i = 0; i < 2; i++) {
				Comboitem item = new Comboitem();
				item.setValue(i);
				item.setLabel(ApplicationChangeStatus.values()[i].toString());
				changeTypeCombo.appendChild(item);
			}
		}

	}

	public void onSave() {
		if (validateData() == false)
			return;

		String cmd = "add";
		String pcmd = (String) params.get("cmd");
		if (pcmd != null)
			cmd = pcmd;

		int changeType=Integer.valueOf(changeTypeCombo
				.getSelectedItem().getValue().toString());
		appStatusRecord.setChangeType(changeType);

		if (cmd.equals("add")) {
			appStatusRecord.setApplyDate(new Date());
			appStatusRecord.setStatus(0);
			appStatusRecord.setApplication(application);

			rowId = AppStatusRecordInfoServiceUtil.create(appStatusRecord)
					.toString();

			// 如果不是恢复的申请，则记下application的原有状态
//			if (appStatusRecord.getChangeType() != 2) {
//				application.setStauts0(application.getStauts());// 原有状态
//
//			}
			
		
		} else {
			AppStatusRecordInfoServiceUtil.update(appStatusRecord);
			rowId = appStatusRecord.getAsid();
		}
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onCancel() {
		this.detach();
	}

	public void onSubmit() {
		if (processId != null && processId != "0") {
			this.onSave();
			Page page = this.getPage();
			// 发起工作流
			Map vs = new HashMap();
			vs.put("rowId", rowId);
			vs.put("entityName", "AppStatusRecordInfo");

			// 取得受理申请流程
			Long app_processid = application.getProcessInstanceId();
			TaskInstance ti = BpmUtil.getCurrentTaskInstance(app_processid);
			String actorId = ti.getActorId();
			String receiveUserId = "";
			if (actorId != null)
				receiveUserId = actorId;
			else {
				Set actorIds = BpmUtil.getPooledActors(ti.getId());
				if (actorIds != null){
					PooledActor pactor=(PooledActor)actorIds.iterator().next();
					receiveUserId = pactor.getActorId();//如果是Pool执行者则由第一个人审批
				}
					
			}
			BpmUtil.startProcessToUser(Long.parseLong(processId), vs, userId,
					receiveUserId);
			try {
				
				this.detach();
				Messagebox.show("变更申请成功提交，请等待审批!", "操作提示", Messagebox.OK,
						Messagebox.INFORMATION);

				// 修改application的status
				application.setStauts0(7);// 变更申请中
				ApplicationInfoServiceUtil.update(application);

				
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else {
			try {
				Messagebox.show("工作流未绑定，不能发起工作流!");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	boolean validateData() {
		changeTypeCombo.getValue();
		return true;
	}

}

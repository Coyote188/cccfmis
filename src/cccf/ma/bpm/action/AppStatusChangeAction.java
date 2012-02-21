package cccf.ma.bpm.action;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.aidi.bpm.service.BpmUtil;

import cccf.ma.model.*;
import cccf.ma.service.*;

public class AppStatusChangeAction implements ActionHandler {

	private static final long serialVersionUID = 1L;

	public void execute(ExecutionContext executionContext) throws Exception {

		String rowId = executionContext.getVariable("rowId").toString();
		if (rowId != null) {
			AppStatusRecordInfo appStatusRecordInfo = AppStatusRecordInfoServiceUtil
					.getByPrimaryKey(rowId);
			appStatusRecordInfo.setStatus(status);
			AppStatusRecordInfoServiceUtil.update(appStatusRecordInfo);		

		}

	}

	private int status;

}

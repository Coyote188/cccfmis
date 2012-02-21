package cccf.ma.bpm.action;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import cccf.ma.model.*;
import cccf.ma.service.*;

public class ApplicationStatusAction implements ActionHandler {

	private static final long serialVersionUID = 1L;

	public void execute(ExecutionContext executionContext) throws Exception {
		
		if(executionContext.getVariable("rowId")==null){
			return ;
		}
		String rowId=executionContext.getVariable("rowId").toString();
		if(rowId!=null){
			ApplicationInfo application=ApplicationInfoServiceUtil.getByPrimaryKey(rowId);
			if(application==null)return;
			application.setStauts(status);
			ApplicationInfoServiceUtil.update(application);
		}

	}
	
	private int status;

}

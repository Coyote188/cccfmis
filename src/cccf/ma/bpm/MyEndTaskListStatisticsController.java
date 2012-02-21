package cccf.ma.bpm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

public class MyEndTaskListStatisticsController extends GenericForwardComposer {
	
	private static final long serialVersionUID = 1L;
	
	private List<MyTaskInstanceInfo> taskInstanceInfoList=new ArrayList();
	Map params = Executions.getCurrent().getArg();
	private Listbox taskListbox;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		taskInstanceInfoList=(List<MyTaskInstanceInfo>) params.get("endtasklist");
		//taskListbox.setModel(new ListModelList( taskInstanceInfoList));
	}
	public List<MyTaskInstanceInfo> getTaskInstanceInfoList() {
		return taskInstanceInfoList;
	}
	public void setTaskInstanceInfoList(
			List<MyTaskInstanceInfo> taskInstanceInfoList) {
		this.taskInstanceInfoList = taskInstanceInfoList;
	}

}

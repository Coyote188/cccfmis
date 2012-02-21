package cccf.ma.web.zk.contract;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import cccf.ma.function.Functions;

public class ContractToSignController extends GenericForwardComposer {
	private static final long serialVersionUID = 9014304719586531796L;

	private List<Map> taskList;
	
	private Listbox lbxContractTosignList;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		taskList = Functions.getCurrentLoginUserTaskList("合同制定");
		
		System.out.println(taskList.size());
	}
	
	public void onClick$btnContractSign(){
		Set<Listitem> items = lbxContractTosignList.getSelectedItems();
		for(Listitem item : items){
			String applyId = (String) item.getValue();
			
		}
	}

	public void setTaskList(List<Map> taskList) {
		this.taskList = taskList;
	}

	public List<Map> getTaskList() {
		return taskList;
	}
	
	
}

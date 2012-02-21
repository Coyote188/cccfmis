package cccf.ma.web.zk.factorycheck;

import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;

import cccf.ma.model.FactoryChecklist;
import cccf.ma.model.FactoryInspection;

public class FactoryChecklistEditController extends GenericForwardComposer {
	private static final long serialVersionUID = 7848780822996476298L;

	private Map params = Executions.getCurrent().getArg();
	private List<FactoryChecklist> checklist;
	private FactoryInspection inspection;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setInspection((FactoryInspection) params.get("inspection"));
		setChecklist(inspection.getEnterpriseCheckList());
		init();
	}
	
	private void init(){
		for(FactoryChecklist t : getChecklist()){
			t.init(getInspection());
		}
	}
	
	public void onResultCheck(ForwardEvent ent){
		Checkbox ckx = (Checkbox) ent.getOrigin().getTarget();
		FactoryChecklist entity = (FactoryChecklist) ent.getOrigin().getTarget().getAttribute("checkitem");
		entity.check(ckx.isChecked());
		System.out.println(entity.getCheckContent() + entity.getCurrentResult().getOpinion());
	}
	
	public void onClick$btnSubmit(){
		for(FactoryChecklist e : checklist){
			e.getCurrentResult().updata();
		}
	}

	public void setChecklist(List<FactoryChecklist> checklist) {
		this.checklist = checklist;
	}

	public List<FactoryChecklist> getChecklist() {
		return checklist;
	}

	public void setInspection(FactoryInspection inspection) {
		this.inspection = inspection;
	}

	public FactoryInspection getInspection() {
		return inspection;
	}	
}

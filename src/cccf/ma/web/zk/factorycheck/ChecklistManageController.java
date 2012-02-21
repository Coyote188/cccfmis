package cccf.ma.web.zk.factorycheck;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import cccf.ma.model.FactoryChecklist;
import cccf.ma.service.FactoryCheckServiceUtil;

public class ChecklistManageController extends GenericForwardComposer{
	private static final long serialVersionUID = 1L;
	
	private Combobox cbxItemParent;
	private Listbox lbxChecklist;
	private Textbox tbxPath,tbxCheckItem,tbxCheckContent;
	
	private List<FactoryChecklist> checklist;
	private FactoryChecklist facCheck;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setChecklist(FactoryCheckServiceUtil.getFirstLevel());
		facCheck = new FactoryChecklist();
	}
	
	public void onClick$btnAdd() throws InterruptedException{
		validata();
		if(Messagebox.YES != Messagebox.show("是否确认该操作?","操作提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION))
			return;
		if(null != cbxItemParent.getSelectedItem()){
			FactoryChecklist obj = (FactoryChecklist) cbxItemParent.getSelectedItem().getValue();
			facCheck.setParent(obj);
		}
		facCheck.persist();
		
		facCheck = new FactoryChecklist();
		onDataReload();
	}
	
	public void onRemove(ForwardEvent event) throws InterruptedException{
		if(Messagebox.YES != Messagebox.show("是否确认该操作?","操作提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION))
			return;
		FactoryChecklist obj = (FactoryChecklist) event.getOrigin().getTarget().getAttribute("arg");
		obj.remove();
		onDataReload();
	}
	
	private void onDataReload(){
		setChecklist(FactoryCheckServiceUtil.getFirstLevel());
		ListModelList model = new ListModelList(getChecklist());
		lbxChecklist.setModel(model);
	}
	
	void validata(){
		tbxPath.getValue();
		tbxCheckItem.getValue();
		tbxCheckContent.getValue();
	}

	public void setChecklist(List<FactoryChecklist> checklist) {
		this.checklist = checklist;
	}

	public List<FactoryChecklist> getChecklist() {
		return checklist;
	}

	public void setFacCheck(FactoryChecklist facCheck) {
		this.facCheck = facCheck;
	}

	public FactoryChecklist getFacCheck() {
		return facCheck;
	}
	
	

}

package cccf.ma.model;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseChecklist {
	private String id;
	private List<FactoryCheckResult> enterpriseCheckList;
	
	public List<FactoryCheckResult> getFirstLevel(){
		List<FactoryCheckResult> firstLevel = new ArrayList<FactoryCheckResult>();
		for(FactoryCheckResult obj : getEnterpriseCheckList()){
			if(null == obj.getCurrentChecklist().getParent())
				firstLevel.add(obj);
		}
		return firstLevel;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<FactoryCheckResult> getEnterpriseCheckList() {
		return enterpriseCheckList;
	}
	public void setEnterpriseCheckList(List<FactoryCheckResult> enterpriseCheckList) {
		this.enterpriseCheckList = enterpriseCheckList;
	}
}

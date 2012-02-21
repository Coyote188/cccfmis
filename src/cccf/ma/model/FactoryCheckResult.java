package cccf.ma.model;

import com.aidi.core.service.BaseDAOServcieUtil;

public class FactoryCheckResult {
	private String id;
	private boolean result;
	private String opinion;
	
	private FactoryInspection factoryInspection;
	private FactoryChecklist checklist;

	public FactoryCheckResult(){}
	
	public FactoryCheckResult(boolean result, String opinion,
			FactoryChecklist checklist) {
		this.result = result;
		this.opinion = opinion;
		this.checklist = checklist;
	}

	public FactoryCheckResult(FactoryChecklist checklist,FactoryInspection inspection) {
		this.checklist = checklist;
		this.factoryInspection = inspection;
	}

	public void persist() throws Exception{
//		if(null == opinion || null == checklist){
//			throw new Exception("opinion or checklist can not be null");
//		}
		BaseDAOServcieUtil.saveOrUpdata(this);
	}
	public void updata(){
		BaseDAOServcieUtil.upDate(this);
	}
	public void remove(){
		BaseDAOServcieUtil.remove(this);
	}
	
	public FactoryChecklist getCurrentChecklist(){
		String queryStr = "FROM FactoryChecklist f WHERE f.id = '" + checklist + "'" ;
		return (FactoryChecklist) BaseDAOServcieUtil.findByQueryString(queryStr).get(0);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public FactoryChecklist getChecklist() {
		return checklist;
	}

	public void setChecklist(FactoryChecklist checklist) {
		this.checklist = checklist;
	}

	public void setFactoryInspection(FactoryInspection factoryInspection) {
		this.factoryInspection = factoryInspection;
	}

	public FactoryInspection getFactoryInspection() {
		return factoryInspection;
	}
	
	
}

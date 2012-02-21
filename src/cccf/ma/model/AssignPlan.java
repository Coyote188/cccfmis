package cccf.ma.model; 
 

/**
 * 派组检验计划  
 *
 */
public class AssignPlan {
	private String id;
	private boolean doFactoryInspect; //是否进行工厂检查
	private String inspectPlace;//检查场所
	private boolean doProductTest;//是否进行产品检验
	private double inspectDays;//检查天数
	
	private String applyno;// 申请号
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDoFactoryInspect() {
		return doFactoryInspect;
	}
	public void setDoFactoryInspect(boolean doFactoryInspect) {
		this.doFactoryInspect = doFactoryInspect;
	}
	public String getInspectPlace() {
		return inspectPlace;
	}
	public void setInspectPlace(String inspectPlace) {
		this.inspectPlace = inspectPlace;
	}
	public boolean isDoProductTest() {
		return doProductTest;
	}
	public void setDoProductTest(boolean doProductTest) {
		this.doProductTest = doProductTest;
	}
	 
	public String getApplyno() {
		return applyno;
	}
	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}
	public double getInspectDays() {
		return inspectDays;
	}
	public void setInspectDays(double inspectDays) {
		this.inspectDays = inspectDays;
	} 
}

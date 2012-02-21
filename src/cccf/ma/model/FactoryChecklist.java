package cccf.ma.model;

import java.util.List;

import com.aidi.core.service.BaseDAOServcieUtil;

@SuppressWarnings("unchecked")
public class FactoryChecklist {
	private String id; //id
	private FactoryChecklist parent;  //父结点
	private String path; //路径
	private Integer serialNum; //序号
	private String checkItem; //检查项目
	private String checkContent; //检查内容
	private FactoryCheckResult currentResult;
	private List<FactoryChecklist> children;
	
	public FactoryCheckResult getCurrentResult(FactoryInspection entity){
		String queryStr = "FROM FactoryCheckResult f WHERE f.checklist = '" + getId() + "' and f.factoryInspection = '" + entity.getId() + "'";
		List list = BaseDAOServcieUtil.findByQueryString(queryStr);
		if(list.isEmpty())
			return null;
		else
			return (FactoryCheckResult) list.get(0);
	}
	
	public void init(FactoryInspection arg) {
		if (null == getCurrentResult(arg)) {
			FactoryCheckResult entity = new FactoryCheckResult(this, arg);
			try {
				entity.persist();
				setCurrentResult(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			setCurrentResult(getCurrentResult(arg));
		}
	}
	
	public void check(boolean result){
		this.getCurrentResult().setResult(result);
	}
	public void submit(){
		this.getCurrentResult().updata();
	}
	public void save(){
		BaseDAOServcieUtil.save(this);
	}
	public void remove(){
		BaseDAOServcieUtil.remove(this);
	}
	public void update(){
		BaseDAOServcieUtil.upDate(this);
	}
	public void persist(){
		BaseDAOServcieUtil.saveOrUpdata(this);
	}
	/**
	 * 得到当前节点的父节点
	 * @return FactoryChecklist
	 */
	public FactoryChecklist getParentNode(){
		String queryStr = "FROM FactoryChecklist f WHERE f.id= '" + this.getId() + "'";
		List<FactoryChecklist> objs = BaseDAOServcieUtil.findByQueryString(queryStr);
		if(null != objs){
			return objs.get(0);
		}
		return null;
	}
	/**
	 * 得到当前节点的所有子节点
	 * @return List<FactoryChecklist>
	 */
	public List<FactoryChecklist> getChildren(){
		String queryStr = "FROM FactoryChecklist f WHERE f.parent = '" + this.getId() + "'";
		return BaseDAOServcieUtil.findByQueryString(queryStr);
	}
	
	public FactoryCheckResult getFactoryCheckResult(){
		return null;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public FactoryChecklist getParent() {
		return parent;
	}
	public void setParent(FactoryChecklist parent) {
		this.parent = parent;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	public String getCheckItem() {
		return checkItem;
	}
	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}
	public String getCheckContent() {
		return checkContent;
	}
	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}
	public void setChildren(List<FactoryChecklist> children) {
		this.children = children;
	}
	public void setCurrentResult(FactoryCheckResult currentResult) {
		this.currentResult = currentResult;
	}
	public FactoryCheckResult getCurrentResult() {
		return currentResult;
	}
}

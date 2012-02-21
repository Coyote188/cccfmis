package cccf.ma.model;


public class  CommonListModel{
	 
	private int id;
	private String value;
	public CommonListModel(int id,String value)
	{
		this.id=id;
		this.value=value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	 

}

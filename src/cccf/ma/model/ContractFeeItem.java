package cccf.ma.model;

/**
 * 合同收费项目
 */
public class ContractFeeItem {
	private String id;
	private Contract contract;// 合同
	private String itemName;// 项目名称
	private String unit;// 单位
	private double unitPrice;// 单价
	private double quantity;// 数量
	private double quantity_1;// 数量
	private double price;// 价钱 = unitPrice*（quantity+quantity_1）
	private String memo;//备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getQuantity_1() {
		return quantity_1;
	}

	public void setQuantity_1(double quantity_1) {
		this.quantity_1 = quantity_1;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}

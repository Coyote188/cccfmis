package cccf.ma.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EnterpriseProductModel {
	private String id;
	private EnterpriseInfo enterprise;
	private ProductCatalogueInfo product;
	private String model;
	private String name;
	private String checkno;
	private String orgname;
	private String checkby;
	private String report;
	private Date setdate;
	private String checktype, checkfile, tempname, productimage, designfile, otherfile, productname, menuname;

	public String getFrmSetdate() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		if(null == setdate)
			return "";
		return fm.format(setdate);
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getCheckfile() {
		return checkfile;
	}

	public void setCheckfile(String checkfile) {
		this.checkfile = checkfile;
	}

	public String getTempname() {
		return tempname;
	}

	public void setTempname(String tempname) {
		this.tempname = tempname;
	}

	public String getProductimage() {
		return productimage;
	}

	public void setProductimage(String productimage) {
		this.productimage = productimage;
	}

	public String getDesignfile() {
		return designfile;
	}

	public void setDesignfile(String designfile) {
		this.designfile = designfile;
	}

	public String getOtherfile() {
		return otherfile;
	}

	public void setOtherfile(String otherfile) {
		this.otherfile = otherfile;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getCheckby() {
		return checkby;
	}

	public void setCheckby(String checkby) {
		this.checkby = checkby;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public Date getSetdate() {
		return setdate;
	}

	public void setSetdate(Date setdate) {
		this.setdate = setdate;
	}

	public String getChecktype() {
		return checktype;
	}

	public void setChecktype(String checktype) {
		this.checktype = checktype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseInfo enterprise) {
		this.enterprise = enterprise;
	}

	public ProductCatalogueInfo getProduct() {
		return product;
	}

	public void setProduct(ProductCatalogueInfo product) {
		this.product = product;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}

package cccf.ma.model;
/**
 *  申请产品型号
 */
public class ApplicationInfoProductModel {
	private String id;
    private ApplicationInfo applicationInfo;
    private ProductModel productModel;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}
	public void setApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}
	public ProductModel getProductModel() {
		return productModel;
	}
	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}
}

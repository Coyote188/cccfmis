package cccf.ma.model;
/**
 * 产品型号证书 
 */
public class ProductModelCertification {
	private String id;  
	private ProductModel productModel;  //产品型号
	private Certification certification;//证书
	private int status; // 关系状态：0:临时，1:有效
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ProductModel getProductModel() {
		return productModel;
	}
	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}
	public Certification getCertification() {
		return certification;
	}
	public void setCertification(Certification certification) {
		this.certification = certification;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	} 
	
}

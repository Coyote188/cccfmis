package cccf.ma.model;

/**
 *  产品  型号 附件
 *
 */
public class ProductModelAttachment {
	private String id;  
	private ProductModel productModel;
	private Attachment attachment;//产品图片
	private String name;  //附件名称：产品图片、产品设计文件、其它文件
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
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	} 
	
}

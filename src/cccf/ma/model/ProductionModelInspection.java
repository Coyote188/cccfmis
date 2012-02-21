package cccf.ma.model;

public class ProductionModelInspection {
	private String id;
	private ProductionModelInfo model;
	private int inspectionType;
	private String productDesignFile;
	private String productImg;
	private String productCharacterization;
	
	private boolean whetherAppearanceStructureConswhethertent;
	private boolean whetherKeyComponentsConswhethertent;
	private boolean whetherProductionProcessConswhethertent;
	
	private int inspectionResult;
	private String simpleImgs;
	
	
	public String getId() {
		return id;
	}
	public ProductionModelInfo getModel() {
		return model;
	}
	public int getInspectionType() {
		return inspectionType;
	}
	public String getProductDesignFile() {
		return productDesignFile;
	}
	public String getProductImg() {
		return productImg;
	}
	public String getProductCharacterization() {
		return productCharacterization;
	}
	public boolean isWhetherAppearanceStructureConswhethertent() {
		return whetherAppearanceStructureConswhethertent;
	}
	public boolean isWhetherKeyComponentsConswhethertent() {
		return whetherKeyComponentsConswhethertent;
	}
	public boolean isWhetherProductionProcessConswhethertent() {
		return whetherProductionProcessConswhethertent;
	}
	public int getInspectionResult() {
		return inspectionResult;
	}
	public String getSimpleImgs() {
		return simpleImgs;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setModel(ProductionModelInfo model) {
		this.model = model;
	}
	public void setInspectionType(int inspectionType) {
		this.inspectionType = inspectionType;
	}
	public void setProductDesignFile(String productDesignFile) {
		this.productDesignFile = productDesignFile;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	public void setProductCharacterization(String productCharacterization) {
		this.productCharacterization = productCharacterization;
	}
	public void setWhetherAppearanceStructureConswhethertent(
			boolean whetherAppearanceStructureConswhethertent) {
		this.whetherAppearanceStructureConswhethertent = whetherAppearanceStructureConswhethertent;
	}
	public void setWhetherKeyComponentsConswhethertent(
			boolean whetherKeyComponentsConswhethertent) {
		this.whetherKeyComponentsConswhethertent = whetherKeyComponentsConswhethertent;
	}
	public void setWhetherProductionProcessConswhethertent(
			boolean whetherProductionProcessConswhethertent) {
		this.whetherProductionProcessConswhethertent = whetherProductionProcessConswhethertent;
	}
	public void setInspectionResult(int inspectionResult) {
		this.inspectionResult = inspectionResult;
	}
	public void setSimpleImgs(String simpleImgs) {
		this.simpleImgs = simpleImgs;
	}
	
	
}

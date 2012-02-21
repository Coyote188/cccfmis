package cccf.ma.model;

import java.io.File;
import java.util.Date;

import org.zkoss.zk.ui.Executions;

/**
 * 证书信息
 * 
 */
public class Certification {
	
	/**
	 * system Certification file path
	 */
	public static final String path = "/attachments/enterprise_attachment/certification_file/";
	public static final String repoPath = "/attachments/enterprise_attachment/report_file/";
	
	private String id;
	private String applyid;// 申请号
	private String certNumber;// 证书编号
	private String appId; // 申请人ID
	private String appName; // 申请人名称
	private String appAdress; // 申请人地址
	private String productionEnterpriseAdress;// 产生企业地址
	private String productionEnterpriseName;// 产生企业名称
	private String productionEnterpriseId;// 产生企业ID
	private String manufactureAdress;// 造制商地址
	private String manufactureName;// 造制商名称
	private String manufactureId;// 造制商ID
	private String productName;// 产品名称
	private String productModel;// 品产规格型号
	private Date validityBeginDate;// 证书有效期开始时间
	private Date validityEndDate;// 证书有效期结束时间
	private String observedStandard;// 执行标准
	private String technicalRequirement;// 产品标准和技术要求
	private String caRule; //认证规则
	private String systemStandards;//体系标准
	
	private int status; // 证书状态

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyid() {
		return applyid;
	}

	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}

	public String getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppAdress() {
		return appAdress;
	}

	public void setAppAdress(String appAdress) {
		this.appAdress = appAdress;
	}

	public String getProductionEnterpriseAdress() {
		return productionEnterpriseAdress;
	}

	public void setProductionEnterpriseAdress(String productionEnterpriseAdress) {
		this.productionEnterpriseAdress = productionEnterpriseAdress;
	}

	public String getProductionEnterpriseName() {
		return productionEnterpriseName;
	}

	public void setProductionEnterpriseName(String productionEnterpriseName) {
		this.productionEnterpriseName = productionEnterpriseName;
	}

	public String getProductionEnterpriseId() {
		return productionEnterpriseId;
	}

	public void setProductionEnterpriseId(String productionEnterpriseId) {
		this.productionEnterpriseId = productionEnterpriseId;
	}

	public String getManufactureAdress() {
		return manufactureAdress;
	}

	public void setManufactureAdress(String manufactureAdress) {
		this.manufactureAdress = manufactureAdress;
	}

	public String getManufactureName() {
		return manufactureName;
	}

	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}

	public String getManufactureId() {
		return manufactureId;
	}

	public void setManufactureId(String manufactureId) {
		this.manufactureId = manufactureId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public Date getValidityBeginDate() {
		return validityBeginDate;
	}

	public void setValidityBeginDate(Date validityBeginDate) {
		this.validityBeginDate = validityBeginDate;
	}

	public Date getValidityEndDate() {
		return validityEndDate;
	}

	public void setValidityEndDate(Date validityEndDate) {
		this.validityEndDate = validityEndDate;
	}

	public String getObservedStandard() {
		return observedStandard;
	}

	public void setObservedStandard(String observedStandard) {
		this.observedStandard = observedStandard;
	}

	public String getTechnicalRequirement() {
		return technicalRequirement;
	}

	public void setTechnicalRequirement(String technicalRequirement) {
		this.technicalRequirement = technicalRequirement;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCaRule() {
		return caRule;
	}

	public void setCaRule(String caRule) {
		this.caRule = caRule;
	}

	public String getSystemStandards() {
		return systemStandards;
	}

	public void setSystemStandards(String systemStandards) {
		this.systemStandards = systemStandards;
	} 
	
	public static String mkDir(String updir){
		String dirPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(updir);
		boolean creadok = false;
		File dirFile = null;
		try {
			dirFile = new File(dirPath);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				creadok = dirFile.mkdirs();
				if (creadok) {
					System.out.println(" ok:创建文件夹成功！ ");
				} else {
					System.out.println(" err:创建文件夹失败！ ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dirPath;
	}
}

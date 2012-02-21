package cccf.ma.pdfutil;

import java.util.List;
import java.text.DecimalFormat;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.FeeDetailInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.FeeDetailInfoServiceUtil;

public class ApplicationPdfDataBeanImpl implements PdfDataBean {
	private String eterprise_name;
	private String eterprise_postcode;
	private String eterprise_lxr;
	private String eterprise_phone;
	private String eterprise_fax;
	private String eterprise_email;
	private String eterprise_address;
	private String productInfo;
	private String productType;
	// 获证
	private int hz_sq_num;// 申请费
	private float hz_sq_sum;
	private float hz_jc_sum;// 工厂 审查费
	private int hz_jc_num1;// 规定人日数
	private int hz_jc_num2;// 异地人日数
	private int hz_zc_num;// 注册费
	private float hz_zc_sum;
	// 保持
	private float bc_nj_sum;// 年金
	private int bc_nj_num;
	private float bc_jd_sum;// 监督费
	private int bc_jc_num1;// 规定人日数
	private int bc_jc_num2;// 异地人日数
	
	//合同条款5中的选择项
	private String contractChoice1;
	private String contractChoice2;
	private String contractYear;
	private String contractMonth;
	private String contractNo;
	private String ysYear;//预审年
	private String ysMonth;
	

	private String formatStr = "￥###,###,###.##";

	public ApplicationPdfDataBeanImpl(ApplicationInfo application) {
		eterprise_name = application.getEnterprise().getName();
		eterprise_postcode = application.getEnterprise().getPostalcode();
		eterprise_phone = application.getEnterprise().getTelephoneNum();
		eterprise_fax = application.getEnterprise().getFaxNum();
		eterprise_email = application.getEnterprise().getEmailAddress();
		eterprise_address = application.getEnterprise().getContactAddress();

		// 产品
		ProductCatalogueInfo production = application.getProduction();
		productInfo = "名称:" + production.getProductName() + ",执行标准:"
				+ production.getObservedStandard();
		ProductCatalogueInfo parent = application.getProduction()
				.getProductParent();
		if (parent.getProductLevel() == 3) {
			productType = parent.getProductParent().getProductParent()
					.getProductName();
		}
		if (parent.getProductLevel() == 2) {
			productType = parent.getProductParent().getProductName();
		}

		List<FeeDetailInfo> feeList = FeeDetailInfoServiceUtil
				.getFeeListByApplication(application.getId());
		if (feeList != null) {
			for (FeeDetailInfo fee : feeList) {
				if (fee.getAccountItem().getAccountType().getName().equals(
						"获证费")) {
					if (fee.getAccountItem().getName().equals("申请费")) {
						hz_sq_num = fee.getNumber();
						hz_sq_sum = fee.getSum();
					}
					if (fee.getAccountItem().getName().equals("工厂审查费")) {
						hz_jc_sum = fee.getSum();
						hz_jc_num1 = fee.getNumber();
						hz_jc_num2 = fee.getNumber2();
					}
					if (fee.getAccountItem().getName().equals("批准与注册费")) {
						hz_zc_num = fee.getNumber();
						hz_zc_sum = fee.getSum();
					}
				}
				if (fee.getAccountItem().getAccountType().getName().equals(
						"保持费")) {
					if (fee.getAccountItem().getName().equals("年金")) {
						bc_nj_num = fee.getNumber();
						bc_nj_sum = fee.getSum();
					}
					if (fee.getAccountItem().getName().equals("监督费")) {
						bc_jd_sum = fee.getSum();
						bc_jc_num1 = fee.getNumber();
						bc_jc_num2 = fee.getNumber2();
					}
				}
			}
		}
		
		//合同条款5中的选择项
		contractChoice1=application.getContractChoice1();
		contractChoice2=application.getContractChoice2();
		contractYear=application.getContractYear();
		contractMonth=application.getContractMonth();
		contractNo=application.getContractNo();
		ysYear=application.getYsYear();//预审年
		ysMonth=application.getYsMonth();
		
	}

	public String getEterprise_name() {
		return eterprise_name;
	}

	public void setEterprise_name(String eterprise_name) {
		this.eterprise_name = eterprise_name;
	}

	public String getEterprise_zipCode() {
		return eterprise_postcode;
	}

	public void setEterprise_zipCode(String eterprise_zipCode) {
		this.eterprise_postcode = eterprise_zipCode;
	}

	public String getEterprise_lxr() {
		return eterprise_lxr;
	}

	public void setEterprise_lxr(String eterprise_lxr) {
		this.eterprise_lxr = eterprise_lxr;
	}

	public String getEterprise_phone() {
		return eterprise_phone;
	}

	public void setEterprise_phone(String eterprise_phone) {
		this.eterprise_phone = eterprise_phone;
	}

	public String getEterprise_fax() {
		return eterprise_fax;
	}

	public void setEterprise_fax(String eterprise_fax) {
		this.eterprise_fax = eterprise_fax;
	}

	public String getEterprise_email() {
		return eterprise_email;
	}

	public void setEterprise_email(String eterprise_email) {
		this.eterprise_email = eterprise_email;
	}

	public String getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}

	public String getEterprise_address() {
		return eterprise_address;
	}

	public void setEterprise_address(String eterprise_address) {
		this.eterprise_address = eterprise_address;
	}

	public String getEterprise_postcode() {
		return eterprise_postcode;
	}

	public void setEterprise_postcode(String eterprise_postcode) {
		this.eterprise_postcode = eterprise_postcode;
	}

	public String getHz_sq_num() {
		return String.valueOf(hz_sq_num);
	}

	public void setHz_sq_num(int hz_sq_num) {
		this.hz_sq_num = hz_sq_num;

	}

	public String getHz_sq_sum() {
		String sum = new DecimalFormat(formatStr).format(hz_sq_sum);
		return sum;

	}

	public void setHz_sq_sum(float hz_sq_sum) {
		this.hz_sq_sum = hz_sq_sum;
	}

	public String getHz_jc_sum() {
		String sum = new DecimalFormat(formatStr).format(hz_jc_sum);
		return sum;
	}

	public void setHz_jc_sum(float hz_jc_sum) {
		this.hz_jc_sum = hz_jc_sum;
	}

	public String getHz_zc_num() {
		return String.valueOf(hz_zc_num);
	}

	public void setHz_zc_num(int hz_zc_num) {
		this.hz_zc_num = hz_zc_num;
	}

	public String getHz_zc_sum() {
		String sum = new DecimalFormat(formatStr).format(hz_zc_sum);
		return sum;
	}

	public void setHz_zc_sum(float hz_zc_sum) {
		this.hz_zc_sum = hz_zc_sum;
	}

	public String getBc_nj_sum() {
		String sum = new DecimalFormat(formatStr).format(bc_nj_sum);
		return sum;
	}

	public void setBc_nj_sum(float bc_nj_sum) {
		this.bc_nj_sum = bc_nj_sum;
	}

	public String getBc_nj_num() {
		return String.valueOf(bc_nj_num);
	}

	public void setBc_nj_num(int bc_nj_num) {
		this.bc_nj_num = bc_nj_num;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getBc_jd_sum() {
		String sum = new DecimalFormat(formatStr).format(bc_jd_sum);
		return sum;
	}

	public void setBc_jd_sum(float bc_jd_sum) {
		this.bc_jd_sum = bc_jd_sum;
	}

	public String getHz_jc_num1() {
		return String.valueOf(hz_jc_num1);
	}

	public void setHz_jc_num1(int hz_jc_num1) {
		this.hz_jc_num1 = hz_jc_num1;
	}

	public String getHz_jc_num2() {
		return String.valueOf(hz_jc_num2);
	}

	public void setHz_jc_num2(int hz_jc_num2) {
		this.hz_jc_num2 = hz_jc_num2;
	}

	public String getBc_jc_num1() {
		return String.valueOf(bc_jc_num1);
	}

	public void setBc_jc_num1(int bc_jc_num1) {
		this.bc_jc_num1 = bc_jc_num1;
	}

	public String getBc_jc_num2() {
		return String.valueOf(bc_jc_num2);
	}

	public void setBc_jc_num2(int bc_jc_num2) {
		this.bc_jc_num2 = bc_jc_num2;
	}

	public String getContractChoice1() {
		return contractChoice1;
	}

	public void setContractChoice1(String contractChoice1) {
		this.contractChoice1 = contractChoice1;
	}

	public String getContractChoice2() {
		return contractChoice2;
	}

	public void setContractChoice2(String contractChoice2) {
		this.contractChoice2 = contractChoice2;
	}

	public String getContractYear() {
		return contractYear;
	}

	public void setContractYear(String contractYear) {
		this.contractYear = contractYear;
	}

	public String getContractMonth() {
		return contractMonth;
	}

	public void setContractMonth(String contractMonth) {
		this.contractMonth = contractMonth;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getYsYear() {
		return ysYear;
	}

	public void setYsYear(String ysYear) {
		this.ysYear = ysYear;
	}

	public String getYsMonth() {
		return ysMonth;
	}

	public void setYsMonth(String ysMonth) {
		this.ysMonth = ysMonth;
	}

}
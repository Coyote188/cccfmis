package cccf.ma.model;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

public class ProductionModelInfo {
	private String id;
	private String model;
	private String name;
	private String fullReport;
	private String report;
	private String flowChart;//流程图
	private String characterForm;//产品特性描述表
	
	private String productImg;//产品图片
	
	private ApplicationInfo application;
	private String pumperCcc;
	
	private boolean isMain;

	// 用于上传状态显示
	private String fullReportStatus;
	private String reportStatus;
	private String flowChartStatus;
	private String characterFormStatus;
	private String pumperCccFormStatus;
	private String productImgsStatus;

	private String fileStatusName = "已上传";

	public void setId(String value) {
		this.id = value;
	}

	public void setModel(String value) {
		this.model = value;
	}

	public void setName(String value) {
		this.name = value;
	}

	public void setFullReport(String value) {
		this.fullReport = value;
	}

	public void setReport(String value) {
		this.report = value;
	}

	public void setFlowChart(String value) {
		this.flowChart = value;
	}

	public void setCharacterForm(String value) {
		this.characterForm = value;
	}

	public void setApplication(ApplicationInfo value) {
		this.application = value;
	}

	public String getId() {
		return id;
	}

	public String getModel() {
		return model;
	}

	public String getName() {
		return name;
	}

	public String getFullReport() {
		return fullReport;
	}

	public String getReport() {
		return report;
	}

	public String getFlowChart() {
		return flowChart;
	}

	public String getCharacterForm() {
		return characterForm;
	}

	public ApplicationInfo getApplication() {
		return application;
	}

	public String getFullReportStatus() {
		if (fullReport != null && !fullReport.isEmpty())
			fullReportStatus = fileStatusName;
		else
			fullReportStatus = "";
		return fullReportStatus;
	}

	public String getReportStatus() {
		if (report != null && !report.isEmpty())
			reportStatus = fileStatusName;
		else
			reportStatus = "";
		return reportStatus;
	}

	public String getFlowChartStatus() {
		if (flowChart != null && !flowChart.isEmpty())
			flowChartStatus = fileStatusName;
		else
			flowChartStatus = "";
		return flowChartStatus;
	}

	public String getCharacterFormStatus() {
		if (characterForm != null && !characterForm.isEmpty())
			characterFormStatus = fileStatusName;
		else
			characterFormStatus = "";
		return characterFormStatus;
	}
	
	public String getPumperCccFormStatus() {
		if (pumperCcc != null && !pumperCcc.isEmpty())
			pumperCccFormStatus = fileStatusName;
		else
			pumperCccFormStatus = "";
		return pumperCccFormStatus;
	}

	public String getPumperCcc() {
		return pumperCcc;
	}

	public void setPumperCcc(String pumperCcc) {
		this.pumperCcc = pumperCcc;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getProductImg() {
		return productImg;
	}

	public String getProductImgsStatus() {
		if (productImg != null && !productImg.isEmpty())
			productImgsStatus = "已上传多文件";
		else
			productImgsStatus = "";
		return productImgsStatus;
	}

	public void setIsMain(boolean isMain) {
		this.isMain = isMain;
	}

	public boolean getIsMain() {
		return isMain;
	}

}
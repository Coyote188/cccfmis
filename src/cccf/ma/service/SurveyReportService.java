package cccf.ma.service;

import cccf.ma.model.Attachment;
import cccf.ma.model.ProductModel;
import cccf.ma.model.ProductModelAttachment;
import cccf.ma.model.SurveyReport;

public interface SurveyReportService extends BaseService{
	//保存检验报告
	public SurveyReport save(SurveyReport bean)  ;
	//修改检验报告
	public void update(SurveyReport bean) ;
	//修改附件
	public void saveUpdateSurveyReportAttachment(String surveyReportId, Attachment attachment); 
	
	
	//保存规格（样品）
	public ProductModel save(ProductModel bean);
	//修改规格
	public void update(ProductModel bean);
	//保存规格（样品）附件
	public ProductModelAttachment  save(ProductModelAttachment bean);
	//删除规格（样品）附件
	public void delete(ProductModelAttachment bean);
}

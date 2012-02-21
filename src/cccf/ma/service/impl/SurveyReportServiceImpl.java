package cccf.ma.service.impl;

import cccf.ma.model.Attachment;
import cccf.ma.model.ProductModel;
import cccf.ma.model.ProductModelAttachment;
import cccf.ma.model.SurveyReport;
import cccf.ma.service.SurveyReportService;  

public class SurveyReportServiceImpl extends AbstractBaseService implements SurveyReportService {

	@Override
	public SurveyReport save(SurveyReport bean) {
		if(bean.getAttachment()!=null){
			bean.getAttachment().save();
			 
		}
		getHibernateTemplate().save(bean);
		getHibernateTemplate().flush();
		 
	    if(bean.getProductModelList()!=null){
		    for (ProductModel obj : bean.getProductModelList() ){
		    	obj.setEnterpriseInfo(bean.getEnterpriseInfo());
		    	obj.setProductCatalogueInfo(bean.getProductCatalogueInfo());
		    	obj.setProductionEnterpriseInfo(bean.getProductionEnterpriseInfo());
		    	obj.setManufactureInfo(bean.getManufactureInfo());
		    	obj.setSurveyReport(bean);
		    	getHibernateTemplate().save(obj);
		    	getHibernateTemplate().flush();
		    	for( ProductModelAttachment att : obj.getProductModelAttachment() ){ 
		    		att.setProductModel(obj);
		    		if(att.getAttachment()!=null){
				    	 att.getAttachment().persist();
				    	 getHibernateTemplate().save(att); 
				    	 getHibernateTemplate().flush();
		    		}
		    	} 
		    }
	    }
		return bean;
	}

	@Override
	public void update(SurveyReport bean) { 
		getHibernateTemplate().update(bean);
	}

	@Override
	public ProductModel save(ProductModel bean) {
		getHibernateTemplate().save(bean);
		return bean;
	}

	@Override
	public void update(ProductModel bean) {
		getHibernateTemplate().update(bean);
	}

	@Override
	public ProductModelAttachment save(ProductModelAttachment bean) {
		if(bean.getAttachment()!=null){
			bean.getAttachment().persist();
			getHibernateTemplate().save(bean);
		}
		return bean;
	}

	@Override
	public void delete(ProductModelAttachment bean) { 
		bean.getAttachment().remove();
		getHibernateTemplate().delete(bean) ; 
	}

 

	@Override
	public void saveUpdateSurveyReportAttachment(String surveyReportId,Attachment attachment) {
		if(surveyReportId==null)return;
		 SurveyReport sr = (SurveyReport)getHibernateTemplate().load(SurveyReport.class, surveyReportId);
		 if(sr==null)return;
		 if( sr.getAttachment()!=null){
			 sr.getAttachment().remove();
		 }
		 attachment.save();
		 sr.setAttachment(attachment);
		  
		 getHibernateTemplate().update(sr); 
	}

}

package cccf.ma.web.zk.surveyreport;

import java.util.ArrayList;

import javax.smartcardio.ATR;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event; 
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import cccf.ma.model.Attachment;
import cccf.ma.model.ProductModel;
import cccf.ma.model.ProductModelAttachment;
import cccf.ma.service.EnterpriseInfoServiceUtil;

/**
 * 检验样品 
 */
public class ProductModelController extends GenericForwardComposer {
	private static final long serialVersionUID = -3039777696982952204L;
    // 产品名称 :
	private Textbox _name;
	// 规格型号 :
	private Textbox _specification;
	// 特性描述 :
	private Textbox _characterization;
	private Checkbox isMainModel;
	//   产品图片 
	private Textbox  tb_uploadProductPicture; 
	private Attachment uploadProductPicture;
	//   产品设计文件 :
	private Textbox  tb_designDocument; 
	private Attachment designDocument;
	//  其它文件:
	private Textbox  tb_otherDocument; 
	private Attachment otherDocument;

	private Component comp;
	
    // 文件存放路径
	private String path = "/SurveyReport/ProductModelAttachment";
	 
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.comp = comp;   
	} 
	
	public void onUploadProductPicture(ForwardEvent event){
		UploadEvent ent = (UploadEvent) event.getOrigin();
		Media attFile = ent.getMedia(); 
		tb_uploadProductPicture.setText(attFile.getName());
		uploadProductPicture = new Attachment(path,attFile.getName(),attFile); 
	}
	public void onDesignDocument(ForwardEvent event){
		UploadEvent ent = (UploadEvent) event.getOrigin();
		Media attFile = ent.getMedia(); 
		tb_designDocument.setText(attFile.getName());
		designDocument = new Attachment(path,attFile.getName(),attFile); 
	}
	public void onOtherDocument(ForwardEvent event){
		UploadEvent ent = (UploadEvent) event.getOrigin();
		Media attFile = ent.getMedia(); 
		tb_otherDocument.setText(attFile.getName());
		otherDocument = new Attachment(path,attFile.getName(),attFile); 
	}
	
	public void onSave(){ 
		ProductModel bo = new ProductModel(); 
		bo.setSpecification(_specification.getText());
		bo.setCharacterization(_characterization.getText());
		bo.setProductModelAttachment(new ArrayList<ProductModelAttachment>()); 
		bo.setIsMainModel(isMainModel.isChecked());
		
		ProductModelAttachment att= new  ProductModelAttachment();
		att.setProductModel(bo);
		att.setAttachment(uploadProductPicture);
		att.setName("产品图片");
		bo.getProductModelAttachment().add(att);
		
		att= new  ProductModelAttachment();
		att.setProductModel(bo);
		att.setAttachment(designDocument);
		att.setName("产品设计文件 ");
		bo.getProductModelAttachment().add(att);
		
		att= new  ProductModelAttachment();
		att.setProductModel(bo);
		att.setAttachment(otherDocument);
		att.setName("其它文件");
		bo.getProductModelAttachment().add(att); 
		 
		Events.sendEvent(new Event("onSaveProductSample", comp, bo)); 
		
	}

}

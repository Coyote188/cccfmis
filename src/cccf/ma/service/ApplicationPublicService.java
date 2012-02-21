package cccf.ma.service;

import java.util.List;
import java.util.Map;

import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.model.ApplicationPublicInfoAttachment;
import cccf.ma.model.Attachment;

/**
 *  申请处理服务
 */
public interface ApplicationPublicService extends BaseService{
	 
	
	/**
	 * 保存 
	 * @param bean
	 * @param models
	 * @param Attachments
	 */
	public void doSave(ApplicationPublicInfo bean,List<Map> models,List<ApplicationPublicInfoAttachment>Attachments);
	
	/**
	 * 保存并提交
	 * @param bean
	 * @param models
	 * @param Attachments
	 */
	public void doSaveSubmit(ApplicationPublicInfo bean,List<Map> models,List<ApplicationPublicInfoAttachment>Attachments,String userId); 
	 
}
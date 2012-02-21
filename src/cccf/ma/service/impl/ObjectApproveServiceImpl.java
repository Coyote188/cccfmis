package cccf.ma.service.impl;

import java.util.Date; 

import cccf.ma.model.ObjectApproveInfo;
import cccf.ma.service.ObjectApproveService;

public class ObjectApproveServiceImpl extends AbstractBaseService implements ObjectApproveService {

	@Override
	public ObjectApproveInfo save(ObjectApproveInfo obj) {
		if(obj.getObjectId()==null || obj.getObjectId().trim().length()==0){
			throw new RuntimeException("处理对象不能为空！");
		}
		if(obj.getObjectType()==null || obj.getObjectType().trim().length()==0){
			throw new RuntimeException("处理对象类型不能为空！");
		}
		if(obj.getApproverID()==null || obj.getApproverID().trim().length()==0){
			throw new RuntimeException("处理人不能为空！");
		} 
		
		obj.setApproveDate(new Date());
		getHibernateTemplate().save(obj);
		return obj;
	}

	 

}

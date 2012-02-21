package cccf.ma.service;

import cccf.ma.model.ObjectApproveInfo;

/**
 * 对象审核 服务 
 */
public interface ObjectApproveService extends BaseService{
	//新增对象审核
   public ObjectApproveInfo save(ObjectApproveInfo obj);
    
}

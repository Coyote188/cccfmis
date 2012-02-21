package cccf.ma.service;

import java.util.List;
import java.util.Map;

import com.aidi.bpm.model.Approve;

import cccf.ma.model.ObjectApproveInfo;

/**
 * 符合性审核
 *
 */
public interface ConformityReviewService extends BaseService {
	
 
	/**
	 * 产品型号附件 审核保存
	 * @param applyno
	 * @param productModelAttachmentId
	 * @param sn
	 * @param approverID
	 * @param approverName
	 * @param approveResult
	 * @param approveMemo
	 */
	public void doSaveProModAttApprove(String applyno, String productModelAttachmentId ,String approverID, String approverName,String approveResult,String approveMemo);
	 
	/**
	 * 申请附件审核信息保存 
	 * @param applicationPublicInfoAttachmentId
	 * @param sn
	 * @param approverID
	 * @param approverName
	 * @param approveResult
	 * @param approveMemo
	 */
	public void doSaveAppAttApprove(String applyno, String applicationPublicInfoAttachmentId ,String approverID, String approverName,String approveResult,String approveMemo );
	/**
	 *  提交 “符合性审核”
	 * @param applyno  申请号
	 * @param approveResult
	 * @param approveMemo
	 * @param usid
	 * @param fileApprovelist
	 */
    public void doSubmit(String applyno,String approveResult,String approveMemo,String usid,List<ObjectApproveInfo> fileApprovelist);
    
    /**
     * 根据申请号 获取 申请信息
     * @param applyno
     * @return 
     *    申请信息 map{申请号,提交日期,申请类型,业务类型,申请企业,所在地区,企业地址,邮编,法人姓名,法人电话}
     *      申请材料列表list[{序号,文件名,审核状态,意见}]
     *      产品材料列表list[{产品名称,产品大类,质检中心,检验报告,认证规则,执行标准,体系标准,申请企业,制造商,生产厂,生产厂地址
     *                      ,规格型号列表list[{序号,产品名称,规格型号,产品描述,主/分型
     *                                        ,相关文件列表list[{文件名}]
     *                                       }]
     *                    }]
     *    }
     */
    public Map getApplyDatasByNo(String applyno);
}

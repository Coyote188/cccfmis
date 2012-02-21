package openjframework.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import openjframework.model.OrganizationInfo;

public class OrganizationInfoTreeService {

	public static List<OrganizationInfo> getRoot()
	{
		List<OrganizationInfo>root=OrganizationInfoServiceUtil.getByOrganizationLevel(1);
		return root;
	}
	
	public static List<OrganizationInfo> getOrganizationChildren(OrganizationInfo parent) {
		
        Set pcset=parent.getOrganizationChildren();
        List<OrganizationInfo> searchResult=new ArrayList(pcset);
        return searchResult;  
	}
	
	public static int getOrganizationChildrenCount(OrganizationInfo parent)
    {
    	return getOrganizationChildren(parent).size();
    }
    /**
     * 判断节点是否为叶子节点
     * @param node
     * @return
     */
    public static boolean checkIsLeaf(OrganizationInfo node) {
		
		return getOrganizationChildrenCount(node)==0?true:false;
		
	}
}

package openjframework.web.zk;

import java.util.List;

import openjframework.model.OrganizationInfo;
import openjframework.service.OrganizationInfoTreeService;

import org.zkoss.zul.TreeModel;
import org.zkoss.zul.event.TreeDataListener;

public class OrganizationTreeModel implements TreeModel {
	@Override
	public void addTreeDataListener(TreeDataListener arg0) {
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof List) {
			return ((List) parent).get(index);
		} else if (parent instanceof OrganizationInfo) {
			List<OrganizationInfo> productList = OrganizationInfoTreeService
					.getOrganizationChildren((OrganizationInfo) parent);
			return productList.get(index);
		} else
			return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof List) {

			return ((List) parent).size();
		}

		else if (parent instanceof OrganizationInfo) {

			return (int) OrganizationInfoTreeService
					.getOrganizationChildrenCount((OrganizationInfo) parent);
		} else
			return 0;
	}

	@Override
	public int[] getPath(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRoot() {
		return OrganizationInfoTreeService.getRoot();
	}

	@Override
	public boolean isLeaf(Object node) {
		return OrganizationInfoTreeService.checkIsLeaf((OrganizationInfo) node);
	}

	@Override
	public void removeTreeDataListener(TreeDataListener arg0) {
	}

}

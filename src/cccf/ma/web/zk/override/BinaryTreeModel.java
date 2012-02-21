package cccf.ma.web.zk.override;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.AbstractTreeModel;

public class BinaryTreeModel extends AbstractTreeModel{
		private ArrayList _tree =null;
		
		/**
		 * Contructor
		 * @param tree the list is contained all data of nodes.
		 */
		public BinaryTreeModel(List tree){
			super(tree.get(0));
			_tree = (ArrayList)tree;
		}
		
		//-- TreeModel --//
		public Object getChild(Object parent, int index) {
			int i = _tree.indexOf(parent) *2 +1 + index;
			if( i>= _tree.size())
				return null;
			else
				return _tree.get(i);
		}
		
		//-- TreeModel --//
		public int getChildCount(Object parent) {
			int count = 0;
			if(getChild(parent,0) != null)
				count++;
			if(getChild(parent,1) != null)
				count++;
			return count;
		}
		
		//-- TreeModel --//
		public boolean isLeaf(Object node) {
			return (getChildCount(node) == 0);
		}
}

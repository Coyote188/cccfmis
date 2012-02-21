package cccf.ma.web.zk;

import java.util.*;

import openjframework.service.*;
import openjframework.model.*;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

import cccf.ma.service.*;
import cccf.ma.model.*;

public class CommentItemeidtWindow extends Window {

	public CommentItemInfo commentItem;
	Map params = Executions.getCurrent().getArg();
	String userId;
	String processId = "0";
	String rowId;
	UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();	  
	public void onCreate() {
		commentItem = (CommentItemInfo) this.getPage().getVariable(
				"commentItem");

	}

	public void onSave() {
		validateData();
		String cmd = "add";
		String pcmd = (String) params.get("cmd");
		commentItem.setCreatedTime(new Date());
		commentItem.setUser(user);
		if (pcmd != null)
			cmd = pcmd;
		if (cmd.equals("add")) {
			rowId = CommentItemInfoServiceUtil.create(commentItem).toString();
				refreshParentListbox();
			
		} else {
			CommentItemInfoServiceUtil.update(commentItem);
			rowId = commentItem.getId();
			try {
				Messagebox.show("修改成功!", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				refreshParentListbox();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.detach();
	}

	public void refreshParentListbox() {
		Listbox listbox = (Listbox) params.get("parentListbox");
		if (listbox != null) {
			List<CommentItemInfo> list = (List<CommentItemInfo>) listbox
					.getModel();
			if (list != null) {
				list.clear();
				list.addAll(CommentItemInfoServiceUtil.findByUserInfo(user));
			}
		}
	}

	public void onCancel() {
		this.detach();
	}

	void validateData() {
		Textbox title = (Textbox) this.getFellow("title");
		title.getValue();
		Textbox content = (Textbox) this.getFellow("content");
		content.getValue();
	}

}

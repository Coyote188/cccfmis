package openjframework.web.zk;

import java.util.List;

import openjframework.model.PoliticalStatusInfo;
import openjframework.model.ProfileInfo;
import openjframework.model.UserInfo;
import openjframework.service.PoliticalStatusInfoServiceUtil;
import openjframework.service.ProfileInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ModifyProfile extends GenericForwardComposer{
	public List<PoliticalStatusInfo> getPoliticallist() {
		return politicallist;
	}
	public void setPoliticallist(List<PoliticalStatusInfo> politicallist) {
		this.politicallist = politicallist;
	}
	public ProfileInfo getProfile() {
		return profile;
	}
	public void setProfile(ProfileInfo profile) {
		this.profile = profile;
	}
	private static final long serialVersionUID = 1L;
	private ProfileInfo profile;
	private List<PoliticalStatusInfo>politicallist;
	private Combobox politicalCbx;
	private Window modifyProfileWin;
	private Textbox idcardNoTbx,emailTbx;
	private Datebox birthdayDbx;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		politicallist=PoliticalStatusInfoServiceUtil.getAll();
		UserInfo user=UserInfoServiceUtil.getCurrentLoginUser();
		profile=user.getProfile();
		System.out.println("profile:"+profile.getName());
	}
	public void onSelect$politicalCbx()
	{
		PoliticalStatusInfo political=(PoliticalStatusInfo) politicalCbx.getSelectedItem()
				.getAttribute("political");
		profile.setPoliticalStatus(political);
		
	}
	public void onClick$modify() throws InterruptedException
	{
		vilidateData();
		ProfileInfoServiceUtil.update(profile);
		Messagebox.show("信息维护成功!","提示",Messagebox.OK,Messagebox.INFORMATION);
	}
	void vilidateData()
	{
		idcardNoTbx.getValue();
		birthdayDbx.getValue();
		emailTbx.getValue();
			
	}
	public void onClick$cancel()
	{
		Tabpanel tp=(Tabpanel)modifyProfileWin.getParent();
  		Tab tab=tp.getLinkedTab();
  		tab.getTabbox().setSelectedIndex(tp.getIndex()-1);
  		tab.close();
//		modifyProfileWin.detach();
	}

}

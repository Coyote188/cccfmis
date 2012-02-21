package openjframework.web.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import openjframework.model.OrganizationInfo;
import openjframework.model.PoliticalStatusInfo;
import openjframework.model.PositionInfo;
import openjframework.model.ProfileInfo;
import openjframework.model.UserInfo;
import openjframework.myenum.SexEnum;
import openjframework.service.OrganizationInfoServiceUtil;
import openjframework.service.PoliticalStatusInfoServiceUtil;
import openjframework.service.PositionInfoServiceUtil;
import openjframework.service.ProfileInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.TabPanelRefreshForm;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.CommonListModel;

public class RegisterUserComposer extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;
	private ProfileInfo profile;
	private UserInfo account;
	private Window registWin;
	private Textbox usernameTbx,passTbx,affirmTbx,userNoTbx,nameTbx,idcardNoTbx,teleTbx,
				  	emailTbx;
	private Toolbarbutton checkUsernameTol;
	private Button registBtn;
	private Combobox sexCbx,politicalCbx,positionCbx,organizationCbx;
	private Datebox birthdayDbx;
	private List<CommonListModel>sexlist;
	private List<OrganizationInfo>organizationlist;
	private List<PoliticalStatusInfo>politicallist;
	private List<PositionInfo>positionlist;
	private Hbox dynamicHbx;
	Map params=Executions.getCurrent().getArg();
	String isdoModel=null;
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		profile=(ProfileInfo) params.get("profile");
		if(profile==null)
		{
			profile=new ProfileInfo();
		}
		else
			account=profile.getUser();
		if(account==null)
			account=new UserInfo();
		isdoModel=(String) params.get("cmd");
		if(isdoModel!=null)
		{
			dynamicHbx.setVisible(false);
			if(isdoModel.equals("edit"))
			{
				usernameTbx.setDisabled(true);
				passTbx.setDisabled(true);
				affirmTbx.setDisabled(true);
				checkUsernameTol.setDisabled(true);
				registBtn.setLabel("保存修改");
			}
			
		}
		sexlist=new ArrayList<CommonListModel>();
		for(int i=0;i<SexEnum.values().length;i++){
			 CommonListModel model=new CommonListModel(i,SexEnum.values()[i].toString());
			 sexlist.add(model);
		 }
		politicallist=PoliticalStatusInfoServiceUtil.getAll();
		positionlist=PositionInfoServiceUtil.getAll();
		organizationlist=OrganizationInfoServiceUtil.getAll();
		
	}
	
	public void onClick$assignTlb()
	{
		Tabpanel tp=(Tabpanel) registWin.getParent();
		Tabs tbs=tp.getTabbox().getTabs();
		List<Tab> tab = tbs.getChildren();
		int i = -1;
		for (Tab t : tab) {
			if (t.getLabel().equals("人员分配管理")) {
				i = t.getIndex();
				break;
			}
		}
		if(i==-1)
		{
			registWin.detach();
			tp.getLinkedTab().setLabel("人员分配管理");
			Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/organization-manage.zul",null,null);
			objWindow.setParent(tp);
			objWindow.doEmbedded();
		}
		else
			tp.getTabbox().setSelectedIndex(i);
		
		
	}
	
	public void onSelect$politicalCbx()
	{
		PoliticalStatusInfo political=(PoliticalStatusInfo) politicalCbx.getSelectedItem()
				.getAttribute("political");
		profile.setPoliticalStatus(political);
		
	}
	public void onSelect$positionCbx()
	{
		PositionInfo position=(PositionInfo)positionCbx.getSelectedItem()
				.getAttribute("position");
		profile.setPosition(position);
	}
	public void onSelect$organizationCbx()
	{
		OrganizationInfo organization=(OrganizationInfo)organizationCbx.getSelectedItem()
				.getAttribute("organization");
		profile.setOrganization(organization);
	}
	public void onSelect$sexCbx(){
		int sex=(Integer)sexCbx.getSelectedItem().getValue();
		profile.setGender(sex);
	}
	public void onClick$registBtn() throws InterruptedException
	{
		if(isdoModel==null)
		{
			validateData();
			dataBinding();
			UserInfoServiceUtil.createAccount(account, profile);
			Messagebox.show("注册成功!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			//刷新form
			TabPanelRefreshForm.refreshForm(registWin, "/SysForm/SystemAdministrator/registerUser.zul");
		}
		else
		{
			
			if(isdoModel.equals("add")){
				
				dataBinding();
				UserInfoServiceUtil.createAccount(account, profile);
				
			}
			if(isdoModel.equals("edit")){
				
				ProfileInfoServiceUtil.update(profile);
				
			}
			//刷新父窗口;
			Listbox userLbx=(Listbox )registWin.getParent().getFellow("userLbx");
			List<ProfileInfo>resultlist=ProfileInfoServiceUtil.getAll();
			BindingListModelList list=new BindingListModelList(resultlist,true);
			userLbx.setModel(list);
			registWin.detach();
		}
	}
	
	public void onClick$checkUsernameTol()
	{
		if(UserInfoServiceUtil.isExistByUsername(usernameTbx.getValue().trim())){
			checkUsernameTol.setLabel("此用户名已存在");
			checkUsernameTol.setStyle("color:Red;font-weight:bold;");
			checkUsernameTol.setImage("/image/common/close.png");
			registBtn.setDisabled(true);
		}
		else{
			checkUsernameTol.setLabel("此用户名可以注册");
			checkUsernameTol.setStyle("color:Green;font-weight:bold;");
			checkUsernameTol.setImage("/image/16/apply.png");
			registBtn.setDisabled(false);
		}
		
	}
	public void onChanging$usernameTbx()
	{
		checkUsernameTol.setLabel("-检测用户名是否存在-");
		checkUsernameTol.setStyle("color:Blue;font-weight:bold;");
		checkUsernameTol.setImage("/image/common/define_permissions.png");
		registBtn.setDisabled(false);
	}
	private void dataBinding() {
		String password = Encrypter.encrypt(affirmTbx.getValue());
		account.setPassword(password);
		account.setName(nameTbx.getValue());
		account.setStatus(0);
		account.setType(0);
	}
	
	private boolean confirmPassword() {
		return passTbx.getValue().equals(affirmTbx.getValue());
	}
	void validateData() {
		if(UserInfoServiceUtil.isExistByUsername(usernameTbx.getValue().trim()))
		{
			throw new WrongValueException(usernameTbx, "用户名已存在");
		}
		usernameTbx.getValue();
		passTbx.getValue();
		affirmTbx.getValue();
		if (!confirmPassword()) {
			throw new WrongValueException(affirmTbx, "两次输入密码不一致!");
		}
		
		userNoTbx.getValue();
		nameTbx.getValue();
		if(sexCbx.getValue().contains("—"))
			throw new WrongValueException(sexCbx, "请选择一个性别");
		birthdayDbx.getValue();
		idcardNoTbx.getValue();
		teleTbx.getValue();
		emailTbx.getValue();
	}
	public List<CommonListModel> getSexlist() {
		return sexlist;
	}
	public void setSexlist(List<CommonListModel> sexlist) {
		this.sexlist = sexlist;
	}
	public List<OrganizationInfo> getOrganizationlist() {
		return organizationlist;
	}
	public void setOrganizationlist(List<OrganizationInfo> organizationlist) {
		this.organizationlist = organizationlist;
	}
	public List<PoliticalStatusInfo> getPoliticallist() {
		return politicallist;
	}
	public void setPoliticallist(List<PoliticalStatusInfo> politicallist) {
		this.politicallist = politicallist;
	}
	public List<PositionInfo> getPositionlist() {
		return positionlist;
	}
	public void setPositionlist(List<PositionInfo> positionlist) {
		this.positionlist = positionlist;
	}
	public ProfileInfo getProfile() {
		return profile;
	}
	public void setProfile(ProfileInfo profile) {
		this.profile = profile;
	}
	public UserInfo getAccount() {
		return account;
	}
	public void setAccount(UserInfo account) {
		this.account = account;
	}
}

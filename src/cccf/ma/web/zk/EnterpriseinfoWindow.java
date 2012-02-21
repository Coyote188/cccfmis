package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import openjframework.model.MessageInfo;
import openjframework.model.UserInfo;
import openjframework.myenum.MessageEnum;
import openjframework.service.MessageInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;
import openjframework.web.zk.Encrypter;

import org.zkforge.bwcaptcha.Captcha;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.Attachment;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.LocationInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.model.StateInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;
import cccf.ma.service.LocationInfoServiceUtil;
import cccf.ma.service.ManufactureInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.Product_User_ListInfoServiceUtil;
import cccf.ma.service.ProductionEnterpriseInfoServiceUtil;
import cccf.ma.service.StateInfoServiceUtil;
import cccf.myenum.OrganizationNature;

@SuppressWarnings( { "serial", "unused", "unchecked" })
public class EnterpriseinfoWindow extends GenericForwardComposer {

	public final static String onAddObject = "onAddToList";
	public final static String ON_SENDDATA = "onSendData";
	public final static String ON_SENDDATA2 = "onSendData2";
	
	public EnterpriseInfo enterprise;
	public UserInfo user;
	public List<LocationInfo> pList;
	public List<LocationInfo> cList;
	public List<StateInfo> sList;
	public List<ProductCatalogueInfo> productList, selectProList;
	String attachfilePath = "";
	private List<Object> objList;
	private List<ManufactureInfo> manuList;
	private List<ProductionEnterpriseInfo> proList;
	private List<String> organizationNatureArray;
	private List<Attachment> atts = new ArrayList<Attachment>();

	private Map params = Executions.getCurrent().getArg();
	private Window enterpriseCreateWindow,loginWin;
	private Media media, media2;
	private Captcha cap;
	private Textbox capCode, password, password2, userNo, txtFileName,txtFileName2, tbxLocation, userName,tbxObjectName,tbxObjectDetail;
	private Textbox tbx001,tbx002,tbx003,tbx004,tbx005,tbx006,tbx007,tbx008,tbx009,tbx010,tbx011,tbx012,tbx013,tbx014,tbx015,tbx016,tbx017,tbx018,tbx019,tbx020,tbx021,tbx022,tbx023;
	private Button btnSubmit;
	private Combobox cbxProvince, cbxCity, cbxState;
	private Div divCbxLocation;
	private Row rOption;
	private Listbox lbxProductList,lbxAddedManu,lbxAddedFac;
	private Bandbox bdProduct,bdMPList;
	private Combobox cbxObjType,cbxOrganizationNature;
	private Grid grdAddItem;
	private Popup ppAddSuccess;
	private Tabbox tbbxRegister;
	private Radiogroup rgpBusinessScope;

	public void onClick$btnSubmit() throws InterruptedException {

		validateData();
		dataBinding();
		if (EnterpriseInfoServiceUtil.isEnterpriseExist(enterprise.getName()))
			throw new WrongValueException(tbx001, "该企业信息已经存在!");
		if (UserInfoServiceUtil.isExistByUsername(user.getUsername()))
			throw new WrongValueException(userNo, "用户名已经存在，请重新输入!");
		int reply = Messagebox.show("是否确认注册？", "提示", Messagebox.YES
				| Messagebox.NO, Messagebox.QUESTION);
		if (reply == Messagebox.YES) {
			save();
			onSaveSelectedItems();
			onSendActiveInfomations();
			enterpriseCreateWindow.detach();
			Messagebox.show("注册成功，请等待激活！");
			Executions.getCurrent().sendRedirect("login.zul");
			
			//发送消息
			/**
			 * EventQueues.lookup(msg.getReceiveUser().getId() + "msgEventQueue",
					EventQueues.APPLICATION, true).publish(
					new Event("onMsgEventQueue", null, msg));
			 */
			
//			MessageInfo msg = new MessageInfo();/
//			MessageInfoServiceUtil.sendMessageToDesktop(msg);
//			Events.sendEvent(LoginController.onFillText, loginWin, userName.getText().toString());
		}
	}
	
	Window frmERM,frmERF;
	public void onSendData(){
		Events.sendEvent(ManuEditController.ON_SETSAVETOENTERPRISE,frmERM,enterprise);
	}
	public void onSendData2(){
		Events.sendEvent(PEEditController.ON_SETSAVETOENTERPRISE,frmERF,enterprise);
	}
	
	private void onSendActiveInfomations(){
		List<UserInfo> userList = Product_User_ListInfoServiceUtil.findUserByProductList(selectProList);
		if(!userList.isEmpty())
			for(UserInfo user : userList){
				MessageInfo msg = new MessageInfo();
				msg.setReceiveUser(user);
	//			msg.setSendUser(this.user);
				msg.setSendDate(new Date());
				msg.setReadStatus(0);
				msg.setType(MessageEnum.企业激活.ordinal());
				msg.setSubject("企业激活申请通知");
				msg.setContent("新企业注册，请点击企业注册受理查看详细!");
				MessageInfoServiceUtil.sendMessageToDesktop(msg);
			}
	}

	public void onSaveSelectedItems() {
		Set<Listitem> list = lbxProductList.getSelectedItems();
		for (Listitem item : list) {
			ProductCatalogueInfo pro = (ProductCatalogueInfo) item.getValue();
			selectProList.add(pro);
		}
		EnterpriseOwnActivatedProductListInfoServiceUtil.saveProductOfEnterpriseList(selectProList,enterprise);
	}

	private void save() {
		UserInfoServiceUtil.create(user);
		enterprise.setAccount(user);
		// attachfilePath = "/attachments/" + "enterprise" + "/"
		// + enterprise.getName();
			for (Attachment att : atts) {
				att.persist();
			}
			enterprise.setCopyOfBusinessLicense("unused");
			enterprise.setAttachments(atts);
			EnterpriseInfoServiceUtil.create(enterprise);
			if (!manuList.isEmpty())
				for (ManufactureInfo m : manuList) {
					ManufactureInfoServiceUtil.create(m);
				}
			if (!proList.isEmpty())
				for (ProductionEnterpriseInfo p : proList) {
					ProductionEnterpriseInfoServiceUtil.create(p);
				}
			// }
	}

	private void dataBinding() {
		String location = "";
		String password = Encrypter.encrypt(this.password.getValue());
		user.setPassword(password);
		user.setType(1);
		user.setStatus(0);

		if (divCbxLocation.isVisible()) {
			location = cbxProvince.getSelectedItem().getLabel().toString()
					+ cbxCity.getSelectedItem().getLabel().toString();
		} else {
			location = tbxLocation.getText();
		}
		enterprise.setLocation(location);
		enterprise.setState(cbxState.getSelectedItem().getLabel().toString());
	}
	
	public void onCheck$rgpBusinessScope(){
//		System.out.println("on bookmark changed " + rgpBusinessScope.getSelectedItem().getValue() + "--" + rgpBusinessScope.getSelectedIndex());
		enterprise.setScale(rgpBusinessScope.getSelectedIndex());
	}

	public void onSelect$lbxProductList() {
		Set<Listitem> list = lbxProductList.getSelectedItems();
		String pName = "";
		for (Listitem item : list) {
			ProductCatalogueInfo pro = (ProductCatalogueInfo) item.getValue();
			pName = pro.getProductName() + "," + pName;

		}
		bdProduct.setText(pName);
	}

	/**
	 * Cancel this screen
	 * 
	 */
	public void onClick$btnCancel() {
		enterpriseCreateWindow.detach();
	}

	public void onSelect$cbxState() {
		StateInfo state = (StateInfo) cbxState.getSelectedItem().getAttribute(
				"stateInfo");
		if (!state.getName().equals("中国")) {
			tbxLocation.setVisible(true);
			divCbxLocation.setVisible(false);
			rOption.setVisible(false);
		} else {
			tbxLocation.setVisible(false);
			divCbxLocation.setVisible(true);
			pList = LocationInfoServiceUtil.getProvinceList();
			ListModelList pListModel = new ListModelList(pList);
			cbxProvince.setModel(pListModel);
			rOption.setVisible(true);
		}
	}

	public void onSelect$cbxProvince() {
		LocationInfo province = (LocationInfo) cbxProvince.getSelectedItem()
				.getAttribute("province");
		List<LocationInfo> cList = LocationInfoServiceUtil
				.getCityList(province);
		ListModelList cityListModel = new ListModelList(cList);
		cbxCity.setModel(cityListModel);
	}

	public void onAttachmentUpload(ForwardEvent ent){
		UploadEvent event = (UploadEvent) ent.getOrigin();
		media = event.getMedia();
		Attachment att = new Attachment(attachfilePath ,"", media);
		if(ent.getData().equals("BusinessOrganization")){
			txtFileName2.setValue(media.getName());
			att.setName("企业组织机构代码复印件");
			whetherAttachmentOnly("企业组织机构代码复印件");
		}
		else{
			txtFileName.setValue(media.getName());
			att.setName("企业营业执照复印件");
			whetherAttachmentOnly("企业营业执照复印件");
		}
		
		this.atts.add(att);
	}
	
	private void whetherAttachmentOnly(String fileName){
		if(!atts.isEmpty()){
			Iterator it = atts.iterator();
			while (it.hasNext()) {
				Attachment a = (Attachment) it.next();
				if(a.getName().equals(fileName))
					it.remove();
				
			}
		}
	}
	
//	public void onUpload$btnUpload(UploadEvent event)
//			throws InterruptedException {
//		media = event.getMedia();
//		if ((media instanceof org.zkoss.image.Image)
//				|| (media.getFormat().toLowerCase().equals("pdf"))) {
//			txtFileName.setValue(media.getName());
//		} else {
//			Messagebox.show("文件有误: " + media, "Error", Messagebox.OK,
//					Messagebox.ERROR);
//		}
//	}
//
//	public void onUpload$btnUpload2(UploadEvent event)
//			throws InterruptedException {
//		media2 = event.getMedia();
//		if ((media2 instanceof org.zkoss.image.Image)
//				|| (media2.getFormat().toLowerCase().equals("pdf"))) {
//			txtFileName2.setValue(media2.getName());
//		} else {
//			Messagebox.show("文件有误: " + media, "Error", Messagebox.OK,
//					Messagebox.ERROR);
//		}
//	}
	
	public void onRemoveAnObj(ForwardEvent evt){
		Toolbarbutton btn = (Toolbarbutton) evt.getOrigin().getTarget();
		if(evt.getData().toString().equals("removeafac")){
			ProductionEnterpriseInfo p = (ProductionEnterpriseInfo) btn.getAttribute("fac");
			proList.remove(p);
			ListModelList temp = new ListModelList(proList);
			lbxAddedFac.setModel(temp);
		}else if(evt.getData().toString().equals("removeamanu")){
			ManufactureInfo m = (ManufactureInfo) btn.getAttribute("manu");
			manuList.remove(m);
			ListModelList temp = new ListModelList(manuList);
			lbxAddedManu.setModel(temp);
		}
		
		try {
			Messagebox.show("删除成功!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void onClick$btnAddObj(){
		
		String urlStr = "";
		
		if (cbxObjType.getValue().contains("—")) 
			throw new WrongValueException(cbxObjType,"请选择您要添加的信息分类");
		
		int type = Integer.parseInt(cbxObjType.getSelectedItem().getValue().toString());
		if(0==Integer.parseInt(cbxObjType.getSelectedItem().getValue().toString()))
			urlStr = "/SysForm/e-register-fac.zul";
		else
			urlStr = "/SysForm/e-register-manu.zul";
		param.put("enterprise", enterprise);
		
		try{
			Window objWindow = (Window) Executions.createComponents(urlStr, null, param);
			objWindow.setTitle("企业注册");
			objWindow.setClosable(true);
			objWindow.setWidth("800px");
			objWindow.doModal();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//修改页面使用
	Window frmEnterpriseInfoEdit,enterpriseEditWindow;
	public void onClick$btnModify() throws InterruptedException{
		validateData2();
		if(Messagebox.YES == Messagebox.show("是否确认保存修改?","操作提示",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION)){
			EnterpriseInfoServiceUtil.update(enterprise);
			//Events.sendEvent(EnterprisemanageWindow.ON_RELOAD, , null);
			frmEnterpriseInfoEdit.detach();
		}
	}
	public void onClick$btnDetach(){
		frmEnterpriseInfoEdit.detach();
	}
	
	public void onAddToList(Event event){
		
		Object obj = event.getData();
		boolean isDone = false;
		if (obj instanceof ProductionEnterpriseInfo) {
			ProductionEnterpriseInfo pro = (ProductionEnterpriseInfo) obj;
			pro.setEnterprise(enterprise);
			pro.setStatus(0);
			if (!isAddObjToListDone(obj))
				throw new WrongValueException(lbxAddedFac,"生产企业已经存在!");
			else{
				ListModelList proModel = new ListModelList(proList);
				lbxAddedFac.setModel(proModel);
				System.out.println("Manufacture---------->"+proModel.getSize());
			}	
		}else {
			ManufactureInfo manu = (ManufactureInfo) obj;
			manu.setEnterprise(enterprise);
			manu.setStatus(0);
			if(!isAddObjToListDone(obj))
				throw new WrongValueException(lbxAddedManu,"制造商已经存在!");
			else{
				ListModelList manuModel = new ListModelList(manuList);
				lbxAddedManu.setModel(manuModel);
				System.out.println("ProductionEnterprise---------->"+manuModel.getSize());
			}
		}
		
//		if(isDone){
//			ListModelList model = new ListModelList(objList);
//			grdAddItem.setModel(model);
			try{
				Messagebox.show("添加成功！","",Messagebox.OK,Messagebox.INFORMATION);
			}catch(Exception e){
				e.printStackTrace();
			}
//		}
	}
	
//	private boolean isExist(Object obj) {
//		boolean isExist = false;
//		for (Object object : objList) {
//			if ((obj instanceof ManufactureInfo) && (object instanceof ManufactureInfo)) {
//				if (((ManufactureInfo) obj).getName().equals(((ManufactureInfo) object).getName()))
//					isExist = true;
//			}else if ((obj instanceof ProductionEnterpriseInfo) && (object instanceof ProductionEnterpriseInfo)) {
//				if (((ProductionEnterpriseInfo) obj).getName().equals(((ProductionEnterpriseInfo) object).getName()))
//					isExist = true;
//			}
//		}
//		return isExist;
//	}
	
//	private void addObjectToList(Object obj){
//		objList.add(obj);
//		ListModelList model = new ListModelList(objList);
//		grdAddItem.setRowRenderer(new GrdRanderer());
//		grdAddItem.setModel(model);
//	}
//	
	private boolean isAddObjToListDone(Object obj){
		boolean isExist = true;
		if (obj instanceof ManufactureInfo) {
			if (manuList.isEmpty()) {
				manuList.add((ManufactureInfo) obj);
				isExist = false;
			} else {
				for (ManufactureInfo m : manuList) {
					if (!m.getName().equals(((ManufactureInfo) obj).getName())) {
						isExist = false;
						break;
					}
				}
				if (!isExist)
					manuList.add((ManufactureInfo) obj);
			}
		}
		else {
			if (proList.isEmpty()) {
				proList.add((ProductionEnterpriseInfo) obj);
				isExist = false;
			} else {
				for (ProductionEnterpriseInfo p : proList) {
					if (!p.getName().equals(
							((ProductionEnterpriseInfo) obj).getName())) {
						isExist = false;
						break;
					}
				}
				if (!isExist)
					proList.add((ProductionEnterpriseInfo) obj);
			}
		}
		return !isExist;
	}

	boolean verifyCaptcha() {
		boolean is = false;
		if (!cap.getValue().equalsIgnoreCase(capCode.getValue())) {
			throw new WrongValueException(capCode, "验证码输入错误!");
		} else {
			is = true;
		}
		return is;
	}

	private boolean confirmPassword() {
		return password.getValue().equals(password2.getValue());
	}

	void validateData() {
		tbx001.getValue();
		tbx002.getValue();
		bdProduct.getValue();
		if (cbxState.getValue().contains("—")) {
			throw new WrongValueException(cbxState, "请选择国家！");
		}
		if (divCbxLocation.isVisible()) {
			if (cbxProvince.getValue().contains("—")
					|| cbxCity.getValue().contains("—")) {
				throw new WrongValueException(cbxCity, "请选择省和市！");
			}
		} else {
			tbxLocation.getValue();
			InputValidateUtil.validate(InputValidateUtil.LENGTH_VARCHAR,tbxLocation);
		}
		tbx003.getValue();
		cbxOrganizationNature.getValue();
		tbx004.getValue();
		tbx005.getValue();
		tbx006.getValue();
		tbx007.getValue();
		tbx008.getValue();
		tbx009.getValue();
		tbx010.getValue();
		tbx011.getValue();
		tbx012.getValue();
		tbx013.getValue();
		tbx014.getValue();
		tbx015.getValue();
		tbx016.getValue();
		tbx017.getValue();
		tbx018.getValue();
		tbx019.getValue();
		tbx020.getValue();
		tbx021.getValue();
		tbx022.getValue();
		tbx023.getValue();
		
		txtFileName.getValue();
		if (rOption.isVisible()) {
			txtFileName2.getValue();
		}
		userNo.getValue();
		InputValidateUtil.validate(InputValidateUtil.LENGTH_USERNAME,userNo);
		password.getValue();
		InputValidateUtil.validate(InputValidateUtil.LENGTH_PASSWORD,password);
		password2.getValue();
		if (!confirmPassword()) {
			throw new WrongValueException(password2, "两次输入密码不一致!");
		}
		userName.getValue();
		InputValidateUtil.validate(InputValidateUtil.LENGTH_NAME,userName);
		verifyCaptcha();
		if (manuList.isEmpty() || proList.isEmpty()) {
			throw new WrongValueException(tbbxRegister.getSelectedTab(),"请添加至少待激活的生产企业和至少一个制造商!");
		}

	}
	
	void validateData2() {
		tbx001.getValue();
		tbx002.getValue();
		tbx003.getValue();
		cbxOrganizationNature.getValue();
		tbx004.getValue();
		tbx005.getValue();
		tbx006.getValue();
		tbx007.getValue();
		tbx008.getValue();
		tbx009.getValue();
		tbx010.getValue();
		tbx011.getValue();
		tbx012.getValue();
		tbx013.getValue();
		tbx014.getValue();
		tbx015.getValue();
		tbx016.getValue();
		tbx017.getValue();
		tbx018.getValue();
		tbx019.getValue();
		tbx020.getValue();
		tbx021.getValue();
		tbx022.getValue();
		tbx023.getValue();
	}

	private void changeCap() {
		char[] capChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z' };
		cap.setBgColor(0xffffff);
		cap.setCaptchars(capChars);
		cap.setLineColor(0xccffcc);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if(null != params.get("enterprise"))
			this.enterprise = (EnterpriseInfo) params.get("enterprise");
		else{
			enterprise = new EnterpriseInfo();
			changeCap();
			objList = new ArrayList<Object>();
			user = new UserInfo();
			
			selectProList = new ArrayList<ProductCatalogueInfo>();
			sList = StateInfoServiceUtil.getAll();
			pList = LocationInfoServiceUtil.getProvinceList();
			productList = ProductCatalogueInfoServiceUtil.getLeafData();
			manuList = new ArrayList<ManufactureInfo>();
			proList = new ArrayList<ProductionEnterpriseInfo>();
			attachfilePath = "/attachments/" + "enterprise_attachment" + "/" + UUID.randomUUID().toString();
			init();
		}
//		ListModelList manuModel = new ListModelList(manuList);
//		lbxAddedManu.setModel(manuModel);
//		ListModelList proModel = new ListModelList(proList);
//		lbxAddedFac.setModel(proModel);
	}
	
	public void init(){
		organizationNatureArray = new ArrayList<String>();
		OrganizationNature[] org = OrganizationNature.values();
		for(OrganizationNature str : org){
			organizationNatureArray.add(str.toString());
		}
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseInfo enterprise) {
		this.enterprise = enterprise;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public List<StateInfo> getSList() {
		return sList;
	}

	public void setSList(List<StateInfo> list) {
		sList = list;
	}

	public List<ProductCatalogueInfo> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductCatalogueInfo> productList) {
		this.productList = productList;
	}
	
	private class GrdRanderer implements RowRenderer{

		@Override
		public void render(Row row, Object data) throws Exception {
			final Object obj = data;
			final Label lblType = new Label();
			lblType.setParent(row);
			final Label lblObjName = new Label();
			lblObjName.setParent(row);
			final Label lblObjDetail = new Label();
			lblObjDetail.setParent(row);
			if (obj instanceof ManufactureInfo) {
				lblType.setValue(ObjectType.制造商.toString());
				lblObjName.setValue(((ManufactureInfo) obj).getName());
				lblObjDetail.setValue(((ManufactureInfo) obj).getContactAddress());
			}else {
				lblType.setValue(ObjectType.生产企业.toString());
				lblObjName.setValue(((ProductionEnterpriseInfo) obj).getName());
				lblObjDetail.setValue(((ProductionEnterpriseInfo) obj).getContactAddress());
			}
			final Toolbarbutton btnRemove = new Toolbarbutton("删除");
			btnRemove.addEventListener(Events.ON_CLICK, new EventListener(){

				@Override
				public void onEvent(Event arg0) throws Exception {
					onRemove(obj);
				}
				
			});
			btnRemove.setParent(row);
		}
		
	}
	
	private void onRemove(Object obj){
		Iterator it = objList.iterator();
		while(it.hasNext()){
			Object temp = it.next();
			if (temp.equals(obj)) {
				it.remove();
			}
		}
		ListModelList model = new ListModelList(objList);
		grdAddItem.setModel(model);
	}
	
	private enum ObjectType{
		生产企业,制造商
	}

	public List<ManufactureInfo> getManuList() {
		return manuList;
	}

	public void setManuList(List<ManufactureInfo> manuList) {
		this.manuList = manuList;
	}

	public List<ProductionEnterpriseInfo> getProList() {
		return proList;
	}

	public void setProList(List<ProductionEnterpriseInfo> proList) {
		this.proList = proList;
	}

	public List<String> getOrganizationNatureArray() {
		return organizationNatureArray;
	}

	public void setOrganizationNatureArray(List<String> organizationNatureArray) {
		this.organizationNatureArray = organizationNatureArray;
	}
}

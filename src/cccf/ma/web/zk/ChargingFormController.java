package cccf.ma.web.zk;

import java.math.BigDecimal;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cccf.ma.model.AccountItemInfo;
import cccf.ma.model.AccountTypeInfo;
import cccf.ma.model.FeeDetailInfo;
import cccf.ma.service.AccountItemInfoServiceUtil;
import cccf.ma.service.AccountTypeInfoServiceUtil;
import cccf.ma.service.FeeDetailInfoServiceUtil;

public class ChargingFormController extends GenericForwardComposer{
	private static final long serialVersionUID = 1L;
	private Groupbox feeGroup;
	private Grid feeGrid;
	private Label amountFee;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		generateFeeForm();
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		// TODO Auto-generated method stub
		return super.doBeforeCompose(page, parent, compInfo);
	}
	@SuppressWarnings("unchecked")
	public void generateFeeForm() {
		Rows rows = feeGrid.getRows();
		// 类别
		List<AccountTypeInfo> accountTypeList = AccountTypeInfoServiceUtil.getAll();
		for (AccountTypeInfo accountType : accountTypeList) {
			List<AccountItemInfo> accountItemList = AccountItemInfoServiceUtil.getItemListByType(accountType.getAtypeId());
			if (accountItemList != null)
				for (int i = 0; i < accountItemList.size(); i++) {
					Row rowType = new Row();
					rowType.setParent(rows);
					if (i == 0) {
						Cell cell = new Cell();
						cell.setRowspan(accountItemList.size());
						cell.setParent(rowType);
						Label label1 = new Label(accountType.getName());
						label1.setParent(cell);
						Cell cell2 = new Cell();
						cell2.setParent(rowType);
						Label label2 = new Label(accountItemList.get(i).getName());
						label2.setParent(cell2);
					} else {
						Cell cell = new Cell();
						cell.setParent(rowType);
						Label label1 = new Label(accountItemList.get(i).getName());
						label1.setParent(cell);
					}
					// 单位
					Cell cellUnit = new Cell();
					cellUnit.setAlign("center");
					cellUnit.setParent(rowType);
					Label unitlab = new Label();
					unitlab.setWidth("80px");
					unitlab.setId("unitbox_" + accountItemList.get(i).getAccountItemId());
					unitlab.setParent(cellUnit);
					unitlab.setValue(accountItemList.get(i).getUnit());
					// 数量
					Cell cellNumber = new Cell();
					cellNumber.setAlign("left");
					cellNumber.setParent(rowType);
					if (accountItemList.get(i).getName().equals("工厂审查费") || accountItemList.get(i).getName().equals("监督费")) {
						Hbox hbox = new Hbox();
						hbox.setParent(cellNumber);
						Label l1 = new Label("规定人日");
						l1.setStyle("vertical-align: middle");
						l1.setParent(hbox);
						Intbox numberbox = new Intbox();
						numberbox.setWidth("40px");
						numberbox.setValue(0);
						numberbox.setId("numberbox_" + accountItemList.get(i).getAccountItemId());
						numberbox.setParent(hbox);
						Label l2 = new Label("异地人日");
						l2.setStyle("vertical-align: middle");
						l2.setParent(hbox);
						Intbox numberbox2 = new Intbox();
						numberbox2.setWidth("40px");
						numberbox2.setValue(0);
						numberbox2.setId("numberbox2_" + accountItemList.get(i).getAccountItemId());
						numberbox2.setParent(hbox);
					} else {
						Intbox numberbox = new Intbox();
						numberbox.setWidth("200px");
						numberbox.setValue(0);
						numberbox.setId("numberbox_" + accountItemList.get(i).getAccountItemId());
						numberbox.setParent(cellNumber);
					}
					// 金额
					Cell cell3 = new Cell();
					cell3.setAlign("left");
					cell3.setParent(rowType);
					Decimalbox feebox = new Decimalbox();
					feebox.setWidth("150px");
					feebox.setFormat("#,###.##");
					feebox.setValue(BigDecimal.valueOf(0));
					feebox.addEventListener(Events.ON_CHANGE,new EventListener(){

						@Override
						public void onEvent(Event arg0) throws Exception {
							onSumFee();// 计算合计金额
						}
						
					});
					feebox.setId("feebox_" + accountItemList.get(i).getAccountItemId());
					feebox.setParent(cell3);
				}
		}
	}
	// 计算合计金额
	public void onSumFee() {
		float sum = 0;
		List<AccountItemInfo> accountItemList = AccountItemInfoServiceUtil.getAll();
		if (accountItemList != null)
			for (int i = 0; i < accountItemList.size(); i++) {
				Decimalbox feebox = (Decimalbox) feeGroup.getFellow("feebox_" + accountItemList.get(i).getAccountItemId());
				if (feebox != null) {
					if (feebox.getValue() != null)
						sum = sum + feebox.getValue().floatValue();
				}
			}
		amountFee.setValue(String.valueOf(sum));
	}
	public void onSave()
	{
		
	}
}

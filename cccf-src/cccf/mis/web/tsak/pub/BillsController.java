package cccf.mis.web.tsak.pub;

import java.math.BigDecimal;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Decimalbox;

public class BillsController extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	private Grid billGrid;
	private Decimalbox r1_c1,r1_c2,r1_c3;
	private Decimalbox r2_c1,r2_c2,r2_c3,r2_c4;
	private Decimalbox r3_c1,r3_c2,r3_c3;
	private Decimalbox r4_c1,r4_c2,r4_c3;
	private Decimalbox r5_c1,r5_c2,r5_c3,r5_c4;
	private Decimalbox r6_c1,r6_c2,r6_c3,r6_c4;
	private Decimalbox r7_c1,r7_c2,r7_c3;
	private Decimalbox r8_c1,r8_c2,r8_c3;
	private Decimalbox r9_c2,r9_c3,r9_c4,r10_c1;
	private Textbox r1_c4,r2_c5,r3_c4,c4_r4,r5_c5,r6_c5,r7_c4,r8_c4,r9_c1,r9_c5;
	
	private Map<?, ?>				params				= Executions.getCurrent().getArg();
	
	public BigDecimal getSubValue(Decimalbox box1,Decimalbox box2,Decimalbox box3){
		int total=0;
			if (box2.getValue()==null) {
				total=box1.getValue().intValue()*box3.getValue().intValue();
			}else if (box3.getValue()==null) {
				total=box1.getValue().intValue()*box2.getValue().intValue();
			}else{
				total=box1.getValue().intValue()*(box2.getValue().intValue()+box3.getValue().intValue());
			}
			
		return new BigDecimal(total);
	}
	public BigDecimal getSubValue(Decimalbox box1,Decimalbox box2){
		int total=box1.getValue().intValue()*box2.getValue().intValue();
		return new BigDecimal(total);
	}
	public BigDecimal setTotal(){
		  int total=getValue(r1_c3)+getValue(r2_c4)+getValue(r3_c3)+getValue(r4_c3)+getValue(r5_c4)+getValue(r6_c4)+getValue(r7_c3)+getValue(r8_c3)+getValue(r9_c4);
		  return new BigDecimal(total);
	}
	
	public int getValue(Decimalbox box){
		int theValue=0;
		if (box.getValue()!=null) {
			theValue= box.getValue().intValue();
		}
		return theValue;
	}
	
	public void onChanged(ForwardEvent event){
		if (r1_c2.getValue()!=null&& r1_c1.getValue()!=null) {
			r1_c3.setValue(getSubValue(r1_c1, r1_c2));
		}
		if (r2_c1.getValue()!=null && (r2_c2.getValue()!=null || r2_c3.getValue()!=null)) {
			r2_c4.setValue(getSubValue(r2_c1, r2_c2, r2_c3));
		}
		if (r3_c1.getValue()!=null&& r3_c2.getValue()!=null) {
			r3_c3.setValue(getSubValue(r3_c1, r3_c2));
		}
		if (r4_c1.getValue()!=null&& r4_c2.getValue()!=null) {
			r4_c3.setValue(getSubValue(r4_c1, r4_c2));
		}
		if (r5_c1.getValue()!=null && (r5_c2.getValue()!=null || r5_c3.getValue()!=null)) {
			r5_c4.setValue(getSubValue(r5_c1, r5_c2, r5_c3));
		}
		if (r6_c1.getValue()!=null && (r6_c2.getValue()!=null || r6_c3.getValue()!=null)) {
			r6_c4.setValue(getSubValue(r6_c1, r6_c2, r6_c3));
		}
		if (r7_c1.getValue()!=null&& r7_c2.getValue()!=null) {
			r7_c3.setValue(getSubValue(r7_c1, r7_c2));
		}
		if (r8_c1.getValue()!=null&& r8_c2.getValue()!=null) {
			r8_c3.setValue(getSubValue(r8_c1, r8_c2));
		}
		if (r9_c2.getValue()!=null&& r9_c3.getValue()!=null) {
			r9_c4.setValue(getSubValue(r9_c2, r9_c3));
		}
		r10_c1.setValue(setTotal());
	}
}

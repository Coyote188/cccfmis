package cccfmis.bpm.zk;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;

public class FactoryData extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<?, ?> params = Executions.getCurrent().getArg();
	ApplicationInfo applicationInfo;
	Listbox factorydata;

	public void onCreate() {
		applicationInfo = (ApplicationInfo) this.getPage().getAttribute("inspectionApplication");
		factorydata = (Listbox) getFellow("factorydata");
		if (applicationInfo != null) {
			try {
				DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
				JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_factorycheck", " where Aid='" + applicationInfo.getId() + "'");
				List<Map<String, Object>> factorycheck = adapter.load();
				Iterator<Map<String, Object>> iterator = factorycheck.iterator();
				while (iterator.hasNext()) {
					Map<String, Object> map = iterator.next();
					Listitem listitem = new Listitem();
					listitem.setValue(map.get("id"));
					listitem.setId(map.get("id").toString());

					Listcell filetype = new Listcell();
					filetype.setLabel(map.get("filetype").toString());

					Listcell filename = new Listcell();
					filename.setLabel(map.get("filename").toString());

					Listcell filepath = new Listcell();
					filepath.setValue(map.get("filepath").toString());
					Toolbarbutton toolshowbtn = new Toolbarbutton("查看文件");
					toolshowbtn.setTooltiptext("点击查看文件");
					Component comp = null;
					toolshowbtn.addForward(Events.ON_CLICK, comp, "onOpenFile", map.get("filepath"));
					filepath.appendChild(toolshowbtn);

					listitem.appendChild(filetype);
					listitem.appendChild(filename);
					listitem.appendChild(filepath);

					factorydata.appendChild(listitem);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
	}

	public void onOpenFile(ForwardEvent event) throws InterruptedException {
		String fielpath = event.getData().toString();
		Window seefile = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, null);
		Iframe iframe = (Iframe) seefile.getFellow("ifrme");
		iframe.setSrc(fielpath);
		seefile.doModal();
	}
}

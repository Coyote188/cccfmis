package cccf.ma.service;

import java.math.*;
import java.util.*;
import java.sql.*;
import java.io.Serializable;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cccf.ma.model.*;

import com.aidi.core.web.framework.ServiceAdapter;
import com.aidi.core.spring.CustomerContextHolder;

public class AttachFileMemoInfoServiceUtil {
	public static Serializable create(AttachFileMemoInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		return service.create(bean);
	}

	public static void delete(AttachFileMemoInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		service.delete(bean);
	}

	public static void saveOrUpdate(AttachFileMemoInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		service.saveOrUpdate(bean);
	}

	public static void update(AttachFileMemoInfo bean) {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		service.update(bean);
	}

	public static List getAll() {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		return service.getAll();
	}

	public static AttachFileMemoInfo getById(String id) {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		return service.getById(id);
	}

	public static AttachFileMemoInfo getByPrimaryKey(String key) {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		return service.getByPrimaryKey(key);
	}

	public static List findByQuery(String querystr) {
		CustomerContextHolder.setCustomerType("cccf");
		AttachFileMemoInfoService service = (AttachFileMemoInfoService) ServiceAdapter
				.getServiceByName("AttachFileMemoInfoService");
		return service.findByQuery(querystr);
	}

	/**
	 * 功能:保存申请中的每个附件的审核意见
	 * 
	 * @param grid
	 * @param aid
	 * @param tid
	 * @param modelId
	 */
	public static void saveAttachFileMemo(Grid grid, String aid, Long tid,
			String modelId) {

		for (Object obj : grid.getRows().getChildren()) {
			Row row = (Row) obj;
			for (Object o : row.getChildren()) {
				Component c = (Component) o;
				if (c.getId().indexOf("memo") >= 0) {
					if (!((Textbox) c).getValue().isEmpty()) {
						AttachFileMemoInfo mo = new AttachFileMemoInfo();
						mo.setApplicationId(aid);
						mo.setFieldName(c.getId().substring(5));
						mo.setMemo(((Textbox) c).getValue());
						mo.setModelId(modelId);
						mo.setTaskInstanceId(tid);

						create(mo);
					}
				}
			}
		}
	}

	/**
	 * 取得指定工作流实例ID对应的附件意见
	 * 
	 * @param tid
	 *            :taskInstanceId
	 * @return HashMap方式
	 */
	public static Map getMemoByTaskId(Long tid) {

		Map memoMap = null;
		String sql = "from AttachFileMemoInfo where taskInstanceId=" + tid;
		List<AttachFileMemoInfo> list = findByQuery(sql);
		if (list != null) {
			memoMap = new HashMap();
			for (AttachFileMemoInfo mo : list) {
				memoMap.put(mo.getFieldName(), mo.getMemo());
			}
		}

		return memoMap;

	}

	/**
	 * 保存产品型号附件的意见
	 * 
	 * @param listbox
	 * @param aid
	 * @param tid
	 */
	public static void saveModelAttachFileMemo(Listbox listbox, String aid,
			Long tid) {

		for (Object obj : listbox.getItems()) {
			Listitem row = (Listitem) obj;
			String modelId = row.getValue().toString();
			for (Object o : row.getChildren()) {
				Component cell = (Component) o;
				for (Object co : cell.getChildren()) {
					Component v = (Component) co;
					for (Object vo : v.getChildren()) {
						Component c = (Component) vo;
						int k = c.getId().indexOf("memo");
						if (k >= 0) {
							if (!((Textbox) c).getValue().isEmpty()) {
								AttachFileMemoInfo mo = new AttachFileMemoInfo();
								mo.setApplicationId(aid);
								mo.setFieldName(c.getId().substring(k + 5));
								mo.setMemo(((Textbox) c).getValue());
								mo.setModelId(modelId);
								mo.setTaskInstanceId(tid);

								create(mo);
							}
						}
					}
				}
			}
		}
	}

}

package cccf.ma.common;
 

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

public class CommomsController {
   public static void pagingListboxBinder(Paging listPaging,final Listbox listbox, String hqlForTotalSize,final String hqlForPage){ 
		Long maxCount_ =(Long) CommomsDAO.querySingleResult(hqlForTotalSize); ;//分页控件上显示的总页数 
		int maxCount = maxCount_.intValue(); 
		final int PAGE_SIZE = listPaging.getPageSize();
		listPaging.setTotalSize( maxCount); 
		listPaging.setActivePage(0);
		listbox.setModel(new ListModelList(CommomsDAO.getListForPage(hqlForPage, 0, PAGE_SIZE)));
	     // 注册onpaging事件 
		listPaging.addEventListener("onPaging", new EventListener() {
           public void onEvent(Event event) { 
               PagingEvent pe = (PagingEvent) event;
               int pgno = pe.getActivePage();// 页数(从零计算)
               int start = pgno * PAGE_SIZE; 
               listbox.setModel(new ListModelList(CommomsDAO.getListForPage(hqlForPage, start, PAGE_SIZE))); 
           }
       }); 
   }
}

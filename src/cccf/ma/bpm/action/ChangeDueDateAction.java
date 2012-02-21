package cccf.ma.bpm.action;

import java.util.Date;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.job.Timer;
import org.jbpm.scheduler.SchedulerService;
import org.jbpm.svc.Services;
import org.jbpm.taskmgmt.def.Task;

import org.jbpm.calendar.BusinessCalendar;
import org.jbpm.calendar.Duration;

import com.aidi.bpm.service.TaskFormServiceUtil;

public class ChangeDueDateAction implements ActionHandler {

	private static final long serialVersionUID = 1L;

	static BusinessCalendar businessCalendar = new BusinessCalendar();

	public void execute(ExecutionContext cxt) throws Exception {
		Timer timer = cxt.getTimer();

		Task task = cxt.getTask();
		if (task != null) {

			String durationString = TaskFormServiceUtil
					.getDurationByTaskId(task.getId());//到得自定义的定时器时间
			if (durationString != null) {
				if (durationString.length() > 0) {
					Duration duration = new Duration(durationString);
					Date dueDate = businessCalendar.add(new Date(), duration);
					if (timer != null) {
						timer.setDueDate(dueDate);
						SchedulerService schedulerService = (SchedulerService) Services
								.getCurrentService(Services.SERVICENAME_SCHEDULER);
						schedulerService.createTimer(timer);
						
					}
				}
			}
			cxt.getTaskInstance().setDueDate(timer.getDueDate());
		}
	}

}

package es.grammata.evaluation.evs.scheduling;

import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;

/**
 * @author ajmedialdea
 *
 */
public abstract class AbstractSchedulableTask implements Schedulable {

    private Runnable task;

    private TaskScheduler scheduler;

    private ScheduledFuture scheduledFuture;

    @Override
    public void cancel() {
	if (scheduledFuture != null) {
	    scheduledFuture.cancel(true);
	}
    }

    @Override
    public boolean isScheduled() {
	if (scheduledFuture != null) {
	    return !(scheduledFuture.isCancelled() || scheduledFuture.isDone());
	}
	return false;
    }

    public Runnable getTask() {
	return task;
    }

    public void setTask(Runnable task) {
	this.task = task;
    }

    public TaskScheduler getScheduler() {
	return scheduler;
    }

    public void setScheduler(TaskScheduler scheduler) {
	this.scheduler = scheduler;
    }

    public ScheduledFuture getScheduledFuture() {
	return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("AbstractSchedulableTask [task=");
	builder.append(task);
	builder.append(", scheduler=");
	builder.append(scheduler);
	builder.append(", scheduledFuture=");
	builder.append(scheduledFuture);
	builder.append("]");
	return builder.toString();
    }

}

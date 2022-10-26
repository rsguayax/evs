package es.grammata.evaluation.evs.scheduling;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

/**
 * @author ajmedialdea
 *
 */
public class SchedulableTask extends AbstractSchedulableTask {

    private Trigger trigger;

    public SchedulableTask(Runnable task, TaskScheduler scheduler, Trigger trigger) {
	setTask(task);
	setScheduler(scheduler);
	this.trigger = trigger;
    }

    @Override
    public void schedule() {
	setScheduledFuture(getScheduler().schedule(getTask(), trigger));
    }

}

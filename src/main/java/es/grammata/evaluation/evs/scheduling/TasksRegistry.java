package es.grammata.evaluation.evs.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author ajmedialdea
 *
 */
public class TasksRegistry {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static Map<String, SchedulableTask> registry = new HashMap<String, SchedulableTask>();

    public void register(Long evaluationEventId, SchedulableTask task) {
	task.schedule();
	String key = task.getTask().getClass().getSimpleName().toLowerCase()
		+ "_" + String.valueOf(evaluationEventId);
	SchedulableTask previousTask = registry.put(key, task);
	if (previousTask != null && previousTask.isScheduled()) {
	    previousTask.cancel();
	}
    }

    public String toJSONString() {
	JSONArray ja = new JSONArray();
	for (Map.Entry<String, SchedulableTask> entry : registry.entrySet()) {
	    String id = entry.getKey();
	    SchedulableTask task = entry.getValue();
	    JSONObject jo = new JSONObject();
	    jo.put("id", id);
	    jo.put("class", task.getTask().getClass().getName());

	    long delay = -1L;
	    String next = "NA";
	    if (task.getScheduledFuture() != null) {
		delay = task.getScheduledFuture().getDelay(TimeUnit.MILLISECONDS);
		Date now = new Date();
		now.setTime(now.getTime() + delay);
		next = SDF.format(now);
	    }
	    jo.put("delay", delay);
	    jo.put("next", next);

	    ja.put(jo);
	}
	return ja.toString();
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("TasksRegistry [registry=");
	builder.append(registry);
	builder.append("]");
	return builder.toString();
    }

}

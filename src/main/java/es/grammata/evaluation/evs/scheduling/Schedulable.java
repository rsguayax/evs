package es.grammata.evaluation.evs.scheduling;

/**
 * @author ajmedialdea
 *
 */
public interface Schedulable {

    void schedule();

    void cancel();

    boolean isScheduled();

}

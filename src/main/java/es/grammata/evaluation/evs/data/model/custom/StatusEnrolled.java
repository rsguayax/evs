package es.grammata.evaluation.evs.data.model.custom;

public enum StatusEnrolled {

	SUITABLE(1),
	NOT_CONSUMED(2),
	ZERO_COURSE(3),
	UNSUITABLE(4),
	MANUALLY_ADMITTED(5),
	IN_PROCESS(6),
	NOT_APPLICABLE(7),
	DENIED(8),
	UNKNOW(9);
	
    public final int value;
	
    private StatusEnrolled(int value)
    {
        this.value = value;
    }
    
    public int status() {
    	return this.value;
    }
}
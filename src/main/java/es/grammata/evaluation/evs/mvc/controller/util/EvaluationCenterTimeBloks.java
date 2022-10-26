package es.grammata.evaluation.evs.mvc.controller.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import es.grammata.evaluation.evs.data.model.repository.AvailableState;
import es.grammata.evaluation.evs.data.model.repository.ClassroomTimeBlock;
import es.grammata.evaluation.evs.data.model.repository.EvaluationCenter;

public class EvaluationCenterTimeBloks {
	
	private EvaluationCenter evaluationCenter;
	private LinkedHashMap<Long, ClassroomTimeBlock> timeBlocks;
	private LinkedHashMap<Long, ClassroomTimeBlock> availableStateTimeBlocks;
	private LinkedHashMap<Long, ClassroomTimeBlock> fullStateTimeBlocks;
	
	public EvaluationCenterTimeBloks(EvaluationCenter evaluationCenter, List<ClassroomTimeBlock> timeBlocks) {
		this.evaluationCenter = evaluationCenter;
		this.timeBlocks = new LinkedHashMap<Long, ClassroomTimeBlock>();
		this.availableStateTimeBlocks = new LinkedHashMap<Long, ClassroomTimeBlock>();
		this.fullStateTimeBlocks = new LinkedHashMap<Long, ClassroomTimeBlock>();
		
		for (ClassroomTimeBlock timeBlock : timeBlocks) {
			this.timeBlocks.put(timeBlock.getId(), timeBlock);
			
			if (timeBlock.getAvailableState().getValue().equals(AvailableState.FULL)) {
				this.fullStateTimeBlocks.put(timeBlock.getId(), timeBlock);
			} else {
				this.availableStateTimeBlocks.put(timeBlock.getId(), timeBlock);
			}
		}
	}

	public EvaluationCenter getEvaluationCenter() {
		return evaluationCenter;
	}

	public void setEvaluationCenter(EvaluationCenter evaluationCenter) {
		this.evaluationCenter = evaluationCenter;
	}

	public List<ClassroomTimeBlock> getTimeBlocks() {
		return new ArrayList<ClassroomTimeBlock>(timeBlocks.values());
	}
	
	public void setTimeBlocks(List<ClassroomTimeBlock> timeBlocks) {
		for (ClassroomTimeBlock timeBlock : timeBlocks) {
			this.timeBlocks.put(timeBlock.getId(), timeBlock);
		}
	}
	
	public boolean hasTimeBlocks() {
		return !timeBlocks.isEmpty();
	}
	
	public List<ClassroomTimeBlock> getAvailableStateTimeBlocks() {
		return new ArrayList<ClassroomTimeBlock>(availableStateTimeBlocks.values());
	}
	
	public List<ClassroomTimeBlock> getFullStateTimeBlocks() {
		return new ArrayList<ClassroomTimeBlock>(fullStateTimeBlocks.values());
	}
	
	public void updateTimeBlockState(ClassroomTimeBlock timeBlock) {
		if (timeBlock.getAvailableState().getValue().equals(AvailableState.FULL)) {
			availableStateTimeBlocks.remove(timeBlock.getId());
			fullStateTimeBlocks.put(timeBlock.getId(), timeBlock);
		} else {
			fullStateTimeBlocks.remove(timeBlock.getId());
			availableStateTimeBlocks.put(timeBlock.getId(), timeBlock);
		}
	}
	
	public void updateTimeBlock(ClassroomTimeBlock timeBlock) {
		timeBlocks.put(timeBlock.getId(), timeBlock);
		
		if (fullStateTimeBlocks.containsKey(timeBlock.getId())) {
			fullStateTimeBlocks.put(timeBlock.getId(), timeBlock);
		} else if(availableStateTimeBlocks.containsKey(timeBlock.getId())) {
			availableStateTimeBlocks.put(timeBlock.getId(), timeBlock);
		}
	}
}

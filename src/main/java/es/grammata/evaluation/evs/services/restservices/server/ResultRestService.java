package es.grammata.evaluation.evs.services.restservices.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.grammata.evaluation.evs.data.model.repository.EvaluationEventMatterTest;
import es.grammata.evaluation.evs.data.model.repository.Matter;
import es.grammata.evaluation.evs.data.model.repository.MatterTestStudent;
import es.grammata.evaluation.evs.data.model.repository.Session;
import es.grammata.evaluation.evs.data.model.repository.Test;
import es.grammata.evaluation.evs.data.model.repository.User;
import es.grammata.evaluation.evs.data.services.repository.EvaluationEventMatterTestService;
import es.grammata.evaluation.evs.data.services.repository.MatterTestStudentService;
import es.grammata.evaluation.evs.data.services.repository.UserService;
import es.grammata.evaluation.evs.services.restservices.server.wrapper.ResultListRequest;
import es.grammata.evaluation.evs.services.restservices.server.wrapper.ResultListResponse;
import es.grammata.evaluation.evs.services.restservices.server.wrapper.TestListRequest;
import es.grammata.evaluation.evs.services.restservices.server.wrapper.TestListResponse;


@Controller
@RequestMapping("/rest/resultService")
public class ResultRestService {
	@Autowired
	private UserService userService;
	
	@Autowired
	private EvaluationEventMatterTestService evaluationEventMatterTestService;

	@Autowired
	private MatterTestStudentService matterTestStudentService;
	

	@RequestMapping(value="/tests", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody List<TestListResponse> getTestsList(@RequestBody TestListRequest testRequest) {
		List<EvaluationEventMatterTest> eemts = evaluationEventMatterTestService.findByEvaluationEventCode(testRequest.getEvaluationEventCode(), true);
		
		List<TestListResponse> response = new ArrayList<TestListResponse>();	
		for(EvaluationEventMatterTest eemt : eemts) {
			TestListResponse tlr = new TestListResponse();
			Matter matter = eemt.getEvaluationEventMatter().getMatter();
			Test test = eemt.getTest();
			tlr.setTestId(eemt.getId());
			tlr.setName(test.getName());
			tlr.setMatterCode(matter.getCode());
			tlr.setEvaluationTypeCode(test.getEvaluationType().getCode());
			tlr.setLocker(test.getLocker());
			tlr.setExternalId(test.getExternalId());
			response.add(tlr);
		}
		
		return response;
	}
	
	
	@RequestMapping(value="/results", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody List<ResultListResponse> getResultList(@RequestBody ResultListRequest resultRequest) {
		EvaluationEventMatterTest eemt = evaluationEventMatterTestService.findById(resultRequest.getTestId());
		
		List<ResultListResponse> response = new ArrayList<ResultListResponse>();	
		List<MatterTestStudent> mtss = matterTestStudentService.findByEvaluationEventMatterTestPublishedOrNotified(eemt);

		Matter matter = eemt.getEvaluationEventMatter().getMatter();
		Test test = eemt.getTest();
		for(MatterTestStudent mts : mtss) {
			User user = mts.getEvaluationAssignmentMatter().getEvaluationAssignment().getUser();
			ResultListResponse rlr = new ResultListResponse();
			rlr.setLocker(test.getLocker());
			rlr.setMatterCode(matter.getCode());
			rlr.setMatterName(matter.getName());
			rlr.setStudentFullName(user.getFullName());
			rlr.setStudentIdentifier(user.getIdentification());
			rlr.setPeriodCode(eemt.getEvaluationEventMatter().getAcademicPeriod().getCode());
			rlr.setPeriodName(eemt.getEvaluationEventMatter().getAcademicPeriod().getName());
			rlr.setnBlanks(mts.getSession().getnBlanks());
			rlr.setnSuccesses(mts.getSession().getnSuccesses());
			rlr.setnFails(mts.getSession().getnFails());
			rlr.setResult(mts.getSession().getRate());
			response.add(rlr);
		}
		
		return response;
	}
	
	
}
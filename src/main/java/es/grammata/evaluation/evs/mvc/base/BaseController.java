package es.grammata.evaluation.evs.mvc.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BaseController {
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("errors/error");
		mav.addObject("headText",  "Se ha producido un error");
		mav.addObject("errorMessage",  "Se ha producido una excepción en la ejecución");
		return mav;
	}
}
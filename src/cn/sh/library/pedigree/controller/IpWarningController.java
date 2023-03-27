package cn.sh.library.pedigree.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ipwarning")
public class IpWarningController {
	@RequestMapping("/error")
	public String stopIP(HttpServletRequest req, HttpServletResponse res) {
		long stoptime = (long) req.getSession().getAttribute("timego");
		req.setAttribute("stoptime", stoptime);

		return "error/ErrorInfo";
	}
}
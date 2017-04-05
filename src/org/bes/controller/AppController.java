package org.bes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

	@RequestMapping("/")
	public String login() {
		return "/login";
	}

	@RequestMapping("/page/index")
	public String index() {
		return "/page/index";
	}

	@RequestMapping("/page/home")
	public String home() {
		return "/page/home";
	}
}

package com.bjsxt.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bjsxt.service.ImgService;

@Controller
public class ImgController {
	@Resource
	private ImgService imgService;
	@RequestMapping("show")
	public String show(Model model){
		model.addAttribute("list", imgService.selAll());
		return "/index.jsp";
	}
}

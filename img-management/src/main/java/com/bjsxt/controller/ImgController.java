package com.bjsxt.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bjsxt.service.ImgService;

@Controller
public class ImgController {
	@Resource
	private ImgService imgServiceImpl;
	@RequestMapping("show")
	public String show(Model model){
		model.addAttribute("list", imgServiceImpl.selAll());
		return "/index.jsp";
	}
	@RequestMapping("delete")
	public String delete(int id){
		int index = imgServiceImpl.delById(id);
		return "show";
	}
}

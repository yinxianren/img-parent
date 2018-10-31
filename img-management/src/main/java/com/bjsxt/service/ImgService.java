package com.bjsxt.service;

import java.util.List;

import com.bjsxt.pojo.Img;

public interface ImgService {
	List<Img> selAll();
	
	int delById(int id);
}

package com.bjsxt.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bjsxt.commons.utils.JsonUtils;
import com.bjsxt.dao.JedisClusterDao;
import com.bjsxt.mapper.ImgMapper;
import com.bjsxt.pojo.Img;
import com.bjsxt.pojo.ImgExample;
import com.bjsxt.service.ImgService;

@Service
public class ImgServiceImpl implements ImgService {
	@Resource
	private ImgMapper imgMapper;
	@Value("${bigpic.key}")
	private String key;
	
	
	@Resource
	private JedisClusterDao jedisClusterDaoImpl;
	@Override
	public List<Img> selAll() {
		return imgMapper.selectByExample(new ImgExample());
	}

	@Override
	public int delById(int id) {
		//1. 删除mysql中数据
		int index = imgMapper.deleteByPrimaryKey(id);
		if(index>0){
			//2. 判断redis中是否有缓存有缓存数据
			if(jedisClusterDaoImpl.exists(key)){
				String value = jedisClusterDaoImpl.get(key);
				if(value!=null&&!value.equals("")){
					//3. 如果有缓存数据,修改缓存数据
					List<Img> list = JsonUtils.jsonToList(value, Img.class);
					Img imgExists = null;
					for (Img img : list) {
						if((int)img.getId()==id){
							imgExists = img;
						}
					}
					if(imgExists!=null){
						list.remove(imgExists);
					}
					jedisClusterDaoImpl.set(key, JsonUtils.objectToJson(list));
				}
			}
			
			
			
		}
		return index;
	}
}

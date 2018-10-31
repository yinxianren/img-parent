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
	@Resource
	private JedisClusterDao jedisClusterDaoImpl;
	@Value("${bigpic.key}")
	private String key;

	@Override
	public List<Img> selAll() {
		// 判断redis中是否存在指定key
		if (jedisClusterDaoImpl.exists("bigimg")) {
			// 如果存在取出,取出后判断是否为null或""
			String value = jedisClusterDaoImpl.get("bigimg");
			if (value != null && !value.equals("")) {
				return JsonUtils.jsonToList(value, Img.class);
			}
		}
		// 如果不存在,从mysql中取出

		// 查询体
		ImgExample example = new ImgExample();
		//
		// example.createCriteria().andIdEqualTo(1).andPathEqualTo("");
		List<Img> list = imgMapper.selectByExample(example);

		// 把数据缓存到redis中
		jedisClusterDaoImpl.set("bigimg", JsonUtils.objectToJson(list));

		return list;

	}

}

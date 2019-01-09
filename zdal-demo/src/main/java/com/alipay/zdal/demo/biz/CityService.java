package com.alipay.zdal.demo.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.zdal.demo.mapper.CityMapper;
import com.alipay.zdal.demo.model.City;

@Service
public class CityService {

	@Autowired
	private CityMapper cityMapper;

	public void insert(City city) {
		System.out.println("开始插入");
		int i = cityMapper.insertCity(city);
		System.out.println("插入结果为：" + i);
	}
}
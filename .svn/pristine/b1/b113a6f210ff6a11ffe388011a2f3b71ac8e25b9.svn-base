package cn.jj.flight.controller;

import java.util.List;

import cn.jj.dao.service.RedisService;
import cn.jj.dao.service.impl.RedisServiceImpl;
import cn.jj.entity.data.Flightinfo;
import cn.jj.entity.data.Flightinputinfo;
import cn.jj.entity.data.Flightpriceinfo;
import cn.jj.utils.RedisUtil;

public class Test {
	private RedisService redisService=new RedisServiceImpl();
	RedisUtil redis=RedisUtil.getInstance();
	@org.junit.Test
	public void test1(){
		String key = "flight";
		redis.del(key);
	
		Flightinputinfo inputinfo =  new Flightinputinfo();
		Flightinfo flightinfo =  new Flightinfo();
		Flightpriceinfo flightpriceinfo = new Flightpriceinfo();	
		flightinfo.setCreator("chenwenqi");
		flightpriceinfo.setDatasource("Tuniu");
		inputinfo.setCreator("chenwenqi");
		redisService.deleteByMultipleAttribute(flightinfo);
		redisService.deleteByMultipleAttribute(flightpriceinfo);
		redisService.deleteByMultipleAttribute(inputinfo);
		

	}
	
}

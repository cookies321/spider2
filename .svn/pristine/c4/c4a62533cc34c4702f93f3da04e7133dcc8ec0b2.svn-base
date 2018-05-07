package cn.jj.flight.service.impl;

import java.io.Serializable;
import java.util.Map;

import cn.jj.service.IDownLoadService;
import cn.jj.utils.PageDownLoadUtil;

public class FlightDownload implements IDownLoadService {

	@Override
	public Serializable download(String url, Map<?, ?>... maps) {
		if(url.startsWith("http://flight-api.tuniu.com/open/flight/v0/flightDetails")||url.startsWith("https://flight.qunar.com/site/oneway_list.htm")){
			return "OK"; 
		}else if(url.startsWith("http://flights.ctrip.com/domesticsearch/search/SearchFirstRouteFlights")){
			return "OK";
		}else if(url.startsWith("http://m.ly.com/flightnew/json/firstflightlist.html")){
			return "OK";
		}
		String content="";
		content=PageDownLoadUtil.httpClientGet(url, maps);
		return content;
		
	}

	@Override
	public Serializable downloadByPost(String url, String param, Map<?, ?>... maps) {
		// TODO Auto-generated method stub
		return null;
	}

}

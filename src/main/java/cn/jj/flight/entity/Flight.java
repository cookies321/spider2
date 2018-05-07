package cn.jj.flight.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import cn.jj.entity.data.Flightinfo;
import cn.jj.entity.data.Flightinputinfo;
import cn.jj.entity.data.Flightpriceinfo;
import cn.jj.entity.data.Flighttransferinfo;

public class Flight implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//当前分页的url
	private String pageUrl;
	
	//详情的url
	private String url;
	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	//当前航班的分页的document
	private String content;
	
	//关联的inpuit信息
	private Flightinputinfo flightinputinfo;
	
	public Flightinputinfo getFlightinputinfo() {
		return flightinputinfo;
	}

	public void setFlightinputinfo(Flightinputinfo flightinputinfo) {
		this.flightinputinfo = flightinputinfo;
	}

	//航班基础信息
	private Flightinfo flightinfo;
	
	//出发城市
	private String beginCity;
	
	//到达城市
	private String endCity;
	
	//航班出发时间
	private String flightDate;
	
	
	private List<Flightpriceinfo> flightpriceinfoList = new LinkedList<>();
	
	private List<Transferinfo> transferList = new LinkedList<>();
	
	
	public List<Transferinfo> getFlighttransferList() {
		return transferList;
	}

	public void setFlighttransferList(List<Transferinfo> transferList) {
		this.transferList = transferList;
	}

	public List<Flightpriceinfo> getFlightpriceinfoList() {
		return flightpriceinfoList;
	}

	public void setFlightpriceinfoList(List<Flightpriceinfo> flightpriceinfoList) {
		this.flightpriceinfoList = flightpriceinfoList;
	}

	public String getBeginCity() {
		return beginCity;
	}

	public void setBeginCity(String beginCity) {
		this.beginCity = beginCity;
	}

	public String getEndCity() {
		return endCity;
	}

	public void setEndCity(String endCity) {
		this.endCity = endCity;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	public Flightinfo getFlightinfo() {
		return flightinfo;
	}

	public void setFlightinfo(Flightinfo flightinfo) {
		this.flightinfo = flightinfo;
	}

	@Override
	public String toString() {
		return "Flight [url=" + url + ", content=" + content + ", flightinputinfoid="+
	", flightinfo=" + flightinfo + ", beginCity=" + beginCity + ", endCity=" + endCity + ", flightDate="
				+ flightDate + "]";
	}

	
	
	
	
}

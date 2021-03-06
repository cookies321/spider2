package cn.jj.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jj.entity.data.Commentinfo;
import cn.jj.entity.data.Pictureinfo;
import cn.jj.entity.data.Roombasicinfo;
import cn.jj.entity.data.Roomprice;
import cn.jj.entity.data.Routeinfo;
import cn.jj.entity.data.Routepriceinfo;
import cn.jj.entity.data.Sceinfo;
import cn.jj.entity.data.Scepriceinfo;
import cn.jj.utils.Param;

/**
 * @Description: 房型，票价，好评率，评论等url任务公共类
 * @author 徐仁杰
 * @date 2017年12月7日 下午2:23:54
 */
public class Params implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 类型枚举类
	 */
	private Param type;

	/**
	 * 数据来源网站
	 */
	private Param dataSource;
	
	/**
	 * 上一级url
	 */
	private String parentUrl;
	/**
	 * 请求链接
	 */
	private String url;

	/**
	 * 基础信息关联uuid
	 */
	private String uuid;

	/**
	 * 请求类型
	 */
	private Param httpType;

	/**
	 * post请求参数
	 */
	private String postParams;

	
	/**
	 * 评论日期
	 */
	private String commentDate;

	/**
	 * 页面、json内容
	 */
	private String content;
	
	/**
	 * 请求中需要带的头部信息
	 */
	private Map<String,String> header;

	/**
	 * 门票详情
	 */
	private Sceinfo sceinfo;
	
	/**
	 * 行程价格信息
	 */
	private List<Routepriceinfo> routePriceList;
	
	/**
	 * 酒店房型基本信息
	 */
	private Roombasicinfo roomInfo;
	
	/**
	 * 行程基本信息
	 */
	private Routeinfo routeInfo;
	
	public Params() {
		header = new HashMap<String, String>();
		routePriceList = new LinkedList<>();
		
	}
	
	

	public Routeinfo getRouteInfo() {
		return routeInfo;
	}



	public void setRouteInfo(Routeinfo routeInfo) {
		this.routeInfo = routeInfo;
	}



	public String getParentUrl() {
		return parentUrl;
	}



	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}



	public Roombasicinfo getRoomInfo() {
		return roomInfo;
	}

	public void setRoomInfo(Roombasicinfo roomInfo) {
		this.roomInfo = roomInfo;
	}


	public List<Routepriceinfo> getRoutePriceList() {
		return routePriceList;
	}

	public void setRoutePriceList(Routepriceinfo routePrice) {
		this.routePriceList.add(routePrice);
	}

	public Sceinfo getSceinfo() {
		return sceinfo;
	}

	public void setSceinfo(Sceinfo sceinfo) {
		this.sceinfo = sceinfo;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(String key,String value) {
		this.header.put(key, value);
	}

	public Param getDataSource() {
		return dataSource;
	}

	public void setDataSource(Param dataSource) {
		this.dataSource = dataSource;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPostParams() {
		return postParams;
	}

	public void setPostParams(String postParams) {
		this.postParams = postParams;
	}
	

	public Param getHttpType() {
		return httpType;
	}

	public void setHttpType(Param httpType) {
		this.httpType = httpType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Param getType() {
		return type;
	}

	public void setType(Param type) {
		this.type = type;
	}

}

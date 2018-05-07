package cn.jj.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import cn.jj.entity.data.Pictureinfo;
import cn.jj.entity.data.Routeinfo;
import cn.jj.entity.data.Routepriceinfo;

/**
 * @Description: 行程
 * @author 徐仁杰 
 * @date 2017年11月30日 上午10:03:01
 */
public class Route implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;

	/**
	 * 测试用
	 */
	private String name;

	/**
	 * 页面内容
	 */
	private String content;

	/**
	 * 当前正在请求的url链接
	 */
	private String url;

	/**
	 * 收集到的链接
	 */
	private List<String> urlList;

	/**
	 * 存放行程信息
	 */
	private Routeinfo routeInfo;

	//城市名称
	private String cityName;

	//一级主页url
	private String parentUrl;
	//二级分页url
	private String pageUrl;
	
	private String otherInformation;
	
	/**
	 * 存放行程价格的url链接
	 */
	private String routepriceURL;

	public String getRoutepriceURL() {
		return routepriceURL;
	}

	public void setRoutepriceURL(String routepriceURL) {
		this.routepriceURL = routepriceURL;
	}

	public String getOtherInformation() {
		return otherInformation;
	}

	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public Routeinfo getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(Routeinfo routeInfo) {
		this.routeInfo = routeInfo;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

	public Route() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(String url) {
		this.urlList.add(url);
	}

}

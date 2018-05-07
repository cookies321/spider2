/** 
 * Project Name:spider-grab 
 * File Name:Hotel.java 
 * Package Name:cn.jj.entity 
 * Date:2017年12月1日 上午9:02:00 
 * author 汤玉林
 */ 
package cn.jj.entity;

import java.io.Serializable;
import java.util.List;

import cn.jj.entity.data.Addressinfo;
import cn.jj.entity.data.Hotelinfo;
/**
 * @Description: 酒店
 * @author 汤玉林
 * @date 2017年12月1日 上午9:02:00 
 */
public class Hotel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用于后面所有的url和实体类的关联id
	 */
	private String id;
	
	private String name;

	private String content;

	private List<String> urlList;

	private String url;

	//一级主页url
	private String parentUrl;

	//二级分页url
	private String pageUrl;

	//酒店详情实体类
	private Hotelinfo hotelbasicInfo;

	//地址详情
	private Addressinfo addressInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Hotelinfo getHotelbasicInfo() {
		return hotelbasicInfo;
	}

	public void setHotelbasicInfo(Hotelinfo hotelbasicInfo) {
		this.hotelbasicInfo = hotelbasicInfo;
	}

	public Addressinfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(Addressinfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
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

	@Override
	public String toString() {
		return "Hotel [id=" + id + ", name=" + name + ", content=" + content
				+ ", urlList=" + urlList + ", url=" + url + ", parentUrl="
				+ parentUrl + ", pageUrl=" + pageUrl + ", hotelbasicInfo="
				+ hotelbasicInfo + ", addressInfo=" + addressInfo + "]";
	}


}

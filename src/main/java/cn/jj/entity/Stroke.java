package cn.jj.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import cn.jj.entity.data.Addressinfo;
import cn.jj.entity.data.Pictureinfo;
import cn.jj.entity.data.Sceinfo;
import cn.jj.entity.data.Scepriceinfo;

/**
 * @Description: 景点
 * @author 徐仁杰
 * @date 2017年11月30日 上午10:03:12
 */
public class Stroke implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private String name;

	private String url;

	private List<String> urlList;
	

	/**
	 * 页面内容
	 */
	private String content;

	/**
	 * 景点对象
	 */
	private Sceinfo sceinfo;


	/**
	 * 四级地址集合
	 */
	private Addressinfo addressinfo;


	//一级主页url
	private String parentUrl;

	//二级分页url
	private String pageUrl;

	//输入目的地城市名称
	private String cityName;
	//其他有用信息
	private String otherinformation;
	
	

	public String getOtherinformation() {
		return otherinformation;
	}

	public void setOtherinformation(String otherinformation) {
		this.otherinformation = otherinformation;
	}

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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public Addressinfo getAddressinfo() {
		return addressinfo;
	}

	public void setAddressinfo(Addressinfo addressinfo) {
		this.addressinfo = addressinfo;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
	public Stroke() {
		super();
	}

	public Sceinfo getSceinfo() {
		return sceinfo;
	}

	public void setSceinfo(Sceinfo sceinfo) {
		this.sceinfo = sceinfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}

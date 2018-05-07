/** 
 * Project Name:spider-grab 
 * File Name:HotelDownLoad.java 
 * Package Name:cn.jj.service.impl 
 * Date:2017年12月1日 上午8:49:22 
 * author 汤玉林
 */ 
package cn.jj.service.impl;

import java.io.Serializable;
import java.util.Map;
import cn.jj.service.IDownLoadService;
import cn.jj.utils.PageDownLoadUtil;

/**
 * @Description: TODO
 * @author 汤玉林
 * @date 2017年12月1日 上午8:49:22 
 */
public class DownLoad implements IDownLoadService{
	
	@Override
	public Serializable download(String url, Map<?, ?>... maps) {
		String content="";
		if(url.startsWith("https://www.ly.com/go/RainbowClientAjax/getdianping")){
			
			content=PageDownLoadUtil.sendPost(url, "");
		
		}else{
			content=PageDownLoadUtil.httpClientGet(url, maps);
		}
		return content;
	}
	
	@Override
	public Serializable downloadByPost(String url, String param, Map<?, ?>... maps) {
		
		String content="";
		//携程自由行评论url
		//携程景点想去人数和去过人数
		if(url.startsWith("http://online.ctrip.com/restapi/soa2/12447/json/GetCommentInfoList")||url.startsWith("http://you.ctrip.com/Destinationsite/SharedComm/ShowGowant")){
			
			content=PageDownLoadUtil.sendPost(url, param, maps);
		
		}else{
			content=PageDownLoadUtil.post(url, param, maps);
		}
		return content;
	}

}

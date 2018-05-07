package cn.jj.service;

import java.io.Serializable;
import java.util.Map;

public interface IDownLoadService {

	Serializable download(String url,Map<?, ?>... maps);
	
	Serializable downloadByPost(String url,String param,Map<?, ?>... maps);
}

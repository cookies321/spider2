package cn.jj.utils;

import org.apache.commons.lang3.StringUtils;

public class ValidateUtil {

	public static boolean valid(String content) {
		boolean isNull = StringUtils.isBlank(content);
		
		if (!isNull) {
			
			if (content.contains("请完成验证后继续访问")||content.contains("想不想来一次环球旅行")||
					content.contains("Bad Request")||content.contains("too many request")||
					content.equals("reload") || content.contains("404 Not Found_您要访问的页面不存在_途牛旅游网")) {
				return true;
			}else{
				return false;
			}
			
		}
	
		return true;
	}
}

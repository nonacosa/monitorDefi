package com.online.book.util;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenda.zhuang
 * @Date 2020/10/3 00:51
 * @Description ...
 * @E-mail sis.nonacosa@gmail.com
 */
public class CovertUtil {

	public static Map convertBean2Map(Object bean, String... ignoreProperties) {
		try {
			List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

			Map<String, Object> map = new HashMap<>();
			if (bean != null) {
				BeanMap beanMap = BeanMap.create(bean);
				for (Object key : beanMap.keySet()) {
					Object val = beanMap.get(key);
					if ((ignoreList == null || !ignoreList.contains(key)) && !ObjectUtils.isEmpty(val)) {
						map.put(String.valueOf(key), val);
					}
				}
			}
			return map;
		} catch (Exception e) {
		}
		return null;
	}
}

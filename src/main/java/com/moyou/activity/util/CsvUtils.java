package com.moyou.activity.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * csv工具
 * 
 * @author xiangshuo
 *
 */
public final class CsvUtils {

	private CsvUtils() {}
	
	public static String format(List<String> list) {
		if (null == list) {
			return null;
		}
		
		if (list.size() == 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			
			sb.append(str);
			sb.append(",");
		}
		
		return sb.substring(0, sb.length() - 1);
	}
	
	public static List<String> parse(String csv) {
		if (StringUtils.isBlank(csv)) {
			return Collections.emptyList();
		}
		
		List<String> list = new ArrayList<>();
		
		if (! csv.contains(",")) {
			
			list.add(csv);
		} else {
			
			String[] arr = csv.split(",");
			for (String s : arr) {
				
				list.add(s);
			}
		}
		
		return list;
	}
	
	public static List<Integer> parseInt(String csv) {
		if (StringUtils.isBlank(csv)) {
			return Collections.emptyList();
		}
		
		List<Integer> list = new ArrayList<>();
		
		if (! csv.contains(",")) {
			
			list.add(Integer.parseInt(csv));
		} else {
			
			String[] arr = csv.split(",");
			for (String s : arr) {
				
				list.add(Integer.parseInt(s));
			}
		}
		
		return list;
	}
	
}

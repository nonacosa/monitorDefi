package com.online.book.util;

/**
 * @author wenda.zhuang
 * @Date 2020/11/11 12:43 下午
 * @Description ...
 * @E-mail sis.nonacosa@gmail.com
 */
public class Money {

	/**
	 * 是否超出变化阈值
	 * @param before         前一个波动
	 * @param after			 后一个波动
	 * @param threshold		 阈值
	 * @return
	 */
	public static Boolean changeDegreeRange(Double before,Double after,Double threshold) {
		if(before == 0 || after == 0) return false;
		double change = Math.abs(after - before) / before;
		double changePercentage =  change / before;
		System.out.println("此次变化：>>> " + changePercentage);
		if(changePercentage > threshold) {
			return true;
		}
		return false;
	}
}

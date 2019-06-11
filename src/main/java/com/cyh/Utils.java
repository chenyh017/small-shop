package com.cyh;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.cyh.vo.BuyShop;
import com.cyh.vo.SaleShop;
import com.cyh.vo.ShopSalePrice;
import com.cyh.vo.ShopStock;

public class Utils {

	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final String PATH = "C:/small-shop/";

	public static String getCsv(Class<?> clazz) {
		if (clazz == BuyShop.class) {
			return PATH + "商品进货.csv";
		}
		if (clazz == SaleShop.class) {
			return PATH + "商品出售.csv";
		}
		if (clazz == ShopSalePrice.class) {
			return PATH + "商品售价.csv";
		}
		if (clazz == ShopStock.class) {
			return PATH + "商品库存.csv";
		}

		//not assert
		return null;
	}

	public static String stringWrap(String obj) {
		if (obj == null) {
			return "";
		}
		return obj;
	}

	public static String numberWrap(BigDecimal obj) {
		if (obj == null) {
			return "";
		}
		return obj.doubleValue() + "";
	}

	public static String idWrap(String id) {
		if (id == null || "".equals(id)) {
			return UUID.randomUUID().toString();
		}
		return id;
	}

	public static String locaDateWrap(LocalDate obj) {
		if (obj == null) {
			return "";
		}
		return obj.format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
	}

	public static BigDecimal parseNumber(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		return new BigDecimal(value);
	}

	public static LocalDate parseLocalDate(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		return LocalDate.parse(value, DateTimeFormatter.ofPattern(YYYY_MM_DD));
	}

	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}
	

}

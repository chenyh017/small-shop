package com.cyh.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cyh.Utils;

public class ShopSalePrice implements CsvEntity {
	private String id;
	private String name;

	/**
	 * 小商品,香烟
	 */
	private String type;
	private String unit;
	private BigDecimal price;
	private LocalDate date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String[] getCsvLine() {
		String[] results = new String[6];
		results[0] = Utils.idWrap(id);
		results[1] = Utils.stringWrap(name);
		results[2] = Utils.stringWrap(type);
		results[3] = Utils.stringWrap(unit);
		results[4] = Utils.numberWrap(price);
		results[5] = Utils.locaDateWrap(date);
		return results;
	}

}

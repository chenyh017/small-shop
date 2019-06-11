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
	public String getCsvLine() {
		StringBuilder results = new StringBuilder();
		results.append(Utils.idWrap(id)).append(",");
		results.append(Utils.stringWrap(name)).append(",");
		results.append(Utils.stringWrap(type)).append(",");
		results.append(Utils.stringWrap(unit)).append(",");
		results.append(Utils.numberWrap(price)).append(",");
		results.append(Utils.locaDateWrap(date));
		return results.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShopSalePrice other = (ShopSalePrice) obj;
		if (Utils.isEmpty(id) || Utils.isEmpty(other.id)) {
			return false;
		}
		return id.equals(other.id);
	}

}

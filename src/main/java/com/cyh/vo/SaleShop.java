package com.cyh.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cyh.Utils;

public class SaleShop implements CsvEntity {

	private String id;
	private String name;
	private BigDecimal price;
	private BigDecimal count;
	private LocalDate time;

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public LocalDate getTime() {
		return time;
	}

	public void setTime(LocalDate time) {
		this.time = time;
	}


	@Override
	public String[] getCsvLine() {
		String[] results = new String[5];
		results[0] = Utils.idWrap(id);
		results[1] = Utils.stringWrap(name);
		results[2] = Utils.numberWrap(price);
		results[3] = Utils.numberWrap(count);
		results[4] = Utils.locaDateWrap(time);
		return results;
	}

}

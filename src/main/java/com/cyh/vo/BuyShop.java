package com.cyh.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cyh.Utils;

public class BuyShop implements CsvEntity {

	private String id;
	private String name;
	private BigDecimal sumPrice;
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

	public BigDecimal getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(BigDecimal sumPrice) {
		this.sumPrice = sumPrice;
	}

	@Override
	public String getCsvLine() {
		StringBuilder results = new StringBuilder();
		results.append(Utils.idWrap(id)).append(",");
		results.append(Utils.stringWrap(name)).append(",");
		results.append(Utils.numberWrap(sumPrice)).append(",");
		results.append(Utils.numberWrap(count)).append(",");
		results.append(Utils.locaDateWrap(time));
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyShop other = (BuyShop) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	

}

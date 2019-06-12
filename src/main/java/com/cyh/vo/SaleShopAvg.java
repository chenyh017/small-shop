package com.cyh.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SaleShopAvg {

	private String name;
	private BigDecimal avgPrice;
	private BigDecimal sumCount;
	private BigDecimal sumPrice;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

	public BigDecimal getSumCount() {
		return sumCount;
	}

	public void setSumCount(BigDecimal sumCount) {
		this.sumCount = sumCount;
	}

	public BigDecimal getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(BigDecimal sumPrice) {
		this.sumPrice = sumPrice;
	}
	

	public void addSumCount(BigDecimal count) {
		if (this.sumCount == null) {
			this.sumCount = BigDecimal.ZERO;
		}
		if (count != null) {
			this.sumCount = this.sumCount.add(count);
		}
	}

	public void addSumPrice(BigDecimal sumPrice) {
		if (this.sumPrice == null) {
			this.sumPrice = BigDecimal.ZERO;
		}
		if (sumPrice != null) {
			this.sumPrice = this.sumPrice.add(sumPrice);
		}
	}

	public void calcAvg() {
		try {
			this.avgPrice = this.sumPrice.divide(this.sumCount,2,RoundingMode.HALF_UP);
		} catch (Exception e) {
			e.printStackTrace();
			this.avgPrice = BigDecimal.ZERO;
		}
	}

}

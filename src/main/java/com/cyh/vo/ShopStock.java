package com.cyh.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cyh.Utils;

public class ShopStock implements CsvEntity {
	private String id;
	private String name;
	//理论库存
	private BigDecimal stock;
	//实际库存
	private BigDecimal actualStock;
	private BigDecimal buyCount;
	private BigDecimal buyAvgPrice;
	private BigDecimal saleCount;
	private BigDecimal saleAvgPrice;
	//成本
	private BigDecimal cost;
	//利润
	private BigDecimal profit;
	//统计时间
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

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public BigDecimal getActualStock() {
		return actualStock;
	}

	public void setActualStock(BigDecimal actualStock) {
		this.actualStock = actualStock;
	}

	public BigDecimal getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(BigDecimal buyCount) {
		this.buyCount = buyCount;
	}

	public BigDecimal getBuyAvgPrice() {
		return buyAvgPrice;
	}

	public void setBuyAvgPrice(BigDecimal buyAvgPrice) {
		this.buyAvgPrice = buyAvgPrice;
	}

	public BigDecimal getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(BigDecimal saleCount) {
		this.saleCount = saleCount;
	}

	public BigDecimal getSaleAvgPrice() {
		return saleAvgPrice;
	}

	public void setSaleAvgPrice(BigDecimal saleAvgPrice) {
		this.saleAvgPrice = saleAvgPrice;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public LocalDate getTime() {
		return time;
	}

	public void setTime(LocalDate time) {
		this.time = time;
	}

	@Override
	public String getCsvLine() {
		StringBuilder results = new StringBuilder();
		results.append(Utils.idWrap(id)).append(",");
		results.append(Utils.stringWrap(name)).append(",");
		results.append(Utils.numberWrap(stock)).append(",");
		results.append(Utils.numberWrap(actualStock)).append(",");
		results.append(Utils.numberWrap(buyAvgPrice)).append(",");
		results.append(Utils.numberWrap(saleCount)).append(",");
		results.append(Utils.numberWrap(saleAvgPrice)).append(",");
		results.append(Utils.numberWrap(cost)).append(",");
		results.append(Utils.numberWrap(profit)).append(",");
		results.append(Utils.locaDateWrap(time));
		return results.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShopStock other = (ShopStock) obj;
		if (Utils.isEmpty(id) || Utils.isEmpty(other.id)) {
			return false;
		}
		return id.equals(other.id);
	}

}

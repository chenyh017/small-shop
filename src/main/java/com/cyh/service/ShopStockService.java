package com.cyh.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyh.Utils;
import com.cyh.vo.BuyShopAvg;
import com.cyh.vo.SaleShopAvg;
import com.cyh.vo.ShopStock;

@Service
public class ShopStockService extends CsvDao<ShopStock> {

	@Autowired
	private ShopSalePriceService shopSalePriceService;
	@Autowired
	private BuyShopService buyShopService;
	@Autowired
	private SaleShopService saleShopService;

	public List<ShopStock> queryByMonth(LocalDate date, String type) {
		List<ShopStock> all = loadAll(new ShopStock()).getDatas();
		if (all.isEmpty()) {
			return all;
		}
		return all.stream().filter(b -> {
			boolean flag = b.getTime().getYear() == date.getYear()
					&& b.getTime().getMonthValue() == date.getMonthValue();
			if (!Utils.isEmpty(type)) {
				flag = flag && type.equals(shopSalePriceService.getTypeByName(b.getName()));
			}
			return flag;
		}).sorted((b1, b2) -> b1.getName().compareTo(b2.getName())).collect(Collectors.toList());
	}

	public void updateStock(String name, LocalDate date) {
		List<ShopStock> all = queryByMonth(date, "");
		ShopStock bean = all.stream().filter(b -> b.getName().equals(name)).findFirst().orElse(new ShopStock());
		bean.setName(name);
		List<BuyShopAvg> buyShopAvgs = buyShopService.avg(date, "");
		buyShopAvgs.forEach(buyShopAvg -> {
			if (buyShopAvg.getName().equals(name)) {
				bean.setBuyCount(buyShopAvg.getSumCount());
				bean.setBuyAvgPrice(buyShopAvg.getAvgPrice());
			}
		});
		List<SaleShopAvg> salShopAvgs = saleShopService.avg(date, "");
		salShopAvgs.forEach(salShopAvg -> {
			if (salShopAvg.getName().equals(name)) {
				bean.setSaleCount(salShopAvg.getSumCount());
				bean.setSaleAvgPrice(salShopAvg.getAvgPrice());
				bean.setCost(bean.getSaleCount().multiply(bean.getBuyAvgPrice()));
				bean.setProfit(salShopAvg.getSumPrice().subtract(bean.getCost()));
			}
		});
		bean.setStock(bean.getBuyCount().multiply(bean.getSaleCount()));
		bean.setTime(date);
		saveOrUpdate(bean);
	}

	public void reCalc(LocalDate date) {
		List<ShopStock> all = queryByMonth(date, "");
		Map<String, BigDecimal> nameActualPrice = new HashMap<>();
		all.forEach(b -> {
			nameActualPrice.put(b.getName(), b.getActualStock());
		});
		delete(all);
		List<BuyShopAvg> buyShopAvgs = buyShopService.avg(date, "");

		buyShopAvgs.forEach(b -> {
			updateStock(b.getName(), date);
			updateActualStock(b.getName(),nameActualPrice.get(b.getName()),date);
		});
	}

	public void updateActualStock(String name, BigDecimal actualStock, LocalDate date) {
		List<ShopStock> all = queryByMonth(date, "");
		ShopStock bean = all.stream().filter(b -> b.getName().equals(name)).findFirst().orElse(new ShopStock());
		if(bean.getId()==null) {
		}
		bean.setActualStock(actualStock);
	}

	@Override
	protected ShopStock toEntity(String csvLine) {
		ShopStock bean = new ShopStock();
		String[] arr = csvLine.split(",");
		bean.setId(arr[0]);
		bean.setName(arr[1]);
		bean.setStock(Utils.parseNumber(arr[2]));
		bean.setActualStock(Utils.parseNumber(arr[3]));
		bean.setBuyCount(Utils.parseNumber(arr[4]));
		bean.setBuyAvgPrice(Utils.parseNumber(arr[5]));
		bean.setSaleCount(Utils.parseNumber(arr[6]));
		bean.setSaleAvgPrice(Utils.parseNumber(arr[7]));
		bean.setCost(Utils.parseNumber(arr[8]));
		bean.setProfit(Utils.parseNumber(arr[9]));
		bean.setTime(Utils.parseLocalDate(arr[10]));
		return bean;
	}
}

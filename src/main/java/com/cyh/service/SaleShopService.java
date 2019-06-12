package com.cyh.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyh.Utils;
import com.cyh.vo.SaleShop;
import com.cyh.vo.SaleShopAvg;

@Service
public class SaleShopService extends CsvDao<SaleShop> {

	@Autowired
	private ShopSalePriceService shopSalePriceService;

	@Override
	protected SaleShop toEntity(String csvLine) {
		String[] arr = csvLine.split(",");
		SaleShop bean = new SaleShop();
		bean.setId(arr[0]);
		bean.setName(arr[1]);
		bean.setPrice(Utils.parseNumber(arr[2]));
		bean.setCount(Utils.parseNumber(arr[3]));
		bean.setTime(Utils.parseLocalDate(arr[4]));
		return bean;
	}

	public List<SaleShop> queryByMonth(LocalDate date, String type) {
		List<SaleShop> all = loadAll(new SaleShop()).getDatas();
		if (all.isEmpty()) {
			return all;
		}
		return all.stream().filter(bean -> {
			boolean flag = bean.getTime().getYear() == date.getYear()
					&& bean.getTime().getMonthValue() == date.getMonthValue();
			if (!Utils.isEmpty(type)) {
				flag = flag && type.equals(shopSalePriceService.getTypeByName(bean.getName()));
			}
			return flag;
		}).sorted((bean1, bean2) -> bean2.getTime().compareTo(bean1.getTime())).collect(Collectors.toList());
	}

	public List<SaleShop> queryByDate(LocalDate date, String type) {
		List<SaleShop> all = loadAll(new SaleShop()).getDatas();
		if (all.isEmpty()) {
			return all;
		}
		return all.stream().filter(bean -> {
			boolean flag = bean.getTime().equals(date);
			if (!Utils.isEmpty(type)) {
				flag = flag && type.equals(shopSalePriceService.getTypeByName(bean.getName()));
			}
			return flag;
		}).sorted((bean1, bean2) -> bean2.getTime().compareTo(bean1.getTime())).collect(Collectors.toList());
	}

	public List<SaleShopAvg> avg(LocalDate date, String type) {
		List<SaleShop> all = queryByMonth(date, type);
		List<SaleShopAvg> results = new ArrayList<>();
		if (!all.isEmpty()) {
			Map<String, List<SaleShop>> nameBuyShops = new HashMap<>();
			all.forEach(bean -> nameBuyShops.computeIfAbsent(bean.getName(), key -> new ArrayList<>()).add(bean));
			nameBuyShops.forEach((key, value) -> {
				SaleShopAvg buyShopAvg = new SaleShopAvg();
				value.forEach(shop -> {
					buyShopAvg.setName(key);
					buyShopAvg.addSumCount(shop.getCount());
					buyShopAvg.addSumPrice(shop.getPrice().multiply(shop.getCount()));
				});
				buyShopAvg.calcAvg();
				results.add(buyShopAvg);
			});
		}
		results.sort((bean1, bean2) -> bean1.getName().compareTo(bean2.getName()));
		return results;
	}

}

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
import com.cyh.vo.BuyShop;
import com.cyh.vo.BuyShopAvg;

@Service
public class BuyShopService extends CsvDao<BuyShop> {

	@Autowired
	private ShopSalePriceService shopSalePriceService;

	public List<BuyShop> queryByMonth(LocalDate date, String type) {
		List<BuyShop> all = loadAll(new BuyShop()).getDatas();
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

	public List<BuyShop> queryByDate(LocalDate date, String type) {
		List<BuyShop> all = loadAll(new BuyShop()).getDatas();
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

	@Override
	protected BuyShop toEntity(String csvLine) {
		String[] arr = csvLine.split(",");
		BuyShop bean = new BuyShop();
		bean.setId(arr[0]);
		bean.setName(arr[1]);
		bean.setSumPrice(Utils.parseNumber(arr[2]));
		bean.setCount(Utils.parseNumber(arr[3]));
		bean.setTime(Utils.parseLocalDate(arr[4]));
		return bean;
	}

	public List<BuyShopAvg> avg(LocalDate date, String type) {
		List<BuyShop> all = queryByMonth(date, type);
		List<BuyShopAvg> results = new ArrayList<>();
		if (!all.isEmpty()) {
			Map<String, List<BuyShop>> nameBuyShops = new HashMap<>();
			all.forEach(bean -> nameBuyShops.computeIfAbsent(bean.getName(), key -> new ArrayList<>()).add(bean));
			nameBuyShops.forEach((key, value) -> {
				BuyShopAvg buyShopAvg = new BuyShopAvg();
				value.forEach(shop -> {
					buyShopAvg.setName(key);
					buyShopAvg.addSumCount(shop.getCount());
					buyShopAvg.addSumPrice(shop.getSumPrice());
				});
				buyShopAvg.calcAvg();
				results.add(buyShopAvg);
			});
		}
		results.sort((bean1, bean2) -> bean1.getName().compareTo(bean2.getName()));
		return results;
	}

}

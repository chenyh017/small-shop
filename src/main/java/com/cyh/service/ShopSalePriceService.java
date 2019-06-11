package com.cyh.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cyh.CsvFile;
import com.cyh.Utils;
import com.cyh.vo.ShopSalePrice;

@Service
public class ShopSalePriceService extends CsvDao<ShopSalePrice> {

	@Override
	protected ShopSalePrice toEntity(String csvLine) {
		ShopSalePrice bean = new ShopSalePrice();
		String[] arr = csvLine.split(",");
		bean.setId(arr[0]);
		bean.setName(arr[1]);
		bean.setType(arr[2]);
		bean.setUnit(arr[3]);
		bean.setPrice(Utils.parseNumber(arr[4]));
		bean.setDate(Utils.parseLocalDate(arr[5]));
		return bean;
	}

	public List<ShopSalePrice> queryByDate(LocalDate date) {
		CsvFile<ShopSalePrice> all = loadAll(new ShopSalePrice());
		return all.getDatas().stream().filter(
				bean -> date.getYear() == bean.getDate().getYear() && date.getMonth() == bean.getDate().getMonth())
				.sorted((bean1, bean2) -> bean1.getName().compareTo(bean2.getName())).collect(Collectors.toList());
	}

	public List<ShopSalePrice> queryAll() {
		return loadAll(new ShopSalePrice()).getDatas();
	}

	public void copyLastMonthDatas() {
		List<ShopSalePrice> currents = queryByDate(LocalDate.now());
		delete(currents);
		List<ShopSalePrice> list = queryByDate(LocalDate.now().minusMonths(1));
		if (list.isEmpty()) {
			return;
		}
		for (ShopSalePrice bean : list) {
			bean.setId("");
			bean.setDate(LocalDate.now());
		}
		saveOrUpdate(list);

	}

	public List<ShopSalePrice> filterName(List<ShopSalePrice> queryByDate, String name) {
		if (queryByDate.isEmpty() || Utils.isEmpty(name)) {
			return queryByDate;
		}
		return queryByDate.stream().filter((ShopSalePrice bean) -> bean.getName().contains(name))
				.collect(Collectors.toList());
	}

}

package com.cyh.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cyh.Utils;
import com.cyh.vo.BuyShop;

@Service
public class BuyShopService extends CsvDao<BuyShop>{


	public List<BuyShop> queryByDate(LocalDate date) {
		List<BuyShop> all = loadAll(new BuyShop()).getDatas();
		if (all.isEmpty()) {
			return all;
		}
		all.stream().filter(
				bean -> bean.getTime().getYear() == date.getYear() && bean.getTime().getMonth() == date.getMonth())
				.sorted((bean1, bean2) -> bean1.getTime().compareTo(bean2.getTime())).collect(Collectors.toList());
		return all;
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

}

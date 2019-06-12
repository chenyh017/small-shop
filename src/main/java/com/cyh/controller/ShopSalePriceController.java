package com.cyh.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyh.service.ShopSalePriceService;
import com.cyh.vo.ShopSalePrice;

@RestController
public class ShopSalePriceController {

	@Autowired
	private ShopSalePriceService shopSalePriceService;

	@PostMapping("/shopSalePrice/queryByDate")
	public List<ShopSalePrice> queryByDate(int year, int month, @RequestParam(required = false) String name,
			@RequestParam(required = false) String type) {
		LocalDate date = LocalDate.of(year, month, 1);
		return shopSalePriceService.filter(shopSalePriceService.queryByDate(date), name, type);
	}

	@PostMapping("/shopSalePrice/deleteById")
	public void deleteById(ShopSalePrice bean) {
		shopSalePriceService.delete(bean);
	}

	@PostMapping("/shopSalePrice/copy")
	public void copy() {
		shopSalePriceService.copyLastMonthDatas();
	}

	@PostMapping("/shopSalePrice/save")
	public void save(ShopSalePrice bean, int year, int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		bean.setDate(date);
		shopSalePriceService.saveOrUpdate(bean);
	}

	@PostMapping("/shopSalePrice/getPrice")
	public double save(String name, int year, int month) {
		List<ShopSalePrice> list = queryByDate(year, month, name, "");
		if (list.isEmpty() || list.get(0).getPrice() == null) {
			return -1;
		}
		return list.get(0).getPrice().doubleValue();
	}

}

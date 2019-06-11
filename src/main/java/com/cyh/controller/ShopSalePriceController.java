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
	public List<ShopSalePrice> queryByDate(int year, int month, @RequestParam(required = false) String name) {
		LocalDate date = LocalDate.of(year, month, 01);
		return shopSalePriceService.filterName(shopSalePriceService.queryByDate(date), name);
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
	public void save(ShopSalePrice bean,int year,int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		bean.setDate(date);
		shopSalePriceService.saveOrUpdate(bean);
	}

}

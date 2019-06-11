package com.example.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cyh.SmallShopApplication;
import com.cyh.service.ShopSalePriceService;
import com.cyh.vo.ShopSalePrice;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SmallShopApplication.class })
@FixMethodOrder(MethodSorters.JVM)
public class ShopSaleServiceTest {

	@Autowired
	private ShopSalePriceService shopSaleService;

	@Test
	public void queryByDate() {
		shopSaleService.queryByDate(LocalDate.of(2019, 5, 1));
	}

	@Test
	public void initData() {
		List<ShopSalePrice> shopSales = shopSaleService.queryAll();
		for (ShopSalePrice bean : shopSales) {
//			bean.setDate(LocalDate.of(2019, 5, 1));
			bean.setPrice(BigDecimal.valueOf(20.15));
		}
		shopSaleService.saveOrUpdate(shopSales);
	}

	@Test
	public void delete() {
		ShopSalePrice bean = new ShopSalePrice();
		bean.setId("7c443953-ae65-45eb-bf8a-39155a33cada");
		shopSaleService.delete(bean);
	}
	
	@Test
	public void copyLastMonthDatas() {
		shopSaleService.copyLastMonthDatas();
	}
}

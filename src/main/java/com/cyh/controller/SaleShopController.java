package com.cyh.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyh.ExcelUtils;
import com.cyh.Utils;
import com.cyh.service.SaleShopService;
import com.cyh.service.ShopStockService;
import com.cyh.vo.SaleShop;

@RestController
public class SaleShopController {

	@Autowired
	private SaleShopService saleShopService;
	@Autowired
	private ShopStockService shopStockService;

	@PostMapping("/saleShop/queryByDate")
	public List<SaleShop> queryByDate(int year, int month, @RequestParam(required = false) int day,
			@RequestParam(required = false) String type) {
		if (day == 0) {
			LocalDate date = LocalDate.of(year, month, 1);
			return saleShopService.queryByMonth(date, type);
		} else {
			LocalDate date = LocalDate.of(year, month, day);
			return saleShopService.queryByDate(date, type);
		}
	}

	@PostMapping("/saleShop/queryByDate/export")
	public ResponseEntity<byte[]> exportBuyShop(HttpServletRequest request, int year, int month,
			@RequestParam(required = false) int day, @RequestParam(required = false) String type) throws Exception {
		String[] tableHeads = { "商品名称", "单价", "数量", "出售日期" };
		String fileName = "出售明细.xls";
		ServletContext servletContext = request.getServletContext();
		String realPath = servletContext.getRealPath("/" + fileName);//得到文件所在位置
		String sheetName = "sheet";
		List<SaleShop> datas = queryByDate(year, month, day, type);
		ExcelUtils.createExcel(tableHeads, sheetName, realPath, datas, (bean, row) -> {
			List<HSSFCell> cells = new ArrayList<>();
			HSSFCell cell0 = row.createCell(0, CellType.STRING);
			cells.add(cell0);
			cell0.setCellValue(bean.getName());
			HSSFCell cell1 = row.createCell(1, CellType.NUMERIC);
			cells.add(cell1);
			cell1.setCellValue(bean.getPrice().doubleValue());
			HSSFCell cell2 = row.createCell(2, CellType.NUMERIC);
			cells.add(cell2);
			cell2.setCellValue(bean.getCount().doubleValue());
			HSSFCell cell3 = row.createCell(3, CellType.STRING);
			cells.add(cell3);
			cell3.setCellValue(bean.getTime().format(DateTimeFormatter.ofPattern(Utils.YYYY_MM_DD)));
			return cells;
		});

		return ExcelUtils.export(fileName, realPath);
	}

	@PostMapping("/saleShop/save")
	public void save(SaleShop bean, int year, int month, int day) {
		LocalDate date = LocalDate.of(year, month, day);
		bean.setTime(date);
		saleShopService.saveOrUpdate(bean);
		shopStockService.updateStock(bean.getName(), date);
	}

	@PostMapping("/saleShop/delete")
	public void delete(SaleShop bean) {
		saleShopService.delete(bean);
		shopStockService.updateStock(bean.getName(), bean.getTime());

	}

}

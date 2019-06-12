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
import com.cyh.service.BuyShopService;
import com.cyh.service.ShopStockService;
import com.cyh.vo.BuyShop;
import com.cyh.vo.BuyShopAvg;

@RestController
public class BuyShopController {

	@Autowired
	private BuyShopService buyShopService;
	@Autowired
	private ShopStockService shopStockService;

	@PostMapping("/buyShop/queryByDate")
	public List<BuyShop> queryByDate(int year, int month, @RequestParam(required = false) int day,
			@RequestParam(required = false) String type) {
		if (day == 0) {
			LocalDate date = LocalDate.of(year, month, 1);
			return buyShopService.queryByMonth(date, type);
		} else {
			LocalDate date = LocalDate.of(year, month, day);
			return buyShopService.queryByDate(date, type);
		}
	}

	@PostMapping("/buyShop/queryByDate/export")
	public ResponseEntity<byte[]> exportBuyShop(HttpServletRequest request, int year, int month,
			@RequestParam(required = false) int day, @RequestParam(required = false) String type) throws Exception {
		String[] tableHeads = { "商品名称", "总价", "数量", "进货日期" };
		String fileName = "进货明细.xls";
		ServletContext servletContext = request.getServletContext();
		String realPath = servletContext.getRealPath("/" + fileName);//得到文件所在位置
		String sheetName = "sheet";
		List<BuyShop> datas = queryByDate(year, month, day, type);
		ExcelUtils.createExcel(tableHeads, sheetName, realPath, datas, (bean, row) -> {
			List<HSSFCell> cells = new ArrayList<>();
			HSSFCell cell0 = row.createCell(0, CellType.STRING);
			cells.add(cell0);
			cell0.setCellValue(bean.getName());
			HSSFCell cell1 = row.createCell(1, CellType.NUMERIC);
			cells.add(cell1);
			cell1.setCellValue(bean.getSumPrice().doubleValue());
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

	@PostMapping("/buyShop/avg/export")
	public ResponseEntity<byte[]> exportAvg(HttpServletRequest request, int year, int month,
			@RequestParam(required = false) String type) throws Exception {
		String[] tableHeads = { "商品名称", "总价", "总数量", "均价" };
		String fileName = "进货均价统计.xls";
		ServletContext servletContext = request.getServletContext();
		String realPath = servletContext.getRealPath("/" + fileName);//得到文件所在位置
		List<BuyShopAvg> datas = avg(year, month, type);
		String sheetName = year + "-" + month;
		ExcelUtils.createExcel(tableHeads, sheetName, realPath, datas, (bean, row) -> {
			List<HSSFCell> cells = new ArrayList<>();
			HSSFCell cell0 = row.createCell(0, CellType.STRING);
			cells.add(cell0);
			cell0.setCellValue(bean.getName());
			HSSFCell cell1 = row.createCell(1, CellType.NUMERIC);
			cells.add(cell1);
			cell1.setCellValue(bean.getSumPrice().doubleValue());
			HSSFCell cell2 = row.createCell(2, CellType.NUMERIC);
			cells.add(cell2);
			cell2.setCellValue(bean.getSumCount().doubleValue());
			HSSFCell cell3 = row.createCell(3, CellType.NUMERIC);
			cells.add(cell3);
			cell3.setCellValue(bean.getAvgPrice().doubleValue());
			return cells;

		});

		return ExcelUtils.export(fileName, realPath);
	}

	@PostMapping("/buyShop/save")
	public void save(BuyShop bean, int year, int month, int day) {
		LocalDate date = LocalDate.of(year, month, day);
		bean.setTime(date);
		buyShopService.saveOrUpdate(bean);
		shopStockService.updateStock(bean.getName(), date);
	}

	@PostMapping("/buyShop/delete")
	public void delete(BuyShop bean) {
		buyShopService.delete(bean);
		shopStockService.updateStock(bean.getName(), bean.getTime());

	}

	@PostMapping("/buyShop/avg")
	public List<BuyShopAvg> avg(int year, int month, @RequestParam(required = false) String type) {
		LocalDate date = LocalDate.of(year, month, 01);
		return buyShopService.avg(date, type);
	}

}

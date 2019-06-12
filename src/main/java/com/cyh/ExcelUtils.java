package com.cyh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExcelUtils {

	/* 
	 * 列头单元格样式
	 */
	public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

		// 设置字体
		HSSFFont font = workbook.createFont();
		//设置字体大小
		font.setFontHeightInPoints((short) 11);
		//字体加粗
		font.setBold(true);
		//设置字体名字 
		font.setFontName("Courier New");
		//设置样式; 
		HSSFCellStyle style = workbook.createCellStyle();
		//设置底边框; 
		style.setBorderBottom(BorderStyle.THIN);
		//设置底边框颜色;  
		style.setBottomBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//设置左边框;   
		style.setBorderLeft(BorderStyle.THIN);
		//设置左边框颜色; 
		style.setLeftBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//设置右边框; 
		style.setBorderRight(BorderStyle.THIN);
		//设置右边框颜色; 
		style.setRightBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//设置顶边框; 
		style.setBorderTop(BorderStyle.THIN);
		//设置顶边框颜色;  
		style.setTopBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//在样式用应用设置的字体;  
		style.setFont(font);
		//设置自动换行; 
		style.setWrapText(false);
		//设置水平对齐的样式为居中对齐;  
		style.setAlignment(HorizontalAlignment.CENTER);
		//设置垂直对齐的样式为居中对齐; 
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		//设置单元格背景颜色
		style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return style;

	}

	/*  
	* 列数据信息单元格样式
	*/
	public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		//设置字体大小
		//font.setFontHeightInPoints((short)10);
		//字体加粗
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		//设置字体名字 
		font.setFontName("Courier New");
		//设置样式; 
		HSSFCellStyle style = workbook.createCellStyle();
		//设置底边框; 
		style.setBorderBottom(BorderStyle.THIN);
		//设置底边框颜色;  
		style.setBottomBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//设置左边框;   
		style.setBorderLeft(BorderStyle.THIN);
		//设置左边框颜色; 
		style.setLeftBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//设置右边框; 
		style.setBorderRight(BorderStyle.THIN);
		//设置右边框颜色; 
		style.setRightBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//设置顶边框; 
		style.setBorderTop(BorderStyle.THIN);
		//设置顶边框颜色;  
		style.setTopBorderColor(HSSFColorPredefined.BLACK.getIndex());
		//在样式用应用设置的字体;  
		style.setFont(font);
		//设置自动换行; 
		style.setWrapText(false);
		//设置水平对齐的样式为居中对齐;  
		style.setAlignment(HorizontalAlignment.CENTER);
		//设置垂直对齐的样式为居中对齐; 
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		return style;
	}

	public static <T> void createExcel(String[] tableHeads, String sheetName, String filePath, List<T> datas,
			BiFunction<T, HSSFRow, List<HSSFCell>> func) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFRow rowm = sheet.createRow(0);
		rowm.setHeight((short) (25 * 25)); //设置高度
		HSSFCellStyle columnTopStyle = ExcelUtils.getColumnTopStyle(workbook);//获取列头样式对象
		HSSFCellStyle style = ExcelUtils.getStyle(workbook); //单元格样式对象
		for (int i = 0; i < tableHeads.length; i++) {
			HSSFCell cellRowName = rowm.createCell(i); //创建列头对应个数的单元格
			cellRowName.setCellType(CellType.STRING); //设置列头单元格的数据类型
			HSSFRichTextString text = new HSSFRichTextString(tableHeads[i]);
			cellRowName.setCellValue(text); //设置列头单元格的值
			cellRowName.setCellStyle(columnTopStyle); //设置列头单元格样式
		}
		for (int i = 0; i < datas.size(); i++) {
			T data = datas.get(i);
			HSSFRow row = sheet.createRow(i + 1);//创建所需的行数
			row.setHeight((short) (25 * 20)); //设置高度

			List<HSSFCell> cells = func.apply(data, row);

			for (HSSFCell cell : cells) {
				cell.setCellStyle(style);
			}
		}

		ExcelUtils.autoWidth(tableHeads, sheet);

		try (FileOutputStream out = new FileOutputStream(filePath)) {
			workbook.write(out);
		}
	}

	private static void autoWidth(String[] tableHeads, HSSFSheet sheet) {
		//让列宽随着导出的列长自动适应
		for (int colNum = 0; colNum < tableHeads.length; colNum++) {
			int columnWidth = sheet.getColumnWidth(colNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				HSSFRow currentRow;
				//当前行未被使用过
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}
				if (currentRow.getCell(colNum) != null) {
					HSSFCell currentCell = currentRow.getCell(colNum);
					if (currentCell.getCellType() == CellType.STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);

		}
	}

	public static ResponseEntity<byte[]> export(String fileName, String realPath) throws Exception {
		try (InputStream in = new FileInputStream(new File(realPath))) {//将该文件加入到输入流之中
			byte[] body = null;
			body = new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
			in.read(body);//读入到输入流里面

			fileName = new String(fileName.getBytes("gbk"), "iso8859-1");//防止中文乱码
			HttpHeaders headers = new HttpHeaders();//设置响应头
			headers.add("Content-Disposition", "attachment;filename=" + fileName);
			HttpStatus statusCode = HttpStatus.OK;//设置响应吗
			return new ResponseEntity<byte[]>(body, headers, statusCode);
		}
	}
}

package com.cyh.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cyh.service.BuyShopService;
import com.cyh.vo.BuyShop;

@RestController
public class BuyShopController {

	@Autowired
	private BuyShopService buyShopService;

	@PostMapping("/buyShop/queryByDate")
	public List<BuyShop> queryByDate(int year, int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		return buyShopService.queryByDate(date);
	}

	public void save(BuyShop bean) {
		buyShopService.saveOrUpdate(bean);
	}

	public void importExcel(@RequestParam("file") MultipartFile uploadFile, int year, int month, int day) {

	}

	public void delete(BuyShop bean) {
		buyShopService.delete(bean);

	}

	public ResponseEntity<byte[]> export(HttpServletRequest request, int year, int month) throws Exception {
		ServletContext servletContext = request.getServletContext();
		String fileName = "童话镇.mp3";
		String realPath = servletContext.getRealPath("/WEB-INF/" + fileName);//得到文件所在位置
		try (InputStream in = new FileInputStream(new File(realPath))) {//将该文件加入到输入流之中
			byte[] body = null;
			body = new byte[in.available()];// 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
			in.read(body);//读入到输入流里面

			fileName = new String(fileName.getBytes("gbk"), "iso8859-1");//防止中文乱码
			HttpHeaders headers = new HttpHeaders();//设置响应头
			headers.add("Content-Disposition", "attachment;filename=" + fileName);
			HttpStatus statusCode = HttpStatus.OK;//设置响应吗
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, statusCode);
			return response;
		}

	}
}

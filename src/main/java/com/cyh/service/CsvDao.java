package com.cyh.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.cyh.CsvFile;
import com.cyh.Utils;
import com.cyh.vo.CsvEntity;

public abstract class CsvDao<T extends CsvEntity> {

	public void saveOrUpdate(T bean) {
		List<T> list = new ArrayList<>();
		list.add(bean);
		saveOrUpdate(list);
	}

	public void delete(T bean) {
		List<T> list = new ArrayList<>();
		list.add(bean);
		delete(list);
	}

	public void delete(List<T> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		String filePath = Utils.getCsv(list.get(0).getClass());
		CsvFile<T> all = loadAll(list.get(0));
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
				BufferedWriter br = new BufferedWriter(writer)) {
			all.getDatas().removeAll(list);
			writer.write(all.getHeads());
			writer.write("\r\n");
			for (T bean : all.getDatas()) {
				writer.write(bean.getCsvLine());
				writer.write("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdate(List<T> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		String filePath = Utils.getCsv(list.get(0).getClass());
		CsvFile<T> all = loadAll(list.get(0));
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
				BufferedWriter br = new BufferedWriter(writer)) {
			all.getDatas().removeAll(list);
			all.getDatas().addAll(list);
			writer.write(all.getHeads());
			writer.write("\r\n");
			for (T bean : all.getDatas()) {
				writer.write(bean.getCsvLine());
				writer.write("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CsvFile<T> loadAll(T bean) {
		CsvFile<T> result = new CsvFile<>();
		String filePath = Utils.getCsv(bean.getClass());
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(reader);) {
			//读表头
			result.setHeads(br.readLine());
			// 读内容
			String content = br.readLine();
			while (content != null) {
				result.addData(toEntity(content));
				content = br.readLine();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}

	}

	protected abstract T toEntity(String csvLine);

}

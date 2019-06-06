package com.cyh.dao;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.cyh.Utils;
import com.cyh.vo.CsvEntity;

public abstract class CsvDao<T extends CsvEntity> {

	public void save(T bean) {
		List<T> list = new ArrayList<>();
		list.add(bean);
		save(list);
	}

	public void delete(T bean) {
		String filePath = Utils.getCsv(bean.getClass());
		CsvWriter csvWriter = new CsvWriter(filePath, ',', StandardCharsets.UTF_8);
		try {
			List<T> all = loadAll(bean);
			all.remove(bean);
			for (T b : all) {
				csvWriter.writeRecord(b.getCsvLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			csvWriter.close();
		}
	}

	public void save(List<T> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		String filePath = Utils.getCsv(list.get(0).getClass());
		CsvWriter csvWriter = new CsvWriter(filePath, ',', StandardCharsets.UTF_8);
		try {
			List<T> all = loadAll(list.get(0));
			all.removeAll(list);
			all.addAll(list);
			for (T bean : all) {
				csvWriter.writeRecord(bean.getCsvLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			csvWriter.close();
		}
	}

	public List<T> loadAll(T bean) {
		List<T> results = new ArrayList<>();
		String filePath = Utils.getCsv(bean.getClass());
		try {
			CsvReader csvReader = new CsvReader(filePath);
			// 读表头
			csvReader.readHeaders();
			// 读内容
			while (csvReader.readRecord()) {
				results.add(toEntity(csvReader.getRawRecord()));
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	protected abstract T toEntity(String csvLine);

}

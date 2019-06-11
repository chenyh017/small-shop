package com.cyh;

import java.util.ArrayList;
import java.util.List;

public class CsvFile<T> {

	private String heads;
	private List<T> datas = new ArrayList<>();

	public String getHeads() {
		return heads;
	}

	public void setHeads(String heads) {
		this.heads = heads;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public void addData(T entity) {
		datas.add(entity);
	}

}

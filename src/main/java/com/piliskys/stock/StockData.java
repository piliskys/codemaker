package com.piliskys.stock;

public class StockData {
	private String code; // ��Ʊ����
	private String name; // ��Ʊ����
	private String date; // ��������
	private double open = 0.0; // ���̼�
	private double high = 0.0; // ��߼�
	private double low = 0.0; // ��ͼ�
	private double close = 0.0; // ���һ�ν��׼۸��൱�����̼�
	private double volume = 0.0;// �ܽ�����
	private double adj = 0.0; // ���һ�ν��׼۸� (��������̼۵�����Ȩ�۸�)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getAdj() {
		return adj;
	}
	public void setAdj(double adj) {
		this.adj = adj;
	}
	@Override
	public String toString() {
		return "StockData [code=" + code + ", name=" + name + ", date=" + date
				+ ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", volume=" + volume + ", adj=" + adj
				+ "]";
	}

	
	

}

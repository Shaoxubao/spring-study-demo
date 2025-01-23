package com.baoge.utils;

import org.apache.poi.ss.usermodel.CellStyle;

import java.io.Serializable;

/**
 * 模板for each是的参数
 * 
 */
public class ForEachCells implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * key
	 */
	private String name;
	/**
	 * cell对应的map值
	 */
	private String mapValue;
	/**
	 * 模板的cellStyle
	 */
	private CellStyle cellStyle;
	/**
	 * 行高
	 */
	private short height;
	/**
	 * 常量值
	 */
	private String constValue;
	/**
	 * 列合并
	 */
	private int colspan = 1;
	/**
	 * 行合并
	 */
	private int rowspan = 1;
	/**
	 * 列号
	 */
	private int colnum = 1;
	/**
	 * 行号
	 */
	private int rownum = 1;

	private boolean needSum;
	
	CellData cellData;

	public ForEachCells() {

	}

	public ForEachCells(String name, CellStyle cellStyle, short height) {
		this.name = name;
		this.cellStyle = cellStyle;
		this.height = height;
	}

	public ForEachCells(String name, CellStyle cellStyle, short height, boolean needSum) {
		this.name = name;
		this.cellStyle = cellStyle;
		this.height = height;
		this.needSum = needSum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CellStyle getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public String getConstValue() {
		return constValue;
	}

	public void setConstValue(String constValue) {
		this.constValue = constValue;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public boolean isNeedSum() {
		return needSum;
	}

	public void setNeedSum(boolean needSum) {
		this.needSum = needSum;
	}

	public int getColnum() {
		return colnum;
	}

	public void setColnum(int colnum) {
		this.colnum = colnum;
	}

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public String getMapValue() {
		return mapValue;
	}

	public void setMapValue(String mapValue) {
		this.mapValue = mapValue;
	}

	public CellData getCellData() {
		return cellData;
	}

	public void setCellData(CellData cellData) {
		this.cellData = cellData;
	}

}

package com.baoge.utils;

import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForEachBlock {
	// foreach块的起始行
	int firstRow = 0;
	// foreach块的结束行
	int lastRow = 0;
	// foreach块的起始列
	int firstCol = 0;
	// foreach块的结束列
	int lastCol = 0;
	// 偏移的行数
	int shiftRow = 0;
	// foreach块的cell集合
	String ListName;
	
	public String getListName() {
		return ListName;
	}
	public void setListName(String listName) {
		ListName = listName;
	}

	ArrayList<ArrayList<ForEachCells>> listForEachCells = new ArrayList<ArrayList<ForEachCells>>();
	// foreach块的合并单元格集合
	Map<String, CellRangeAddress> listCellRangeAddress = new HashMap<String, CellRangeAddress>();
	
	public void addMergedRegion(CellRangeAddress mergedRegion)
	{
		this.listCellRangeAddress.put(mergedRegion.formatAsString(), mergedRegion);
	}
	public CellRangeAddress  getMergedRegion(String key)
	{
		return this.listCellRangeAddress.get(key);
	}
	
	public void addRow(ArrayList<ForEachCells> row)
	{
		this.listForEachCells.add(row);
	}
	public ArrayList<ForEachCells>  getRow(int index)
	{
		return this.listForEachCells.get(index);
	}
	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	public int getFirstCol() {
		return firstCol;
	}

	public void setFirstCol(int firstCol) {
		this.firstCol = firstCol;
	}

	public int getLastCol() {
		return lastCol;
	}

	public void setLastCol(int lastCol) {
		this.lastCol = lastCol;
	}

	public int getShiftRow() {
		return shiftRow;
	}

	public void setShiftRow(int shiftRow) {
		this.shiftRow = shiftRow;
	}

	
	public ArrayList<ArrayList<ForEachCells>> getListForEachCells() {
		return listForEachCells;
	}

	public void setListForEachCells(ArrayList<ArrayList<ForEachCells>> listForEachCells) {
		this.listForEachCells = listForEachCells;
	}

	public Map<String, CellRangeAddress> getListCellRangeAddress() {
		return listCellRangeAddress;
	}

	public void setListCellRangeAddress(Map<String, CellRangeAddress> listCellRangeAddress) {
		this.listCellRangeAddress = listCellRangeAddress;
	}

}

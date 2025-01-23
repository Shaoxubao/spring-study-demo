package com.baoge.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CellData {
	public static final String LENGTH = "le:";
	public static final String FOREACH = "fe:";
	public static final String FOREACH_NOT_CREATE = "!fe:";
	public static final String FOREACH_AND_SHIFT = "$fe:";
	public static final String FOREACH_COL = "#fe:";
	public static final String FOREACH_COL_VALUE = "v_fe:";
	public static final String START_STR = "{{";
	public static final String END_STR = "}}";
	public static final String WRAP = "]]";
	public static final String NUMBER_SYMBOL = "n:";
	public static final String FORMAT_DATE = "fd:";
	public static final String FORMAT_NUMBER = "fn:";
	public static final String SUM = "sum:";
	public static final String IF_DELETE = "!if:";
	public static final String EMPTY = "";
	public static final String CONST = "'";
	public static final String NULL = "&NULL&";
	public static final String LEFT_BRACKET = "(";
	public static final String RIGHT_BRACKET = ")";
	public static final String CAL = "cal:";
	// 按指定模式在字符串查找
	String line = "This order was placed for QT3000! OK?";
	// String pattern = "(\\D*)(\\d+)(.*)";
	TemplateType celltype;
	String cellMapName;
	String ListName;

	public TemplateType getCelltype() {
		return celltype;
	}

	public void setCelltype(TemplateType celltype) {
		this.celltype = celltype;
	}

	public String getCellMapName() {
		return cellMapName;
	}

	public void setCellMapName(String cellMapName) {
		this.cellMapName = cellMapName;
	}

	public String getListName() {
		return ListName;
	}

	public void setListName(String listName) {
		ListName = listName;
	}

	public TemplateType parseCell(String Cellstr) {
		if (parseCellRegStr(Cellstr) != TemplateType.NOMATCH)
			return this.celltype;
		if (parseFeCellStr(Cellstr) != TemplateType.NOMATCH)
			return this.celltype;
		if (parseCellStr(Cellstr) != TemplateType.NOMATCH)
			return this.celltype;
		;
		if (parseFeEndCellStr(Cellstr) != TemplateType.NOMATCH)
			return this.celltype;
		this.cellMapName = Cellstr;
		return this.celltype = TemplateType.NOMATCH;
	}

	public TemplateType parseFeCellStr(String Cellstr) {
		String pattern = "(\\{\\{[$#!]fe:\\s+)([a-zA-Z_$][a-zA-Z0-9_$]*)(\\s+t\\.[a-zA-Z_$][a-zA-Z0-9_$]*)?";

		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(Cellstr);
		if (m.find()) {
			this.celltype = TemplateType.LOOPBEGEN;
			this.ListName = m.group(2);
			if (m.group(3) != null) {
				this.cellMapName =getparseCellStr( m.group(3));
				this.celltype = TemplateType.LOOPBEGENWITHDATA;
			} else {
				this.cellMapName = null;

			}
			return this.celltype;
		} else {
			this.celltype = TemplateType.NOMATCH;
			return TemplateType.NOMATCH;
		}
	}

	public TemplateType parseCellStr(String Cellstr) {
		String pattern = "(t\\.)([a-zA-Z_$][a-zA-Z0-9_$]*)";
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(Cellstr);
		if (m.find()) {

			this.celltype = TemplateType.ISLISTFIELD;
			this.cellMapName = m.group(2);
			return this.celltype;
		} else {
			this.celltype = TemplateType.NOMATCH;
			return TemplateType.NOMATCH;
		}

	}

	public String getparseCellStr(String Cellstr) {
		String pattern = "(t\\.)([a-zA-Z_$][a-zA-Z0-9_$]*)";
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(Cellstr);
		if (m.find()) {

			this.cellMapName = m.group(2);
			return this.cellMapName;
		} else {
			
			return null;
		}

	}
	public TemplateType parseFeEndCellStr(String Cellstr) {
		String pattern = "\\}\\}";
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(Cellstr);
		if (m.find()) {

			this.celltype = TemplateType.LOOPEND;

			return this.celltype;
		} else {
			this.celltype = TemplateType.NOMATCH;
			return TemplateType.NOMATCH;
		}

	}

	public TemplateType parseCellRegStr(String Cellstr) {
		String pattern = "(\\{\\{)([a-zA-Z_$][a-zA-Z0-9_$]*)(\\}\\})";
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(Cellstr);
		if (m.find()) {
			this.celltype = TemplateType.ISMAPFIELD;
			this.cellMapName = m.group(2);
			return this.celltype;
		} else {
			this.celltype = TemplateType.NOMATCH;
			return TemplateType.NOMATCH;
		}

	}

}

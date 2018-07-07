package com.rick.dev.plugin.office.excel.excel2007;

import com.rick.dev.plugin.office.excel.Builder;
import com.rick.dev.plugin.office.excel.excel2007.ExcelCell.ExcelCellBuilder;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Rick.Xu
 *
 */
public class ExcelWorkbook {
	private XSSFWorkbook book = new XSSFWorkbook();
	
	private CellThemes theme = new CellThemes(book);

	private Map<XSSFSheet,Map<Integer,XSSFRow>> sheetList = new LinkedHashMap<XSSFSheet,Map<Integer,XSSFRow>>();
	
	private Map<XSSFSheet,List<CellRangeAddress>> border = new LinkedHashMap<XSSFSheet,List<CellRangeAddress>>();
	
	private List<XSSFSheet> sheets = new ArrayList<XSSFSheet>();
	
	public List<XSSFSheet> getSheets() {
		return Collections.unmodifiableList(sheets);
	}
	
	public XSSFCellStyle createStyle() {
		XSSFCellStyle style =  book.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
		style.setWrapText(true);
		return style;
	}
	
	
	public XSSFFont createFont() {
		return book.createFont();
	}
	

	public CellThemes getTheme() {
		return theme;
	}

	public ExcelWorkbook() {
		
	}
	
	/**
	 * name can be dup
	 * @param name
	 * @return
	 */
	public XSSFSheet createSheet(String name) {
		XSSFSheet sheet = book.createSheet(name);
		sheets.add(sheet);
		return sheet;
	}
	
	/**
	 * 创建单元格
	 * @param ecell
	 */
	public void createCell(XSSFSheet sheet, Builder<ExcelCell> builder) {
		ExcelCell ecell = builder.build();
		CellRangeAddress region = null;
		
		region = new CellRangeAddress(ecell.getY(), ecell.getY()+ecell.getHeight()-1, ecell.getX(), ecell.getX()+  ecell.getWidth()-1);
		
		sheet.addMergedRegion(region);
		
		Map<Integer,XSSFRow> rowMap = sheetList.get(sheet);
		if(rowMap == null) {
			rowMap = new HashMap<Integer, XSSFRow>();
			sheetList.put(sheet, rowMap);
			
			List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
			border.put(sheet, list);
		}
		
		
		XSSFRow row = rowMap.get(ecell.getY());
		if(row == null) {
			row = sheet.createRow(ecell.getY());
			rowMap.put(ecell.getY(), row);
			row.setHeightInPoints(ecell.getHeightInPoints());
		} else {
			if(ecell.getHeightInPoints()> row.getHeightInPoints())
				row.setHeightInPoints(ecell.getHeightInPoints());
		}
		
		
		XSSFCell cell = row.createCell(ecell.getX());
		
		cell.setCellValue(ecell.getValue());
		
		XSSFCellStyle style = null;
		if(ecell.getStyle() != null) {
			style = ecell.getStyle();
			cell.setCellStyle(style);
		} 
		
		//设置边框
		if(cell.getCellStyle().getBorderBottom() > 0 ) {
			List<CellRangeAddress> list = border.get(sheet);
			list.add(region);
		} 
	}
	
	public void createRow(XSSFSheet sheet, Builder<ExcelRow> builder) {
		ExcelRow row = builder.build();
		String[] header = row.getValues();
		int len = header.length;
		for (int i = 0; i <len; i++) {
			createCell(sheet, new ExcelCellBuilder(row.getX()+i, row.getY(), header[i]).heightInPoints(row.getHeightInPoints()).style(row.getStyle()));
		}
	}
	
	/**
	 * 设置列的宽度
	 * @param width
	 */
	public void setColumnWidth(XSSFSheet sheet,int[] width) {
		for(int i = 0 ; i < width.length; i++) {
			sheet.setColumnWidth(i, width[i]);
		}
	}
	
	
	/**
	 * 设置边框
	 * @param region
	 */
	 private void setRegionStyle(XSSFSheet sheet,CellRangeAddress region) {
		//全部完成之后
		XSSFRow frow = XSSFCellUtil.getRow(region.getFirstRow(), sheet);
		XSSFCell fcell = XSSFCellUtil.getCell(frow, region.getFirstColumn());
		
		XSSFCellStyle style = fcell.getCellStyle();
		for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
			XSSFRow row = XSSFCellUtil.getRow(i, sheet);
			for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
				XSSFCell cell = XSSFCellUtil.getCell(row, j);
				cell.setCellStyle(style);
			}
		}
	 }
	 
	public void write(File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		write(fos);
	}
	
	public void write(OutputStream os) throws Exception {
		Set<Entry<XSSFSheet, List<CellRangeAddress>>> set = border.entrySet();
		Iterator<Entry<XSSFSheet, List<CellRangeAddress>>> it = set.iterator();
		while(it.hasNext()) {
			Entry<XSSFSheet, List<CellRangeAddress>> en = it.next();
			
			XSSFSheet sheet = en.getKey();
			List<CellRangeAddress> list = en.getValue();
			
			for(CellRangeAddress cr :list) {
				setRegionStyle(sheet, cr);
			}
		}
		
		book.write(os);
		os.flush();
		os.close();
	}
}

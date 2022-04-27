package com.demo.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;


public class ExcelRW {

	public Workbook wb;
	public Sheet sh;
	
	public FileOutputStream fos;
	public FileInputStream fis;
	
	
	public Row row;
	public Cell cell;
	
	
	public void writeLinkToExcel(String dId,String fileLink) throws EncryptedDocumentException, IOException {
		
		
		fis=new FileInputStream("./FileDBExcelSheet.xlsx");
		
		wb=WorkbookFactory.create(fis);
		
		sh=wb.getSheet("sheet1");
		int noOfRows=sh.getLastRowNum();
		
		System.out.println();
		for(int i=1;i<=noOfRows;i++) {
			
			Cell devid=sh.getRow(i).getCell(0);
			if(devid.getStringCellValue().equals(dId)) {
				
				System.out.println("ROW VALUE WAS:"+sh.getRow(i).getCell(0));
				System.out.println("ROW VALUE WAS:"+sh.getRow(i).getCell(1));
				
				cell=sh.getRow(i).createCell(1);
				cell.setCellValue(fileLink);
				
				CellStyle hlinkstyle = wb.createCellStyle();
			      Font hlinkfont = wb.createFont();
			      hlinkfont.setUnderline(XSSFFont.U_SINGLE);
			      hlinkfont.setColor(IndexedColors.BLUE.index);
			      hlinkstyle.setFont(hlinkfont);
			      
				CreationHelper helper
	            = wb.getCreationHelper();
				XSSFHyperlink link= (XSSFHyperlink)helper.createHyperlink(HyperlinkType.URL);
				link.setAddress(fileLink);
				cell.setHyperlink((XSSFHyperlink)link);
				cell.setCellStyle(hlinkstyle);
				
				System.out.println("\n After Change:"+sh.getRow(i).getCell(1));
				fos=new FileOutputStream("./FileDBExcelSheet.xlsx");
				wb.write(fos);
				fos.flush();
			}
			
		}
		
		
	}
	
	
	
	
	
	
}

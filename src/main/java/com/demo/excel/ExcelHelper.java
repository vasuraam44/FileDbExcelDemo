package com.demo.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.demo.model.Device;

public class ExcelHelper {
	
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "DeviceNo", "Link" };
	  static String SHEET = "sheet1";
	
	  public static boolean hasExcelFormat(MultipartFile file) {
		    if (!TYPE.equals(file.getContentType())) {
		      return false;
		    }
		    return true;
		  }
	  
	  
	  
	  public static List<Device> excelToTutorials(InputStream is) {
		    try {
		      Workbook workbook = new XSSFWorkbook(is);
		      Sheet sheet = workbook.getSheet(SHEET);
		      Iterator<Row> rows = sheet.iterator();
		      List<Device> devices = new ArrayList<Device>();
		      int rowNumber = 0;
		      while (rows.hasNext()) {
		        Row currentRow = rows.next();
		        // skip header
		        if (rowNumber == 0) {
		          rowNumber++;
		          continue;
		        }
		        Iterator<Cell> cellsInRow = currentRow.iterator();
		        Device device = new Device();
		        int cellIdx = 0;
		        while (cellsInRow.hasNext()) {
		          Cell currentCell = cellsInRow.next();
		          switch (cellIdx) {
		          case 0:
		        	  device.setId((long) currentCell.getNumericCellValue());
		            break;
		          case 1:
		        	  device.setDeviceNo(currentCell.getStringCellValue());
		            break;
		          case 2:
		        	  device.setUrl(currentCell.getStringCellValue());
		            break;
		      
		          default:
		            break;
		          }
		          cellIdx++;
		        }
		        devices.add(device);
		      }
		      workbook.close();
		      return devices;
		    } catch (IOException e) {
		      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		    }
		  }
	  
	  

}

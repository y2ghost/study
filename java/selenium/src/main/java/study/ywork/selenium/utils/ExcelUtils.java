package study.ywork.selenium.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {
    public static HSSFWorkbook openExcelFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        return workbook;
    }

    public static HSSFSheet getSheet(HSSFWorkbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public static String getCellValue(HSSFSheet sheet, int rowNumber, int cellNumber) {
        HSSFCell cell = sheet.getRow(rowNumber).getCell(cellNumber);
        return cell.getStringCellValue();
    }

    public static int getRowCount(HSSFSheet sheet) {
        return sheet.getLastRowNum() - sheet.getFirstRowNum();
    }

    public static void setCellValue(HSSFSheet sheet, int rowNum, int cellNum, String cellValue) {
        sheet.getRow(rowNum).createCell(cellNum).setCellValue(cellValue);
    }
    
    public static void writeFile(HSSFWorkbook workbook) throws IOException {
        workbook.write();     
    }

    private ExcelUtils() {
    }
}

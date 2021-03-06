package com.xinleju.cloud.oa.office.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.xinleju.cloud.oa.office.dto.OfficeRecordDto;
  
  
public class ExportExcelUtil {  
      
    /** 
     * 描述：根据文件路径获取项目中的文件 
     * @param fileDir 文件路径 
     * @return 
     * @throws Exception 
     */  
    public  File getExcelDemoFile(String fileDir) throws Exception{  
        String classDir = null;  
        String fileBaseDir = null;  
        File file = null;  
        classDir = Thread.currentThread().getContextClassLoader().getResource("/").getPath();  
        fileBaseDir = classDir.substring(0, classDir.lastIndexOf("classes"));  
          
        file = new File(fileBaseDir+fileDir);  
        if(!file.exists()){  
        	file.createNewFile();
        }  
        return file;  
    }  
      
    public  Workbook writeNewExcel(File file,String sheetName,List<OfficeRecordDto> lis) throws Exception{  
        Workbook wb = null;  
        Row row = null;   
        Cell cell = null;  
          
        FileInputStream fis = new FileInputStream(file);  
        wb = new ImportExcelUtil().getWorkbook(fis, file.getName());    //获取工作薄  
        Sheet sheet = wb.getSheet(sheetName);  
          
        //循环插入数据  
        int lastRow = sheet.getLastRowNum()+1;    //插入数据的数据ROW  
        CellStyle cs = setSimpleCellStyle(wb);    //Excel单元格样式  
        for (int i = 0; i < lis.size(); i++) {  
            row = sheet.createRow(lastRow+i); //创建新的ROW，用于数据插入  
              
            //按项目实际需求，在该处将对象数据插入到Excel中  
            OfficeRecordDto vo  = lis.get(i);  
            if(null==vo){  
                break;  
            }  
            //Cell赋值开始  
            cell = row.createCell(0); 
            if(vo.getStockName() != null && !"".equals(vo.getStockName())){
            	cell.setCellValue(vo.getStockName()); 
            }else{
            	cell.setCellValue("");
            }
            cell.setCellStyle(cs);  
              
            cell = row.createCell(1); 
      /*      if(vo.getHouseName() != null && !"".equals(vo.getHouseName())){
            	cell.setCellValue(vo.getHouseName());  
            }else{
            	cell.setCellValue("");   
            }*/
            cell.setCellStyle(cs);  
              
            cell = row.createCell(2); 
            if(vo.getStockNum() != null && !"".equals(vo.getStockNum())){
            	 cell.setCellValue(vo.getStockNum());    
            }else{
            	 cell.setCellValue("");    
            }
            cell.setCellStyle(cs);  
              
            cell = row.createCell(3);  
            if(vo.getInCount() != null && !"".equals(vo.getInCount())){
            	cell.setCellValue(vo.getInCount());  
            }else{
            	cell.setCellValue("");  
            }
            cell.setCellStyle(cs);  
        }  
        return wb;  
    }  
      
    /** 
     * 描述：设置简单的Cell样式 
     * @return 
     */  
    public  CellStyle setSimpleCellStyle(Workbook wb){  
        CellStyle cs = wb.createCellStyle();  
          
        cs.setBorderBottom(CellStyle.BORDER_THIN); //下边框  
        cs.setBorderLeft(CellStyle.BORDER_THIN);//左边框  
        cs.setBorderTop(CellStyle.BORDER_THIN);//上边框  
        cs.setBorderRight(CellStyle.BORDER_THIN);//右边框  
  
        cs.setAlignment(CellStyle.ALIGN_CENTER); // 居中  
          
        return cs;  
    }  
    public static void main(String[] args) throws Exception {  
		Workbook wb = null;  
		Row row = null;   
		Cell cell = null; 
		String filePath="D:\\办公用品导入模板.xlsx";
		File file=new File(filePath);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			wb = ImportExcelUtil.getWorkbook(fis, file.getName());    //获取工作薄  
		} catch (FileNotFoundException e1) {
			//e1.printStackTrace();
		}  
		Sheet sheet1 = wb.getSheet("Sheet1"); 
		String[] dlData ={"办公","用品","232","12","00","789"};
		//参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
		sheet1.addValidationData(setDataValidation(sheet1, dlData, 4, 5000,2,2)); 

		FileOutputStream out = new FileOutputStream(file);
		out.flush();  
		wb.write(out);    
		out.close(); 
		System.err.println("123");
	}  

	/**
	 * 设置下拉菜单
	 * @param filePath excel文件路径
	 * @param dlData 下拉菜单数据
	 * @throws Exception
	 */
	public static void setDataValidation(String filePath,String[] dlData) throws Exception {
		File file=new File(filePath);
		Workbook wb = null;  
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			wb = ImportExcelUtil.getWorkbook(fis, file.getName());
		} catch (FileNotFoundException e1) {
			//e1.printStackTrace();
		}  catch (Exception e) {
			////e.printStackTrace();
		}
		Sheet sheet1 = wb.getSheet("Sheet1"); 
//		String[] dlData ={"办公","用品","232","12","00","789"};
		//参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
		sheet1.addValidationData(setDataValidation(sheet1, dlData, 4, 5000,2,2)); 

		FileOutputStream out = new FileOutputStream(file);
		out.flush();  
		wb.write(out);    
		out.close();
	}
	/**
	 * 
	 * @Title: SetDataValidation 
	 * @Description: 下拉列表元素很多的情况 (255以上的下拉)
	 * @param @param strFormula
	 * @param @param firstRow   起始行
	 * @param @param endRow     终止行
	 * @param @param firstCol   起始列
	 * @param @param endCol     终止列
	 * @param @return
	 * @return HSSFDataValidation
	 * @throws
	 */
	private static HSSFDataValidation SetDataValidation(String strFormula, 
			int firstRow, int endRow, int firstCol, int endCol) {

		// 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
		HSSFDataValidation dataValidation = new HSSFDataValidation(regions,constraint);

		dataValidation.createErrorBox("Error", "Error");
		dataValidation.createPromptBox("", null);

		return dataValidation;
	}


	/**
	 * 
	 * @Title: setDataValidation 
	 * @Description: 下拉列表元素不多的情况(255以内的下拉)
	 * @param @param sheet
	 * @param @param textList
	 * @param @param firstRow
	 * @param @param endRow
	 * @param @param firstCol
	 * @param @param endCol
	 * @param @return
	 * @return DataValidation
	 * @throws
	 */
	private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {

		DataValidationHelper helper = sheet.getDataValidationHelper();
		//加载下拉列表内容
		DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
		//DVConstraint constraint = new DVConstraint();
		constraint.setExplicitListValues(textList);

		//设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol, (short) endCol);

		//数据有效性对象
		DataValidation data_validation = helper.createValidation(constraint, regions);
		//DataValidation data_validation = new DataValidation(regions, constraint);

		return data_validation;
	}
}  
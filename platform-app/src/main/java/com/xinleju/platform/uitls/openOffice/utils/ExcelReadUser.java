package com.xinleju.platform.uitls.openOffice.utils;

import com.xinleju.platform.finance.utils.excel.DateUtil;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.uitls.InvalidCustomException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取Excel
 *
 * @author lp
 */
public class ExcelReadUser {
    public int totalRows; //sheet中总行数
    public static int totalCells = 13; //每一行总单元格数

    /**
     * read the Excel .xlsx,.xls
     *
     * @param file jsp中的上传文件
     * @return
     * @throws IOException
     */
    public List<UserDto> readExcel(MultipartFile file) throws IOException {
        if (file == null || ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())) {
            return null;
        } else {
            String postfix = ExcelUtil.getPostfix(file.getOriginalFilename());
            if (!ExcelUtil.EMPTY.equals(postfix)) {
                if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(file);
                } else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(file);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * read the Excel 2010 .xlsx
     *
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public List<UserDto> readXlsx(MultipartFile file) {
        List<UserDto> list = new ArrayList<UserDto>();
        // IO流读取文件
        InputStream input = null;
        XSSFWorkbook wb = null;
        UserDto org = null;
        String cellVal = null;
        try {
            input = file.getInputStream();
            // 创建文档
            wb = new XSSFWorkbook(input);
            //excel 总页数，目前只获取第一页
            int totalNumSheet = 1;//wb.getNumberOfSheets();
            //读取sheet(页)
            for (int numSheet = 0; numSheet < totalNumSheet; numSheet++) {
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                //校验第一行
                XSSFRow firstRow = xssfSheet.getRow(0);
                if(firstRow == null){
                    break;
                }
                //校验excel格式
                boolean flag = checkExcel(firstRow);
                if(!flag){
                    throw new InvalidCustomException("excel模板不合法");
                }
                //总行数
                totalRows = xssfSheet.getLastRowNum();
                if(totalRows > 5000){
                    throw new InvalidCustomException("您导入的数据太多，不要超过5000，请分批导入");
                }
                //读取Row,从第二行开始
                for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        org = new UserDto();
                        //总列数，目前只读前13列
//                        totalCells = 13;//xssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for (int c = 0; c < totalCells; c++) {
                            if(org == null){
                                break;
                            }
                            XSSFCell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                cellVal = ExcelUtil.EMPTY;
                            }else{
                                cellVal = ExcelUtil.getXValue(cell).trim();
                            }
                            org = readToUser(c,org,cellVal);
                        }
                        if(org != null){
                            list.add(org);
                        }
                    }
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * read the Excel 2003-2007 .xls
     *
     * @param file
     * @return
     * @throws IOException
     */
    public List<UserDto> readXls(MultipartFile file) {
        List<UserDto> list = new ArrayList<UserDto>();
        // IO流读取文件
        InputStream input = null;
        HSSFWorkbook wb = null;
        UserDto org = null;
        String cellVal = null;
        try {
            input = file.getInputStream();
            // 创建文档
            wb = new HSSFWorkbook(input);
            //excel 总页数，目前只获取第一页
            int totalNumSheet = 1;//wb.getNumberOfSheets();
            //读取sheet(页)
            for (int numSheet = 0; numSheet < totalNumSheet; numSheet++) {
                HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                //校验第一行
                HSSFRow firstRow = hssfSheet.getRow(0);
                if(firstRow == null){
                    break;
                }
                //校验excel格式
                boolean flag = checkExcel(firstRow);
                if(!flag){
                    throw new InvalidCustomException("excel模板不合法");
                }
                totalRows = hssfSheet.getLastRowNum();
                if(totalRows > 5000){
                    throw new InvalidCustomException("您导入的数据太多，不要超过5000，请分批导入");
                }
                //读取Row,从第二行开始
                for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
                        org = new UserDto();
                        //总列数，目前只读前13列
//                        totalCells = 13;// hssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for (short c = 0; c < totalCells ; c++) {
                            if(org == null){
                                break;
                            }
                            HSSFCell cell = hssfRow.getCell(c);
                            if (cell == null) {
                                cellVal = ExcelUtil.EMPTY;
                            }else{
                                cellVal = ExcelUtil.getHValue(cell).trim();
                            }
                            org = readToUser(c,org,cellVal);
                        }
                        if(org != null){
                            list.add(org);
                        }
                    }
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public UserDto readToUser(int c,UserDto user,String cellVal){
        switch (c){
            case 0:
                user.setRealName(cellVal);
                break;
            case 1:
                user.setLoginName(cellVal);
                break;
            case 2:
                user.setPassword(cellVal);
                break;
            case 3:
                user.setBelongOrgId(cellVal);
                break;
            case 4:
                user.setMobile(cellVal);
                break;
            case 5:
                user.setEmail(cellVal);
                break;
            case 6:
                if(StringUtils.isNotBlank(cellVal) && StringUtils.isNumeric(cellVal) ){
                    user.setSort(Long.valueOf(cellVal));
                }
                break;
            case 7:
                user.setWeChat(cellVal);
                break;
            case 8:
                if(StringUtils.isNotBlank(cellVal)){
                    user.setEntryDate(StringToTimestamp(cellVal));
                }
                break;
            case 9:
                if(StringUtils.isNotBlank(cellVal)){
                    user.setLeaveDate(StringToTimestamp(cellVal));
                }
                break;
            case 10:
                if(StringUtils.isNotBlank(cellVal)){
                    user.setWorkTime(StringToTimestamp(cellVal));
                }
                break;
            case 11:
                if(StringUtils.isNotBlank(cellVal)){
                    if(cellVal.equals("男")){
                        user.setIsMale("1");
                    }else if(cellVal.equals("女")){
                        user.setIsMale("0");
                    }
                }
                break;
            case 12:
                user.setRemark(cellVal);
                break;
            default:
//                user = null;
                break;
        }
        return user;
    }

    public Timestamp StringToTimestamp(String tsStr){
        Timestamp ts = null;
        try {
            if(tsStr.length()==10){
                tsStr = tsStr.replaceAll("/","-");
                tsStr += " 00:00:00";
            }
            ts = Timestamp.valueOf(tsStr);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return ts;
    }

    /**
     * 校验excel格式
     * @param firstRow
     * @return
     */
    public boolean checkExcel(XSSFRow firstRow){
        boolean flag = true;
        int totalCells_= firstRow.getLastCellNum();
        if(totalCells_<totalCells){
            return false;
        }
        for(int i =0;i<totalCells;i++){
            XSSFCell cell = firstRow.getCell(i);
            switch (i){
                case 0:
                    if(!ExcelUtil.getXValue(cell).trim().equals("*用户名")){
                        flag = false;
                    }
                    break;
                case 1:
                    if(!ExcelUtil.getXValue(cell).trim().equals("*账号")){
                        flag = false;
                    }
                    break;
                case 2:
                    if(!ExcelUtil.getXValue(cell).trim().equals("*密码")){
                        flag = false;
                    }
                    break;
                case 3:
                    if(!ExcelUtil.getXValue(cell).trim().equals("*所属组织")){
                        flag = false;
                    }
                    break;
           case 4:
                    if(!ExcelUtil.getXValue(cell).trim().equals("*手机")){
                        flag = false;
                    }
                    break;
                case 5:
                    if(!ExcelUtil.getXValue(cell).trim().equals("*邮箱")){
                        flag = false;
                    }
                    break;
                case 6:
                    if(!ExcelUtil.getXValue(cell).trim().equals("排序号")){
                        flag = false;
                    }
                    break;
                case 7:
                    if(!ExcelUtil.getXValue(cell).trim().equals("微信账号")){
                        flag = false;
                    }
                    break;
                case 8:

                    if(!ExcelUtil.getXValue(cell).trim().equals("入职时间")){
                        flag = false;
                    }
                    break;
                case 9:
                    if(!ExcelUtil.getXValue(cell).trim().equals("离职时间")){
                        flag = false;
                    }
                    break;
                case 10:
                    if(!ExcelUtil.getXValue(cell).trim().equals("工作时间")){
                        flag = false;
                    }
                    break;
                case 11:
                    if(!ExcelUtil.getXValue(cell).trim().equals("性别")){
                        flag = false;
                    }
                    break;
                case 12:
                    if(!ExcelUtil.getXValue(cell).trim().equals("备注说明")){
                        flag = false;
                    }
                    break;
            }

        }
        return flag;
    }
    /**
     * 校验excel格式
     * @param firstRow
     * @return
     */
    public boolean checkExcel(HSSFRow firstRow){
        boolean flag = true;
        int totalCells_= firstRow.getLastCellNum();
        if(totalCells_<totalCells){
            return false;
        }
        for(int i =0;i<totalCells;i++){
            HSSFCell cell = firstRow.getCell(i);
            switch (i){
                case 0:
                    if(!ExcelUtil.getHValue(cell).trim().equals("*用户名")){
                        flag = false;
                    }
                    break;
                case 1:
                    if(!ExcelUtil.getHValue(cell).trim().equals("*账号")){
                        flag = false;
                    }
                    break;
                case 2:
                    if(!ExcelUtil.getHValue(cell).trim().equals("*密码")){
                        flag = false;
                    }
                    break;
                case 3:
                    if(!ExcelUtil.getHValue(cell).trim().equals("*所属组织")){
                        flag = false;
                    }
                    break;
                case 4:
                    if(!ExcelUtil.getHValue(cell).trim().equals("*手机")){
                        flag = false;
                    }
                    break;
                case 5:
                    if(!ExcelUtil.getHValue(cell).trim().equals("*邮箱")){
                        flag = false;
                    }
                    break;
                case 6:
                    if(!ExcelUtil.getHValue(cell).trim().equals("排序号")){
                        flag = false;
                    }
                    break;
                case 7:
                    if(!ExcelUtil.getHValue(cell).trim().equals("微信账号")){
                        flag = false;
                    }
                    break;
                case 8:

                    if(!ExcelUtil.getHValue(cell).trim().equals("入职时间")){
                        flag = false;
                    }
                    break;
                case 9:
                    if(!ExcelUtil.getHValue(cell).trim().equals("离职时间")){
                        flag = false;
                    }
                    break;
                case 10:
                    if(!ExcelUtil.getHValue(cell).trim().equals("工作时间")){
                        flag = false;
                    }
                    break;
                case 11:
                    if(!ExcelUtil.getHValue(cell).trim().equals("性别")){
                        flag = false;
                    }
                    break;
                case 12:
                    if(!ExcelUtil.getHValue(cell).trim().equals("备注说明")){
                        flag = false;
                    }
                    break;
            }

        }
        return flag;
    }
}


package com.hiscene.flytech.util;

import com.github.weiss.core.utils.FileUtils;
import com.hiscene.flytech.C;
import com.hiscene.flytech.entity.AttachFourModel;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.AttachThreeModel;
import com.hiscene.flytech.entity.ExecuteModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des POI操作工具类
 */
public class POIUtil {

    //判断后缀为xlsx的excel文件的数据类
    @SuppressWarnings("deprecation")
    public static String getValue(XSSFCell xssfRow) {
        if (xssfRow == null) {
            return "---";
        }
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            double cur = xssfRow.getNumericCellValue();
            long longVal = Math.round(cur);
            Object inputValue = null;
            if (Double.parseDouble(longVal + ".0") == cur)
                inputValue = longVal;
            else
                inputValue = cur;
            return String.valueOf(inputValue);
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BLANK || xssfRow.getCellType() == xssfRow.CELL_TYPE_ERROR) {
            return "---";
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

    //判断后缀为xls的excel文件的数据类型
    @SuppressWarnings("deprecation")
    public static String getValue(HSSFCell hssfCell) {
        if (hssfCell == null) {
            return "---";
        }
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            double cur = hssfCell.getNumericCellValue();
            long longVal = Math.round(cur);
            Object inputValue = null;
            if (Double.parseDouble(longVal + ".0") == cur)
                inputValue = longVal;
            else
                inputValue = cur;
            return String.valueOf(inputValue);
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BLANK || hssfCell.getCellType() == hssfCell.CELL_TYPE_ERROR) {
            return "---";
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    public static Workbook workbook ;

    public static Sheet sheet;

    public static FileInputStream fileInputStream ;

    public static FileOutputStream out = null;

    /**
     * 确认读取文件的版本类型
     * @param filePath
     */
    public static void insureExcelType(String filePath) {
        try {
            fileInputStream = new FileInputStream(filePath);
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fileInputStream);
                // sheet = workbook.getSheetAt(0);
            } else if (filePath.endsWith(".xls")) {
                POIFSFileSystem fileSystem = new POIFSFileSystem(fileInputStream);
                workbook = new HSSFWorkbook(fileSystem);
                // sheet = workbook.getSheetAt(0);
            } else {
                throw new RuntimeException("错误提示: 您设置的Excel文件名不合法!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void insureExcelType(String filePath,String fileName) {
        try {
            fileInputStream = new FileInputStream(filePath + fileName);
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fileInputStream);
            } else if (fileName.endsWith(".xls")) {
                POIFSFileSystem fileSystem = new POIFSFileSystem(fileInputStream);
                workbook = new HSSFWorkbook(fileSystem);
            } else {
                throw new RuntimeException("错误提示: 您设置的Excel文件名不合法!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取第一个Sheet中的单元格
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static Cell getCellInSheet(int rowIndex, int colIndex) {
        sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowIndex-1 );
        Cell cell = row.getCell(colIndex-1 );
        return cell ;
    }

    /**
     * 获取指定Sheet中的单元格
     * @param st
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public static Cell getCellInSheet(int st,int rowIndex, int colIndex) {
        sheet = workbook.getSheetAt(st);
        Row rowst = sheet.getRow(rowIndex - 1);
        if(rowst == null) {
            rowst.setRowNum(Cell.CELL_TYPE_STRING);
            Cell cellst = rowst.getCell(colIndex - 1);
            return cellst ;
        }
        Cell cellst = rowst.getCell(colIndex - 1);
        return cellst ;
    }

    /**
     * 根据行和列获取单元格内容
     * @param filePath文件路径
     * @param rowIndex行号
     * @param colIndex列号
     * @return
     * @throws Exception
     */
    public static String getCellValueAt(String filePath,int rowIndex, int colIndex) throws Exception {

        insureExcelType(filePath);

        Cell cell = getCellInSheet(rowIndex,colIndex);
        String cellValue = getCellValue(cell);
        return cellValue ;
    }

    /**
     * 根据行和列获取单元格内容
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws Exception
     */
    public static String getCellValueAt(int rowIndex, int colIndex) throws Exception {

        Cell cell = getCellInSheet(rowIndex,colIndex);
        String cellValue = getCellValue(cell);
        return cellValue ;
    }

    /**
     * 获取指定Sheet中的单元格值
     * @param st
     * @param rowIndex
     * @param colIndex
     * @return
     * @throws Exception
     */
    public static String getCellValueAt(int st,int rowIndex, int colIndex) throws Exception {

        Cell cellst = getCellInSheet(st,rowIndex,colIndex);
        String cellValue = getCellValue(cellst);
        return cellValue ;
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String value = "";
        // 以下是判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                value = cell.getNumericCellValue() + "";
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        value = new SimpleDateFormat("d-m-yy").format(date);
                    } else {
                        value = "";
                    }
                } else {
                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                value = cell.getBooleanCellValue() + "";
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                value = cell.getCellFormula() + "";
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                value = "";
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                value = "非法字符";
                break;
            default:
                value = "未知类型";
                break;
        }
        return value;
    }

    /**
     * 设置指定位置的单元格内容
     * @param filePath  源文件地址
     * @param descPath  保存文件的目的地
     * @param rowIndex  需要设置单元格的行号(根据单元格行号写即可)
     * @param colIndex  需要设置的单元格的列号(将单元格的列对应的字母转换为数字即可)
     * @param object    需要设置的内容
     * @throws Exception
     */
    public static void setCellValueAt(String filePath, String descPath, int rowIndex, int colIndex, List<String> list) throws Exception {

        insureExcelType(filePath);
        for(int i=0;i<list.size();i++){
            Cell cell = getCellInSheet(rowIndex,colIndex);//
            setCellValue(workbook,cell,list.get(i));
            rowIndex++;
        }
        try{
            if(!FileUtils.isFileExists(descPath)){
                FileUtils.createOrExistsFile(descPath);
            }
            out = new FileOutputStream(descPath);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }

    /**
     * 设置指定位置的单元格内容
     * @param descPath
     * @param rowIndex
     * @param colIndex
     * @param object
     * @throws Exception
     */
    public static void setCellValueAt(int rowIndex, int colIndex,Object object) throws Exception {

        Cell cell = getCellInSheet(rowIndex,colIndex);
        setCellValue(workbook,cell,object);

    }

    /**
     * 设置指定Sheet中的单元格值
     * @param st
     * @param rowIndex
     * @param colIndex
     * @param object
     * @throws Exception
     */
    public static void setCellValueAt(int st,int rowIndex, int colIndex,Object object) throws Exception {

        Cell cellst = getCellInSheet(st,rowIndex,colIndex);
        setCellValue(workbook,cellst,object);

    }

    /**
     * 将Excel输出到指定地点
     * @param descPath
     */
    public static void fileOutPut(String descPath,String filename) {
        try{
            out = new FileOutputStream(descPath + filename);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }
    public static void fileOutPut(String descPath) {
        try{
            out = new FileOutputStream(descPath);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }


    /**
     *
     * 设置单元格值
     *
     * @param cell
     *            需要设置的单元格
     * @param value
     *            设置给单元格cell的值
     * @return 设置好的单元格列对象
     */
    public static Cell setCellValue(Workbook workbook,Cell cell, Object value) {
        if(value==null){
            return cell;
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        } else if (value instanceof Date) {
            //获取设置时间格式对象
            CreationHelper creationHelper = workbook.getCreationHelper();
            //获取单元格样式对象
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yy-mm-dd hh:mm:ss"));
            cell.setCellValue((Date) value);
            // cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellStyle(cellStyle);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else if (value instanceof Calendar) {
            //获取设置时间格式对象
            CreationHelper creationHelper = workbook.getCreationHelper();
            //获取单元格样式对象
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("d-m-yy"));
            cell.setCellValue((Calendar) value);
            //cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellStyle(cellStyle);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        } else {
            cell.setCellValue(String.valueOf(value));
            cell.setCellType(Cell.CELL_TYPE_STRING);
            // System.out.println("错误提示: 您设置的单元格内容【"+value+"】不符合要求,不是常用类型!");
        }
        return cell;
    }

    /**
     *
     * @param ImgPath:图片路径
     * @param row1：起始行
     * @param row2：终止行
     * @param col1：起始列
     * @param col2：终止列
     * @throws IOException
     */
    public static void setImg(String ImgPath,int row1,int row2,int col1,int col2) throws IOException {

        // 插入 PNG 图片至 Excel
        InputStream is = new FileInputStream(ImgPath);
        byte[] bytes = IOUtils.toByteArray(is);
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        Sheet sheet2 = workbook.getSheetAt(1);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = sheet2.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        // 图片插入坐标
        anchor.setDx1(0);
        anchor.setDy1(0);
        anchor.setDx2(0);
        anchor.setDy2(0);
        anchor.setRow1(row1);
        anchor.setRow2(row2);
        anchor.setCol1(col1);
        anchor.setCol2(col2);
        // 插入图片
        Picture pict = drawing.createPicture(anchor, pictureIdx);

    }
    /**
     * 设置措施单内容
     * @param filePath  源文件地址
     * @param descPath  保存文件的目的地
     * @param executeModelList 要设置得内容
     */
    public static void setCellValueAtExecute(String filePath, String descPath, int rowIndex,List<ExecuteModel> executeModelList) throws
            Exception {
        //行：从第5行开始，中间第32行不是步骤
        //列：2，3，8，9
        insureExcelType(filePath);
        ExecuteModel executeModel;
        for(int i=0;i<executeModelList.size();i++){
            executeModel=executeModelList.get(i);
            String execute_result="";
            String recover_result="";
            switch (executeModel.excute_result){
                case 0:
                    execute_result="×";
                    break;
                case 1:
                    execute_result="√";
                    break;
                case -1:
                    execute_result="无";
                    break;
            }
            switch (executeModel.recover_result){
                case 0:
                    recover_result="×";
                    break;
                case 1:
                    recover_result="√";
                    break;
                case -1:
                    recover_result="无";
                    break;
            }
            Cell cell_1 = getCellInSheet(rowIndex,2);//执行结果
            Cell cell_2 = getCellInSheet(rowIndex,3);//执行时间
            Cell cell_3 = getCellInSheet(rowIndex,8);//恢复结果
            Cell cell_4 = getCellInSheet(rowIndex,9);//恢复时间
            setCellValue(workbook,cell_1,execute_result);
            setCellValue(workbook,cell_2,executeModel.execute_date);
            setCellValue(workbook,cell_3,recover_result);
            setCellValue(workbook,cell_4,executeModel.recover_date);
            rowIndex++;
            if(rowIndex==32){
                rowIndex++;
            }
        }
        try{
            if(!FileUtils.isFileExists(descPath)){
                FileUtils.createOrExistsFile(descPath);
            }
            out = new FileOutputStream(descPath);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }

    public static void setCellValueAtSecond(String filePath, String descPath,int rowIndex, List<AttachSecondModel> attachSecondModels) throws
            Exception {
        //列3，4，5，6
        insureExcelType(filePath);
        AttachSecondModel attachSecondModel;
        for(int i=0;i<attachSecondModels.size();i++){
            attachSecondModel=attachSecondModels.get(i);

            Cell cell_1 = getCellInSheet(rowIndex,3);//执行结果
            Cell cell_2 = getCellInSheet(rowIndex,4);//执行时间
            Cell cell_3 = getCellInSheet(rowIndex,5);//恢复结果
            Cell cell_4 = getCellInSheet(rowIndex,6);//恢复时间
            setCellValue(workbook,cell_1,attachSecondModel.number);
            setCellValue(workbook,cell_2,attachSecondModel.check_code);
            setCellValue(workbook,cell_3,attachSecondModel.verison);
            setCellValue(workbook,cell_4,attachSecondModel.factory);
            rowIndex++;
        }
        try{
            if(!FileUtils.isFileExists(descPath)){
                FileUtils.createOrExistsFile(descPath);
            }
            out = new FileOutputStream(descPath);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }

    public static void setCellValueAtThird(String filePath, String descPath,int rowIndex, List<AttachThreeModel> attachThreeModelList) throws
            Exception {
        //列2，3，5，6
        insureExcelType(filePath);
        AttachThreeModel attachThreeModel;
        int collndex_a=0,collndex_b=0;
        int result=0;
        for(int i=0;i<attachThreeModelList.size();i++){
            attachThreeModel=attachThreeModelList.get(i);
            result=i/6;
            switch (result){
                case 0:
                case 2:
                    collndex_a=2;
                    collndex_b=3;
                    break;
                case 1:
                    collndex_a=5;
                    collndex_b=6;
                    break;

            }
            Cell cell_1 = getCellInSheet(rowIndex,collndex_a);//A通道
            Cell cell_2 = getCellInSheet(rowIndex,collndex_b);//B通道
            setCellValue(workbook,cell_1,attachThreeModel.a);
            setCellValue(workbook,cell_2,attachThreeModel.b);

            rowIndex++;
            if(rowIndex>C.ATTACH_THIRD_ROW_END_1&&result==0) {//项目2
                    rowIndex=C.ATTACH_THIRD_ROW_BEGIN_1;
            }
            if(rowIndex>C.ATTACH_THIRD_ROW_END_1&&result==1){//项目3
                    rowIndex=C.ATTACH_THIRD_ROW_BEGIN_2;
            }
        }
        try{
            if(!FileUtils.isFileExists(descPath)){
                FileUtils.createOrExistsFile(descPath);
            }
            out = new FileOutputStream(descPath);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }

    public static void setCellValueAtFour(String filePath, String descPath,int rowIndex, List<AttachFourModel> attachFourModelList) throws
            Exception {
        //列2,3
        insureExcelType(filePath);
        AttachFourModel attachFourModel;
        for(int i=0;i<attachFourModelList.size();i++){
            attachFourModel=attachFourModelList.get(i);

            Cell cell_1 = getCellInSheet(rowIndex,2);//第一组跳闸mS
            Cell cell_2 = getCellInSheet(rowIndex,3);//第二组跳闸mS
            setCellValue(workbook,cell_1,attachFourModel.time_1);
            setCellValue(workbook,cell_2,attachFourModel.time_2);
            rowIndex++;
        }
        try{
            if(!FileUtils.isFileExists(descPath)){
                FileUtils.createOrExistsFile(descPath);
            }
            out = new FileOutputStream(descPath);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }




    public static void setCellValueAtMulCol(String filePath, String descPath, int rowIndex, int[] colIndex, List<String> list) throws Exception {

        insureExcelType(filePath);
        int num=colIndex.length;

        for(int j=0;j<colIndex.length;j++){
                Cell cell=getCellInSheet(rowIndex,colIndex[j]);
                setCellValue(workbook,cell,list.get(j));
        }
        try{
            if(!FileUtils.isFileExists(descPath)){
                FileUtils.createOrExistsFile(descPath);
            }
            out = new FileOutputStream(descPath);
            workbook.write(out);
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            try {
                out.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }


}

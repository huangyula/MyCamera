package com.hiscene.flytech.util;
/**
 * Created by and on 2017-07-24.
 */

public class ExcelUtils {

//    public static<E> Boolean exportExcel(List<FileFormatBean> list, List<E> listInfo1, String strHastitle, String path) {
//        boolean result=false;
//
//        if(path.isEmpty()){
//            return false;
//        }else {
//            path=path+".xls";
//        }
//
//
//        try {
//            HSSFWorkbook wb = new HSSFWorkbook();
//            HSSFSheet sheet = wb.createSheet("导出数据");
//            HSSFRow row = sheet.createRow(0);
//            // Sheet样式
//            HSSFCellStyle style = wb.createCellStyle();//设置标题样式
//            Font font = wb.createFont();
//            font.setFontHeightInPoints((short) 10);
//            font.setBoldweight((short) 600);
//            font.setColor(HSSFColor.DARK_TEAL.index);
//            style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
//            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
//            style.setFont(font);
//            row.setHeightInPoints(26);
//            HSSFCellStyle centerstyle = wb.createCellStyle();//设置普通表格样式
//            //centerstyle.setFont(font);
//            //centerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
//            //centerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
//            //centerstyle.setWrapText(true);
//            // centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
//            //centerstyle.setBorderLeft((short) 1);
//            //centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
//            //centerstyle.setBorderRight((short) 1);
////            centerstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
//            // centerstyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
//            //centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
//           if(strHastitle.equals("Y")){
//               int i = 0;
//               for (FileFormatBean fileFormat : list) {
//
//                   String value = fileFormat.getCtitle();
//                   HSSFCell cell = row.createCell(i);
//                   cell.setCellValue(value);
//                   cell.setCellStyle(style);
//                   i++;
//               }
//
//           }
//
//            List<E> listInfo = listInfo1;
//
//
//            int m;
//            if(strHastitle.equals("Y")){
//                m = 1;
//            }else{
//                m=0;
//            }
//
//
//            if (listInfo != null && listInfo.size() > 0) {
//
//                for (E scaninfo : listInfo) {
//                    StringBuilder sb = new StringBuilder();
//                    row = sheet.createRow(m);//行
//                    int n = 0;
//                    for (FileFormatBean fileFormat : list) {
//
//                        String field = fileFormat.getCfieldName();
//                        String cellVal = "";
//
//                        //获取类名
//                        Class c = scaninfo.getClass();
//                        //获取属性名
//                        Field f = ReflectUtil.getFieldByName(field, c);
//                        //获取属性值
//                        String fieldValue = ReflectUtil.getFieldValueByName(f.getName(), scaninfo).toString();
//
//                        cellVal=fieldValue;
//
//                        HSSFCell createCell = row.createCell(n);//单元格
//                        createCell.setCellValue(cellVal);
//                        createCell.setCellStyle(centerstyle);
//                        sheet.setColumnWidth(n,(cellVal.getBytes().length+2)*2*256);
//
//                        n++;
//                    }
//
//                    m++;
//
//                }
//            }
//                    FileUtil.makeDirs(path);
//                    FileOutputStream fout = new FileOutputStream(path);
//                    wb.write(fout);
//                    fout.close();
//                    result=true;
//                } catch(Exception e){
//                    e.printStackTrace();
//                }finally {
//                    return result;
//                }
//            }
}

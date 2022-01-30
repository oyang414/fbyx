
package com.ouyang.fbyx.controller;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @description:
 * @author: ouyangxingjie
 * @create: 2021/11/24 21:26
 **/

public class ExcelData {
    private XSSFSheet sheet;


/**
     * 构造函数，初始化excel数据
     * @param filePath  excel路径
     * @param sheetName sheet表名
     */

    ExcelData(String filePath,String sheetName){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = sheets.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/**
     * 根据行和列的索引获取单元格的数据
     * @param row
     * @param column
     * @return
     */

    public String getExcelDateByIndex(int row,int column){
        XSSFRow row1 = sheet.getRow(row);
        String cell = row1.getCell(column).toString();
        return cell;
    }


/**
     * 根据某一列值为“******”的这一行，来获取该行第x列的值
     * @param caseName
     * @param currentColumn 当前单元格列的索引
     * @param targetColumn 目标单元格列的索引
     * @return
     */
    public String getCellByCaseName(String caseName,int currentColumn,int targetColumn){
        String operateSteps="";
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i=0;i<rows;i++){
            XSSFRow row = sheet.getRow(i);
            String cell = row.getCell(currentColumn).toString();
            if(cell.equals(caseName)){
                operateSteps = row.getCell(targetColumn).toString();
                break;
            }
        }
        return operateSteps;
    }

    //打印excel数据
    public void readExcelData(){
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i=0;i<rows;i++){
            //获取列数
            XSSFRow row = sheet.getRow(i);
            int columns = row.getPhysicalNumberOfCells();
            for(int j=0;j<columns;j++){
                String cell = row.getCell(j).toString();
                System.out.println(cell);
            }
        }
    }

    //测试方法
    public static void main(String[] args)throws IOException{
        ExcelData sheet1 = new ExcelData("D:\\test.xlsx", null);
        //获取第二行第4列
        String cell2 = sheet1.getExcelDateByIndex(1, 3);
        //根据第3列值为“customer23”的这一行，来获取该行第2列的值
        //String cell3 = sheet1.getCellByCaseName("customer23", 2,1);
        FileOutputStream out = new FileOutputStream("D:\\test.xlsx");
        System.out.println(cell2);
       // System.out.println(cell3);
    }
}


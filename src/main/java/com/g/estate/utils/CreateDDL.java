package com.g.estate.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class CreateDDL {

    public static String FileName = "DDL.xlsx";

    public static String ResourcesPath = "\\src\\main\\resources\\";

    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir");
        File file = new File(projectPath + ResourcesPath + FileName);

        XSSFWorkbook xssfWorkbook = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            xssfWorkbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            System.out.println("Excel data file cannot be found!");
        }

        //項目一覧を取得する
        XSSFSheet itemsSheet;
        itemsSheet = xssfWorkbook.getSheet("項目一覧");
        Map<String, Item> map = new HashMap<>();
        // 第２列をキーとして、Mapに格納する
        Iterator<Row> rows = itemsSheet.iterator();
        rows.next();
        for (int index = 1; rows.hasNext(); index ++) {
            XSSFRow row = itemsSheet.getRow(index);
            if (row == null) {
                break;
            }
            Item item = new Item();
            // No.設定
            item.setNo(row.getCell(0).getNumericCellValue());
            // 論理項目名設定
            item.setItemName(row.getCell(1).getStringCellValue());
            // 物理項目名設定
            item.setColumnName(row.getCell(2).getStringCellValue());
            // 属性設定
            item.setProp(row.getCell(3).getStringCellValue());
            // 桁数設定
            if (row.getCell(4) != null) {
                item.setLen(row.getCell(4).getNumericCellValue());
            }
            // 説明設定
            if (row.getCell(6) != null) {
                item.setDesc(row.getCell(6).getStringCellValue());
            }
            // 初期値設定
            item.setDefualt(row.getCell(7).getStringCellValue());
            map.put(row.getCell(1).getStringCellValue(), item);
        }

        // テーブル一覧を取得する
        XSSFSheet tableListSheet;
        tableListSheet = xssfWorkbook.getSheet("テーブル一覧");
        Map<String, Table> tables = new HashMap<>();
        Iterator<Row> tablerows = tableListSheet.iterator();
        tablerows.next();
        for (int index = 1; tablerows.hasNext(); index ++) {
            XSSFRow row = tableListSheet.getRow(index);
            if (row == null) {
                break;
            }

            Table table = new Table();
            // テーブルID
            table.setTableId(row.getCell(0).getStringCellValue());
            // テーブル論理名
            table.setFormalName(row.getCell(1).getStringCellValue());
            // テーブル物理名
            table.setTableName(row.getCell(2).getStringCellValue());
            // データベース名
            table.setDatabaseName(row.getCell(3).getStringCellValue());
            // 備考
            if (row.getCell(4) != null) {
                table.setComment(row.getCell(4).getStringCellValue());
            }
        }

        // テーブル定義を読み込み
        XSSFSheet tableDsSheet;
        tableDsSheet = xssfWorkbook.getSheet("テーブル定義");
        Iterator<Row> definitionRows = tableDsSheet.iterator();
        definitionRows.next();
        for (int index = 1; definitionRows.hasNext(); index ++) {
            XSSFRow row = tableDsSheet.getRow(index);

            if (row == null) {
                break;
            }

            System.out.println(row.getCell(0));

        }



        //定义行
        //默认第一行为标题行，index = 0
        XSSFRow titleRow = tableDsSheet.getRow(0);
        XSSFRow tableRow = tableDsSheet.getRow(1);
        XSSFRow itemRow = tableDsSheet.getRow(2);
//        System.out.println(titleRow);
//        System.out.println(tableRow);
//        System.out.println(itemRow);
        System.out.println(itemRow.getCell(1));
        System.out.println(map.get(itemRow.getCell(1)));
    }
}

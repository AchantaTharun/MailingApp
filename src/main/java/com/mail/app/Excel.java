package com.mail.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
    int in = 0;
    private String path = "";
    InputStream stream = null;

    Excel(String p, InputStream is) {
	this.path = p;
	this.stream = is;
    }

    public static void main(String[] args) {

	// getEmailList();
    }

    List<String> list;

    public void setList(List<String> resultList) {

	// TODO Auto-generated method stub
	this.list = resultList;

	try {
	    FileInputStream file = new FileInputStream(path);

	    XSSFWorkbook workbook = new XSSFWorkbook(file);
	    XSSFSheet sheet = workbook.getSheetAt(0);
	    Cell cell = null;
	    // System.out.println(resultList.size() + "::" + resultList);
	    for (int i = 1; i <= resultList.size(); i++) {
		try {
		    cell = sheet.getRow(i).getCell(in);
		} catch (NullPointerException np) {
		    cell = sheet.getRow(i).createCell(in);
		}

		if (cell == null) {
		    cell = sheet.getRow(i).createCell(in);
		    // System.out.println("ssssssss" + sheet.getRow(i) + "::::::::" +
		    // sheet.getRow(i).createCell(in));

		}
		// System.out.println(in + " " + i + " " + cell);
		cell.setCellValue(resultList.get(i - 1));
	    }
	    file.close();

	    FileOutputStream outFile = new FileOutputStream(new File(path));
	    workbook.write(outFile);
	    outFile.close();

	} catch (Exception e) {

	    e.printStackTrace();
	}
    }

    // FileInputStream fis = null;

    public List getEmailList(String path) {

	List EmailList = new ArrayList();
	SendEmail se = new SendEmail();

	try {

	    // fis = new FileInputStream(path);
	    Workbook workbook = new XSSFWorkbook(stream);
	    int numberOfSheets = workbook.getNumberOfSheets();
	    Sheet s = workbook.getSheetAt(0);
	    int n = 10000;
	    int index = 0;

	    for (int i = 0; i < n && s.getRow(0).getCell(i) != null; i++) {
		Cell r = s.getRow(0).getCell(i);

		if (r.toString().toLowerCase().equals("email")) {
		    index = i;
		}

		if (r.toString().toLowerCase().equals("status")) {
		    in = i;

		}
		// System.out.println(r + " " + index);
	    }

	    int num = s.getLastRowNum();
	    // System.out.println(num);
	    for (int i = 1; i <= num; i++) {
		// System.out.println(s.getRow(i).getCell(index));

		try {
		    String t = s.getRow(i).getCell(index).toString();
		    EmailList.add(t);
		    // se.createEmailMessage(t, sub, msg);
		} catch (NullPointerException ae) {
		    // System.out.println("no email address" + i);
		    EmailList.add("n");
		} catch (Exception k) {
		    System.out.println(k);
		}
	    }
	    // fis.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return EmailList;

    }
}

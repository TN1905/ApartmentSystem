package com.poly.services;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.poly.dao.RentApartmentDetailDAO;
import com.poly.entity.ApartStatus;
import com.poly.entity.Rented;

import com.poly.entity.Revenue;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportService {
	@Autowired
	private RentApartmentDetailDAO reportDao;
	
	public void generateRevenueExcel(HttpServletResponse response) throws IOException  {
		List<Revenue> revenues = reportDao.getRevenue();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Apartment Report Revenue");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Apartment ID");
		row.createCell(1).setCellValue("Apartment Name");
		row.createCell(2).setCellValue("Username");
		row.createCell(3).setCellValue("Revenue");
		int dataRowIndex = 1;
		for (Revenue revenue : revenues) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(revenue.getId());
			dataRow.createCell(1).setCellValue(revenue.getName());
			dataRow.createCell(2).setCellValue(revenue.getPerson());
			dataRow.createCell(2).setCellValue(revenue.getSum());
			dataRowIndex++;
		}
		
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}
	
	public void generateRentedExcle(HttpServletResponse response) throws IOException  {
		List<Rented> renteds = reportDao.getRented();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Apartment Report Rented");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Apartment ID");
		row.createCell(1).setCellValue("Apartment Name");
		row.createCell(2).setCellValue("Username");
		int dataRowIndex = 1;
		for (Rented rented : renteds) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(rented.getId());
			dataRow.createCell(1).setCellValue(rented.getName());
			dataRow.createCell(2).setCellValue(rented.getPerson());
			dataRowIndex++;
		}
		
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}
	
	public void generateApartStatusExcle(HttpServletResponse response) throws IOException  {
		List<ApartStatus> apartStatuss = reportDao.getStatus();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Apartment Report Status");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Apartment ID");
		row.createCell(1).setCellValue("Apartment Name");
		row.createCell(2).setCellValue("Status");
		int dataRowIndex = 1;
		for (ApartStatus status : apartStatuss) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(status.getId());
			dataRow.createCell(1).setCellValue(status.getName());
			dataRow.createCell(2).setCellValue(status.isStatus());
			dataRowIndex++;
		}
		
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}
}

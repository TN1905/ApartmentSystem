package com.poly.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.dao.AccountDAO;
import com.poly.dao.ApartTypeDAO;
import com.poly.dao.RentApartmentDetailDAO;
import com.poly.entity.Account;
import com.poly.entity.ApartStatus;
import com.poly.entity.ApartType;
import com.poly.entity.Rented;

import com.poly.entity.Revenue;
import com.poly.services.ReportService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
	@Autowired
	ApartTypeDAO dao;
	@Autowired
	RentApartmentDetailDAO reportDao;
	@Autowired
	private ReportService reportService;
	@Autowired
	private AccountDAO accountDAO;
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping("/admin/apartmentType")
	public String form(Model model) {
		ApartType apartType = new ApartType();
		model.addAttribute("apartType",apartType);
		List<ApartType> list = dao.findAll();
		model.addAttribute("readonly",false);
		model.addAttribute("apartTypeList",list);
		return "admin/apartmentType";
	}

	@RequestMapping("admin/createApartType")
	public String insertApartType(Model model,@ModelAttribute("apartType") ApartType apartType) {
		dao.save(apartType);
		List<ApartType> list = dao.findAll();
		model.addAttribute("apartTypeList",list);
		return "admin/apartmentType";
	}
		
	@RequestMapping("admin/updateApartType")
	public String updateApartType(Model model,@ModelAttribute("apartType") ApartType apartType) {
		dao.save(apartType);
		List<ApartType> list = dao.findAll();
		model.addAttribute("readonly",true);
		model.addAttribute("apartTypeList",list);
		return "admin/apartmentType";
	}
	
	@RequestMapping("admin/deleteApartType/{id}")
	public String deleteApartType(Model model,@PathVariable("id") String id, @ModelAttribute("apartType") ApartType apartType, BindingResult bindingResult) {
		dao.deleteById(id);
		List<ApartType> list = dao.findAll();
		model.addAttribute("apartTypeList",list);
		return "admin/apartmentType";
	}
	
	@RequestMapping("/editApartType/{id}")
	public String edit(Model model,@PathVariable("id") String id) {
		ApartType apartType = dao.findById(id).get();
		model.addAttribute("apartType",apartType);
		model.addAttribute("readonly",true);
		List<ApartType> apartTypeList = dao.findAll();
		model.addAttribute("apartTypeList",apartTypeList);
		return "admin/apartmentType";
	}
	
	@RequestMapping("/admin/revenue")
	public String revenue(Model model) {
		List<Revenue> items = reportDao.getRevenue();
		model.addAttribute("items",items);
		return "admin/revenue";
	}
	
	@RequestMapping("/admin/rented")
	public String rented(Model model) {
		List<Rented> items = reportDao.getRented();
		model.addAttribute("items",items);
		return "admin/rented";
	}
	
	@RequestMapping("/admin/apartStatus")
	public String apartStatus(Model model) {
		List<ApartStatus> items = reportDao.getStatus();
		model.addAttribute("items",items);
		return "admin/apartStatus";
	}
	
	@RequestMapping("/admin/revenue/excel")
	public String generateRevenueExcelReport(HttpServletResponse response) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=courses.xls";

		response.setHeader(headerKey, headerValue);
		
		reportService.generateRevenueExcel(response);
		
		response.flushBuffer();
		return "admin/revenue";
	}
	
	@ModelAttribute("gender")
	public List<String> getGenders(){
		List<String> list = new ArrayList<>();
		list.add("male");
		list.add("female");
		list.add("order");
		return list;
	}
	
	@RequestMapping("/admin/rented/excel")
	public String generateRentedExcelReport(HttpServletResponse response) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=courses.xls";

		response.setHeader(headerKey, headerValue);
		
		reportService.generateRentedExcle(response);
		
		response.flushBuffer();
		return "admin/rented";
	}
	
	@RequestMapping("/admin/apartStatus/excel")
	public String generateApartStatusExcelReport(HttpServletResponse response) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=courses.xls";

		response.setHeader(headerKey, headerValue);
		
		reportService.generateApartStatusExcle(response);
		
		response.flushBuffer();
		return "admin/apartStatus";
	}
	
	
	
	@RequestMapping("/admin/accountManagement")
	public String account(Model model) {
		Account user = new Account();
		model.addAttribute("user", user);
		List<Account> userList = accountDAO.findAll();
		model.addAttribute("userList", userList);
		return "admin/accountManagement";
	}

	@RequestMapping("/editUser/{id}")
	public String editUser(Model model, @PathVariable("id") Long id) {
		Account user = accountDAO.findById(id).orElse(null);
		model.addAttribute("user", user);
		List<Account> userList = accountDAO.findAll();
		model.addAttribute("userList", userList);
		
		return "admin/accountManagement";
	}

	@RequestMapping("/admin/updateUser")
	public String updateUser(Model model,@ModelAttribute("user") Account user ) {
		System.out.println(user.getGender());
		try {
			
			accountDAO.save(user);
			model.addAttribute("success", "Update user successful");	
		} catch (Exception e) {
			System.out.println(e);
			model.addAttribute("error", "Update failed");
		}
		List<Account> userList = accountDAO.findAll();
		model.addAttribute("userList", userList);
		
		return "admin/accountManagement";
	}

	@RequestMapping("/deleteUser/{id}")
	public String deleteUser(Model model, @PathVariable("id") Long id,@ModelAttribute("user") Account user) {
		try {
			accountDAO.deleteById(id);
			model.addAttribute("success", "Delete user successful");
			
		} catch (Exception e) {
			model.addAttribute("error", "Delete failed");
		}
		List<Account> userList = accountDAO.findAll();
		model.addAttribute("userList", userList);
		return "admin/accountManagement";
	}
	

}

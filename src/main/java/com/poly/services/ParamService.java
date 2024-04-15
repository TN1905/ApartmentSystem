package com.poly.services;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class ParamService {
	@Autowired
	HttpServletRequest req;
	
	/**
	* Đọc chuỗi giá trị của tham số
	* @param name tên tham số
	* @param defaultValue giá trị mặc định
	* @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
	*/
	
	public String getString(String name,String defaultValue) {
		String value = req.getParameter(name);
		return value != null ? value : defaultValue;
	}
	
	public int getInt(String name, int defaultValue) {
		 String value = req.getParameter(name);
		 return (value != null) ? Integer.parseInt(value) : defaultValue;
	}
	
	public double getDouble(String name, double defaultValue) {
		String value = req.getParameter(name);
		 return (value != null) ? Double.parseDouble(value) : defaultValue;
	}
	
	public boolean getBoolean(String name, boolean defaultValue) {
		String value = req.getParameter(name);
		return (value != null) ? true : defaultValue;
	}
	
	public Date getDate(String name, String pattern, String defaultValue) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			String value = req.getParameter(name);
			return (value != null) ? dateFormat.parse(value) : dateFormat.parse(defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
            return null;
		}
	}
	
	public File save(MultipartFile file, String path) throws IOException {
        String fileName = file.getOriginalFilename();
        File destinationFile = new File(path + File.separator + fileName);

        // Tạo thư mục nếu nó không tồn tại
        destinationFile.getParentFile().mkdirs();

        // Lưu tệp (file) vào đường dẫn
        file.transferTo(destinationFile);

        return destinationFile;
    }
}

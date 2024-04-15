package com.poly.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dao.AccountRepository;
import com.poly.entity.Account;

@Service
public class UserService {
	@Autowired
    private AccountRepository accountRepository;

    public Account getUserByUsername(String username) {
    	System.out.println("Getting user by username: " + username);
        // Thực hiện logic để lấy thông tin người dùng từ cơ sở dữ liệu
        return accountRepository.findByUsername(username);
    }

    public void updateUser(Account user) {
    	System.out.println("Updating user: " + user.getUsername());
        // Thực hiện logic để lưu thông tin người dùng vào cơ sở dữ liệu
        accountRepository.save(user);
    }
    public Account getuserInfo(String username) {
        return accountRepository.findByUsername(username);
    }
}

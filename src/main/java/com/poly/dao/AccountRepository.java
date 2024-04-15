package com.poly.dao;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.entity.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long>{
	// Các phương thức tùy chỉnh nếu cần
    Account findByUsername(String username);
}

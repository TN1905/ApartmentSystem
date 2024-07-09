package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Role;
import com.poly.entity.Wallet;

public interface RoleDAO extends JpaRepository<Role, Integer> {

}

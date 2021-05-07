package com.itheima.store.service;

import java.sql.SQLException;

import com.itheima.store.domain.User;

/**
 * 用户模块的Service的接口
 * @author admin
 *
 */
public interface UserService {

	User findByUsername(String username) throws SQLException;

	void save(User user) throws SQLException;

	User findByCode(String code)throws SQLException;

	void update(User existUser)throws SQLException;

	User login(User user)throws SQLException;

}

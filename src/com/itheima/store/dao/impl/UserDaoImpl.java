package com.itheima.store.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.store.dao.UserDao;
import com.itheima.store.domain.User;
import com.itheima.store.utils.JDBCUtils;

/**
 * 用户模块的DAO的实现类
 * 
 * @author admin
 *
 */
public class UserDaoImpl implements UserDao {

	@Override
	public User findByUsername(String username) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user where username = ?";
		User user = queryRunner.query(sql, new BeanHandler<User>(User.class), username);
		return user;
	}

	@Override
	public void save(User user) throws SQLException {
		/*QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into user values (?,?,?,?,?,?,?,?,?,?)";
		Object[] params = { user.getUid(), user.getUsername(), user.getPassword(), user.getName(), user.getEmail(),
				user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(), user.getCode() };
		queryRunner.update(sql, params);*/
	    Connection conn = JDBCUtils.getConnection();
        String sql = "insert into user(uid, username, password, name, email, telephone, birthday, sex, state, code) "
                + "values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUid());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getTelephone());
            if (user.getBirthday() != null) {
                ps.setDate(7, new java.sql.Date(user.getBirthday().getTime()));
            } else {
                ps.setDate(7, null);
            }
            ps.setString(8, user.getSex());
            ps.setInt(9, user.getState());
            ps.setString(10, user.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
	}

	@Override
	public User findByCode(String code) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user where code = ?";
		User user = queryRunner.query(sql, new BeanHandler<User>(User.class), code);
		return user;
	}

	@Override
	public void update(User user) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "update user set username = ?,password = ?,name=?,email=?,telephone=?,birthday=?,sex=?,state=?,code=? where uid = ?";
		Object[] params = {  user.getUsername(), user.getPassword(), user.getName(), user.getEmail(),
				user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(), user.getCode(),user.getUid() };
		queryRunner.update(sql, params);
	}

	@Override
	public User login(User user) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user where username = ? and password = ? and state = ?";
		System.out.print(user);
		User existUser = queryRunner.query(sql, new BeanHandler<User>(User.class), user.getUsername(),user.getPassword(),2);
		return existUser;
	}
	
	

}

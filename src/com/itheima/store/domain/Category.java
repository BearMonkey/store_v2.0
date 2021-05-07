package com.itheima.store.domain;

import java.io.Serializable;

/**
 * 分类模块的实体对象:
 * @author admin
 *
 */
public class Category implements Serializable{
	private String cid;
	private String cname;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
}

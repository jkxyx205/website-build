package com.rick.dev.persistence;

import com.rick.dev.sys.User;
import com.rick.dev.utils.JacksonUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//import com.yodean.lftc.modules.sys.entity.User;
//import com.yodean.lftc.modules.sys.utils.UserUtils;

@MappedSuperclass 
public class DataEntity<T> implements Serializable {
	public static String DEL_FLAG_NORMAL = "1";
	
	public static String DEL_FLAG_REMOVE = "0";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="create_by",updatable=false)
	private String createBy;
	
	@Column(name="create_date",updatable=false)
	private Date createDate;
	
	@Column(name="update_by")
	private String updateBy;
	
	@Column(name="update_date")
	private Date updateDate;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="del_flag")
	private String delFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	private void preInsert() {
		User user = new User();
		user.setId(1);
		//UserUtils.getUser();

		Date now = new Date();
		this.createBy = String.valueOf(user.getId());
		this.createDate = now;
		this.updateBy = String.valueOf(user.getId());
		this.updateDate = now;
		this.delFlag = DEL_FLAG_NORMAL;
	}

	private void preUpdate() {
//		User user = UserUtils.getUser();
		User user = new User();
		user.setId(1);
		Date now = new Date();
		this.updateBy = String.valueOf(user.getId());
		this.updateDate = now;
		if (this.delFlag == null) this.delFlag = DEL_FLAG_NORMAL;
	}

	public void preSave() {
		if (this.id == null)
			preInsert();
		else
			preUpdate();
	}

	@Override
	public String toString() {
		return JacksonUtils.toJSon(this);
	}
}

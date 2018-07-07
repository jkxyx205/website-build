package com.rick.dev.plugin.log.entity;

import com.rick.dev.persistence.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 2017/7/12.
 */
@Entity
@Table(name="sys_log")
public class Log extends DataEntity<Log> {

}

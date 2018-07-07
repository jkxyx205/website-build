package com.rick.dev.service;


import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * Created by rick on 2017/7/12.
 */
public class DefaultService {
    @Resource
    protected QueryService queryService;

    @Resource
    protected JqgridService jqgridService;

    @Resource
    protected JdbcTemplateService jdbcTemplateService;

    @Resource
    protected JdbcTemplate JdbcTemplate;
}

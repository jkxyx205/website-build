package com.yodean.site.web.tpl.service;

import com.rick.dev.service.DefaultService;
import com.yodean.site.web.common.cache.PlatformCache;
import com.yodean.site.web.tpl.entity.Header;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/7/12.
 */
@Service
public class HeaderService extends DefaultService{

    public String getHeaderHtmlByWebId(Integer webId) {
        String queryName = "com.yodean.site.web.tpl.header.getHeaderByWebId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("webId", webId);

        List<Header> list = jdbcTemplateService.queryForSpecificParam(queryName, params, Header.class);
        if (CollectionUtils.isNotEmpty(list)) {
            String html = list.get(0).getHtml();

            //获取菜单内容

            return html;
        }
        return "";
    }

    public Header getHeaderById(Integer id) {
        return PlatformCache.getHeader().get(id);
    }

    public List<Header> getHeaders() {
        return new ArrayList<Header>(PlatformCache.getHeader().values());
    }
}

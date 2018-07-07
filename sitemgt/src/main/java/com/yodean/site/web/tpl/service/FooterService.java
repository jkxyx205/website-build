package com.yodean.site.web.tpl.service;

import com.rick.dev.service.DefaultService;
import com.yodean.site.web.common.cache.PlatformCache;
import com.yodean.site.web.tpl.entity.Footer;
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
public class FooterService extends DefaultService{

    public String getFooterHtmlByWebId(Integer webId) {
        String queryName = "com.yodean.site.web.tpl.footer.getFooterByWebId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("webId", webId);
        List<Footer> list = jdbcTemplateService.queryForSpecificParam(queryName, params, Footer.class);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0).getHtml();
        }
        return "";
    }

    public Footer getFooterById(Integer id) {
        return PlatformCache.getFooter().get(id);
    }

    public List<Footer> getFooters() {
       return new ArrayList<Footer>(PlatformCache.getFooter().values());
    }
}

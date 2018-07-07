package com.yodean.site.web.tpl.service;

import com.rick.dev.service.DefaultService;
import com.yodean.site.web.common.cache.PlatformCache;
import com.yodean.site.web.tpl.entity.Layout;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2017/8/17.
 */
@Service
public class LayoutService extends DefaultService {
    public List<Layout> getLayouts() {
        return new ArrayList<Layout>(PlatformCache.getLayout().values());
    }

    public Layout getLayoutById(Integer id) {
        return PlatformCache.getLayout().get(id);
    }
}

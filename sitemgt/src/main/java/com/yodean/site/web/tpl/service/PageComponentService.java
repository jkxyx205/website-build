package com.yodean.site.web.tpl.service;

import com.rick.dev.persistence.DataEntity;
import com.rick.dev.service.DefaultService;
import com.rick.dev.service.JdbcTemplateService;
import com.rick.dev.utils.sql.SqlFormatter;
import com.yodean.site.web.tpl.dao.PageComponentDao;
import com.yodean.site.web.tpl.dto.PageComponentDto;
import com.yodean.site.web.tpl.entity.PageComponent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2017/8/14.
 */
@Service
public class PageComponentService extends DefaultService {

    @Resource
    private PageService pageService;

    @Resource
    private PageComponentDao pageComponentDao;

    public PageComponent getPageComponentById(Integer id) {
        return pageComponentDao.findOne(id);
    }

    public PageComponentDto getPageComponentDtoById(Integer id) {
        final String queryName = "com.yodean.site.web.tpl.page.getComponentsByPageId";
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("id", id);

        List<PageComponentDto> pageComponentList = jdbcTemplateService.queryForSpecificParam(queryName, params, PageComponentDto.class);

        if (CollectionUtils.isNotEmpty(pageComponentList)) {
            PageComponentDto pc = pageComponentList.get(0);
//            Integer contentType = pc.getContentType();
//            Integer categoryId = pc.getCategoryId();
//            Map<String, Object> map = RegistrationCenter.getContent(contentType, categoryId, pc.getOptionsMap(), false);
//            pc.setLabel(map.get("label") + "");
//            pc.setContent(map);
            return pc;
        } else {
            return null;
        }

    }

    public void save(PageComponent pageComponent) {
        pageComponent.preSave();
        pageComponentDao.save(pageComponent);
    }

    public void delete(Integer id) {
        PageComponent pageComponent = pageComponentDao.findOne(id);
        pageComponent.setDelFlag(DataEntity.DEL_FLAG_REMOVE);
        save(pageComponent);
    }

    public void deleteByContentId(Integer categoryId) {
        Map<String, Object> params= new HashMap<String, Object>(1);
        params.put("categoryId", categoryId);

        jdbcTemplateService.updateForSpecificParamSQL("delete from w_page_component where category_id = :categoryId", params);
    }

    public void removeAllComponentsByPageId(Integer pageId) {
        Map<String, Object> params= new HashMap<String, Object>(1);
        params.put("pageId", pageId);

        jdbcTemplateService.updateForSpecificParamSQL("update w_page_component set del_flag = '0' where page_id = :pageId", params);
    }

    public void updateRelationShipByHTML(Integer pageId, String html) {
        String cpnIds = pageService.getCpnString(html);
        Map<String, Object> params= new HashMap<String, Object>(1);
        params.put("pageId", pageId);
        params.put("cpnIds", cpnIds);



        jdbcTemplateService.updateForSpecificParamSQL("UPDATE w_page_component\n" +
                "SET del_flag = '0'\n" +
                "WHERE\n" +
                "\tpage_id = :pageId\n" +
                "and id not in(:cpnIds)", params);

    }

    /***
     * null 已删除的控件
     * long 【本体】控件id
     * @param id
     * @return
     */
    public Long getStatusOfComponent(Integer id) {

        String sql = "SELECT\n" +
                "\tmin(wpc2.id) s\n" +
                "FROM\n" +
                "\tw_page_component wpc,\n" +
                "\tw_page_component wpc2\n" +
                "WHERE\n" +
                "\twpc.component_id = wpc2.component_id\n" +
                "AND wpc.category_id = wpc2.category_id\n" +
                "AND wpc.id = :id\n" +
                "AND wpc.del_flag = '1'\n" +
                "AND wpc2.del_flag = '1'";

        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("id", id);

        Long count = jdbcTemplateService.queryForSpecificParamSQLSingle(sql, param, Long.class);

        return count;

    }
}

package com.yodean.site.web.tpl.service;

import com.rick.dev.service.DefaultService;
import com.yodean.site.web.tpl.dao.ThemeDao;
import com.yodean.site.web.tpl.entity.Theme;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by rick on 2017/9/6.
 */
@Service
public class ThemeService extends DefaultService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private ThemeDao themeDao;

    public Theme getThemeById(Integer id) {
        return themeDao.findOne(id);
    }

    public List<Theme> getAllThemes() {
       return getThemes(null, null, null, null);
    }


    public List<Theme> getThemes(Integer styleId, Integer headerId, Integer footerId, Integer blockId) {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("headerId", headerId);
        params.put("footerId", footerId);
        params.put("blockId", blockId);
        params.put("styleId", styleId);

        return jdbcTemplateService.queryForSpecificParamSQL("SELECT\n" +
                "\tid,\n" +
                "\tpage_id AS pageId,\n" +
                "\tstyle_id AS styleId,\n" +
                "\theader_id AS headerId,\n" +
                "\tfooter_id AS footerId,\n" +
                "\tblock_id AS blockId\n" +
                "FROM\n" +
                "\tw_theme\n" +
                "WHERE header_id = :headerId\n" +
                "AND footer_id = :footerId\n" +
                "AND style_id = :styleId\n" +
                "AND block_id = :blockId",params, Theme.class);
    }

    public void initThemes(List<Theme> themes) {
        jdbcTemplate.execute("TRUNCATE TABLE `w_theme`");



        if (CollectionUtils.isEmpty(themes)) return;

        List<Object[]> params = new ArrayList<Object[]>(themes.size());
        Date now = new Date();

        for (Theme theme : themes) {
            Object[] p = new Object[7];
            p[0] = theme.getPageId();
            p[1] = now;
            p[2] = now;
            p[3] = theme.getBlockId();
            p[4] = theme.getFooterId();
            p[5] = theme.getHeaderId();
            p[6] = theme.getStyleId();

            params.add(p);
        }

        jdbcTemplate.batchUpdate("INSERT INTO .`w_theme` (\n" +
                "\t`page_id`,\n" +
                "\t`create_date`,\n" +
                "\t`update_date`,\n" +
                "\t`create_by`,\n" +
                "\t`block_id`,\n" +
                "\t`update_by`,\n" +
                "\t`footer_id`,\n" +
                "\t`header_id`,\n" +
                "\t`style_id`,\n" +
                "\t`del_flag`,\n" +
                "\t`remarks`\n" +
                ")\n" +
                "VALUES\n" +
                "\t(\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t'1',\n" +
                "\t\t?,\n" +
                "\t\t'1',\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t?,\n" +
                "\t\t'1',\n" +
                "\t\tNULL\n" +
                "\t);", params);
    }

}

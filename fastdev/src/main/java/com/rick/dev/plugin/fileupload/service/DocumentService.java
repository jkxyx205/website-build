package com.rick.dev.plugin.fileupload.service;

import com.rick.dev.config.Global;
import com.rick.dev.plugin.fileupload.entity.Document;
import com.rick.dev.service.DefaultService;
import com.rick.dev.service.JqgridService;
import com.rick.dev.utils.FileUtils;
import com.rick.dev.vo.JqGrid;
import com.rick.dev.vo.PageModel;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService extends DefaultService {

	public static final String FILE_FILE = "1";
	
	public static final String FILE_FOLDER = "0";

	@Resource
	private JqgridService jqgridService;
	
	@Resource
	private DocumentDao DocumentDao;
	


	public void save(Document doc) {
		doc.preSave();
		DocumentDao.save(doc);
	}

	public void delete(int id) {
		Document doc = findDocumentById(id);
		if(doc != null) {
			JdbcTemplate.update("update sys_document s set s.del_flag = '0' where s.id = ?", id);
		}
	}

	public Document findDocumentById(int id) {
		Document doc = DocumentDao.findOne(id);
		return doc;
	}

	public JqGrid<Document> findDocument(Document example, Integer page) throws Exception {
		Map params = BeanMap.create(example);
		PageModel pageModel = new PageModel();
		pageModel.setPage(page);
		pageModel.setRows(Global.PAGE_ROWS);
		pageModel.setQueryName("com.yodean.site.web.content.getPics");

		return jqgridService.getJqgirdData(pageModel, params, Document.class);
	}

	
	public JqGrid findAllDocuments(Integer curPage, Integer rows) throws Exception {
		PageModel model = new PageModel();
		model.setQueryName("com.rick.dev.plugin.fileupload.entity.getAllDocuments");
		model.setPage(curPage);
		model.setRows(rows);
		model.setSord("desc");
		model.setSidx("createDate");
		JqGrid<Map<String,Object>> jqGrid = jqgridService.getJqgirdData(model, null, Map.class);
		
		for (Map<String,Object> doc : jqGrid.getRows()) {
			long size = Long.valueOf((String) doc.get("size"));
			doc.put("formatSize", FileUtils.getFormatSize(size));
		}
			
		return jqGrid;
	}

}

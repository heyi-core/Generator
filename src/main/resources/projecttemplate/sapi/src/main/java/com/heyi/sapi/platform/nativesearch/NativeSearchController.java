package com.heyi.${project.dataBase}.platform.${table.tableNameLower}4es;

import com.heyi.${project.dataBase}.basecommon.BaseResponse;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Scope("request")
@RequestMapping("/${table.tableNameLower}4es")
@Api(value = "附近", tags = "附近")
public class ${table.tableNameUpperCamel}4esController {
    @Resource
    private ${table.tableNameUpperCamel}4esService ${table.tableNameLowerCamel}4esService;
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @ApiOperation(value = "创建索引", notes = "创建索引")
    @PostMapping("/creatIndex")
    public BaseResponse creatIndex() {
        elasticsearchRestTemplate.createIndex(${table.tableNameUpperCamel}4es.class);
        elasticsearchRestTemplate.putMapping(${table.tableNameUpperCamel}4es.class);
        return new BaseResponse(true, "创建索引成功");
    }

    @ApiOperation(value = "删除索引", notes = "删除索引")
    @PostMapping("/dropIndex")
    public BaseResponse dropIndex() {
        elasticsearchRestTemplate.deleteIndex(${table.tableNameUpperCamel}4es.class);
        return new BaseResponse(true, "删除索引成功");
    }

    @ApiOperation(value = "全文搜索", notes = "全文搜索")
    @PostMapping(value = "/pagesBySearchWord")
    public BaseResponse pagesBySearchWord(@RequestBody ${table.tableNameUpperCamel}4es ${table.tableNameLowerCamel}4es) {
        if(StringUtils.isBlank(${table.tableNameLowerCamel}4es.getSearchWord())){
            return new BaseResponse(false, "请输入关键词");
        }
        Page<${table.tableNameUpperCamel}4es> page = ${table.tableNameLowerCamel}4esService.pagesBySearchWord(${table.tableNameLowerCamel}4es);
        return new BaseResponse(true, "查询成功", page.getContent(), page.getTotalElements());
    }

}

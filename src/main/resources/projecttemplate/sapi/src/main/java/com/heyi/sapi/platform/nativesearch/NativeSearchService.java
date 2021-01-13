package com.heyi.${project.dataBase}.platform.${table.tableNameLower}4es;

import com.heyi.${project.dataBase}.platform.${table.tableNameLower}.${table.tableNameUpperCamel} ;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.heyi.${project.dataBase}.basecommon.BaseConstant.NOT_DELETED;

@Service
public class ${table.tableNameUpperCamel}4esService {
    @Resource
    private ${table.tableNameUpperCamel}4esDao ${table.tableNameLowerCamel}4esDao;

    public void save(${table.tableNameUpperCamel} ${table.tableNameLowerCamel}){
        ${table.tableNameUpperCamel}4es ${table.tableNameLowerCamel}4es=new ${table.tableNameUpperCamel}4es();
<#list table.columns as column>
<#if column.columnName?contains("content")>
        ${table.tableNameLowerCamel}4es.set${column.columnNameUpperCamel}(RegExUtils.replaceAll(${table.tableNameLowerCamel}.get${column.columnNameUpperCamel}(),"[^\\u4e00-\\u9fa5]",""));
<#else>
        ${table.tableNameLowerCamel}4es.set${column.columnNameUpperCamel}(${table.tableNameLowerCamel}.get${column.columnNameUpperCamel}());
</#if>
</#list>
        GeoPoint geoPoint=new GeoPoint(${table.tableNameLowerCamel}.get${table.tableNameUpperCamel}Lat().doubleValue(),${table.tableNameLowerCamel}.get${table.tableNameUpperCamel}Lng().doubleValue());
        ${table.tableNameLowerCamel}4es.setLocation(geoPoint);
        ${table.tableNameLowerCamel}4esDao.save(${table.tableNameLowerCamel}4es);
    }

public Page<${table.tableNameUpperCamel}4es> pagesBySearchWord(${table.tableNameUpperCamel}4es ${table.tableNameLowerCamel}4es) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(${table.tableNameLowerCamel}4es.getSearchWord())) {
<#list table.columns as column>
<#if column.columnName?contains("content")>
        /*${column.columnComment}*/
        MatchQueryBuilder ${column.columnNameLowerCamel}From = QueryBuilders
        .matchQuery("${column.columnNameLowerCamel}", ${table.tableNameLowerCamel}4es.getSearchWord());
        builder.should(${column.columnNameLowerCamel}From);
</#if>
</#list>
        /*todo 将需要搜索的条件都在这里展开*/
        builder2.must(builder);
        }
        TermQueryBuilder deleteState = QueryBuilders.termQuery("deleteState", NOT_DELETED);
        builder2.must(deleteState);
        Pageable pageRequest = PageRequest.of(${table.tableNameLowerCamel}4es.getStart() - 1, ${table.tableNameLowerCamel}4es.getLength());
        FieldSortBuilder order;
        SearchQuery searchQuery;
        if (StringUtils.isNotBlank(${table.tableNameLowerCamel}4es.getOrderField())) {
        if ("DESC".equals(${table.tableNameLowerCamel}4es.getOrderType())) {
        order = SortBuilders.fieldSort(${table.tableNameLowerCamel}4es.getOrderField()).order(SortOrder.DESC).unmappedType("double");
        } else {
        order = SortBuilders.fieldSort(${table.tableNameLowerCamel}4es.getOrderField()).order(SortOrder.ASC).unmappedType("double");
        }
        searchQuery = new NativeSearchQueryBuilder().withPageable(pageRequest).withSort(new ScoreSortBuilder()).withSort(order).withQuery(builder2).build();
        } else {
        searchQuery = new NativeSearchQueryBuilder().withPageable(pageRequest).withQuery(builder2).build();
        }
        return ${table.tableNameLowerCamel}4esDao.search(searchQuery);
        }

}

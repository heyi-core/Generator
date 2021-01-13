package com.heyi.${project.dataBase}.platform.${table.tableNameLower}4es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ${table.tableNameUpperCamel}4esDao extends ElasticsearchRepository<${table.tableNameUpperCamel}4es,String> {
}
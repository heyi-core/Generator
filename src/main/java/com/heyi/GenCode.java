package com.heyi;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.*;

import static com.heyi.Utils.*;

@Log4j
public class GenCode {

    private static Connection conn;
    private static JSONObject columnTypeJson;


    public static List<Table> gettableinfo(Project project) throws Exception {
        String tablestring = project.getTable();
        conn = DriverManager.getConnection(project.getJdbcUrl(), project.getJdbcUsername(), project.getJdbcPassword());
        String columnTypeString = ResourceUtil.readUtf8Str("columnType.json");
        columnTypeJson = JSONUtil.parseObj(columnTypeString);
//       准备完毕
        List<Table> tables = new ArrayList<>();
        String sql = "select table_name,table_comment from information_schema.tables where table_schema='" + project.getDataBase() + "'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            if (tablestring.contains(",") && !ArrayUtil.contains(tablestring.split(","), result.getString("table_name"))) {
                continue;
            }
            if (tablestring.lastIndexOf("*") > 0) {
                tablestring = tablestring.replace("*", "");
                if (!result.getString("table_name").contains(tablestring)) {
                    continue;
                }
            }
            Table table = new Table();
            table.setTableName(result.getString("table_name"));
            table.setTableComment(result.getString("table_comment"));
            table.setTableNameLowerCamel(toCamel(table.getTableName()));
            table.setTableNameLower(toLower(table.getTableNameLowerCamel()));
            table.setTableNameUpperCamel(toUpperFirst(table.getTableNameLowerCamel()));
            tables.add(table);
            getcoluminfo(table);

        }
        return tables;
    }

    private static void getcoluminfo(Table table) throws Exception {
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet resultSet = dbmd.getColumns(conn.getCatalog(), "%", table.getTableName(), "%");
        List<Colum> colums = new ArrayList<Colum>();
        while (resultSet.next()) {
            Colum colum = new Colum();
            colum.setColumnComment(resultSet.getString("REMARKS"));
            colum.setColumnName(resultSet.getString("COLUMN_NAME"));
            if (colum.getColumnName().equals("creator")) {
                table.setHaveCreator(true);
            }
            if (colum.getColumnName().equals("delete_state")) {
                table.setHaveDeleteState(true);
            }
            if (colum.getColumnComment().contains("经度") || colum.getColumnComment().contains("维度")) {
                table.setHaveLocation(true);
            }
            if (colum.getColumnName().equals("create_time")) {
                table.setHaveCreateTime(true);
            }
            if (colum.getColumnName().equals("modifyor")) {
                table.setHaveModifyor(true);
            }
            if (colum.getColumnName().equals("modify_time")) {
                table.setHaveModifyTime(true);
            }
            colum.setSqlType(resultSet.getString("TYPE_NAME"));
            colum.setJavaType(columnTypeJson.getJSONObject(colum.getSqlType()).getStr("javaType"));
            colum.setJdbcType(columnTypeJson.getJSONObject(colum.getSqlType()).getStr("jdbcType"));
            colum.setImportType(columnTypeJson.getJSONObject(colum.getSqlType()).getStr("importType"));
            colum.setEsType(columnTypeJson.getJSONObject(colum.getSqlType()).getStr("esType"));
            colum.setColumnNameLowerCamel(toCamel(colum.getColumnName()));
            colum.setColumnNameLower(toLower(colum.getColumnNameLowerCamel()));
            colum.setColumnNameUpperCamel(toUpperFirst(colum.getColumnNameLowerCamel()));
            colums.add(colum);
        }
        ResultSet resultSet2 = dbmd.getPrimaryKeys(conn.getCatalog(), "%", table.getTableName());
        while (resultSet2.next()) {
            String name = resultSet2.getString("COLUMN_NAME");
            for (Colum colum : colums) {
                if (colum.getColumnName().equals(name)) {
                    table.setKey(colum);
                }
            }
        }
        table.setColumns(colums);
    }


}

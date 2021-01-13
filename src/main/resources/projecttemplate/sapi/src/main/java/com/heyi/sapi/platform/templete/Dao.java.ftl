package  com.heyi.${project.dataBase}.platform.${table.tableNameLower};

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
* ${table.tableComment}
* @author ${project.author}
* @date  ${project.createTime}
*/
@Mapper
public interface ${table.tableNameUpperCamel}Dao {

    /**
    * 查询多条数据
    */
    List<${table.tableNameUpperCamel}> pages(${table.tableNameUpperCamel} ${table.tableNameLowerCamel});
    /**
    * 查询一条数据
    */
    ${table.tableNameUpperCamel} getone(${table.tableNameUpperCamel} ${table.tableNameLowerCamel});

    /**
    * 查询所有数据
    */
    List<${table.tableNameUpperCamel}> getAll();

    /**
    * 新增数据
    */
    int add(${table.tableNameUpperCamel} ${table.tableNameLowerCamel});

    /**
    * 通过ID查询单条数据
    */
    ${table.tableNameUpperCamel} getById(Long ${table.tableNameLowerCamel}Id);

    /**
    * 修改数据
    */
    int update(${table.tableNameUpperCamel} ${table.tableNameLowerCamel});

    /**
    * 通过主键删除数据
    */
    int deleteById(Long ${table.tableNameLowerCamel}Id);
}

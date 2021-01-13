package  com.heyi.${project.dataBase}.platform.${table.tableNameLower};

import com.heyi.${project.dataBase}.baseutil.SnowFlakeId;
import org.springframework.stereotype.Service;
import com.heyi.${project.dataBase}.platform.sysuser.SysUser;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
<#if  table.haveDeleteState >
import static com.heyi.${project.dataBase}.basecommon.BaseConstant.NOT_DELETED;
</#if>
/**
* ${table.tableComment}
* @author ${project.author}
* @date  ${project.createTime}
*/
@Service
public class ${table.tableNameUpperCamel}Service {
    @Resource
    private ${table.tableNameUpperCamel}Dao dao;

    /**
    * 查询多条数据
    */
    
    public List<${table.tableNameUpperCamel}> pages(${table.tableNameUpperCamel} ${table.tableNameLowerCamel}){
        return this.dao.pages(${table.tableNameLowerCamel});
    }

    /**
    * 查询一条数据
    */

    public ${table.tableNameUpperCamel} getone(${table.tableNameUpperCamel} ${table.tableNameLowerCamel}){
        return this.dao.getone(${table.tableNameLowerCamel});
    }

    /**
    * 查询所有数据
    */
    public List<${table.tableNameUpperCamel}> getAll(){
        return this.dao.getAll();
    }

    /*
     * 新增数据
     */
    public ${table.tableNameUpperCamel} add(${table.tableNameUpperCamel} ${table.tableNameLowerCamel}, SysUser currentUser){
<#if  table.key?? >
        ${table.tableNameLowerCamel} .set${table.key.columnNameUpperCamel}(SnowFlakeId.generateID());
</#if>
<#if  table.haveCreateTime >
        ${table.tableNameLowerCamel} .setCreateTime(Calendar.getInstance().getTime());
</#if>
<#if  table.haveCreator >
        ${table.tableNameLowerCamel} .setCreator(currentUser.getSysUserId());
</#if>
<#if  table.haveDeleteState >
        ${table.tableNameLowerCamel} .setDeleteState(NOT_DELETED);
</#if>
        this.dao.add(${table.tableNameLowerCamel});
        return  ${table.tableNameLowerCamel};
    }

<#if  table.key?? >
    /**
    * 通过ID查询单条数据
    */
    public ${table.tableNameUpperCamel} getById(Long ${table.key.columnNameLowerCamel}){
        return this.dao.getById(${table.key.columnNameLowerCamel});
    }
</#if>

    /**
    * 修改数据
    */
    public ${table.tableNameUpperCamel} update(${table.tableNameUpperCamel} ${table.tableNameLowerCamel},SysUser currentUser){
<#if  table.haveModifyor >
        ${table.tableNameLowerCamel} .setModifyor(currentUser.getSysUserId());
</#if>
<#if  table.haveModifyTime >
        ${table.tableNameLowerCamel} .setModifyTime(Calendar.getInstance().getTime());
</#if>
        this.dao.update(${table.tableNameLowerCamel});
        return ${table.tableNameLowerCamel} ;
    }
<#if  table.key?? >
    /**
    * 通过主键删除数据
    */
    public boolean deleteById(Long ${table.key.columnNameLowerCamel}){
        return this.dao.deleteById(${table.key.columnNameLowerCamel})>0;
    }
</#if>
}
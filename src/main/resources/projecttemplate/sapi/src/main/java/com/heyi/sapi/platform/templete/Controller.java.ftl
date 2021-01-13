package  com.heyi.${project.dataBase}.platform.${table.tableNameLower};

import com.heyi.${project.dataBase}.basecommon.BaseController;
import com.heyi.${project.dataBase}.basecommon.BaseResponse;
import io.swagger.annotations.ApiOperation;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Scope;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.util.List;

/**
* ${table.tableComment}
* @author ${project.author}
* @date  ${project.createTime}
*/
@RestController
@Scope("request")
@RequestMapping("/${table.tableNameLower}")
@Api(value = "${table.tableComment}", tags = "${table.tableComment}操作接口")
public class ${table.tableNameUpperCamel}Controller extends BaseController{

    @Resource
    private ${table.tableNameUpperCamel}Service ${table.tableNameLowerCamel}Service;

    @ApiOperation(value = "分页", notes = "分页查询")
    @PostMapping(value = "/pages")
    public BaseResponse pages(@RequestBody ${table.tableNameUpperCamel} ${table.tableNameLowerCamel}) {
        PageHelper.startPage(${table.tableNameLowerCamel} .getStart(), ${table.tableNameLowerCamel} .getLength());
        List<${table.tableNameUpperCamel}> list = ${table.tableNameLowerCamel}Service.pages(${table.tableNameLowerCamel});
        return new BaseResponse(true, "获取成功", list, ((Page) list).getTotal());
    }

    @ApiOperation(value = "获取所有", notes = "获取所有")
    @PostMapping(value = "/getAll")
    public BaseResponse getAll() {
        List<${table.tableNameUpperCamel}> list = ${table.tableNameLowerCamel}Service.getAll();
        return new BaseResponse(true, "获取成功", list);
    }

    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping("/add")
    public BaseResponse add(@RequestBody ${table.tableNameUpperCamel} ${table.tableNameLowerCamel}) {
        ${table.tableNameUpperCamel} new${table.tableNameUpperCamel} = ${table.tableNameLowerCamel}Service.add(${table.tableNameLowerCamel}, sysUser);
        return new BaseResponse(true, "添加成功", new${table.tableNameUpperCamel});
    }

<#if  table.key?? >
    @ApiOperation(value = "详情", notes = "详情")
    @PostMapping("/detail")
    public BaseResponse detail(@RequestBody ${table.tableNameUpperCamel} ${table.tableNameLowerCamel}) {
        ${table.tableNameUpperCamel} new${table.tableNameUpperCamel} = ${table.tableNameLowerCamel}Service.getById(${table.tableNameLowerCamel}. get${table.key.columnNameUpperCamel}());
        if (new${table.tableNameUpperCamel} != null) {
            return new BaseResponse(true, "查询成功", new${table.tableNameUpperCamel});
        } else {
            return new BaseResponse(false, "没有找到");
        }
    }
</#if>

    @ApiOperation(value = "修改", notes = "用户修改")
    @PostMapping("/update")
    public BaseResponse update(@RequestBody ${table.tableNameUpperCamel} ${table.tableNameLowerCamel}) {
        ${table.tableNameUpperCamel} new${table.tableNameUpperCamel} = ${table.tableNameLowerCamel}Service.update(${table.tableNameLowerCamel}, sysUser);
        return new BaseResponse(true, "修改成功", new${table.tableNameUpperCamel});
    }
<#if  table.key??>
//尽量使用假删除
//    @ApiOperation(value = "删除", notes = "删除")
//    @PostMapping("/delete")
//    public BaseResponse delete(@RequestBody ${table.tableNameUpperCamel} ${table.tableNameLowerCamel}) {
//        ${table.tableNameLowerCamel}Service.deleteById(${table.tableNameLowerCamel}. get${table.key.columnNameUpperCamel}());
//        return new BaseResponse(true, "删除成功");
//    }
</#if>

}
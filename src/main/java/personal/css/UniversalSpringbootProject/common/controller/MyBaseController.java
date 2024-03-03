package personal.css.UniversalSpringbootProject.common.controller;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import personal.css.UniversalSpringbootProject.common.pojo.BaseEntity;
import personal.css.UniversalSpringbootProject.common.service.MyBaseService;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.common.vo.SuccessCount;

import javax.validation.constraints.Pattern;
import java.sql.SQLException;
import java.text.ParseException;


/**
 * @Description: 自定义基础控制层抽象类
 * @Author: CSS
 * @Date: 2024/2/29 8:38
 */
@RestController
@Validated
public abstract class MyBaseController<S extends MyBaseService<T>, T extends BaseEntity> {

    //不能用Resource
    @Autowired
    protected S service;

    /**
     * 新增
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping("/create")
    public ResponseEntity<ResultVo<T>> insert(@ApiParam(hidden = true) Long userId, @RequestBody T model) {
        return new ResponseEntity<>(new ResultVo<>(true, null, this.service.insert(userId, model)), HttpStatus.OK);
    }


    /**
     * 修改
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "修改(可只传：id+修改字段+lockVersion))")
    @PutMapping("/update")
    public ResponseEntity<ResultVo<T>> update(@ApiParam(hidden = true) Long userId, @RequestBody T model) {
        //为配合业务层进行更新判断，在每一次更新数据时，必须传当前数据的乐观锁版本
        if (model.getLockVersion() == null) {
            throw new RuntimeException("lockVersion修改必传");
        }
        return new ResponseEntity<>(new ResultVo<>(true, null, this.service.update(userId, model)), HttpStatus.OK);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    public ResponseEntity<ResultVo<SuccessCount>> delete(@ApiParam(hidden = true) Long userId, Long id) {
        return new ResponseEntity<>(new ResultVo<>(true, null, this.service.delete(userId, id)), HttpStatus.OK);
    }


    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取")
    @GetMapping("/getById")
    public ResponseEntity<ResultVo<T>> getById(@ApiParam(hidden = true) Long userId, String id) {
        return new ResponseEntity<>(new ResultVo<>(true, null, this.service.getById(id)), HttpStatus.OK);
    }


    /**
     * 列表查询（非分页）
     *
     * @return
     */
    @ApiOperation(value = "获取（非分页）")
    @GetMapping("/getList")
    public ResponseEntity<ResultVo<ListResult<T>>> getList(@ApiParam(hidden = true) Long userId, String filter, String order) {
        return new ResponseEntity<>(new ResultVo<>(true, null, this.service.list(filter, order)), HttpStatus.OK);
    }


    /**
     * 列表查询（分页）
     *
     * @return
     */
    @ApiOperation(value = "获取（分页）")
    @GetMapping("/getPaged")
    public ResponseEntity<ResultVo<ListResult<T>>> getPaged(@ApiParam(hidden = true) Long userId, String filter, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String order) {
        return new ResponseEntity<>(new ResultVo<>(true, null, this.service.pageList(filter, pageNum, pageSize, order)), HttpStatus.OK);
    }


    /**
     * 获取建表sql语句
     *
     * @return
     */
    @ApiOperation(value = "获取建表sql语句")
    @GetMapping("/getCreateTableSql")
    public ResponseEntity<ResultVo<String>> getCreateTableSql(@ApiParam(hidden = true) Long userId) throws SQLException {
        return new ResponseEntity<>(new ResultVo<>(true, null, this.service.getCreateTableSql()), HttpStatus.OK);
    }


    @ApiOperation("根据sql查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "表名请使用TABLE_NAME进行代替")
    })
    @GetMapping("/queryBySql")
    public ResponseEntity<ResultVo<?>> queryBySql(@ApiParam(hidden = true) Long userId, @RequestParam @Pattern(regexp = "^.*\\s+TABLE_NAME\\s+.*$", message = "传入sql未包含TABLE_NAME！") String sql) throws SQLException {
        return new ResponseEntity<>(new ResultVo<>(true,null,this.service.queryBySql(sql)), HttpStatus.OK);
    }


    @ApiOperation("根据sql插入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "表名请使用TABLE_NAME进行代替")
    })
    @GetMapping("/insertBySql")
    public ResponseEntity<ResultVo<?>> insertBySql(@ApiParam(hidden = true) Long userId, @RequestParam @Pattern(regexp = "^.*\\s+TABLE_NAME\\s+.*$", message = "传入sql未包含TABLE_NAME！") String sql) throws SQLException {
        return new ResponseEntity<>(new ResultVo<>(true,null,this.service.insertBySql(sql)), HttpStatus.OK);
    }


    @ApiOperation("根据sql更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "表名请使用TABLE_NAME进行代替")
    })
    @PutMapping("/updateBySql")
    public ResponseEntity<ResultVo<?>> updateBySql(@ApiParam(hidden = true) Long userId, @RequestParam @Pattern(regexp = "^.*\\s+TABLE_NAME\\s+.*$", message = "传入sql未包含TABLE_NAME！") String sql) throws SQLException {
        return new ResponseEntity<>(new ResultVo<>(true,null,this.service.updateBySql(sql)), HttpStatus.OK);
    }


    @ApiOperation("根据sql删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "表名请使用TABLE_NAME进行代替")
    })
    @GetMapping("/deleteBySql")
    public ResponseEntity<ResultVo<?>> deleteBySql(@ApiParam(hidden = true) Long userId, @RequestParam @Pattern(regexp = "^.*\\s+TABLE_NAME\\s+.*$", message = "传入sql未包含TABLE_NAME！") String sql) throws SQLException {
        return new ResponseEntity<>(new ResultVo<>(true,null,this.service.deleteBySql(sql)), HttpStatus.OK);
    }

    @ApiOperation(value = "根据属性-值进行插入，完全自定义",notes = "根据属性字符串结合值字符串进行插入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "properties", value = "属性名字符串，使用','隔开"),
            @ApiImplicitParam(name = "values", value = "值字符串，使用','隔开")
    })
    @GetMapping("/insertByPVS")
    public ResponseEntity<ResultVo<?>> insertByPVS(@ApiParam(hidden = true) Long userId, @RequestParam String properties, @RequestParam String values) throws SQLException, InstantiationException, IllegalAccessException {
        //解析属性字符串
        String[] ps = properties.split(",");

        //解析值字符串
        String[] vs = values.split(",");

        if(ps.length != vs.length)
            return new ResponseEntity<>(new ResultVo<>(false,"属性-值数量不匹配！",null), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new ResultVo<>(true,null,this.service.insertByPVS(ps,vs)), HttpStatus.OK);
    }


    @ApiOperation(value = "根据属性-值进行插入，部分自定义",notes = "根据属性字符串结合值字符串进行插入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "properties", value = "属性名字符串，使用','隔开"),
            @ApiImplicitParam(name = "values", value = "值字符串，使用','隔开")
    })
    @GetMapping("/insertByPVS2")
    public ResponseEntity<ResultVo<?>> insertByPVS2(@ApiParam(hidden = true) Long userId, @RequestParam String properties, @RequestParam String values) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException, ParseException {
        //解析属性字符串
        String[] ps = properties.split(",");

        //解析值字符串
        String[] vs = values.split(",");

        if(ps.length != vs.length)
            return new ResponseEntity<>(new ResultVo<>(false,"属性-值数量不匹配！",null), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new ResultVo<>(true,null,this.service.insertByPVS2(userId, ps,vs)), HttpStatus.OK);
    }
}

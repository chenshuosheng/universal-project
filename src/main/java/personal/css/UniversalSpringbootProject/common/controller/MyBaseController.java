package personal.css.UniversalSpringbootProject.common.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.css.UniversalSpringbootProject.common.pojo.BaseEntity;
import personal.css.UniversalSpringbootProject.common.service.MyBaseService;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.common.vo.SuccessCount;


/**
 * @Description: 自定义基础控制层抽象类
 * @Author: CSS
 * @Date: 2024/2/29 8:38
 */
@RestController
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
    public ResponseEntity<ResultVo<SuccessCount>> delete(@ApiParam(hidden = true) Long userId, String id) {
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
}

package personal.css.UniversalSpringbootProject.module.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;
import personal.css.UniversalSpringbootProject.module.admin.pojo.Slf4jLogging;
import personal.css.UniversalSpringbootProject.module.admin.service.Slf4jLoggingService;

/**
 * @Description: Slf4j日志控制层
 * @Author: CSS
 * @Date: 2024/3/7 23:26
 */
@Api(tags = "slf4j日志管理(超级管理员可见)")
@RestController
@RequestMapping("/admin/slf4jLog")
public class Slf4jLoggingController {
    @Autowired
    private Slf4jLoggingService slf4jLoggingService;

    /**
     * 列表查询（非分页）
     *
     * @return
     */
    @ApiOperation(value = "获取（非分页）")
    @GetMapping("/getList")
    public ResponseEntity<ResultVo<ListResult<Slf4jLogging>>> getList(@ApiParam(hidden = true) Long userId, String filter, String order) {
        return new ResponseEntity<>(new ResultVo<>(true, null, slf4jLoggingService.list(filter, order)), HttpStatus.OK);
    }


    /**
     * 列表查询（分页）
     *
     * @return
     */
    @ApiOperation(value = "获取（分页）")
    @GetMapping("/getPaged")
    public ResponseEntity<ResultVo<ListResult<Slf4jLogging>>> getPaged(@ApiParam(hidden = true) Long userId, String filter, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String order) {
        return new ResponseEntity<>(new ResultVo<>(true, null, slf4jLoggingService.pageList(filter, pageNum, pageSize, order)), HttpStatus.OK);
    }
}

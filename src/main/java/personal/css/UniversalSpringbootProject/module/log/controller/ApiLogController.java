package personal.css.UniversalSpringbootProject.module.log.controller;

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
import personal.css.UniversalSpringbootProject.module.account.pojo.ApiLog;
import personal.css.UniversalSpringbootProject.module.log.service.ApiLogService;

/**
 * @Description: 接口日志管理控制层
 * @Author: CSS
 * @Date: 2024/2/29 10:22
 */

@Api(tags = "接口日志管理(超级管理员可见)")
@RestController
@RequestMapping("/apiLog")
public class ApiLogController {

    @Autowired
    private ApiLogService apiLogService;

    /**
     * 列表查询（非分页）
     *
     * @return
     */
    @ApiOperation(value = "获取（非分页）")
    @GetMapping("/getList")
    public ResponseEntity<ResultVo<ListResult<ApiLog>>> getList(@ApiParam(hidden = true) Long userId, String filter, String order) {
        return new ResponseEntity<>(new ResultVo<>(true, null, apiLogService.list(filter, order)), HttpStatus.OK);
    }


    /**
     * 列表查询（分页）
     *
     * @return
     */
    @ApiOperation(value = "获取（分页）")
    @GetMapping("/getPaged")
    public ResponseEntity<ResultVo<ListResult<ApiLog>>> getPaged(@ApiParam(hidden = true) Long userId, String filter, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String order) {
        return new ResponseEntity<>(new ResultVo<>(true, null, apiLogService.pageList(filter, pageNum, pageSize, order)), HttpStatus.OK);
    }
}

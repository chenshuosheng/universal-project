package personal.css.UniversalSpringbootProject.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.css.UniversalSpringbootProject.common.service.CommonService;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;

import java.util.List;

/**
 * @Description: 公共资源
 * @Author: CSS
 * @Date: 2024/2/29 17:49
 */

@Api(value = "/api", tags = "公共资源")
@RestController
@RequestMapping("/api/public")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "获取国家列表")
    @GetMapping(value =  "/countries")
    public ResponseEntity<ResultVo<List<String>>> getCountriesPublic(){
        return new ResponseEntity<>(new ResultVo<>(true,null,commonService.getCountries()), HttpStatus.OK);
    }

    @ApiOperation(value = "获取民族列表")
    @GetMapping(value =  "/nations")
    public ResponseEntity<ResultVo<List<String>>> getNationsPublic(){
        return new ResponseEntity<>(new ResultVo<>(true,null,commonService.getNations()), HttpStatus.OK);
    }
}

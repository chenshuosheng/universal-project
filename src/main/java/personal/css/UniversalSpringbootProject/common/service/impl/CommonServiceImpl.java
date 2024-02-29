package personal.css.UniversalSpringbootProject.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.mapper.CommonMapper;
import personal.css.UniversalSpringbootProject.common.service.CommonService;

import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/2/29 17:50
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public List<String> getCountries() {
        return commonMapper.getCountries();
    }

    @Override
    public List<String> getNations() {
        return commonMapper.getNations();
    }
}

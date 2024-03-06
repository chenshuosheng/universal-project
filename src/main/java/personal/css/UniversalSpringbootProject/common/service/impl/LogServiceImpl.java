package personal.css.UniversalSpringbootProject.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.mapper.LogMapper;
import personal.css.UniversalSpringbootProject.common.pojo.ProjectLog;
import personal.css.UniversalSpringbootProject.common.service.LogService;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/3/6 17:12
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public void insert(ProjectLog projectLog) {
        logMapper.insert(projectLog);
    }
}

package personal.css.UniversalSpringbootProject.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.css.UniversalSpringbootProject.common.mapper.Slf4jLoggingMapper;
import personal.css.UniversalSpringbootProject.common.pojo.Slf4jLogging;
import personal.css.UniversalSpringbootProject.common.service.Slf4jLoggingService;

import java.util.concurrent.CompletableFuture;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2024/3/6 15:52
 */
@Service
public class Slf4JSlf4jLoggingServiceImpl implements Slf4jLoggingService {

    @Autowired
    private Slf4jLoggingMapper slf4jLoggingMapper;

    @Override
    public void asyncSave(Slf4jLogging log) {
        CompletableFuture<Void> async = CompletableFuture.supplyAsync(() -> {
            slf4jLoggingMapper.insert(log);
            return null;
        });
    }
}

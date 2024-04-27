package personal.css.UniversalSpringbootProject.common.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import personal.css.UniversalSpringbootProject.common.annotation.Dict;
import personal.css.UniversalSpringbootProject.common.mapper.CommonMapper;
import personal.css.UniversalSpringbootProject.common.utils.RedisUtil;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;
import personal.css.UniversalSpringbootProject.common.vo.ResultVo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description: 数据字典翻译
 * @Author: CSS
 * @Date: 2024/4/27 0:15
 */

@Aspect
@Component
@Slf4j
@Order(2)
public class DictAspect {

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("execution(public * personal.css.UniversalSpringbootProject..controller..*.*(..))")
    public void all() {
    }

    @Around("all()")
    public Object translation(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("\n进入切面DictAspect...");

        //获取请求参数，处理请求
        Object[] args = point.getArgs();
        Object proceed = point.proceed(args);
        try {
            ResponseEntity responseEntity = (ResponseEntity) proceed;
            ResultVo body = (ResultVo) responseEntity.getBody();
            boolean success = body.isSuccess();
            if (success) {
                //判断是不是自定义封装集合，是则需要处理
                if (body.getResult() instanceof ListResult) {
                    ListResult listResult = (ListResult) body.getResult();
                    List items = listResult.getItems();

                    //转化后的数据
                    ListResult<Object> newListResult = new ListResult<>();
                    List<Object> newItems = new ArrayList<>();

                    //集合非空才处理
                    if (null != items && !items.isEmpty()) {
                        //获取元素类型
                        Class<?> clazz = items.get(0).getClass();
                        //获取属性数组（不包含父类属性，也不需要）
                        Field[] fields = clazz.getDeclaredFields();

                        //属性-注解对应map集合
                        Map<String, JSONObject> map = new HashMap<>();
                        for (Field field : fields) {
                            Annotation annotation = field.getAnnotation(Dict.class);
                            if (null == annotation)
                                continue;
                            else {
                                Dict dict = (Dict) annotation;
                                String table = dict.table();
                                String code = dict.code();
                                String displayName = dict.displayName();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("table", table);
                                jsonObject.put("code", code);
                                jsonObject.put("displayName", displayName);
                                map.put(field.getName(), jsonObject);
                            }
                        }
                        for (Object item : items) {
                            String jsonString = JSONObject.toJSONString(item);
                            JSONObject jsonObject = JSONObject.parseObject(jsonString);
                            for (Map.Entry<String, JSONObject> entry : map.entrySet()) {
                                //属性名
                                String key = entry.getKey();
                                //属性值
                                Object value = jsonObject.get(key);
                                //查表参数
                                JSONObject entryValue = entry.getValue();
                                String table = entryValue.getString("table");
                                String code = entryValue.getString("code");
                                String displayName = entryValue.getString("displayName");

                                StringJoiner sj = new StringJoiner("_", "", "");
                                sj.add(table)
                                        .add(code)
                                        .add(displayName)
                                        .add(value.toString());
                                String redis_key = sj.toString();

                                //先试一下一个一个查，后面再优化，统一处理
                                //查表
                                String translation = null;
                                try {
                                    translation = (String) redisUtil.get(redis_key);
                                } catch (Exception e) {
                                    log.error("redis获取值失败：{}", e);
                                }
                                if (null == translation) {
                                    try {
                                        translation = commonMapper.translation(displayName, table, code, value);
                                        if (null != translation) {
                                            try {
                                                redisUtil.set(redis_key, translation, 30);
                                            } catch (Exception e) {
                                                log.error("redis存储值失败：{}", e);
                                            }
                                        }
                                    } catch (Exception e) {
                                        log.error("查询失败：{}", e);
                                    }
                                }
                                jsonObject.put(key + "_text", translation);
                            }
                            Object newItem = JSONObject.parse(jsonObject.toJSONString());
                            newItems.add(newItem);
                        }
                        newListResult.setItems(newItems);
                        newListResult.setTotalCount(newItems.size());
                        body.setResult(newListResult);
                    }
                }
            }
        } catch (Exception e) {
            log.error("转化失败：{}", e);
        }
        Signature signature = point.getSignature();

        long end = System.currentTimeMillis();

        //请求耗时
        double took = (end - start) / 1000.0;
        log.info("退出切面DictAspect耗时：{}秒", took);

        return proceed;
    }
}

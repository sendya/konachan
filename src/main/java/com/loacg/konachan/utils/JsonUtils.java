package com.loacg.konachan.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Project: ktcloud
 * Author: Sendya <18x@loacg.com>
 * Add: 2016/6/15 18:15
 */
public class JsonUtils {

    public static String toJson(Object obj) {
        String json = null;
        ObjectMapper mapper = new ObjectMapper();

        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy() {
            private static final long serialVersionUID = 1L;

            // 反序列化时调用
            @Override
            public String nameForSetterMethod(MapperConfig<?> config,
                                              AnnotatedMethod method, String defaultName) {
                return method.getName().substring(3);
            }

            // 序列化时调用
            @Override
            public String nameForGetterMethod(MapperConfig<?> config,
                                              AnnotatedMethod method, String defaultName) {
                return method.getName().substring(3);
            }
        });
        try {
            json = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Map<?, ?> json2Map(String json) {
        if (StringUtils.isEmpty(json))
            return null;
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> map = null;
        try {

            map = mapper.readValue(json, Map.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<?> json2List(String json) {
        if(StringUtils.isEmpty(json))
            return null;
        ObjectMapper mapper = new ObjectMapper();
        List<?> list = null;

        try {
            list = mapper.readValue(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}

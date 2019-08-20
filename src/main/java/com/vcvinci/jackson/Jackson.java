package com.vcvinci.jackson;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Jackson {
    static ObjectMapper mapper = new ObjectMapper();

    public static <T> T deserializeObject(final String s, final TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(s, typeReference);
    }
    public static void main(String[] args) throws IOException {
        /*final String json = "{\"*\":\"0.8\",\"test\":\"0.2\",\"test1\":\"0.2\",\"test2\":\"0.2\"}";
        ConcurrentHashMap<String, String> stringStringConcurrentHashMap = deserializeObject(json, new TypeReference<ConcurrentHashMap<String, String>>() {
        });
        stringStringConcurrentHashMap.forEach((k, v) -> System.out.println(k +":" + v));*/
        final List list1 =new ArrayList();
        list1.add("1111");
        list1.add("2222");
        list1.add("3333");

        List list2 =new ArrayList();
        list2.add("3333");
        list2.add("4444");
        list2.add("5555");

        list1.retainAll(list2);
        list1.forEach(s -> System.out.println(s));
    }
}


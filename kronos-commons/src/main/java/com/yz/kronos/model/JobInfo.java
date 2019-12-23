package com.yz.kronos.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author shanchong
 * @date 2019-12-20
 **/
@Data
public class JobInfo {

    Long jobId;

    JSONObject resources;

    NamespaceInfo namespace;

    String clazz;

    Integer index;

    Integer shareTotal;

    String synchronizerKey;

    /**
     * 命名空间
     * @author shanchong
     */
    @Data
    public static class NamespaceInfo {

        private String name;

        private String image;

        private String cmd;

    }

}

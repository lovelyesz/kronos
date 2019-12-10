package com.yz.meson.demo.meson;

import com.alibaba.fastjson.JSONObject;
import com.yz.meson.ExecuteConstant;
import com.yz.meson.exception.MesonException;

import com.yz.meson.execute.MesonJobInfoFactory;
import com.yz.meson.model.ExecuteJobInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Component
public class RedisMesonJobInfoFactory implements MesonJobInfoFactory {

    @Autowired
    RedissonClient redissonClient;

    @Override
    public ExecuteJobInfo executeJobInfo() throws MesonException {
        Object cache = redissonClient.getQueue(ExecuteConstant.MESON_EXECUTOR_ENV_NAME).poll();
        if (cache==null){
            return null;
        }
        return JSONObject.parseObject(cache.toString(), ExecuteJobInfo.class);
    }

}

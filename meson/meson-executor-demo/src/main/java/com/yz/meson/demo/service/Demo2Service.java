package com.yz.meson.demo.service;

import com.yz.meson.demo.dao.UserDao;
import com.yz.meson.demo.model.UserInfo;
import com.yz.meson.execute.MesonErrorHandle;
import com.yz.meson.execute.IsolatedJavaJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shanchong
 * @date 2019-10-29
 **/
@Slf4j
@Service
public class Demo2Service implements IsolatedJavaJob, MesonErrorHandle {

    @Autowired
    UserDao userDao;
    /**
     * 执行业务
     *
     * @param partitions
     * @param totalPartitionNums
     * @param taskId
     */
    @Override
    public void execute(Integer partitions, Integer totalPartitionNums, String taskId) throws Exception {
        log.info(partitions+"/"+totalPartitionNums+"Demo2Service 执行了");
        for (int i = 1; i < 1000; i++) {
            final UserInfo userInfo = userDao.findById((long) i).get();
            log.info(partitions+"/"+totalPartitionNums+"user select {}",userInfo);
        }
        log.info(partitions+"/"+totalPartitionNums+"Demo2Service 结束了");
    }

    /**
     * 任务执行失败
     *
     * @param partitions
     * @param totalPartitionNums
     * @param taskId
     * @param e
     */
    @Override
    public void errorHandle(Integer partitions, Integer totalPartitionNums, String taskId, Exception e) {

    }


}

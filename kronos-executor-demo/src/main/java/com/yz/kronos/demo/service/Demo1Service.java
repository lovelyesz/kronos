package com.yz.kronos.demo.service;

import com.yz.kronos.demo.dao.UserDao;
import com.yz.kronos.demo.model.UserInfo;
import com.yz.kronos.execute.JobErrorHandle;
import com.yz.kronos.execute.IsolatedJavaJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author shanchong
 * @date 2019-10-29
 **/
@Slf4j
@Service
public class Demo1Service implements IsolatedJavaJob, JobErrorHandle {


    @Autowired
    UserDao userDao;
    /**
     * 执行业务
     *
     * @param partitions
     * @param totalPartitionNums
     * @param jobId
     */
    @Override
    public void execute(Integer partitions, Integer totalPartitionNums, String jobId) throws Exception {
        log.info(partitions+"/"+totalPartitionNums+"Demo1Service start");
        for (int i = 0; i < 300; i++) {
            final UserInfo userInfo = new UserInfo();
            userInfo.setName("用户"+new Random().nextLong());
            userInfo.setAge(new Random(100).nextInt());
            userInfo.setAddress("地址"+new Random().nextLong());
            userInfo.setEmail("邮箱"+new Random().nextLong());
            userInfo.setPhone(new Random(99999999999L).nextLong()+"");
            userDao.save(userInfo);
            log.info(partitions+"/"+totalPartitionNums+"user insert {}",userInfo);
        }
        log.info(partitions+"/"+totalPartitionNums+"Demo1Service end");
    }

    /**
     * 任务执行失败
     *
     * @param partitions
     * @param totalPartitionNums
     * @param jobId
     * @param e
     */
    @Override
    public void errorHandle(Integer partitions, Integer totalPartitionNums, String jobId, Exception e) {

    }


}

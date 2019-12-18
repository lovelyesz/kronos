package com.yz.kronos.schedule.execption;

import com.yz.kronos.exception.JobException;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
public class ScheduleException extends JobException {

    public ScheduleException(String message){
        super(message);
    }

}

package com.yz.meson.execute;


public interface JavaJob {
	/**
	 * 执行业务
	 * @param
	 */
	void execute(Integer partitions, Integer totalPartitionNums, String execId) throws Exception ;

}

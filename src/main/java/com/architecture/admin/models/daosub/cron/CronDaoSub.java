package com.architecture.admin.models.daosub.cron;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CronDaoSub {

    /*****************************************************
     * Select
     ****************************************************/
    /**
     * cron 상태값 가져오기
     *
     * @param idx cron.idx
     * @return state [0: 미사용 , 1: 사용]
     */
    Integer getState(Integer idx);

}

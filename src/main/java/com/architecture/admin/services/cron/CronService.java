package com.architecture.admin.services.cron;

import com.architecture.admin.models.daosub.cron.CronDaoSub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CronService {

    private final CronDaoSub cronDaoSub;

    /*****************************************************
     *  SubFunction - Select
     ****************************************************/
    /**
     * 상태값 조회하기
     *
     * @param idx cron.idx
     * @return state [0: 미사용 , 1: 사용]
     */
    public Integer getState(Integer idx) {
        return cronDaoSub.getState(idx);
    }
}

package com.architecture.admin.models.daosub.notification;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NotificationDaoSub {

    /**
     * 이미 보낸 알림 [소멸 예정 코인 ] idx 리스트 조회
     *
     * @return
     */
    List<Long> getCoinUsedIdxList();

    /**
     * 이미 보낸 알림 [소멸 예정 마일리지] idx 리스트 조회
     *
     * @return
     */
    List<Long> getMileageUsedIdxList();

    /**
     * 이미 보낸 알림 [신규 업데이트 회차] idx 리스트 조회
     *
     * @return
     */
    List<Long> getEpisodeIdxAlarmList();
}

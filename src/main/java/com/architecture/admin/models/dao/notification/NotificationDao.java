package com.architecture.admin.models.dao.notification;

import com.architecture.admin.models.dto.notification.MemberNotificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NotificationDao {

    /**
     * 소멸 예정 코인 알림 등록
     * @param expireCoinInfoList
     */
    void insertCoinUsedAlarm(List<MemberNotificationDto> expireCoinInfoList);

    /**
     * 소멸 예정 마일리지 알림 등록
     * @param expireMileageInfoList
     */
    void insertMileageUsedAlarm(List<MemberNotificationDto> expireMileageInfoList);

    /**
     * 신규 업데이트 회차 알림 등록
     * @param newEpisodeInfoList
     */
    void insertEpisodeAlarm(List<MemberNotificationDto> newEpisodeInfoList);
}

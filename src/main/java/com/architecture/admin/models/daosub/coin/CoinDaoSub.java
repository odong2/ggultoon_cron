package com.architecture.admin.models.daosub.coin;

import com.architecture.admin.models.dto.notification.MemberNotificationDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CoinDaoSub {

    /**
     * 만료된 코인 idx 리스트 select
     *
     * @param nowDate
     * @return
     */
    List<Long> getExpireCoinIdxList(String nowDate);

    /**
     * 만료된 마일리지 idx 리스트 select
     *
     * @param nowDate
     * @return
     */
    List<Long> getExpireMileageIdxList(String nowDate);

    
    /**********************************************
     * 알림에서 조회하는 쿼리
     **********************************************/
    
    /**
     * 소멸 예정 코인 정보 조회 [NotificationService 에서 사용]
     *
     * @param nowDate
     * @return
     */
    List<MemberNotificationDto> getExpireCoinInfoList(String nowDate);

    /**
     * 소멸 예정 마일리지 정보 조회 [NotificationService 에서 사용]
     *
     * @param inOneWeekDate
     * @return
     */
    List<MemberNotificationDto> getExpireMileageInfoList(String inOneWeekDate);
}

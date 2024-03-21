package com.architecture.admin.services.coin;

import com.architecture.admin.libraries.utils.CronConstants;
import com.architecture.admin.models.dao.notification.NotificationDao;
import com.architecture.admin.models.daosub.coin.CoinDaoSub;
import com.architecture.admin.models.daosub.notification.NotificationDaoSub;
import com.architecture.admin.models.dto.notification.MemberNotificationDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CoinAlarmService extends BaseService {

    private final CoinDaoSub coinDaoSub;
    private final NotificationDao notificationDao;
    private final NotificationDaoSub notificationDaoSub;

    /*****************************************************
     *  ALARM CRON
     ****************************************************/

    /**
     * 소멸 예정된 코인 & 마일리지 알림
     */
    @Scheduled(cron = "0 0/10 * * * *") // 10분마다 실행
    public void expectedToExpireCoinAlarm() {

        // 활성화된 크론인지 조회
        if (super.checkCronState(CronConstants.CRON_EXPECTED_EXPIRE_COIN)) {

            String nowDate = dateLibrary.getDatetime();

            // 1. 이미 보낸 알림 조회 [소멸 예정 코인]
            List<Long> coinIdxList = notificationDaoSub.getCoinUsedIdxList();

            // 일주일 뒤 정각 시간 구하기
            String inOneWeekDate = dateLibrary.getDateInOneWeek();

            // 2. member_coin_used 테이블 조회 [일주일 뒤 소멸될 예정인 코인 조회]
            List<MemberNotificationDto> expireCoinInfoList = coinDaoSub.getExpireCoinInfoList(inOneWeekDate);

            // 이미 보낸 알림 중복 제거
            if (coinIdxList != null && !coinIdxList.isEmpty()) {
                for (Long coinUsedIdx : coinIdxList) {
                    // 이미 보낸 알림 목록 제거
                    expireCoinInfoList.removeIf(coinInfo -> coinInfo.getTypeIdx().equals(coinUsedIdx));
                }
            }

            // 3. member_notification 등록
            if (expireCoinInfoList != null && !expireCoinInfoList.isEmpty()) {

                for (MemberNotificationDto expireInfo : expireCoinInfoList) {
                    // 등록일 set
                    expireInfo.setRegdate(nowDate);
                }
                /** member_notification 테이블 등록 **/
                notificationDao.insertCoinUsedAlarm(expireCoinInfoList);
            }

            /**************************** 마일리지 *******************************/

            // 1. 이미 보낸 알림 조회 [소멸 예정 코인]
            List<Long> mileageIdxList = notificationDaoSub.getMileageUsedIdxList();

            // 2. member_mileage_used 테이블 조회 [일주일 뒤 소멸될 예정인 코인 조회]
            List<MemberNotificationDto> expireMileageInfoList = coinDaoSub.getExpireMileageInfoList(inOneWeekDate);

            // 이미 보낸 알림 중복 제거
            if (mileageIdxList != null && !mileageIdxList.isEmpty()) {
                for (Long mileageUsedIdx : mileageIdxList) {
                    // 이미 보낸 알림 목록 제거
                    expireMileageInfoList.removeIf(mileage -> mileage.getTypeIdx().equals(mileageUsedIdx));
                }
            }

            // 3. member_notification 등록
            if (expireMileageInfoList != null && !expireMileageInfoList.isEmpty()) {

                for (MemberNotificationDto expireInfo : expireMileageInfoList) {
                    // 등록일 set
                    expireInfo.setRegdate(nowDate);
                }

                /** member_notification 테이블 등록 **/
                notificationDao.insertMileageUsedAlarm(expireMileageInfoList);
            }
        }
    }
}

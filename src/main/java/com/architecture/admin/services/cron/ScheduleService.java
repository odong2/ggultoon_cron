package com.architecture.admin.services.cron;

import com.architecture.admin.libraries.utils.CronConstants;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class ScheduleService extends BaseService {

    /*****************************************************
     *  Scheduler
     *  @Scheduled(cron = "* * * * * *")
     *  (초, 분, 시간, 일, 월, 요일)
     *  초 : 0-59
     *  분 : 0-59
     *  시간 : 0-23
     *  일 : 1-31
     *  월 : 1-12
     *  요일 : 0-7 (0,7 일요일, 1 월요일, 6 토요일)
     *
     ****************************************************/

    /**
     * @Scheduled(cron = "0 0/10 * * * *") // 10분마다 실행
     * 소멸 예정된 코인 & 마일리지 알림 - expectedToExpireCoinAlarm()
     */

    /**
     * @Scheduled(cron = "0 0 0/1 * * *") // 1시간마다 실행
     * 코인 & 보너스 코인 & 마일리지 만료 - expireCoin()
     * 랭킹 일간(시간) - rankingDaily()
     */

    /**
     * @Scheduled(cron = "0 0 22 * * *") // 매일 오후 10시에 실행
     * 특정 작품의 신규 회차 업로드 시 해당 작품 찜한 회원에게 알림 전송 - updateNewEpisodeAndSendAlarm()
     */

    /**
     * @Scheduled(cron = "0 0 0 1 1 *") // 매년 1월 1일 에 실행
     * 미성년자 --> 성인으로 변경되는 회원 상태값 업데이트 - updateMemberAdultState()
     */


    /**
     * @Scheduled(cron = "0 0 3 * * *") // 매일 3시
     * 랭킹 주간 - rankingWeekly()
     * 랭킹 월간 - rankingMonthly()
     * 결제 통계 - cpStat()
     */

    /**
     * @Scheduled(cron = "0 0 0 1 * *") // 매월 1일
     * 회원 등급 조정 - memberGrade()
     */



    /**
     * 스케줄러
     */
    //@Scheduled(cron = "* * * * * *") // 1초 단위
    public void schedule() {

        // cron 활성화 상태 체크
        if (super.checkCronState(CronConstants.CRON_TEST)) {

            // 일주일 전 날짜 구하기 (UTC)
            SimpleDateFormat formatDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE , -7);
            String beforeWeek = formatDatetime.format(calendar.getTime());
            String date = dateLibrary.localTimeToUtc(beforeWeek);
            System.out.println(date);
        }
    }
}

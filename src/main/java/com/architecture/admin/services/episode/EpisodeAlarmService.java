package com.architecture.admin.services.episode;

import com.architecture.admin.libraries.utils.CronConstants;
import com.architecture.admin.models.dao.content.ContentDao;
import com.architecture.admin.models.dao.notification.NotificationDao;
import com.architecture.admin.models.daosub.episode.EpisodeDaoSub;
import com.architecture.admin.models.daosub.member.MemberDaoSub;
import com.architecture.admin.models.daosub.notification.NotificationDaoSub;
import com.architecture.admin.models.dto.content.ContentDto;
import com.architecture.admin.models.dto.episode.EpisodeDto;
import com.architecture.admin.models.dto.notification.MemberNotificationDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeAlarmService extends BaseService {

    private final MemberDaoSub memberDaoSub;
    private final EpisodeDaoSub episodeDaoSub;
    private final ContentDao contentDao;
    private final NotificationDao notificationDao;
    private final NotificationDaoSub notificationDaoSub;

    /*****************************************************
     *  DB + ALARM CRON
     ****************************************************/

    /**
     * 특정 작품의 신규 회차 업로드 시 해당 작품 찜한 회원에게 알림 전송
     */
    @Scheduled(cron = "0 0 22 * * *") // 매일 오후 10시에 실행
    public void updateNewEpisodeAndSendAlarm() {

        // 활성화된 크론인지 조회
        if (super.checkCronState(CronConstants.CRON_NEW_EPISODE_UPDATED)) {

            // 현재 날짜 및 시간
            String nowDate = dateLibrary.getDatetime();

            // 신규 업데이트 회차 리스트 조회(작품 구분X)(발행일로부터 1시간 전 ~ 발행일)
            List<EpisodeDto> newEpisodeList =  episodeDaoSub.getNewEpisodeList(nowDate);

            // 신규 업데이트 회차 리스트가 있는 경우
            if (newEpisodeList != null && !newEpisodeList.isEmpty()) {

                /** 1. contents 테이블 update : last_episode_idx, last_episode_title 변경 **/
                // 작품 idx + 해당 작품에서 신규 업데이트된 회차 중 가장 마지막 회차 idx 리스트 set
                List<ContentDto> lastEpisodeInfoList = new ArrayList<>();
                // 작품 구분용 idx
                Integer contentIdx = null;

                for (EpisodeDto dto : newEpisodeList) {

                    // 조회하고 있는 작품 idx와 현재 작품 idx가 같을 경우 제외
                    if (dto.getContentsIdx().equals(contentIdx)) {
                        continue;
                    }

                    // 작품 idx set
                    contentIdx = dto.getContentsIdx();

                    // dto set
                    ContentDto content = ContentDto.builder()
                                        .idx(contentIdx)                    // 작품 idx
                                        .lastEpisodeIdx(dto.getIdx())       // 작품의 가장 마지막 회차 idx
                                        .lastEpisodeTitle(dto.getTitle())   // 작품의 가장 마지막 회차 제목
                                        .build();

                    // list add
                    lastEpisodeInfoList.add(content);
                }

                // 작품의 마지막 회차 정보 업데이트
                int result = contentDao.updateLastEpisodeInfo(lastEpisodeInfoList);

                /** 2. member_notification 테이블 insert : 회원 알림 전송 **/

                // 업데이트된 신규 회차가 있을 경우에만 알림 전송
                if (result > 0) {

                    // 이미 알림을 전송한 회차 idx 리스트 조회
                    List<Long> episodeAlarmIdxList = notificationDaoSub.getEpisodeIdxAlarmList();

                    // 이미 알림을 전송한 회차는 알림 보낼 회차 idx 리스트에서 제거
                    if (episodeAlarmIdxList != null && !episodeAlarmIdxList.isEmpty()) {
                        for (Long alarmIdx : episodeAlarmIdxList) {
                            lastEpisodeInfoList.removeIf(item -> item.getLastEpisodeIdx().equals(alarmIdx));
                        }
                    }

                    // 중복 제거 후에도 전송할 알림이 남은 경우 -> 알림 전송
                    if (lastEpisodeInfoList != null && !lastEpisodeInfoList.isEmpty()) {

                        // 해당 작품을 찜한 회원 idx 리스트 조회
                        List<Long> memberIdxList = memberDaoSub.getLikeMemberIdxList(lastEpisodeInfoList);

                        // 알림 전송용 리스트 set -> 회원 idx + 신규 업데이트된 회차 중 마지막 회차 idx
                        List<MemberNotificationDto> newEpisodeInfoList = new ArrayList<>();

                        for (ContentDto content : lastEpisodeInfoList) {
                            for (Long memberIdx : memberIdxList) {

                                // dto set
                                MemberNotificationDto dto = MemberNotificationDto.builder()
                                        .memberIdx(memberIdx)
                                        .typeIdx(content.getLastEpisodeIdx())
                                        .regdate(nowDate)
                                        .build();

                                // list add
                                newEpisodeInfoList.add(dto);
                            }
                        }
                        // 회원 알림 전송
                        notificationDao.insertEpisodeAlarm(newEpisodeInfoList);
                    }
                }
            }
        }
    }
}

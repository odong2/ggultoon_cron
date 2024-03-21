package com.architecture.admin.services.rank;

import com.architecture.admin.libraries.utils.CronConstants;
import com.architecture.admin.models.dao.rank.RankDao;
import com.architecture.admin.models.daosub.rank.RankDaoSub;
import com.architecture.admin.models.dto.rank.RankDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class RankService extends BaseService {

    private final RankDaoSub rankDaoSub;

    private final RankDao rankDao;

    /**
     * 랭킹 - 일간(시간)
     * 매시간
     */
    @Scheduled(cron = "0 0 * * * *")
    public void rankingDaily() {
        try {
           // 일간(시간) - 시간 설정
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setLenient(false);
            Calendar daily = Calendar.getInstance();
            Map<String, Object> dayMap = new HashMap<>();

            // 종료일
            String nowDtUtc = dateLibrary.localTimeToUtc(sdf.format(daily.getTime()));
            String endDt = nowDtUtc.substring(0, nowDtUtc.indexOf(":")).concat(":00:00");
            dayMap.put("endDate", endDt);

            // 이전 날짜 비교(1시간)
            daily.setTime(sdf.parse(endDt));
            daily.add(daily.HOUR, -1);
            dayMap.put("compareDate", sdf.format(daily.getTime()));

            // 시작일
            daily.add(daily.HOUR, -23);
            dayMap.put("startDate", sdf.format(daily.getTime()));

            // 일간(시간) - 랭킹 등록
            rankingInsert(dayMap, "ranking_daily");

            // 일간(시간) - 백업 지난 랭킹(-4시간) 조회
            daily.setTime(sdf.parse(endDt));
            daily.add(daily.HOUR, -4);
            dayMap.put("backupDate", sdf.format(daily.getTime()));

            // 일간(시간) - 백업 테이블 생성(매월 생성)
            String backupTableName = ranking_backup(daily);

            List<RankDto> backupList = rankDaoSub.getRankingBackup(dayMap);
            if (!backupList.isEmpty()) {
                dayMap.put("backupList", backupList);

                dayMap.put("tableName", backupTableName);

                // 백업 지난 랭킹(3시간 이전) 복사
                rankDao.insertRankingBackup(dayMap);

                // 지난 랭킹(-3시간) 삭제
                rankDao.deleteRankingBackup(dayMap);
            }

            // 크론 확인용 알림 - prd 서버 만 실행
            String sSever = System.getProperty("spring.profiles.active");
            sSever = sSever == null ? "local" : sSever;
            if(sSever.equals("prd")){
                LocalDateTime now = LocalDateTime.now();
                String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                telegramLibrary.sendMessage("[CRON] - " + formatedNow, "ALARM");
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }


    /**
     * 랭킹 - 주간
     * 매일 3시
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void rankingWeekly() {
        // 주간 - 시간 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        Calendar weekly = Calendar.getInstance();
        Map<String, Object> weekMap = new HashMap<>();

        // 종료일
        String nowDtUtc = sdf.format(weekly.getTime());
        String endDt = nowDtUtc.substring(0, nowDtUtc.indexOf(" ")).concat(" 00:00:00");
        weekMap.put("endDate", dateLibrary.localTimeToUtc(endDt));

        // 시작일
        weekly.add(Calendar.DATE, -7);
        String startDtUtc = sdf.format(weekly.getTime());
        String startDt = startDtUtc.substring(0, nowDtUtc.indexOf(" ")).concat(" 00:00:00");
        weekMap.put("startDate", dateLibrary.localTimeToUtc(startDt));

        // 이전 날짜 비교
        weekly.add(Calendar.DATE, -1);
        weekMap.put("compareDate", dateLibrary.localTimeToUtc(sdf.format(weekly.getTime())));

        rankingInsert(weekMap, "ranking_weekly");
    }


    /**
     * 랭킹 - 월간
     * 매일 3시
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void rankingMonthly() {
        // 월간 - 랭킹 등록
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        Calendar monthly = Calendar.getInstance();
        Map<String, Object> monthMap = new HashMap<>();

        // 종료일
        String nowDtUtc = sdf.format(monthly.getTime());
        String endDt = nowDtUtc.substring(0, nowDtUtc.indexOf(" ")).concat(" 00:00:00");
        monthMap.put("endDate", dateLibrary.localTimeToUtc(endDt));

        // 시작일
        monthly.add(Calendar.DATE, -30);
        String startDtUtc = sdf.format(monthly.getTime());
        String startDt = startDtUtc.substring(0, nowDtUtc.indexOf(" ")).concat(" 00:00:00");
        monthMap.put("startDate", dateLibrary.localTimeToUtc(startDt));

        // 이전 날짜 비교
        monthly.add(Calendar.DATE, -1);
        monthMap.put("compareDate", dateLibrary.localTimeToUtc(sdf.format(monthly.getTime())));

        rankingInsert(monthMap, "ranking_monthly");
    }


    /**
     * 랭킹 등록
     * @param map
     */
    private void rankingInsert(Map<String, Object> map, String insertTable) {
        for (Integer categoryIdx = 1; categoryIdx < 4; categoryIdx++) {
            // categoryIdx 1:웹툰, 2:만화, 3:소설
            map.put("categoryIdx", categoryIdx);

            // 등록 테이블(일간: ranking_daily, 주간: ranking_weekly, 월간: ranking_monthly)
            map.put("insertTable", insertTable);

            // 랭킹 등록일, 이전 비교 날짜
            map.put("nowDate", map.get("endDate"));
            if (insertTable.equals("ranking_weekly") || insertTable.equals("ranking_monthly")) {
                // 랭킹 등록일
                String endDt = map.get("endDate").toString();
                map.put("nowDate", endDt.substring(0, endDt.indexOf(" ")));

                // 이전 날짜 비교
                map.put("compareDate", map.get("compareDate").toString().substring(0, endDt.indexOf(" ")));
            }

            // type :  (genreIdx * 100) + (categoryIdx * 10) + adultPavilion
            Integer type = 0;
            for(Integer adultPavilion = 0; adultPavilion < 3; adultPavilion++) {
                // adultPavilion 0:일반, 1:성인, 2:전체
                map.put("adultPavilion", adultPavilion);

                type = (categoryIdx * 10) + adultPavilion;
                map.put("type", type);
                map.put("genreIdx", null);
                List<RankDto> rankList = rankDaoSub.getRankingTop120(map);
                // 카테고리/성인유무
                if (rankList.size() > 0) {
                    map.put("list", rankList);
                    // 랭킹 등록
                    rankDao.insertRankingTop120(map);
                    // 이전 랭킹 update
                    rankDao.updateRankingPrevTop120(map);
                    // state 변경
                    rankDao.updateRankingStateTop120(map);
                }

                // 장르 조회
                List<Integer> genreList = rankDaoSub.getGenreList();
                for (Integer genreIdx : genreList) {
                    type = (genreIdx * 100) + (categoryIdx * 10) + adultPavilion;
                    map.put("genreIdx", genreIdx);
                    map.put("type", type);

                    // 카테고리/성인유무/장르
                    List<RankDto> rankGenreList = rankDaoSub.getRankingTop120(map);

                    if (rankGenreList.size() > 0) {
                        map.put("list", rankGenreList);
                        // 랭킹 등록
                        rankDao.insertRankingTop120(map);
                        // 이전 랭킹 update
                        rankDao.updateRankingPrevTop120(map);
                        // state 변경
                        rankDao.updateRankingStateTop120(map);
                    }
                }
            }
        }
    }


    /**
     * 백업 테이블 생성(매월 1일)
     */
    private String ranking_backup(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        sdf.setLenient(false);

        // ranking_202308_backup
        String tableName =  "ranking_".concat(sdf.format(calendar.getTime())).concat("_backup");

        // 테이블 유무
        Integer checkTable = rankDaoSub.checkTable(tableName);
        if (checkTable == 0) {
            // 테이블 생성
            rankDao.addTable(tableName);
        }

        return tableName;
    }







    /**
     * 랭킹 TODO 삭제 예정
     * 매일 3시
     */
    public void ranking() {

        try {
            // cron 활성화 상태 체크
            if (super.checkCronState(CronConstants.CRON_RANK)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                sdf.setLenient(false);

                /**
                 * 일간 랭킹(구매수) 등록
                 */
                Calendar daily = Calendar.getInstance();
                Map<String, Object> dayMap = new HashMap<>();
                // -1 일
                dayMap.put("endDate", sdf.format(daily.getTime()));
                // -2 일
                daily.add(Calendar.DATE, -1);
                dayMap.put("startDate", sdf.format(daily.getTime()));

                // 일간 일반 - 작품 구매 수
                dayMap.put("adultPavilion", 0);
                dayMap.put("tableName", "normal");
                List<RankDto> rankingDayNormal = rankDaoSub.getRanking(dayMap);
                if (!rankingDayNormal.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingDayNormal) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 일간 일반 랭킹 등록
                    rankDao.insertRankingNormal(rankingDayNormal);
                    // 전일 구매수 update
                    rankDao.updateRankingPrev(dayMap);
                    // state 변경
                    rankDao.updateRankingState(dayMap);
                }

                // 일간 성인 - 작품 구매 수
                dayMap.put("adultPavilion", 1);
                dayMap.put("tableName", "adult");
                List<RankDto> rankingDayAdult = rankDaoSub.getRanking(dayMap);
                if (!rankingDayAdult.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingDayAdult) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 일간 일반 랭킹 등록
                    rankDao.insertRankingAdult(rankingDayAdult);
                    // 전일 구매수 update
                    rankDao.updateRankingPrev(dayMap);
                    // state 변경
                    rankDao.updateRankingState(dayMap);
                }

                // 일간 전체 - 작품 구매 수
                dayMap.put("adultPavilion", null);
                dayMap.put("tableName", "all");
                List<RankDto> rankingDayAll = rankDaoSub.getRanking(dayMap);
                if (!rankingDayAll.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingDayAll) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 일간 일반 랭킹 등록
                    rankDao.insertRankingAll(rankingDayAll);
                    // 전일 구매수 update
                    rankDao.updateRankingPrev(dayMap);
                    // state 변경
                    rankDao.updateRankingState(dayMap);
                }


                /**
                 * 주간 랭킹(구매수) 등록
                 */
                Calendar weekly = Calendar.getInstance();
                Map<String, Object> weekMap = new HashMap<>();
                // -1 일
                weekMap.put("endDate", sdf.format(weekly.getTime()));
                // -8 일
                weekly.add(Calendar.DATE, -7);
                weekMap.put("startDate", sdf.format(weekly.getTime()));

                // 주간 일반 - 작품 구매 수
                weekMap.put("adultPavilion", 0);
                weekMap.put("tableName", "normal");
                List<RankDto> rankingWeekNormal = rankDaoSub.getRanking(weekMap);
                if (!rankingWeekNormal.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingWeekNormal) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 주간 일반 랭킹 등록
                    rankDao.insertRankingWeekNormal(rankingWeekNormal);
                    // 전주 구매수 update
                    rankDao.updateRankingWeekPrev(weekMap);
                    // state 변경
                    rankDao.updateRankingWeekState(weekMap);
                }

                // 주간 성인 - 작품 구매 수
                weekMap.put("adultPavilion", 1);
                weekMap.put("tableName", "adult");
                List<RankDto> rankingWeekAdult = rankDaoSub.getRanking(weekMap);
                if (!rankingWeekAdult.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingWeekAdult) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 주간 성인 랭킹 등록
                    rankDao.insertRankingWeekAdult(rankingWeekAdult);
                    // 전주 구매수 update
                    rankDao.updateRankingWeekPrev(weekMap);
                    // state 변경
                    rankDao.updateRankingWeekState(weekMap);
                }

                // 주간 전체 - 작품 구매 수
                weekMap.put("adultPavilion", null);
                weekMap.put("tableName", "all");
                List<RankDto> rankingWeekAll = rankDaoSub.getRanking(weekMap);
                if (!rankingWeekAll.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingWeekAll) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 주간 전체 랭킹 등록
                    rankDao.insertRankingWeekAll(rankingWeekAll);
                    // 전주 구매수 update
                    rankDao.updateRankingWeekPrev(weekMap);
                    // state 변경
                    rankDao.updateRankingWeekState(weekMap);
                }


                /**
                 * 월간 랭킹(구매수) 등록
                 */
                Calendar monthly = Calendar.getInstance();
                Map<String, Object> monthMap = new HashMap<>();
                // -1 일
                monthMap.put("endDate", sdf.format(monthly.getTime()));
                // -30 일
                monthly.add(Calendar.DATE, -30);
                monthMap.put("startDate", sdf.format(monthly.getTime()));

                // 월간 일반 - 작품 구매 수
                monthMap.put("adultPavilion", 0);
                monthMap.put("tableName", "normal");
                List<RankDto> rankingMonthNormal = rankDaoSub.getRanking(monthMap);

                if (!rankingMonthNormal.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingMonthNormal) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 월간 일반 랭킹 등록
                    rankDao.insertRankingMonthNormal(rankingMonthNormal);
                    // 전월 구매수 update
                    rankDao.updateRankingMonthPrev(monthMap);
                    // state 변경
                    rankDao.updateRankingMonthState(monthMap);
                }

                // 월간 성인 - 작품 구매 수
                monthMap.put("adultPavilion", 1);
                monthMap.put("tableName", "adult");
                List<RankDto> rankingMonthAdult = rankDaoSub.getRanking(monthMap);
                if (!rankingMonthAdult.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingMonthAdult) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 주간 성인 랭킹 등록
                    rankDao.insertRankingMonthAdult(rankingMonthAdult);
                    // 전주 구매수 update
                    rankDao.updateRankingMonthPrev(monthMap);
                    // state 변경
                    rankDao.updateRankingMonthState(monthMap);
                }

                // 월간 전체 - 작품 구매 수
                monthMap.put("adultPavilion", null);
                monthMap.put("tableName", "all");
                List<RankDto> rankingMonthAll = rankDaoSub.getRanking(monthMap);
                if (!rankingMonthAll.isEmpty()) {
                    Integer webtoonRanking = 1;
                    Integer comicRanking = 1;
                    Integer novelRanking = 1;
                    for (RankDto dto : rankingMonthAll) {
                        if (dto.getCategoryIdx() == 1) {
                            dto.setRanking(webtoonRanking);
                            webtoonRanking++;
                        } else if (dto.getCategoryIdx() == 2) {
                            dto.setRanking(comicRanking);
                            comicRanking++;
                        } else if (dto.getCategoryIdx() == 3) {
                            dto.setRanking(novelRanking);
                            novelRanking++;
                        }
                    }

                    // 주간 전체 랭킹 등록
                    rankDao.insertRankingMonthAll(rankingMonthAll);
                    // 전주 구매수 update
                    rankDao.updateRankingMonthPrev(monthMap);
                    // state 변경
                    rankDao.updateRankingMonthState(monthMap);
                }
            }
        } catch(Exception e) {
            pushAlarm("rank : " + e, "YKH");
            e.printStackTrace();
        }
    }
}

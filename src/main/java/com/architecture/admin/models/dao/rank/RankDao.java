package com.architecture.admin.models.dao.rank;

import com.architecture.admin.models.dto.rank.RankDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface RankDao {

    /**
     * 랭킹 - 랭킹 등록
     * @param map
     */
    void insertRankingTop120(Map<String, Object> map);

    /**
     * 랭킹 - 이전 랭킹 update
     * @param map
     */
    void updateRankingPrevTop120(Map<String, Object> map);

    /**
     * 랭킹- state 변경
     * @param map
     */
    void updateRankingStateTop120(Map<String, Object> map);

    /**
     * 백업 테이블 생성
     * @param tableName
     * @return
     */
    Integer addTable(String tableName);

    /**
     * 일간(시간) - 백업 등록
     * @param dayMap
     */
    void insertRankingBackup(Map<String, Object> dayMap);

    /**
     * 일간(시간) - 백업 삭제
     * @param dayMap
     */
    void deleteRankingBackup(Map<String, Object> dayMap);


    /* TODO 삭제 예정 */

    /**
     * 일간 - 일반 랭킹 등록
     *
     * @param rankingDayNormal
     */
    void insertRankingNormal(List<RankDto> rankingDayNormal);

    /**
     * 일간 - 성인 랭킹 등록
     *
     * @param rankingDayAdult
     */
    void insertRankingAdult(List<RankDto> rankingDayAdult);

    /**
     * 일간 - 전체 랭킹 등록
     *
     * @param rankingDayAll
     */
    void insertRankingAll(List<RankDto> rankingDayAll);


    /**
     * 주간 - 일반 랭킹 등록
     *
     * @param rankingWeekNormal
     */
    void insertRankingWeekNormal(List<RankDto> rankingWeekNormal);

    /**
     * 주간 - 성인 랭킹 등록
     *
     * @param rankingWeekAdult
     */
    void insertRankingWeekAdult(List<RankDto> rankingWeekAdult);

    /**
     * 주간 - 전체 랭킹 등록
     *
     * @param rankingWeekAll
     */
    void insertRankingWeekAll(List<RankDto> rankingWeekAll);

    /**
     * 월간 - 일반 랭킹 등록
     *
     * @param rankingMonthNormal
     */
    void insertRankingMonthNormal(List<RankDto> rankingMonthNormal);

    /**
     * 월간 - 성인 랭킹 등록
     *
     * @param rankingMonthAdult
     */
    void insertRankingMonthAdult(List<RankDto> rankingMonthAdult);

    /**
     * 월간 - 전체 랭킹 등록
     *
     * @param rankingMonthAll
     */
    void insertRankingMonthAll(List<RankDto> rankingMonthAll);


    /**
     * 일간 - 전일 랭킹 update
     *
     * @param dayMap
     */
    void updateRankingPrev(Map<String, Object> dayMap);

    /**
     * 일간 - state 변경
     *
     * @param dayMap
     */
    void updateRankingState(Map<String, Object> dayMap);

    /**
     * 주간 - 전주 랭킹 update
     *
     * @param weekMap
     */
    void updateRankingWeekPrev(Map<String, Object> weekMap);

    /**
     * 주간 - state 변경
     *
     * @param weekMap
     */
    void updateRankingWeekState(Map<String, Object> weekMap);

    /**
     * 월간 - 전월 랭킹 update
     *
     * @param monthMap
     */
    void updateRankingMonthPrev(Map<String, Object> monthMap);

    /**
     * 월간 - state 변경
     *
     * @param monthMap
     */
    void updateRankingMonthState(Map<String, Object> monthMap);


}
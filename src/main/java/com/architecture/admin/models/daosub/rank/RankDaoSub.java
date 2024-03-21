package com.architecture.admin.models.daosub.rank;

import com.architecture.admin.models.dto.rank.RankDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface RankDaoSub {


    /**
     * 작품 구매 수
     * @param dayMap
     * @return
     */
    List<RankDto> getRankingTop120(Map<String, Object> dayMap);

    /**
     * 장르 목록
     * @return
     */
    List<Integer> getGenreList();

    /**
     * 백업 테이블 존재 여부
     * @param tableName
     * @return
     */
    Integer checkTable(String tableName);

    /**
     * 백업 랭킹 조회
     * @param dayMap
     * @return
     */
    List<RankDto> getRankingBackup(Map<String, Object> dayMap);


    /* TODO 삭제 예정 */
    /**
     * 작품 구매 수
     * @param map
     * @return
     */
    List<RankDto> getRanking(Map<String, Object> map);

}

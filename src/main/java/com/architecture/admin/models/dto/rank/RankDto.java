package com.architecture.admin.models.dto.rank;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankDto {

    private Integer idx;
    // 날짜
    private String date;
    // 순위
    private Integer ranking;
    // 이전 순위
    private Integer rankingPrev;
    // 카테고리 idx
    private Integer categoryIdx;
    // 장르 idx
    private Integer genreIdx;
    // 작품
    private Integer contentsIdx;
    // 성인유무(0:일반, 1:성인, 2:전체)
    private Integer adultPavilion;
    // 판매 회차 수
    private Integer episodeCount;
    // 종류 구분(장르*100, 카테고리*10, adultPavilion*1)
    private Integer type;
    // 랭킹 테이블 명
    private String tableName;
}

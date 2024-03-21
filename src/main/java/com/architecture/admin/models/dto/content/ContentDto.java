package com.architecture.admin.models.dto.content;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ContentDto {

    /**
     * contents
     */
    private Integer idx;                // contents.idx
    private Integer categoryIdx;        // category.idx
    private Integer genreIdx;           // genre.idx
    private String title;               // 작품 제목
    private String description;         // 작품 설명
    private Long lastEpisodeIdx;        // 마지막 회차 idx
    private String lastEpisodeTitle;    // 마지막 회차 제목
    private Integer adult;              // 성인 컨텐츠(0:전체이용가, 1:성인)
    private Integer adultPavilion;      // 성인관 유무(0: 일반, 1: 성인관)
    private Integer completeTypeIdx;    // complete_type.idx
    private Integer progress;           // 진행상황(1:연재, 2:휴재, 3:완결)
    private Integer exclusive;          // 독점 컨텐츠(0:비독점, 1:독점)
    private Integer publication;        // 단행본(0:비단행본, 1:단행본)
    private Integer revision;           // 개정판(0:비개정판, 1:개정판)
    private Integer sellType;           // 판매종류(1:소장/대여,2:소장,3:대여)
    private Integer state;              // 상태값
    private String label;               // 레이블
    private String code;                // 제품코드
    private String pubdate;             // 발행일
    private String pubdateTz;           // 발행일 타임존
    private String regdate;             // 등록일
    private String regdateTz;           // 등록일 타임존

}

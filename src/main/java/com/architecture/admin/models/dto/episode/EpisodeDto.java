package com.architecture.admin.models.dto.episode;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EpisodeDto {

    /**
     * episode
     */
    private Long idx;                   // episode.idx
    private Integer contentsIdx;        // contents.idx
    private Integer coin;               // 코인 판매 가격(소장)
    private Integer coinRent;           // 코인 판매 가격(대여)
    private Integer episodeNumber;      // 회차 번호
    private Integer sort;               // 회차 순서
    private Integer episodeTypeIdx;     // episode_type.idx
    private String title;               // 회차 제목
    private Integer checkLogin;         // 회차 로그인 필요 여부(0:로그인 불필요, 1:로그인 필요)
    private Integer checkArrow;         // 회차 체제 방식(0:좌 -> 우, 1:우 -> 좌)
    private Integer state;              // 상태
    private String pubdate;             // 발행일
    private String pubdateTz;           // 발행일 타임존
    private String regdate;             // 등록일
    private String regdateTz;           // 등록일 타임존

    /**
     * member
     */
    private Long memberIdx;             // member.idx

}

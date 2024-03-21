package com.architecture.admin.models.dto.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CpStatDto {

    // 날짜
    private String date;
    // 작품 idx
    private Integer contentsIdx;
    // 코인
    private Integer coin;
    // 대여 코인
    private Integer coinRent;
    // 소장 코인
    private Integer coinKeep;
    // 마일리지
    private Integer mileage;
    // 대여 마일리지
    private Integer mileageRent;
    // 소장 마일리지
    private Integer mileageKeep;
    // cp member idx
    private Integer cpMemberIdx;
    // code idx
    private Integer codeIdx;
    // code 이름
    private String code;
    // 카테고리
    private Integer categoryIdx;
    // 성인관 유무
    private Integer adultPavilion;
    // 성인 유무
    private Integer adult;
    // 정산 타입
    private Integer contract;
    // 웹 정산 비율
    private Integer calculate;
    // 어플 정산 비율
    private Integer calculateApp;
    // 코인 판매 수
    private Integer coinCnt;
    // 전체 판매 수
    private Integer totalCnt;
    // 웹 정산 수수료
    private Integer webFee;
    // 앱 정산 수수료
    private Integer appFee;
    // 구매경로(1:웹, 2: 어플)
    private Integer route;

}

package com.architecture.admin.models.dto.coin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CoinDto {

    private Long idx;
    private Long memberIdx;      // 회원 번호
    private String id;           // 회원 아이디
    private String nick;         // 회원 닉네임
    private Integer paymentIdx;  // 주문 번호
    private Long achievementIdx; // 업적 번호
    private Integer productIdx;  // 상품 번호
    private Integer coin = 0;    // 유료 코인
    private Integer type;        // 코인 유형(1: 코인, 2: 보너스 코인, 3: 마일리지)
    private Integer coinFree = 0;// 보너스 코인 (결제 시 추가지급 코인)
    private Integer mileage = 0; // 마일리지 (업적 달성시 지급되는 코인)
    private Integer usedCoin;    // 사용 코인
    private Integer restCoin;    // 남은 코인
    private Integer usedCoinFree;// 사용 보너스 코인
    private Integer restCoinFree;// 남은 보너스 코인
    private Integer usedMileage; // 사용 마일리지
    private Integer restMileage; // 남은 마일리지
    private Integer ticketCount; // 이용권 개수
    private String position;     // 지급 위치
    private Integer state;       // 상태
    private String expiredate;   // 만료일
    private String expireDateTz; // 만료일 타임존
    private String regdate;
    private String regdateTz;


    /**
     * episode
     */
    private Integer coinRent; // 대여 코인

    /**
     * 만료일
     */
    private String mileageExpireDate;
    private String coinExpireDate;
    private String coinFreeExpireDate;


    /**
     * 기타
     */
    private Long memberCoinSaveIdx;
    private Long mileageUsedIdx;
    private Long coinUsedIdx;
    private String nowDate;

}

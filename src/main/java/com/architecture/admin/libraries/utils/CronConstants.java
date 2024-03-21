package com.architecture.admin.libraries.utils;

/**
 * DB [cron 테이블] 에 등록된 idx 상수값
 */
public abstract class CronConstants {

    public static final int CRON_TEST = 1;                      // 테스트 cron
    public static final int CRON_COIN_EXPIRE = 2;               // [DB] 코인 만료 cron
    public static final int CRON_EXPECTED_EXPIRE_COIN = 3;      // [알림] 소멸 예정 코인 cron
    public static final int CRON_NEW_EPISODE_UPDATED = 4;       // [DB + 알림] 신규 회차 업데이트 cron
    public static final int CRON_MEMBER_ADULT_STATE_UPDATE = 5; // [DB] 회원 성인 여부 상태값 업데이트 cron
    public static final int CRON_CP_STAT = 6;                   // [DB] 작품 구매 통계
    public static final int CRON_RANK = 7;                      // [DB] 일별 랭킹

}

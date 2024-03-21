package com.architecture.admin.services.coin;

import com.architecture.admin.libraries.utils.CronConstants;
import com.architecture.admin.models.dao.coin.CoinDao;
import com.architecture.admin.models.daosub.coin.CoinDaoSub;
import com.architecture.admin.models.dto.coin.CoinDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CoinService extends BaseService {

    private final CoinDao coinDao;
    private final CoinDaoSub coinDaoSub;

    /*****************************************************
     *  DB CRON
     ****************************************************/

    /**
     * 코인 & 보너스 코인 & 마일리지 만료
     * 매일 1시간 마다 실행 [01:00, 02:00, 03:00......]
     * [보너스 코인은 현재 미 사용]
     */
    @Scheduled(cron = "0 0 0/1 * * *") // 1시간마다 실행
    public void expireCoin() {

        // 활성화된 크론인지 조회
        if (super.checkCronState(CronConstants.CRON_COIN_EXPIRE)) {

            /******************************* 코인 만료 ********************************/

            String nowDate = dateLibrary.getDatetime(); // 크론 돌리는 현재시간

            // 1. 업데이트 전 이미 만료된 코인 idxList 조회
            List<Long> coinIdxList = coinDaoSub.getExpireCoinIdxList(nowDate);

            // 2. 크론 돌리는 시간 기준 만료된 코인 state 0 으로 update
            int coinResult = coinDao.updateExpireCoin(nowDate);

            // 업데이트한 row가 있을 경우
            if (coinResult > 0) {

                // 업데이트 후 만료된 코인 idxList 조회
                List<CoinDto> expireCoinInfoList = coinDao.getExpireCoinInfoList(nowDate);

                // 업데이트 전 coin_expire_log 에 쌓인 기록은 제외
                if (coinIdxList != null && !coinIdxList.isEmpty()) {
                    for (Long idx : coinIdxList) {
                        expireCoinInfoList.removeIf(coinDto -> coinDto.getIdx().equals(idx));
                    }
                }

                // 3. member_coin_expire_log 등록
                if (expireCoinInfoList != null && !expireCoinInfoList.isEmpty()) {

                    // 만료 코인 로그 등록용 리스트
                    List<CoinDto> expireCoinLogList = new ArrayList<>();

                    // expire_coin_log 등록 할 데이터 set
                    for (CoinDto coinExpireDto : expireCoinInfoList) {
                        CoinDto coinDto = CoinDto.builder()
                                .memberIdx(coinExpireDto.getMemberIdx())
                                .coinUsedIdx(coinExpireDto.getIdx())
                                .coin(coinExpireDto.getCoin())
                                .restCoin(coinExpireDto.getRestCoin())
                                .type(coinExpireDto.getType())
                                .state(1)
                                .regdate(nowDate)
                                .build();

                        // 등록할 리스트에 dto 추가
                        expireCoinLogList.add(coinDto);
                    }

                    /** 만료된 코인 로그 insert **/
                    coinDao.insertExpireCoinLog(expireCoinLogList);

                    List<Long> memberIdxList = new ArrayList<>();

                    // 업데이트한 memberIdx 리스트에 추가
                    for (CoinDto coinDto : expireCoinLogList) {
                        Long memberIdx = coinDto.getMemberIdx();
                        memberIdxList.add(memberIdx);
                    }

                    // 리스트 내 memberIdx 중복 제거
                    Set<Long> memberIdxSet = new HashSet<>(memberIdxList);

                    // 4. member_coin 업데이트
                    for (Long memberIdx : memberIdxSet) {
                        // 업데이트용 dto
                        CoinDto coinDto = CoinDto.builder()
                                .memberIdx(memberIdx)
                                .nowDate(nowDate).build();

                        // 회원 잔여 코인 & 보너스 코인 조회
                        CoinDto restCoinDto = coinDao.getMemberCoin(coinDto);
                        coinDto.setCoin(restCoinDto.getRestCoin());         // 회원 잔여 코인 set
                        coinDto.setCoinFree(restCoinDto.getRestCoinFree()); // 회원 잔여 보너스 코인 set

                        /** 회원 코인 업데이트 **/
                        coinDao.updateMemberCoinAndCoinFree(coinDto);
                    }
                }
            }

            /******************************* 마일리지 만료 ********************************/

            // 1. 업데이트 전 이미 만료된 마일리지 idxList 조회 [이미 state 0인 것 조회]
            List<Long> mileageIdxList = coinDaoSub.getExpireMileageIdxList(nowDate);

            // 2. 크론 돌리는 시간 기준 만료된 마일리지 state 0 으로 update
            int mileageResult = coinDao.updateExpireMileage(nowDate);

            // 업데이트한 row가 있을 경우
            if (mileageResult > 0) {

                // 업데이트 후 만료된 마일리지 정보 (idx, mileage, rest_mileage) 조회
                List<CoinDto> expireMileageInfoList = coinDao.getExpireMileageInfoList(nowDate);

                // 업데이트 전 mileage_expire_log 에 쌓인 기록은 제외
                if (mileageIdxList != null && !mileageIdxList.isEmpty()) {
                    for (Long idx : mileageIdxList) {
                        expireMileageInfoList.removeIf(coinDto -> coinDto.getIdx().equals(idx));
                    }
                }

                // 3. member_mileage_expire_log 등록
                if (expireMileageInfoList != null && !expireMileageInfoList.isEmpty()) {
                    // 만료 마일리지 등록용 리스트
                    List<CoinDto> expireMileageLogList = new ArrayList<>();

                    // expire_mileage_log 등록 할 데이터 set
                    for (CoinDto mileageExpireDto : expireMileageInfoList) {
                        CoinDto mileageDto = CoinDto.builder()
                                .memberIdx(mileageExpireDto.getMemberIdx())
                                .mileageUsedIdx(mileageExpireDto.getIdx())
                                .mileage(mileageExpireDto.getMileage())
                                .restMileage(mileageExpireDto.getRestMileage())
                                .state(1)
                                .regdate(nowDate)
                                .build();

                        // 등록할 리스트에 dto 추가
                        expireMileageLogList.add(mileageDto);
                    }

                    /** 만료된 마일리지 로그 등록 **/
                    coinDao.insertExpireMileageLog(expireMileageLogList);

                    List<Long> memberIdxList = new ArrayList<>();

                    // 업데이트한 memberIdx 리스트에 추가
                    for (CoinDto coinDto : expireMileageLogList) {
                        Long memberIdx = coinDto.getMemberIdx();
                        memberIdxList.add(memberIdx);
                    }

                    // 리스트 내 memberIdx 중복 제거
                    Set<Long> memberIdxSet = new HashSet<>(memberIdxList);

                    // 4. member_coin 업데이트
                    for (Long memberIdx : memberIdxSet) {

                        CoinDto coinDto = CoinDto.builder()
                                .memberIdx(memberIdx)
                                .nowDate(nowDate).build();

                        // 회원 마일리지 조회
                        Integer restMileage = coinDao.getMemberMileage(coinDto);
                        coinDto.setMileage(restMileage);  // 잔여 마일리지 set

                        /** 회원 마일리지 update **/
                        coinDao.updateMileageFromMemberCoin(coinDto);
                    }
                }
            }
        }
    }
}

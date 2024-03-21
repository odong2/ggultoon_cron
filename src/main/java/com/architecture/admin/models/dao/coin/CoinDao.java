package com.architecture.admin.models.dao.coin;

import com.architecture.admin.models.dto.coin.CoinDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CoinDao {

    /*******************************************************************
     * SELECT
     *******************************************************************/


    /*******************************************************************
     * INSERT
     *******************************************************************/

    /**
     * 만료된 코인 로그 insert
     *
     * @param insertExpireCoinLogList
     */
    void insertExpireCoinLog(List<CoinDto> insertExpireCoinLogList);

    /**
     * 만료된 마일리지 insert
     *
     * @param insertExpireMileageLogList
     */
    void insertExpireMileageLog(List<CoinDto> insertExpireMileageLogList);

    /*******************************************************************
     * UPDATE
     *******************************************************************/

    /**
     * 만료된 코인 update
     *
     * @param nowDate
     * @return
     */
    int updateExpireCoin(String nowDate);

    /**
     * 만료된 마일리지 update
     *
     * @param nowDate
     * @return
     */
    int updateExpireMileage(String nowDate);

    /**
     * 회원 코인 & 보너스 코인 update
     * [member_coin 테이블]
     *
     * @param coinDto
     */
    void updateMemberCoinAndCoinFree(CoinDto coinDto);

    /**
     * 회원 마일리지 update
     * [member_coin 테이블]
     *
     * @param coinDto
     */
    void updateMileageFromMemberCoin(CoinDto coinDto);

    /**
     * 만료된 코인 정보 select
     *
     * @param nowDate
     * @return coinDto : [ idx(used_idx), memberIdx(회원 번호), mileage(마일리지), restMileage(남은 마일리지) ]
     */
    List<CoinDto> getExpireMileageInfoList(String nowDate);

    /**
     * 회원 보유 마일리지 select
     * [member_coin_used table]
     *
     * @param coinDto
     * @return
     */
    Integer getMemberMileage(CoinDto coinDto);

    /**
     * 만료 코인 정보 select
     *
     * @param nowDate : 현재 시각
     * @return coinDto: [ idx(used_idx), memberIdx(회원 번호), coin(코인), restCoin(남은 코인), type(코인 유형) ]
     */

    List<CoinDto> getExpireCoinInfoList(String nowDate);

    /**
     * 회원 코인 select
     *
     * @param coinDto : memberIdx(회원 번호), nowDate(현재 시간)
     * @return coinDto : restCoin(남은 코인), restCoinFree(남은 보너스 코인)
     */
    CoinDto getMemberCoin(CoinDto coinDto);
}

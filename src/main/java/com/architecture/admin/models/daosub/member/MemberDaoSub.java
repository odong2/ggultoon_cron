package com.architecture.admin.models.daosub.member;

import com.architecture.admin.models.dto.content.ContentDto;
import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MemberDaoSub {

    /**
     * 해당 작품을 찜한 회원 idx 리스트 조회
     */
    List<Long> getLikeMemberIdxList(List<ContentDto> lastEpisodeInfoList);

    /**
     * 미성년자 회원 리스트 조회
     */
    List<MemberDto> getMinorList();

    /**
     * 가입 회원 조회
     */
    long getJoinMember(Map<String,Object> map);
    /**
     * 탈퇴 회원 조회
     */
    long getOutMember(Map<String,Object> map);
    /**
     * 회원 결제 금액 조회
     */
    long getPayMember(Map<String,Object> map);
    /**
     * 회원 결제 횟수 조회
     */
    long getPayCnt(Map<String,Object> map);
}

package com.architecture.admin.models.dao.member;

import com.architecture.admin.models.dto.member.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MemberDao {

    /*******************************************************************
     * UPDATE
     *******************************************************************/

    /**
     * 성인이 된 회원들의 본인인증 정보 업데이트
     *
     * @param updateToAdultList : memberIdx(회원 번호), adult(회원 성인 상태값)
     */
    void updateAdultInfo(List<MemberDto> updateToAdultList);
}

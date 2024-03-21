package com.architecture.admin.models.dao.member;

import com.architecture.admin.models.dto.member.MemberGradeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GradeDao {

    /**
     * 회원 등급 등록
     * @param amountList
     */
    void insertMemberGrade(List<MemberGradeDto> amountList);
}

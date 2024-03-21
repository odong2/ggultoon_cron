package com.architecture.admin.models.daosub.member;

import com.architecture.admin.models.dto.member.MemberGradeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface GradeDaoSub {

    /**
     * 결제 금액
     * @param map
     * @return
     */
    List<MemberGradeDto> getMemberPayment(Map<String, Object> map);
}

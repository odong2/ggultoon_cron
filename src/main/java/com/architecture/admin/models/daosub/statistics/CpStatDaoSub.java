package com.architecture.admin.models.daosub.statistics;

import com.architecture.admin.models.dto.statistics.CpStatDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@Mapper
public interface CpStatDaoSub {
    List<CpStatDto> getStat(Map<String, Object> map);
}

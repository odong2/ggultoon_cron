package com.architecture.admin.models.dao.statistics;

import com.architecture.admin.models.dto.statistics.CpStatDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CpStatDao {

    /**
     * 결제 통계
     * @param statList
     */
    void setStat(List<CpStatDto> statList);
}

package com.architecture.admin.models.dao.content;

import com.architecture.admin.models.dto.content.ContentDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ContentDao {

    /**
     * contents 테이블 마지막 회차 정보 update
     *
     * @return
     */
    int updateLastEpisodeInfo(List<ContentDto> lastEpisodeInfoList);
}

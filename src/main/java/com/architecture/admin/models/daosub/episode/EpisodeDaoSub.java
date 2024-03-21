package com.architecture.admin.models.daosub.episode;

import com.architecture.admin.models.dto.episode.EpisodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EpisodeDaoSub {

    /**
     * 신규 업데이트 회차 리스트 조회
     */
    List<EpisodeDto> getNewEpisodeList(String nowDate);
}

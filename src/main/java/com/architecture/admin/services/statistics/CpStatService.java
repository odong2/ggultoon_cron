package com.architecture.admin.services.statistics;

import com.architecture.admin.libraries.utils.CronConstants;
import com.architecture.admin.models.dao.statistics.CpStatDao;
import com.architecture.admin.models.daosub.statistics.CpStatDaoSub;
import com.architecture.admin.models.dto.statistics.CpStatDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CpStatService extends BaseService {

    private final CpStatDao cpStatDao;

    private final CpStatDaoSub cpStatDaoSub;

    /**
     * 결제 통계
     * 매일 03시
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cpStat() {

        // cron 활성화 상태 체크
        if (super.checkCronState(CronConstants.CRON_CP_STAT)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            sdf.setLenient(false);
            Calendar day = Calendar.getInstance();


            // TODO 수동
            // String inputDate = "2023-07-09 00:00:00";
            // Date date = sdf.parse(inputDate);
            // day.setTime(date);

            // -1 일
            String endDate = sdf.format(day.getTime());

            // -2 일
            day.add(Calendar.DATE, -1);
            String startDate = sdf.format(day.getTime());

            Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate);
            map.put("endDate", endDate);

            // 일별 통계 목록
            List<CpStatDto> statList = cpStatDaoSub.getStat(map);
            if (!statList.isEmpty()) {
                cpStatDao.setStat(statList);
            }
        }
    }
}

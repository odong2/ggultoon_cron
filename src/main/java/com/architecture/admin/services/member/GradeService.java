package com.architecture.admin.services.member;

import com.architecture.admin.models.dao.member.GradeDao;
import com.architecture.admin.models.daosub.member.GradeDaoSub;
import com.architecture.admin.models.dto.member.MemberGradeDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GradeService extends BaseService {

    private final GradeDaoSub gradeDaoSub;

    private final GradeDao gradeDao;


    public static final List<Map<String, Object>> gradeList =  new ArrayList<>(){
        {
            add(Map.of("grade", 0, "condition", 0, "addMileage", 0, "payback", 0));
            add(Map.of("grade", 1, "condition", 10000, "addMileage", 0, "payback", 1));
            add(Map.of("grade", 2, "condition", 50000, "addMileage", 1, "payback", 2));
            add(Map.of("grade", 3, "condition", 100000, "addMileage", 2, "payback", 3));
            add(Map.of("grade", 4, "condition", 150000, "addMileage", 3, "payback", 4));
            add(Map.of("grade", 5, "condition", 200000, "addMileage", 5, "payback", 5));
        }
    };

    /**
     * 회원 등급 조정
     */
    @Scheduled(cron = "10 0 0 1 * *") // 매월 1일 10초
    public void memberGrade() {
        // SimpleDateFormat 1일 0시 세팅
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        sdf.setLenient(false);
        Calendar day = Calendar.getInstance();

        // 전달 말일 까지
        String endDate = dateLibrary.localTimeToUtc(sdf.format(day.getTime()));

        // 이번달 이전 3개월
        day.add(Calendar.MONTH, -3);
        String startDate = dateLibrary.localTimeToUtc(sdf.format(day.getTime()));

        Map<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        // 결제 금액
        List<MemberGradeDto> amountList = gradeDaoSub.getMemberPayment(map);
        for (MemberGradeDto dto : amountList) {

            MemberGradeDto memberGradeDto = myGrade(dto.getAmount());
            dto.setGrade(memberGradeDto.getGrade());
            dto.setAddMileage(memberGradeDto.getAddMileage());
            dto.setPayback(memberGradeDto.getPayback());
        }

        // 회원 등급 등록
        gradeDao.insertMemberGrade(amountList);
    }

    /**
     * 결제금액 등급 정보
     * @param memberPayment
     * @return
     */
    private MemberGradeDto myGrade(Integer memberPayment) {

        MemberGradeDto dto = new MemberGradeDto();
        for (Map<String, Object> grade : gradeList) {
            if (memberPayment >= Integer.parseInt(grade.get("condition").toString())) {
                dto.setAmount(memberPayment);
                dto.setGrade(Integer.parseInt(grade.get("grade").toString()));
                dto.setCondition(Integer.parseInt(grade.get("condition").toString()));
                dto.setAddMileage(Integer.parseInt(grade.get("addMileage").toString()));
                dto.setPayback(Integer.parseInt(grade.get("payback").toString()));
            }
        }
        return dto;
    }
}

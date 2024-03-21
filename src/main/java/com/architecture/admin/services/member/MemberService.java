package com.architecture.admin.services.member;

import com.architecture.admin.libraries.utils.CronConstants;
import com.architecture.admin.models.dao.member.MemberDao;
import com.architecture.admin.models.daosub.member.MemberDaoSub;
import com.architecture.admin.models.dto.member.MemberDto;
import com.architecture.admin.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService extends BaseService {

    private final MemberDaoSub memberDaoSub;
    private final MemberDao memberDao;

    /*****************************************************
     *  DB CRON
     ****************************************************/

    /**
     * 미성년자 --> 성인으로 변경되는 회원 상태값 업데이트
     */
    @Scheduled(cron = "0 0 0 1 1 *") // 매년 1월 1일 에 실행
    public void updateMemberAdultState() {

        // 활성화된 크론인지 조회
        if (super.checkCronState(CronConstants.CRON_MEMBER_ADULT_STATE_UPDATE)) {

            // 미성년자 회원 리스트 조회
            List<MemberDto> minorList = memberDaoSub.getMinorList();
            if (minorList != null && !minorList.isEmpty()) {

                // 성인으로 변경된 회원 담을 리스트 생성
                List<MemberDto> updateToAdultList = new ArrayList<>();
                for (MemberDto member : minorList) {
                    if (member != null) {

                        // 미성년자 회원의 생년월일 정보
                        String birth = member.getBirth();

                        // 해당 회원이 성인이 되었는지 체크
                        if(Boolean.TRUE.equals(checkIsMinorBecomeAdult(birth))) { // 성인이 되었다면

                            // 성인으로 상태값 set
                            member.setAdult(1);

                            // list add
                            updateToAdultList.add(member);
                        }
                    }
                }
                // 성인이 된 회원들의 본인인증 정보 업데이트
                memberDao.updateAdultInfo(updateToAdultList);
            }
        }
    }

    /**
     * 생년월일을 통해 미성년자 OR 성인 여부 판별
     */
    private Boolean checkIsMinorBecomeAdult(String birth) {

        // return value (기본값 false)
        boolean result = false;

        // format set
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        // 현재 날짜 및 시간 -> date
        int convertToday = Integer.parseInt(formatter.format(new Date()));

        // 파라미터로 전달받은 생년월일 -> date
        int convertBirth = Integer.parseInt(birth.replace("-", ""));

        // 현재 날짜와 회원 생년월일 비교
        int sum = convertToday - convertBirth;
        if (sum > 190000) {
            result = true;
        }
        return result;
    }

    /**
     * 일별 통계 텔레그램 발송
     */
    @Scheduled(cron = "0 0 9 * * *") // 매일 9시에 실행
    public void statDay() {

        String sSever = System.getProperty("spring.profiles.active");
        sSever = sSever == null ? "local" : sSever;

        // prd 서버에서만 실행
        if(!sSever.equals("prd")){
            return;
        }

        // 조회 시작 일 종료일
        Map<String,Object> map = new HashMap<String,Object>();
        // 어제
        LocalDate startDay = LocalDate.now().minusDays(1);
        // 오늘
        LocalDate endDay = LocalDate.now();
        map.put("startDay",startDay);
        map.put("endDay",endDay);

        // 1000 자릿수 구분 표시
        DecimalFormat df = new DecimalFormat("###,###");

        // 가입회원
        String join = df.format(memberDaoSub.getJoinMember(map));
        // 탈퇴회원
        String out = df.format(memberDaoSub.getOutMember(map));
        // 결제금액
        String payCnt = df.format(memberDaoSub.getPayCnt(map));
        // 결제금액
        String pay = df.format(memberDaoSub.getPayMember(map));

        // 전송 메세지
        String msg = "### 꿀툰 일일 통계 ###\n\n- 통계 날짜 : " + startDay + "\n- 가입 회원 : "+join+"\n- 탈퇴 회원 : "+out+"\n- 결제 횟수 : "+payCnt+"\n- 결제 금액 : "+ pay;

        pushAlarm(msg,"GG");
    }

    /**
     * 월별 통계 텔레그램 발송
     */
    @Scheduled(cron = "3 0 9 1 * *") // 매월 1일 9시에 실행
    public void statMonth() {

        String sSever = System.getProperty("spring.profiles.active");
        sSever = sSever == null ? "local" : sSever;

        // prd 서버 만 실행
        if(!sSever.equals("prd")){
            return;
        }

        // 조회 시작 일 종료일
        Map<String,Object> map = new HashMap<String,Object>();
        // 지난달
        LocalDate startDay = LocalDate.now().minusMonths(1);
        // 오늘
        LocalDate endDay = LocalDate.now();

        String month =  startDay.format(DateTimeFormatter.ofPattern("MM"));

        map.put("startDay",startDay);
        map.put("endDay",endDay);

        // 1000 자릿수 , 표시
        DecimalFormat df = new DecimalFormat("###,###");

        // 가입회원
        String join = df.format(memberDaoSub.getJoinMember(map));
        // 탈퇴회원
        String out = df.format(memberDaoSub.getOutMember(map));
        // 결제금액
        String payCnt = df.format(memberDaoSub.getPayCnt(map));
        // 결제금액
        String pay = df.format(memberDaoSub.getPayMember(map));

        // 전송 메세지
        String msg = "### 꿀툰 "+month+"월 통계 ###\n\n- 가입 회원 : "+join+"\n- 탈퇴 회원 : "+out+"\n- 결제 횟수 : "+payCnt+"\n- 결제 금액 : "+ pay;

        pushAlarm(msg,"GG");
    }
}

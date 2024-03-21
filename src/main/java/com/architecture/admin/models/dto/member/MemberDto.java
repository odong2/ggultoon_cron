package com.architecture.admin.models.dto.member;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MemberDto {

    /**
     * member
     */
    private Long idx;                   // member.idx
    private String id;                  // 아이디
    private String password;            // 비밀번호
    private String nick;                // 닉네임
    private Integer auth;               // 본인인증 상태(0:미인증, 1:인증)
    private Integer adult;              // 성인인증 상태(0:비성인, 1:성인)
    private String site;                // 유입경로
    private Integer state;              // 상태
    private Integer isSimple;           // 간편가입 여부(0:일반가입, 1:간편가입)
    private String joinIp;              // 가입아이피
    private Integer isBenefit;          // 탈퇴시도 혜택(0:받지 않음, 1:받음)
    private String logindate;           // 로그인시간
    private String logindateTz;         // 로그인시간 타임존
    private String regdate;             // 등록일
    private String regdateTz;           // 등록일 타임존

    /**
     * member_info
     **/

    private Long memberIdx;         // 회원번호
    @Email
    private String email;           // 이메일
    private String txseq;           // 본인인증거래번호
    private String phone;           // 전화번호
    private String com;             // 통신사
    private String gender;          // 성별(M: male, F: female)
    private String lang;            // 사용언어
    private String birth;           // 생년월일
    private String ci;              // 개인 식별 고유값
    private String di;              // 업체 중복가입 확인값
    private String name;            // 이름
    private String ip;              // 아이피
}

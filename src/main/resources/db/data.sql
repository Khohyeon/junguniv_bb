
-- 회원(Member pwd: 1234)
INSERT INTO MEMBER (MEMBER_IDX, USER_ID, PWD, BIRTHDAY, SEX, TEL_MOBILE, EMAIL, ZIPCODE, ADDR1, ADDR2, USER_TYPE,
                    AUTHLEVEL, MEMBER_STATE)
VALUES (1, 'qwer', '$2a$10$5mcrIopDr1/WTCSzbMoGo.5L7SYgLLyxH0OZobyOYkSPeItRqxI6G', '1990-01-01', 'M', '010-1234-5678',
        'user123@example.com', '12345', '서울시 강남구 테헤란로 123', '101호', 'ADMIN', 150, 'Y'),
       (2, 'ㅂㅈㄷㄱ', '$2a$10$5mcrIopDr1/WTCSzbMoGo.5L7SYgLLyxH0OZobyOYkSPeItRqxI6G', '1995-05-05', 'F', '010-5678-1234',
        'user5678@example.com', '54321', '서울시 서초구 서초대로 456', '202호',  'ADMIN', 100, 'Y'),
       (3, 'asdf', '$2a$10$5mcrIopDr1/WTCSzbMoGo.5L7SYgLLyxH0OZobyOYkSPeItRqxI6G', '1985-09-10', 'M', '010-9101-1122',
        'user91011@example.com', '67890', '서울시 종로구 종로 789', '303호', 'ADMIN', 80, 'Y'),
       (4, 'zxcv', '$2a$10$5mcrIopDr1/WTCSzbMoGo.5L7SYgLLyxH0OZobyOYkSPeItRqxI6G', '1985-09-10', 'M', '010-9101-1122',
        'user91011@example.com', '67890', '서울시 종로구 종로 789', '303호',  'ADMIN', 0, 'Y'),
       (5, 'qwer1', '$2a$10$5mcrIopDr1/WTCSzbMoGo.5L7SYgLLyxH0OZobyOYkSPeItRqxI6G', '1985-09-10', 'M', '010-9101-1122',
        'user91011@example.com', '67890', '서울시 종로구 종로 789', '303호',  'ADMIN', 0, 'Y'),
       (6, 'qwer2', '$2a$10$5mcrIopDr1/WTCSzbMoGo.5L7SYgLLyxH0OZobyOYkSPeItRqxI6G', '1985-09-10', 'M', '010-9101-1122',
        'user91011@example.com', '67890', '서울시 종로구 종로 789', '303호',  'ADMIN', 0, 'Y'),
       (7, 'qwer3', '$2a$10$5mcrIopDr1/WTCSzbMoGo.5L7SYgLLyxH0OZobyOYkSPeItRqxI6G', '1985-09-10', 'M', '010-9101-1122',
        'user91011@example.com', '67890', '서울시 종로구 종로 789', '303호',  'ADMIN', 0, 'Y');

INSERT INTO UZN_MENU (MENU_NAME, SORTNO, CHK_USE, URL, PARENT_IDX) VALUES
-- 최상위 메뉴
('회원관리', 1, 'Y', NULL, NULL),
('홈페이지관리', 2, 'Y', NULL, NULL),
('일반통계/기록', 3, 'Y', NULL, NULL),
('시스템설정', 4, 'Y', NULL, NULL),

-- 하위 메뉴
('회원정보관리', 1, 'Y', '/masterpage_sys/member/student/', 1),
('권한관리', 2, 'Y', '/masterpage_sys/authlevel/', 1),
('주소록출력', 3, 'Y', '/masterpage_sys/label/student/', 1),
('팝업관리', 1, 'Y', '/masterpage_sys/popup/listForm', 2),
('약관관리', 2, 'Y', '/masterpage_sys/agreement/joinForm', 2),
('게시판관리', 3, 'Y', '/masterpage_sys/board/counsel', 2),
('문의상담관리', 4, 'Y', '/masterpage_sys/counsel', 2),
('일반통계', 1, 'Y', '#', 3),
('기본설정', 1, 'Y', '#', 4),
('기본표시정보', 2, 'Y', '#', 4),
('지원금종류설정', 3, 'Y', '#', 4),
('메뉴관리', 4, 'Y', '#', 4),

-- 탭 메뉴
('수강생', 1, 'Y', '#', 5),
('교강사(튜터)', 2, 'Y', '#', 5),
('위탁기업', 3, 'Y', '#', 5),
('LMS관리자', 4, 'Y', '#', 5),
('관리자권한설정', 1, 'Y', '#', 6),
('홈페이지게시판', 2, 'Y', '#', 6),
('주소록출력', 1, 'Y', '#', 7),
('메인팝업', 1, 'Y', '/masterpage_sys/popup/listForm', 8),
('상단팝업', 2, 'N', '/masterpage_sys/board/mainpopup', 8),
('회원가입약관', 1, 'Y', '/masterpage_sys/agreement/joinForm', 9),
('수강신청약관', 2, 'Y', '/masterpage_sys/agreement/courseForm', 9),
('1:1 상담', 1, 'Y', '/masterpage_sys/board/listForm', 10),
('게시판말머리관리', 2, 'Y', '/masterpage_sys/board/headForm', 10),
('상담예약', 1, 'Y', '/masterpage_sys/counsel/listForm', 11),
('홈페이지 Q&A', 1, 'Y', '#', 12),
('회원통계', 2, 'Y', '#', 12),
('회원접속통계', 3, 'Y', '#', 12),
('기본설정', 1, 'Y', '#', 13),
('환급교육', 1, 'Y', '#', 14),
('일반교육', 2, 'Y', '#', 14),
('환급교육', 1, 'Y', '#', 15),
('메뉴분류관리', 1, 'Y', '#', 16),
('메뉴명관리', 2, 'Y', '#', 16);

INSERT INTO POPUP (POPUP_NAME, START_DATE, END_DATE, WIDTH_SIZE, HEIGHT_SIZE, TOP_SIZE, LEFT_SIZE, CONTENTS, CHK_TODAY, POPUP_TYPE, CHK_OPEN, CHK_SCROLLBAR) VALUES
('이벤트 팝업', '2023-01-01', '2023-01-10', '800', '600', '100', '100', '이벤트 내용입니다.', 'Y', 'popup', 'Y', 'N'),
('공지 팝업', '2023-02-01', '2023-02-05', '500', '400', '50', '50', '공지사항 내용입니다.', 'N', 'layer', 'N', 'Y'),
('할인 팝업', '2023-03-01', '2023-03-20', '600', '450', '120', '120', '할인 이벤트 안내입니다.', 'Y', 'poplayer', 'Y', 'Y'),
('업데이트 팝업', '2023-04-01', '2023-04-15', '700', '500', '80', '80', '업데이트 내용입니다.', 'N', 'popup', 'N', 'N'),
('알림 팝업', '2023-05-01', '2023-05-10', '400', '300', '30', '30', '알림 메시지 내용입니다.', 'Y', 'layer', 'Y', 'N');

INSERT INTO COUNSEL (
    COUNSEL_NAME, NAME, TEL_MOBILE, TALK_TIME, MEMO, ANSWER_MEMO, APPLY_USER_ID, APPLY_CLIENT_IP, ADDR1, ADDR2, ZIPCODE,
    FINAL_EDUCATION_TYPE, FNAME1, LICENSE, FNAME2, FNAME3, BBSID, FNAME1_NAME, FNAME2_NAME, FNAME3_NAME, PWD, DEGREE_HOPE,
    EMAIL, COUNSEL_STATE) VALUES
('온라인 상담', '홍길동', '010-1234-5678', '2023-05-01 10:30', '상담 신청 내용입니다.', '답변 내용입니다.', 'admin', '127.0.0.1',
'서울특별시 강남구', '테헤란로 123', '12345', '대학교 졸업', 'file1.jpg', '자격증 내용', 'file2.jpg', 'file3.jpg',
'ONLINE', 'file1-private.jpg', 'file2-private.jpg', 'file3-private.jpg', 'password1', '웹개발 과정', 'test1@example.com', 'A'),
('전화 상담', '김영희', '010-9876-5432', '2023-05-02 14:00', '전화 상담 요청 내용입니다.', '전화 상담 답변입니다.', 'admin', '127.0.0.2',
'경기도 성남시', '분당로 456', '54321', '석사 졸업', 'file4.jpg', '자격증 내용', 'file5.jpg', 'file6.jpg',
'PHONE', 'file4-private.jpg', 'file5-private.jpg', 'file6-private.jpg', 'password2', '디자인 과정', 'test2@example.com', 'B'),
('온라인 상담', '이철수', '010-1111-2222', '2023-05-03 09:00', '문의 내용입니다.', '답변입니다.', 'admin', '127.0.0.3',
'부산광역시 해운대구', '달맞이길 789', '67890', '고등학교 졸업', 'file7.jpg', '자격증 내용', 'file8.jpg', 'file9.jpg',
'ONLINE', 'file7-private.jpg', 'file8-private.jpg', 'file9-private.jpg', 'password3', '데이터 분석 과정', 'test3@example.com', 'C'),
('전화 상담', '박민수', '010-3333-4444', '2023-05-04 11:15', '상담 요청 내용입니다.', '전화 상담에 대한 답변입니다.', 'admin', '127.0.0.4',
'대구광역시 수성구', '범어로 123', '98765', '박사 졸업', 'file10.jpg', '자격증 내용', 'file11.jpg', 'file12.jpg',
'PHONE', 'file10-private.jpg', 'file11-private.jpg', 'file12-private.jpg', 'password4', 'AI 과정', 'test4@example.com', 'A'),
('온라인 상담', '최지영', '010-5555-6666', '2023-05-05 16:45', '기타 상담 요청입니다.', '기타 상담 답변입니다.', 'admin', '127.0.0.5',
'광주광역시 북구', '문흥로 123', '24680', '고등학교 졸업', 'file13.jpg', '자격증 내용', 'file14.jpg', 'file15.jpg',
'ONLINE', 'file13-private.jpg', 'file14-private.jpg', 'file15-private.jpg', 'password5', '마케팅 과정', 'test5@example.com', 'B');

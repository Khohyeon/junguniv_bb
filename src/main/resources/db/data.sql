
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

-- -- 상위 메뉴 데이터
-- INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
-- VALUES (1, '회원관리', 1, 'Y');
-- INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
-- VALUES (2, '홈페이지관리', 2, 'Y');
-- INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
-- VALUES (3, '일반/통계기록', 3, 'Y');
-- INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
-- VALUES (4, '시스템설정', 4, 'Y');
-- INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
-- VALUES (5, '체크안된메뉴', 5, 'N');
--
-- -- 하위 메뉴 데이터
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('회원정보관리', 1, 1, 'Y', '');
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('권한관리', 1, 2, 'Y', '');
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('주소록출력', 1, 1, 'Y', '');
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('팝업관리', 2, 1, 'Y', '/masterpage/mainpopup');
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('약관관리', 2, 1, 'Y', '');
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('문의상담관리', 2, 1, 'Y', '');
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('게시판관리', 2, 1, 'Y', '');
-- INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
-- VALUES ('홈페이지 Q&A', 3, 1, 'Y', '');


INSERT INTO UZN_MENU (MENU_NAME, SORTNO, CHK_USE, URL, PARENT_IDX) VALUES
-- 최상위 메뉴
('회원관리', 1, 'Y', NULL, NULL),
('홈페이지관리', 2, 'Y', NULL, NULL),

-- 하위 메뉴
('회원정보관리', 1, 'Y', '#', 1),
('권한관리', 2, 'Y', '#', 1),
('팝업관리', 1, 'Y', '/masterpage/popup/listForm', 2),
('약관관리', 2, 'Y', '/masterpage/agreement/joinForm', 2),
('게시판관리', 2, 'Y', '/masterpage/board/counsel', 2),
('문의상담관리', 2, 'Y', '/masterpage/counsel', 2),

-- 탭 메뉴
('상세정보', 1, 'Y', '/member/info/detail', 3),
('수정', 2, 'Y', '/member/info/edit', 3),
('메인팝업', 1, 'Y', '/masterpage/popup/listForm', 5),
('상단팝업', 2, 'Y', '/masterpage/board/mainpopup', 5),
('회원가입약관', 2, 'Y', '/masterpage/agreement/joinForm', 6),
('수강신청약관', 2, 'Y', '/masterpage/agreement/course', 6),
('1:1 상담', 2, 'Y', '/masterpage/board/counsel', 7),
('게시판말머리관리', 2, 'Y', '/masterpage/board/head', 7),

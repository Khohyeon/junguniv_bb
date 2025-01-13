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
('상단팝업', 2, 'N', '/masterpage/board/mainpopup', 5),
('회원가입약관', 2, 'Y', '/masterpage/agreement/joinForm', 6),
('수강신청약관', 2, 'Y', '/masterpage/agreement/courseForm', 6),
('1:1 상담', 2, 'Y', '/masterpage/board/listForm', 7),
('게시판말머리관리', 2, 'Y', '/masterpage/board/headForm', 7),
('상담예약', 2, 'Y', '/masterpage/counsel/listForm', 8);

-- 상위 메뉴 데이터
INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
VALUES (1, '회원관리', 1, 'Y');
INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
VALUES (2, '홈페이지관리', 2, 'Y');
INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
VALUES (3, '일반/통계기록', 3, 'Y');
INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
VALUES (4, '시스템설정', 4, 'Y');
INSERT INTO UZN_BRANCH (BRANCH_IDX, BRANCH_NAME, SORTNO, CHK_USE)
VALUES (5, '체크안된메뉴', 5, 'N');

-- 하위 메뉴 데이터
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('회원정보관리', 1, 1, 'Y', '/masterpage/member/');
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('권한관리', 1, 2, 'Y', '/masterpage/authLevel/');
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('주소록출력', 1, 1, 'Y', '/masterpage/label/');
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('팝업관리', 2, 1, 'Y', '/masterpage/mainpopup');
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('약관관리', 2, 1, 'Y', '');
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('문의상담관리', 2, 1, 'Y', '');
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('게시판관리', 2, 1, 'Y', '');
INSERT INTO UZN_BRANCH_SUB (SUB_NAME, BRANCH_IDX, SORTNO, CHK_USE, URL)
VALUES ('홈페이지 Q&A', 3, 1, 'Y', '');

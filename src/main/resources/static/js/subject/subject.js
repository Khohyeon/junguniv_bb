/**
 * 상세 검색 클릭 시 토글 변경되면서 검색창 변경
 */
document.addEventListener('DOMContentLoaded', function () {
    const searchMoreBtn = document.getElementById('searchMoreBtn');
    const columnTcWrap = document.getElementById('columnTcWrap');
    const formWrap = document.getElementById('formWrap');

    if (searchMoreBtn) {
        searchMoreBtn.addEventListener('click', function () {
            // display 속성 변경
            if (columnTcWrap.style.display === 'none' || columnTcWrap.style.display === '') {
                columnTcWrap.style.display = 'flex';
            } else {
                columnTcWrap.style.display = 'none';
            }

            // form-wrap에 active 클래스 추가/제거
            formWrap.classList.toggle('active');
        });
    }
});


/**
 * 리스트 페이지에서 신규등록 클릭 시 기존 url에 '/save'를 추가하여 이동
 */
document.addEventListener('DOMContentLoaded', function () {
    const saveButton = document.getElementById('saveForm');

    if (saveButton) {
        saveButton.addEventListener('click', () => {
            // 현재 URL 가져오기
            let currentPath = window.location.pathname;

            // 이미 '/save'가 포함되어 있다면 추가하지 않음
            if (!currentPath.endsWith('/save')) {
                window.location.href = currentPath + '/save';
            }
        });
    }
});

/**
 * 과정등록 중 단계별 탭 전환 기능
 */
document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelectorAll('.nav-link');
    const tabPanes = document.querySelectorAll('.tab-pane');

    // 초기 설정: 모든 tab-pane 숨기고 첫 번째만 보이게
    tabPanes.forEach((tab, index) => {
        tab.style.display = index === 0 ? 'block' : 'none';
    });

    navLinks.forEach(link => {
        link.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 앵커 이벤트 막기

            // 현재 클릭한 탭의 ID 가져오기
            const targetId = this.getAttribute('href').replace('#', ''); // step-0, step-1 등

            // 모든 탭에서 active 클래스 제거 & tab-pane 숨기기
            navLinks.forEach(nav => nav.classList.remove('active'));
            tabPanes.forEach(tab => tab.style.display = 'none');

            // 클릭한 탭과 해당하는 콘텐츠 활성화
            this.classList.add('active');
            document.getElementById(targetId).style.display = 'block';
        });
    });
});

document.addEventListener("DOMContentLoaded", function() {

    const state = {
        menuIdx: "51",  // 숫자든 문자열이든 원하는 형태로 지정하세요.
        depth3Menus: [
            {
                url: "http://localhost:8080/masterpage_pro/subject",
                target: "_self",
                name: "과정정보등록"
            },
            {
                url: "#",
                target: "_self",
                name: "콘텐츠차시설정"
            },
            {
                url: "#",
                target: "_self",
                name: "평가문제관리"
            },
            {
                url: "#",
                target: "_self",
                name: "수강정보설정"
            },
            {
                url: "#",
                target: "_self",
                name: "역량평가등록"
            },
            {
                url: "#",
                target: "_self",
                name: "설문평가등록"
            },
        ]
    };

    localStorage.setItem('tabState', JSON.stringify(state));

    restoreTabState();
});
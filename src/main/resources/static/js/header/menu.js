/**
 * Dept2 (2차 메뉴 클릭 시 페이지 이동 이벤트)
 */
document.addEventListener('DOMContentLoaded', () => {
    // Depth2 링크 클릭 이벤트 추가
    document.querySelectorAll('.depth2-link').forEach(link => {
        link.addEventListener('click', (event) => {
            const menuIdx = link.getAttribute('data-menu-id');
            if (menuIdx) {
                loadDepth3Menus(event, link);
            } else {
                console.error('Menu ID is missing');
            }
        });
    });

    // 상태 복원
    restoreTabState();
});

/**
 * 3차 메뉴 (Tab 메뉴 불러오는 함수)
 */
async function fetchDepth3Menus(menuIdx) {
    try {
        const response = await fetch(`/masterpage_sys/manager_menu/api/depth3?parentMenuIdx=${menuIdx}`);
        if (!response.ok) {
            throw new Error('Failed to fetch depth3 menus');
        }
        // 서버에서 이미 권한이 있는 메뉴만 반환하므로 추가 필터링 불필요
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        return [];
    }
}

/**
 * 2차 메뉴의 idx와 그에 해당하는 3차 메뉴의 list를 localStorage에 저장하여 tab 상태 관리
 */
function saveTabState(menuIdx, depth3Menus) {
    const state = {
        menuIdx,
        depth3Menus,
    };
    localStorage.setItem('tabState', JSON.stringify(state));
}

/**
 * 2차 메뉴 클릭 시 함수 (3차 메뉴의 데이터를 받아와 동적으로 추가)
 */
async function loadDepth3Menus(event, element) {
    event.preventDefault();

    // 기존 활성화된 Depth2 메뉴에서 클래스 제거
    document.querySelectorAll('.depth2-link.on').forEach(link => {
        link.classList.remove('on');
    });

    // 클릭된 Depth2 메뉴에 활성화 클래스 추가
    element.classList.add('on');

    // 클릭된 Depth2 메뉴의 부모 li 요소에 활성화 클래스 추가
    const parentLi = element.closest('li');
    if (parentLi) {
        // 다른 li의 on 클래스 제거
        parentLi.parentElement.querySelectorAll('li').forEach(li => li.classList.remove('on'));
        // 클릭된 li에 on 클래스 추가
        parentLi.classList.add('on');
    }

    const menuIdx = element.getAttribute('data-menu-id');
    if (!menuIdx) {
        console.error('Menu ID is missing');
        return;
    }

    // Fetch Depth3 메뉴
    const depth3Menus = await fetchDepth3Menus(menuIdx);

    // Depth3 메뉴 렌더링
    const depth3MenuList = document.getElementById('depth3-menu-list');
    depth3MenuList.innerHTML = ''; // 기존 메뉴 초기화

    depth3Menus.forEach((menu, index) => {
        const li = document.createElement('li');

        const anchor = document.createElement('a');
        anchor.href = menu.url;
        anchor.target = menu.target;
        anchor.textContent = menu.name;

        // 이벤트 리스너로 클릭 이벤트 처리
        anchor.addEventListener('click', (event) => {
            handleDepth3Click(event, anchor);
        });

        li.appendChild(anchor);
        depth3MenuList.appendChild(li);

        // 활성화된 상태 추가 (URL로 판단)
        if (window.location.href === menu.url) {
            li.classList.add('on'); // 현재 URL과 일치하면 활성화
        }
    });

    // 상태 저장
    saveTabState(menuIdx, depth3Menus);

    // 3차 메뉴 첫 번째 버튼 자동 클릭
    if (depth3Menus.length > 0) {
        const firstButton = depth3MenuList.querySelector('li a');
        if (firstButton) {
            firstButton.click(); // 첫 번째 버튼 클릭 이벤트 트리거
        }
    }
}

/**
 * 상태를 복원하는 함수 페이지가 로드될 때 localStorage에서 탭 상태를 읽어와서 복원
 */
function restoreTabState() {
    const state = localStorage.getItem('tabState');
    if (!state) return;

    const { menuIdx, depth3Menus } = JSON.parse(state);

    // Depth2 메뉴 활성화
    document.querySelectorAll('.depth2-link').forEach(link => {
        if (link.getAttribute('data-menu-id') === menuIdx) {
            link.classList.add('on');

            // 클릭된 Depth2 메뉴의 부모 li 활성화
            const parentLi = link.closest('li');
            if (parentLi) {
                parentLi.classList.add('on');
                // Depth2 메뉴의 부모 ul도 활성화 (탭 유지)
                const parentUl = parentLi.closest('.depth2');
                if (parentUl) {
                    parentUl.style.display = 'block'; // 탭 열기
                }
            }
        }
    });

    // Depth3 메뉴 렌더링
    const depth3MenuList = document.getElementById('depth3-menu-list');
    depth3MenuList.innerHTML = ''; // 기존 메뉴 초기화

    depth3Menus.forEach(menu => {
        const li = document.createElement('li');

        const anchor = document.createElement('a');
        anchor.href = menu.url;
        anchor.target = menu.target;
        anchor.textContent = menu.name;

        // 클릭 이벤트 처리
        anchor.addEventListener('click', (event) => {
            handleDepth3Click(event, anchor);
        });

        li.appendChild(anchor);
        depth3MenuList.appendChild(li);

        const currentPath = window.location.pathname;
        const menuPath = new URL(menu.url, window.location.origin).pathname;

        // console.log로 디버그
        console.log('currentPath: ' + currentPath);
        console.log('menuPath: ' + menuPath);

        // 정확한 경로 비교
        const isExactMatch = currentPath === menuPath;
        // 하위 경로 비교
        const isSubPath = currentPath.startsWith(menuPath + '/');

        // 관련 경로 판단
        const isRelated = isExactMatch || isSubPath;

        if (isRelated) {
            li.classList.add('on'); // 관련된 경로를 활성화
        }
    });
}


/**
 * 콘텐츠 로드 함수 - 페이지 이동
 */
function handleDepth3Click(event, element) {
    event.preventDefault(); // 기본 동작(페이지 이동) 막기

    // 기존 활성화된 상태 제거
    document.querySelectorAll('#depth3-menu-list li').forEach(li => li.classList.remove('on'));

    // 클릭된 메뉴에 활성화 클래스 추가
    element.parentElement.classList.add('on');

    // URL 업데이트 및 페이지 이동
    const targetUrl = element.getAttribute('href');

    // 활성화된 상태 저장
    const activeDepth2 = document.querySelector('.depth2-link.on');
    const depth3Menus = Array.from(document.querySelectorAll('#depth3-menu-list li a')).map(anchor => ({
        url: anchor.href,
        target: anchor.target,
        name: anchor.textContent,
    }));

    if (activeDepth2) {
        const menuIdx = activeDepth2.getAttribute('data-menu-id');
        saveTabState(menuIdx, depth3Menus); // 상태 저장
    }

    // 페이지 이동
    window.location.href = targetUrl;
}


/**
 * 페이지 이동 전에 상태를 저장
 */
function saveTabStateOnNavigation() {
    const activeDepth2 = document.querySelector('.depth2-link.on');
    const depth3Menus = Array.from(document.querySelectorAll('#depth3-menu-list li a')).map(anchor => ({
        url: anchor.href,
        target: anchor.target,
        name: anchor.textContent,
    }));

    if (activeDepth2) {
        const menuIdx = activeDepth2.getAttribute('data-menu-id');
        saveTabState(menuIdx, depth3Menus); // 상태 저장
    }
}


/**
 * 메뉴 클릭 시 active 활성화
 */
document.addEventListener("DOMContentLoaded", function() {
    const menuItems = document.querySelectorAll('.menu-category a');

    // 페이지 로드시 로컬 스토리지에서 활성 메뉴를 가져와서 해당 메뉴에 active 클래스 추가
    const activeMenu = localStorage.getItem('activeMenu');
    if (activeMenu) {
        menuItems.forEach(item => {
            if (item.dataset.menu === activeMenu) {
                item.classList.add('active');
            }
        });
    }

    menuItems.forEach(function(item) {
        item.addEventListener('click', function() {
            // 모든 메뉴 항목에서 active 클래스 제거
            menuItems.forEach(el => el.classList.remove('active'));

            // 클릭한 메뉴 항목에 active 클래스 추가
            this.classList.add('active');

            // 활성 메뉴 정보를 로컬 스토리지에 저장
            localStorage.setItem('activeMenu', this.dataset.menu);
        });
    });
});

// document.addEventListener("DOMContentLoaded", function() {
//     // 뒤로가기 등으로 페이지가 로드될 때마다 active 메뉴 초기화
//     localStorage.removeItem('activeMenu');
//
//     const menuItems = document.querySelectorAll('.menu-category a');
//
//     menuItems.forEach(function(item) {
//         item.addEventListener('click', function() {
//             // 모든 메뉴 항목에서 active 클래스 제거
//             menuItems.forEach(el => el.classList.remove('active'));
//
//             // 클릭한 메뉴 항목에 active 클래스 추가
//             this.classList.add('active');
//
//             // 활성 메뉴 정보를 로컬 스토리지에 저장
//             localStorage.setItem('activeMenu', this.dataset.menu);
//         });
//     });
// });
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
});

/**
 * 3차 메뉴 (Tab 메뉴 불러오는 함수)
 */
async function fetchDepth3Menus(menuIdx) {
    try {
        const response = await fetch(`/api/menu/depth3?parentMenuIdx=${menuIdx}`);
        if (!response.ok) {
            throw new Error('Failed to fetch depth3 menus');
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        return [];
    }
}

/**
 * 2차 메뉴의 idx와 그에 해당하는 3차 메뉴의 list를 localStorage에 저장
 */
function saveTabState(menuIdx, depth3Menus) {
    const state = {
        menuIdx,
        depth3Menus,
    };
    localStorage.setItem('tabState', JSON.stringify(state));
}

async function loadDepth3Menus(event, element) {
    event.preventDefault();

    // 기존 활성화된 Depth2 메뉴에서 클래스 제거
    document.querySelectorAll('.depth2-link.on').forEach(link => {
        link.classList.remove('on');
    });

    // 클릭된 Depth2 메뉴에 활성화 클래스 추가
    element.classList.add('on');

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

    // 페이지 이동
    if (depth3Menus.length > 0) {
        window.location.href = depth3Menus[0].url; // 첫 번째 메뉴로 이동
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // 기존 Depth2 클릭 이벤트 설정
    document.querySelectorAll('.depth2-link').forEach(link => {
        link.addEventListener('click', event => {
            loadDepth3Menus(event, event.target);
        });
    });

    // 상태 복원
    restoreTabState();
});

function restoreTabState() {
    const state = localStorage.getItem('tabState');
    if (!state) return;

    const { menuIdx, depth3Menus } = JSON.parse(state);

    // Depth2 메뉴 활성화
    document.querySelectorAll('.depth2-link').forEach(link => {
        if (link.getAttribute('data-menu-id') === menuIdx) {
            link.classList.add('on');
        }
    });

    // Depth3 메뉴 렌더링
    const depth3MenuList = document.getElementById('depth3-menu-list');
    depth3MenuList.innerHTML = ''; // 기존 메뉴 초기화

    // 기존 활성화된 li의 'on' 클래스 제거
    document.querySelectorAll('#depth3-menu-list li').forEach(li => {
        li.classList.remove('on');
    });

    depth3Menus.forEach((menu, index) => {
        const li = document.createElement('li');

        // 기본적으로 첫 번째 li 활성화
        if (index === 0) {
            li.classList.add('on');
        }

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

        // 현재 URL의 경로와 menu.url 비교
        if (window.location.pathname === menu.url) {
            depth3MenuList.querySelectorAll('li').forEach(li => li.classList.remove('on'));
            li.classList.add('on'); // 현재 경로에 해당하는 li 활성화
        }
    });

}

function handleDepth3Click(event, element) {
    event.preventDefault(); // 기본 동작(페이지 이동) 막기

    // 기존 활성화된 상태 제거
    document.querySelectorAll('#depth3-menu-list li').forEach(li => li.classList.remove('on'));

    // 실제 페이지 이동 (수동으로 수행)
    window.location.href = element.getAttribute('href');

    // 현재 클릭된 a 태그의 부모 li에 활성화 클래스 추가
    element.parentElement.classList.add('on');
}

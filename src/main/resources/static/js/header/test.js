// Depth3 메뉴를 가져오는 함수
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
        // 첫 번째 Depth3 메뉴를 기본 활성화 상태로 설정
        if (index === 0) {
            li.classList.add('on');
        }
        li.innerHTML = `<a href="${menu.url}" target="${menu.target}" onclick="handleDepth3Click(event, this)">${menu.name} </a>`;
        depth3MenuList.appendChild(li);
    });

    // 페이지 이동
    if (depth3Menus.length > 0) {
        // window.location.href = depth3Menus[0].url; // 첫 번째 메뉴로 이동
    }
}

function handleDepth3Click(event, element) {
    event.preventDefault(); // 기본 동작(페이지 이동) 막기

    // 기존 활성화된 상태 제거
    document.querySelectorAll('#depth3-menu-list li').forEach(li => li.classList.remove('on'));

    // 현재 클릭된 a 태그의 부모 li에 활성화 클래스 추가
    element.parentElement.classList.add('on');

    // 실제 페이지 이동 (수동으로 수행)
    window.location.href = element.getAttribute('href');
}

//
// // 초기화: Depth2 메뉴에 클릭 이벤트 추가
// document.addEventListener('DOMContentLoaded', () => {
//     document.querySelectorAll('.depth2-link').forEach(link => {
//         link.addEventListener('click', event => {
//             loadDepth3Menus(event, event.target);
//         });
//     });
// });

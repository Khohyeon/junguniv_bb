/**
 * 1차메뉴 추가!
 */
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('menuForm');

    if (form) { // menuForm이 존재하는지 확인
        form.addEventListener('submit', function (event) {
            event.preventDefault();

            const menuName = document.getElementById('menuName').value.trim();
            const menuGroup = document.body.getAttribute('data-menu-group');

            if (!menuName) {
                alert('추가할 메뉴명을 입력해주세요.');
                return;
            }

            const requestData = { menuName: menuName, menuGroup: menuGroup };

            fetch('/masterpage_sys/branch/api/save/depth1', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData),
            })
                .then((response) => {
                    if (response.ok) {
                        alert('1차 메뉴가 추가되었습니다.');
                        location.reload();
                    } else {
                        return response.text().then((err) => {
                            throw new Error(err);
                        });
                    }
                })
                .catch((error) => {
                    alert('메뉴 추가 중 오류가 발생했습니다: ' + error.message);
                });
        });
    } else {
        console.error('menuForm 요소를 찾을 수 없습니다.');
    }
});

/**
 * 2차메뉴 추가!
 */
document.addEventListener('DOMContentLoaded', function () {
    // 모든 2차 메뉴 폼을 선택
    document.querySelectorAll('.menuForm2').forEach((form) => {
        form.addEventListener('submit', function (event) {
            event.preventDefault(); // 기본 폼 제출 방지

            // 입력값 가져오기
            const menuName = this.querySelector('.menuName2').value.trim(); // 현재 폼 내의 input
            const parentIdx = this.querySelector('.parentIdx').value; // 현재 폼 내의 hidden input
            const menuGroup = document.body.getAttribute('data-menu-group');

            // 유효성 검사
            if (!menuName) {
                alert('추가할 2차 메뉴명을 입력해주세요.');
                return;
            }

            // 서버로 보낼 데이터 생성
            const requestData = {
                menuName: menuName,
                parentIdx: parentIdx,
                menuGroup: menuGroup,
            };

            // AJAX 요청
            fetch('/masterpage_sys/branch/api/save/depth2', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestData),
            })
                .then((response) => {
                    if (response.ok) {
                        alert('2차 메뉴가 추가되었습니다.');
                        location.reload(); // 페이지 새로고침
                    } else {
                        return response.text().then((err) => {
                            throw new Error(err);
                        });
                    }
                })
                .catch((error) => {
                    alert('2차 메뉴 추가 중 오류가 발생했습니다: ' + error.message);
                });
        });
    });
});

/**
 * 3차메뉴 추가!
 */
document.addEventListener('DOMContentLoaded', function () {
    const menuForm3 = document.getElementById('menuForm3');

    if (menuForm3) {
        menuForm3.addEventListener('submit', function (event) {
            event.preventDefault(); // 기본 폼 제출 방지

            // 입력값 가져오기
            const menuName = this.querySelector('.menuName3').value.trim(); // 현재 폼 내의 input
            const parentIdx = this.querySelector('#parentIdx').value;
            const menuGroup = document.body.getAttribute('data-menu-group');

            // 유효성 검사
            if (!menuName) {
                alert('추가할 3차 메뉴명을 입력해주세요.');
                return;
            }

            // 서버로 보낼 데이터 생성
            const requestData = {
                menuName: menuName,
                parentIdx: parentIdx,
                menuGroup: menuGroup
            };

            // AJAX 요청
            fetch('/masterpage_sys/branch/api/save/depth3', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestData),
            })
                .then((response) => {
                    if (response.ok) {
                        alert('3차 메뉴가 추가되었습니다.');
                        // 3차 메뉴 리스트 새로고침 (AJAX로 동적 렌더링 가능)
                        loadDepth3Menu(parentIdx); // 기존 함수를 사용하거나 구현 필요
                        this.querySelector('#3rdMenuName').value = ''; // 입력 필드 초기화
                    } else {
                        return response.text().then((err) => {
                            throw new Error(err);
                        });
                    }
                })
        });
    }
});

/**
 * 3차메뉴 모달 열기 (서버에서 데이터바인딩 후 렌더링)
 */
document.addEventListener('DOMContentLoaded', function () {
    // 모달 열기 및 데이터 로드
    window.loadDepth3Menu = function (menuIdx) {
        const modalElement = document.getElementById('depth2-setting');
        const menuListElement = document.getElementById('dp3');
        const parentIdxInput = document.getElementById('parentIdx'); // 숨겨진 input 필드

        // 기존 내용 초기화
        menuListElement.innerHTML = '';
        // 부모 ID 설정
        parentIdxInput.value = menuIdx;

        // AJAX 요청으로 3차 메뉴 가져오기
        fetch(`/masterpage_sys/branch/api/depth3Menus?parentIdx=${menuIdx}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('데이터를 가져오는 중 오류가 발생했습니다.');
                }
                return response.json();
            })
            .then(data => {
                if (data.length > 0) {
                    data.forEach(menu => {
                        // 각 3차 메뉴를 li로 추가
                        const li = document.createElement('li');
                        li.innerHTML = `
                           <div class="depth3-tit-wrap" data-menu-idx="${menu.menuIdx}">
                                    ${menu.menuName}
                                </div>
                                <a href="#none" class="jv-btn fill05-sm tit-del">삭제</a>
                            </div>
                        `;
                        menuListElement.appendChild(li);
                    });
                } else {
                    menuListElement.innerHTML = '<li>등록된 3차 메뉴가 없습니다.</li>';
                }
                modalElement.style.display = 'block'; // 모달 열기
            })
            .catch(error => {
                alert(error.message);
            });
    };

    // 모달 닫기
    document.querySelector('[rel="modal:close"]').addEventListener('click', function (event) {
        event.preventDefault();
        const modalElement = document.getElementById('depth2-setting');
        modalElement.style.display = 'none'; // 모달 닫기

        // 부모 페이지 리로드
        location.reload();
    });
});


/**
 * 삭제 버튼
 */
document.addEventListener('DOMContentLoaded', function () {
    // 1차, 2차 메뉴 삭제 이벤트 설정
    document.querySelectorAll('.tit-del').forEach((button) => {
        button.addEventListener('click', function () {
            handleDelete(this);
        });
    });

    // 모달 내 3차 메뉴 삭제 이벤트 (이벤트 위임 방식 사용)
    const modalMenuList = document.getElementById('dp3'); // 3차 메뉴 리스트

    modalMenuList.addEventListener('click', function (event) {
        // 삭제 버튼 클릭 확인
        if (event.target.classList.contains('tit-del')) {
            const menuElement = event.target.closest('li'); // li 요소 찾기
            const menuIdx = menuElement.querySelector('.depth3-tit-wrap').getAttribute('data-menu-idx');

            if (confirm('정말 삭제하시겠습니까?')) {
                fetch(`/masterpage_sys/branch/api/delete/${menuIdx}`, {
                    method: 'DELETE',
                })
                    .then((response) => {
                        if (!response.ok) {
                            const contentType = response.headers.get('content-type');
                            if (contentType && contentType.includes('application/json')) {
                                return response.json().then(errorData => {
                                    alert(errorData.error.message);
                                    throw new Error('서버에 오류가 발생했습니다.');
                                });
                            } else {
                                throw new Error('서버에 오류가 발생했습니다.');
                            }
                        }
                        return response.json();
                    })
                    .then(() => {
                        alert('메뉴가 삭제되었습니다.');
                        menuElement.remove(); // 삭제된 메뉴 항목 제거
                    })
            }
        }
    });
});

/**
 * 공통 삭제 처리 함수
 */
function handleDelete(button) {
    // 메뉴 타입에 따라 가장 가까운 메뉴 요소와 data-menu-idx 가져오기
    const menuElement = button.closest('.menu-depth1') || button.closest('li'); // 1차 또는 2차 메뉴 구분
    const menuIdx = menuElement.querySelector('[data-menu-idx]').getAttribute('data-menu-idx');

    if (confirm('정말 삭제하시겠습니까?')) {
        fetch(`/masterpage_sys/branch/api/delete/${menuIdx}`, {
            method: 'DELETE',
        })
            .then((response) => {
                if (!response.ok) {
                    const contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json().then(errorData => {
                            alert(errorData.error.message);
                            throw new Error('서버에 오류가 발생했습니다.');
                        });
                    } else {
                        throw new Error('서버에 오류가 발생했습니다.');
                    }
                }
                return response.json();
            })
            .then(() => {
                alert('메뉴가 삭제되었습니다.');
                location.reload(); // 페이지 새로고침
            })
    }
}

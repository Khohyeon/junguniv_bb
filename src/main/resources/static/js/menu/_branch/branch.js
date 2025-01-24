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
    const form2 = document.getElementById('menuForm2');

    if (form2) {
        form2.addEventListener('submit', function (event) {
            event.preventDefault(); // 기본 폼 제출 방지

            // 입력값 가져오기
            const menuName = document.getElementById('menuName2').value.trim();
            const parentIdx = document.getElementById('parentIdx').value; // 1차 메뉴 ID
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
                menuGroup: menuGroup
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
    } else {
        console.error('menuForm2 요소를 찾을 수 없습니다.');
    }
});

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

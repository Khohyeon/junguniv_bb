document.addEventListener('DOMContentLoaded', function () {
    const boardType = document.body.getAttribute('data-board-type') || 'NOTICE'; // 기본값은 'NOTICE'
    const urlType = document.body.getAttribute('data-url-type') || 'notice'; // body에 저장된 data-url-type 값
    const actionType = document.body.getAttribute('data-action-type') || 'update'; // actionType 기본값 'update'

    const form = document.getElementById(actionType+'Form');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 제출 방지

        const formData = new FormData(form); // 폼 데이터 생성

        formData.append('boardType', boardType);

        // 공통 URL 생성
        const apiUrl = `/masterpage_sys/board/api/${actionType}`;

        // 요청 보내기
        fetch(apiUrl, {
            method: actionType === 'update' ? 'PUT' : 'POST', // 'update'는 PUT, 나머지는 POST
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`${actionType.toUpperCase()} 실패`);
                }
                return response.json();
            })
            .then(data => {
                alert(data.response);
                window.location.href = '/masterpage_sys/board/' + urlType; // 성공 후 목록으로 이동
            })
            .catch(error => {
                console.error(`${actionType.toUpperCase()} 중 오류 발생:`, error);
                alert(`${actionType.toUpperCase()} 중 문제가 발생했습니다.`);
            });
    });
});

// 초기 카테고리 설정
document.addEventListener('DOMContentLoaded', function () {
    const boardType = document.body.getAttribute('data-board-type') || 'NOTICE'; // 기본값은 'NOTICE'
    const categoryElement = document.getElementById('category'); // 카테고리 select 요소

    if (!categoryElement) {
        // category ID가 없으면 아무 작업도 하지 않음
        return;
    }
    getCategory(boardType);
});

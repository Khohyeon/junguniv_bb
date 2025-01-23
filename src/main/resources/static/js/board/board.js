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
                    // 응답 헤더의 Content-Type이 JSON인지 확인
                    const contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        // JSON 응답을 파싱하여 에러 메시지를 추출
                        return response.json().then(errorData => {
                            console.log("errorData : "+ errorData);
                            alert(errorData.error.message);
                            throw new Error('서버에 오류가 발생했습니다.');
                        });
                    } else {
                        // JSON이 아닌 경우 일반 에러 메시지 표시
                        throw new Error('서버에 오류가 발생했습니다.');
                    }
                }
                return response.json();
            })
            .then(data => {
                alert(data.response);
                window.location.href = '/masterpage_sys/board/' + urlType; // 성공 후 목록으로 이동
            })
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

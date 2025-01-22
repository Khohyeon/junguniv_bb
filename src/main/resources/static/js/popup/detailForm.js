document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('popupForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault(); // 기본 폼 제출 동작 방지

        const formData = new FormData(form);

        fetch(form.action, {
            method: 'PUT',
            body: formData
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
                // 성공 응답 처리
                alert('팝업이 성공적으로 수정되었습니다.');
                window.location.href = '/masterpage_sys/popup'; // 성공 시 리스트 페이지로 이동
            })

    });
});

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('counselForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault(); // 기본 폼 제출 동작 방지

        const formData = new FormData(form);

        fetch(form.action, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text); // 에러 메시지 처리
                    });
                }
                return response.json();
            })
            .then(data => {
                // 성공 응답 처리
                alert('상담예약이 성공적으로 저장되었습니다.');
                window.location.href = '/masterpage_sys/counsel/listForm'; // 성공 시 리스트 페이지로 이동
            })
            .catch(error => {
                // 에러 응답 처리
                alert('서버 오류 발생: ' + error.message);
                console.error('Error:', error);
            });
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('updateForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 제출 방지

        const formData = new FormData(form); // 폼 데이터 생성
        const boardType = 'Q&A'; // 게시판 타입 추가

        formData.append('boardType', boardType);

        // 업데이트 요청 보내기 (bbsIdx 포함)
        fetch(`/masterpage_sys/board/api/reply`, {
            method: 'PUT', // 서버에서 지원하는 메서드에 따라 'PUT' 또는 'POST'
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('업데이트 실패');
                }
                return response.json();
            })
            .then(data => {
                alert(data.response);
                window.location.href = '/masterpage_sys/board/qna'; // 성공 후 목록으로 이동
            })
            .catch(error => {
                console.error('업데이트 중 오류 발생:', error);
                alert('업데이트 중 문제가 발생했습니다.');
            });
    });
});

document.addEventListener('DOMContentLoaded', function () {
    getCategory('Q&A')
});
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('saveForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 제출 방지

        const formData = new FormData(form); // 폼 데이터 생성
        const boardType = 'SUGGESTION';
        formData.append('boardType', boardType);

        fetch('/masterpage_sys/board/api/save', {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('저장 실패');
                }
                return response.json();
            })
            .then(data => {
                alert(data.response);
                window.location.href = '/masterpage_sys/board/suggestion'; // 성공 후 목록으로 이동
            })
            .catch(error => {
                console.error('저장 중 오류 발생:', error);
                alert('저장 중 문제가 발생했습니다.');
            });
    });
});

document.addEventListener('DOMContentLoaded', function () {
    getCategory('SUGGESTION')
});
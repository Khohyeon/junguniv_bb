document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.form-wrap2');
    const saveButton = document.querySelector('.jv-btn.fill04-lg');


    saveButton.addEventListener('click', function (event) {
        event.preventDefault(); // 기본 제출 방지

        const formData = new FormData(form); // 폼 데이터 생성
        const boardType = 'MATERIAL';
        const editorContent = document.getElementById('editor').value; // 에디터 내용 추가
        formData.append('contents', editorContent);
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
                alert('저장 성공!');
                window.location.href = '/masterpage_sys/board/data/listForm'; // 성공 후 목록으로 이동
            })
            .catch(error => {
                console.error('저장 중 오류 발생:', error);
                alert('저장 중 문제가 발생했습니다.');
            });
    });
});
document.addEventListener('DOMContentLoaded', function () {
    getCategory('MATERIAL')
});
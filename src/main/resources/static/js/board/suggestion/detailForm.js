document.addEventListener('DOMContentLoaded', () => {
    // 수정 버튼
    const updateButton = document.getElementById('updateButton');
    const listButton = document.getElementById('listButton');
    const deleteButton = document.getElementById('deleteButton');

    let url = `/masterpage_sys/board/suggestion`;
    let redirectUrl = url+`/listForm`;

    // 수정 버튼 href 설정
    if (updateButton) {
        const boardId = updateButton.getAttribute('data-id'); // ID 추출
        const updateUrl = url+`/updateForm/${boardId}`; // 동적 URL 생성
        updateButton.setAttribute('href', updateUrl); // href 속성 동적으로 설정
    }

    // 목록 버튼 클릭 이벤트
    if (listButton) {
        listButton.addEventListener('click', () => {
            window.location.href = redirectUrl; // 목록 URL
        });
    }

    // 삭제 버튼 클릭 이벤트
    if (deleteButton) {
        const boardId = updateButton.getAttribute('data-id'); // ID 추출
        deleteButton.addEventListener('click', () => {
            deleteSelectedBoard(boardId, redirectUrl);
        });
    }
});



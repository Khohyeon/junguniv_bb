document.addEventListener('DOMContentLoaded', () => {
    const commentButton = document.getElementById('commentButton');
    const listButton = document.getElementById('listButton');
    const deleteButton = document.getElementById('deleteButton');

    let url = `/masterpage_sys/board/consulting`;

    // 답변보내기 버튼 href 설정
    if (commentButton) {
        const boardId = commentButton.getAttribute('data-id'); // ID 추출
        const updateUrl = url+`/update/${boardId}`; // 동적 URL 생성
        commentButton.setAttribute('href', updateUrl); // href 속성 동적으로 설정
    }

    // 목록 버튼 클릭 이벤트
    if (listButton) {
        listButton.addEventListener('click', () => {
            window.location.href = url; // 목록 URL
        });
    }

    // 삭제 버튼 클릭 이벤트
    if (deleteButton) {
        const boardId = deleteButton.getAttribute('data-id'); // ID 추출
        deleteButton.addEventListener('click', () => {
            deleteSelectedBoard(boardId, url);
        });
    }
});



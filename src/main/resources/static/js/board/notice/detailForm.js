document.addEventListener('DOMContentLoaded', () => {
    // 수정 버튼
    const updateButton = document.getElementById('updateButton');
    const listButton = document.getElementById('listButton');
    const deleteButton = document.getElementById('deleteButton');
    const replyButton = document.getElementById('replyButton');
    const commentButton = document.getElementById('commentButton');

    let url = `/masterpage_sys/board/notice`;
    const boardId = updateButton.getAttribute('data-id'); // ID 추출

    const commentList = document.querySelector('.com-list');
    if (commentList) {
        commentList.addEventListener('click', event => {
            if (event.target.classList.contains('delete')) {
                const commentIdx = event.target.getAttribute('data-id');
                delCommentItem(commentIdx);
            }
        });
    }

    // 수정 버튼 href 설정
    if (updateButton) {
        const updateUrl = url+`/update/${boardId}`; // 동적 URL 생성
        updateButton.setAttribute('href', updateUrl); // href 속성 동적으로 설정
    }

    // 답변 버튼 href 설정
    if (replyButton) {
        const replyUrl = url+`/reply/${boardId}`; // 동적 URL 생성
        replyButton.setAttribute('href', replyUrl); // href 속성 동적으로 설정
    }

    // 목록 버튼 클릭 이벤트
    if (listButton) {
        listButton.addEventListener('click', () => {
            window.location.href = url; // 목록 URL
        });
    }

    // 삭제 버튼 클릭 이벤트
    if (deleteButton) {
        deleteButton.addEventListener('click', () => {
            deleteSelectedBoard(boardId, url);
        });
    }

    // 댓글등록 버튼 클릭 이벤트
    if (commentButton) {
        commentButton.addEventListener('click', () => {
            commentSaveBoard(boardId);
        });
    }
});




document.addEventListener('DOMContentLoaded', () => {
    // 동적 URL의 type 추출 (예: 'notice', 'faq', 'qna')
    const urlType = document.body.getAttribute('data-url-type') || 'notice'; // body에 저장된 data-url-type 값
    let url = `/masterpage_sys/board/${urlType}`;

    // 공통 버튼
    const updateButton = document.getElementById('updateButton');
    const listButton = document.getElementById('listButton');
    const deleteButton = document.getElementById('deleteButton');
    const replyButton = document.getElementById('replyButton');
    const commentButton = document.getElementById('commentButton');

    const boardId = updateButton ? updateButton.getAttribute('data-id') : null; // ID 추출

    // 댓글 리스트 이벤트
    const commentList = document.querySelector('.com-list');
    if (commentList) {
        commentList.addEventListener('click', event => {
            if (event.target.classList.contains('delete')) {
                const commentIdx = event.target.getAttribute('data-id');
                delCommentItem(commentIdx);
            }
        });
    }

    // 동적 href 설정
    if (updateButton) {
        const updateUrl = `${url}/update/${boardId}`;
        updateButton.setAttribute('href', updateUrl);
    }

    if (replyButton) {
        const replyUrl = `${url}/reply/${boardId}`;
        replyButton.setAttribute('href', replyUrl);
    }

    // 목록 버튼 클릭 이벤트
    if (listButton) {
        listButton.addEventListener('click', () => {
            window.location.href = url;
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

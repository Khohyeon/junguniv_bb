/**
 * 체크박스 항목의 popupIdx 가지고 서버로 delete 요청
 */
document.addEventListener('DOMContentLoaded', function () {

// 전체 삭제 버튼 클릭 이벤트 처리
    const deleteBoardButton = document.querySelector('.delete-board-btn');

    if (deleteBoardButton) {
        deleteBoardButton.addEventListener('click', function (event) {
            event.preventDefault();

            // 선택된 체크박스 확인
            const checkedBoxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
            if (checkedBoxes.length === 0) {
                alert('삭제할 항목을 선택해주세요.');
                return;
            }

            // 선택된 bbsIdx 수집
            const boardIds = Array.from(checkedBoxes).map(checkbox => checkbox.value);

            // 삭제 확인
            if (!confirm('선택한 항목을 삭제하시겠습니까?')) {
                return;
            }

            // 서버로 DELETE 요청
            deleteSelectedBoards(boardIds);
        });
    }
});

/**
 * 서버로 Delete 요청
 */
function deleteSelectedBoards(boardIds) {
    fetch('/masterpage_sys/board/api/deletes', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(boardIds) // 선택된 popupIdx 배열을 전송
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data.message || '삭제에 실패했습니다.');
                });
            }
            alert('선택된 항목이 삭제되었습니다.');
            window.location.reload(); // 삭제 후 페이지 새로고침
        })
        .catch(error => {
            alert('오류가 발생했습니다: ' + error.message);
            console.error('Error:', error);
        });
}

function deleteSelectedBoard(boardId, redirectUrl) {
    // 사용자 확인
    if (confirm('정말 삭제하시겠습니까?')) {
        fetch('/masterpage_sys/board/api/delete', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(boardId) // 객체 형태로 감싸서 전송
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
                alert(data.response); // 성공 메시지 표시
                window.location.href = redirectUrl; // 성공 후 목록으로 이동
            })
    } else {
        alert('삭제가 취소되었습니다.'); // 사용자가 취소한 경우
    }
}

function commentSaveBoard(bbsIdx) {

    // 댓글 내용 가져오기
    const commentTextarea = document.getElementById('contents');
    const comment = commentTextarea.value.trim();

    if (!comment) {
        alert('댓글을 작성해주세요.');
        commentTextarea.focus();
        return;
    }

    // 서버로 보낼 데이터 준비
    const formData = new FormData();
    formData.append('contents', comment);
    formData.append('bbsIdx', bbsIdx);

    // 댓글 작성 요청
    fetch('/masterpage_sys/board/api/comment', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('등록 실패');
            }
            return response.json();
        })
        .then(data => {
            alert(data.response); // 성공 메시지 표시
            window.location.reload();
        })
        .catch(error => {
            alert('오류가 발생했습니다: ' + error.message);
            console.error('Error:', error);
        });
}

function delCommentItem(commentIdx) {
    if (confirm('댓글을 삭제하시겠습니까?')) {
        fetch(`/masterpage_sys/board/api/comment/delete/${commentIdx}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) throw new Error('삭제 실패');
                return response.json();
            })
            .then(data => {
                alert('댓글이 삭제되었습니다.');
                location.reload(); // 페이지 새로고침
            })
            .catch(error => {
                alert('댓글 삭제 중 오류가 발생했습니다.');
                console.error(error);
            });
    }
}


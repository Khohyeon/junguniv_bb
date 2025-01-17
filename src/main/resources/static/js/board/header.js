

/**
 * 체크박스 항목의 popupIdx 가지고 서버로 delete 요청
 */
document.addEventListener('DOMContentLoaded', function () {
    // 전체 삭제 버튼 클릭 이벤트 처리
    document.querySelector('.delete-board-btn').addEventListener('click', function (event) {
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
    fetch('/masterpage_sys/board/api/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(boardId) // 객체 형태로 감싸서 전송
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('삭제 실패');
            }
            return response.json();
        })
        .then(data => {
            alert(data.response);
            window.location.href = redirectUrl; // 성공 후 목록으로 이동
        })
        .catch(error => {
            alert('오류가 발생했습니다: ' + error.message);
            console.error('Error:', error);
        });
}

/**
 * 페이지 로드 시 게시판에 있는 카테고리를 select - option 에 ,로 구분하여 넣어주는 함수
 */
function getCategory(boardType) {
    const categorySelect = document.getElementById('category');
    // 카테고리 데이터를 서버에서 가져오기
    fetch(`/masterpage_sys/board/api/categories?boardType=${boardType}`)
        .then(response => response.json())
        .then(data => {
            const categories = data.response.split(','); // ,로 구분하여 배열로 변환
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.trim();
                option.textContent = category.trim();
                categorySelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading categories:', error));
}

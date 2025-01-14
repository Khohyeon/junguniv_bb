/**
 * 체크박스 항목의 popupIdx 가지고 서버로 delete 요청
 */
document.addEventListener('DOMContentLoaded', function () {
    // 전체 삭제 버튼 클릭 이벤트 처리
    document.querySelector('.delete-counsel-btn').addEventListener('click', function (event) {
        event.preventDefault();

        // 선택된 체크박스 확인
        const checkedBoxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
        if (checkedBoxes.length === 0) {
            alert('삭제할 항목을 선택해주세요.');
            return;
        }

        // 선택된 counselIdx 수집
        const counselIds = Array.from(checkedBoxes).map(checkbox => checkbox.value);

        // 삭제 확인
        if (!confirm('선택한 항목을 삭제하시겠습니까?')) {
            return;
        }

        // 서버로 DELETE 요청
        deleteSelectedCounsels(counselIds);
    });
});

/**
 * 서버로 Delete 요청
 */
function deleteSelectedCounsels(counselIds) {
    fetch('/masterpage_sys/counsel/api/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(counselIds) // 선택된 popupIdx 배열을 전송
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

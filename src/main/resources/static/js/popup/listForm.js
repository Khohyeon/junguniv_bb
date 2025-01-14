/**
 * 상위 체크박스 선택 시 하위 체크박스 일괄 선택
 */
document.addEventListener('DOMContentLoaded', function () {
    const selectAllCheckbox = document.querySelector('.select-all-checkbox'); // 전체 선택 체크박스
    const itemCheckboxes = document.querySelectorAll('.select-item-checkbox'); // 개별 체크박스

    // 전체 선택 체크박스 클릭 이벤트
    selectAllCheckbox.addEventListener('change', function () {
        const isChecked = selectAllCheckbox.checked;

        // 모든 개별 체크박스 상태를 전체 선택 체크박스 상태로 변경
        itemCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 개별 체크박스 상태 변경 시 전체 선택 체크박스 상태 동기화
    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            const allChecked = Array.from(itemCheckboxes).every(cb => cb.checked);
            const noneChecked = Array.from(itemCheckboxes).every(cb => !cb.checked);

            // 모든 항목이 선택되면 전체 선택 체크박스도 체크
            if (allChecked) {
                selectAllCheckbox.checked = true;
                selectAllCheckbox.indeterminate = false;
            }
            // 아무 것도 선택되지 않으면 전체 선택 체크박스를 해제
            else if (noneChecked) {
                selectAllCheckbox.checked = false;
                selectAllCheckbox.indeterminate = false;
            }
            // 일부만 선택된 경우 전체 선택 체크박스를 indeterminate 상태로 설정
            else {
                selectAllCheckbox.checked = false;
                selectAllCheckbox.indeterminate = true;
            }
        });
    });
});

/**
 * 체크박스 항목의 popupIdx 가지고 서버로 delete 요청
 */
document.addEventListener('DOMContentLoaded', function () {
    // 전체 삭제 버튼 클릭 이벤트 처리
    document.querySelector('.delete-popup-btn').addEventListener('click', function (event) {
        event.preventDefault();

        // 선택된 체크박스 확인
        const checkedBoxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
        if (checkedBoxes.length === 0) {
            alert('삭제할 항목을 선택해주세요.');
            return;
        }

        // 선택된 popupIdx 수집
        const popupIds = Array.from(checkedBoxes).map(checkbox => checkbox.value);

        // 삭제 확인
        if (!confirm('선택한 항목을 삭제하시겠습니까?')) {
            return;
        }

        // 서버로 DELETE 요청
        deleteSelectedPopups(popupIds);
    });
});

/**
 * 서버로 Delete 요청
 */
function deleteSelectedPopups(popupIds) {
    fetch('/masterpage_sys/popup/api/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(popupIds) // 선택된 popupIdx 배열을 전송
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


/**
 * 검색 기능
 */
document.addEventListener('DOMContentLoaded', function () {
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageDisplay = document.getElementById('currentPage');
    const tableBody = document.getElementById('popupTableBody');
    const popupNameInput = document.getElementById('popupNameInput');
    const searchButton = document.getElementById('searchButton');

    let currentPage = 0; // 현재 페이지 번호
    const pageSize = 20; // 페이지 크기

    // 초기 검색 실행
    searchPopups('', currentPage);

    // 검색 버튼 클릭
    searchButton.addEventListener('click', function () {
        const keyword = popupNameInput.value.trim();
        currentPage = 0; // 검색 시 항상 첫 페이지로 이동
        searchPopups(keyword, currentPage);
    });

    // 이전 페이지 버튼
    prevPageBtn.addEventListener('click', function () {
        if (currentPage > 0) {
            currentPage--;
            searchPopups(popupNameInput.value.trim(), currentPage);
        }
    });

    // 다음 페이지 버튼
    nextPageBtn.addEventListener('click', function () {
        currentPage++;
        searchPopups(popupNameInput.value.trim(), currentPage);
    });

    // 검색 요청
    function searchPopups(keyword, page) {
        fetch(`/masterpage_sys/popup/api/search?popupName=${encodeURIComponent(keyword)}&page=${page}&size=${pageSize}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('검색 실패');
                }
                return response.json();
            })
            .then(data => {
                renderSearchResults(data.response.content);
                updatePagination(data.response);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('검색 중 오류가 발생했습니다.');
            });
    }

    // 검색 결과 렌더링
    function renderSearchResults(content) {
        tableBody.innerHTML = '';

        if (content.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="6">검색 결과가 없습니다.</td></tr>';
            return;
        }

        content.forEach((popup, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${popup.popupName}</td>
                <td>${popup.updateDate || '-'}</td>
                <td>${popup.startDate} ~ ${popup.endDate}</td>
                <td>${popup.popupType}</td>
                <td>${popup.chkOpen}</td>
            `;
            tableBody.appendChild(row);
        });
    }

    // 페이지네이션 업데이트
    function updatePagination(pageable) {
        currentPage = pageable.number;
        currentPageDisplay.textContent = currentPage + 1;

        // 버튼 활성화/비활성화 설정
        prevPageBtn.disabled = pageable.first;
        nextPageBtn.disabled = pageable.last;
    }
});

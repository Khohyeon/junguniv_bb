

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
    const tableBody = document.getElementById('tableBody');
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
                renderSearchResults(data.response.content, data.response.totalElements);
                updatePagination(data.response);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('검색 중 오류가 발생했습니다.');
            });
    }

    // 검색 결과 렌더링
    function renderSearchResults(content, totalElements) {

        // 총 검색 결과 수를 렌더링
        const countDiv = document.querySelector('.count .Pretd_B');
        if (countDiv) {
            countDiv.textContent = totalElements; // 총 검색 결과 수를 설정
        }

        tableBody.innerHTML = ''; // 기존 데이터 초기화

        if (content.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="7">검색 결과가 없습니다.</td></tr>';
            return;
        }

        content.forEach((popup) => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <!-- 개별 선택 체크박스 -->
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" class="select-item-checkbox" value="${popup.popupIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>
            <!-- 팝업 ID -->
            <td>${popup.popupIdx}</td>
            <!-- 팝업 이름 -->
            <td>
                <a href="/masterpage_sys/popup/detailForm/${popup.popupIdx}" 
                   class="jv-btn underline01">${popup.popupName}</a>
            </td>
            <!-- 수정일 -->
            <td>${popup.updateDate || '-'}</td>
            <!-- 공개 기간 -->
            <td>${popup.startDate} ~ ${popup.endDate}</td>
            <!-- 팝업 종류 -->
            
            <!-- 공개 여부 -->
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

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

/**
 * 검색 및 페이지네이션 기능
 */
document.addEventListener('DOMContentLoaded', function () {
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageDisplay = document.getElementById('currentPage');
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');
    const counselStateRadios = document.querySelectorAll('input[name="counsel"]');

    let currentPage = 0; // 현재 페이지 번호
    const pageSize = 20; // 페이지 크기

    // 초기 검색 실행
    search('', currentPage, '0');

    // 검색 버튼 클릭
    searchButton.addEventListener('click', function () {
        const keyword = searchInput.value.trim();
        const selectedCounselState = getSelectedCounselState();
        currentPage = 0; // 검색 시 항상 첫 페이지로 이동
        search(keyword, currentPage, selectedCounselState);
    });

    // 이전 페이지 버튼 클릭
    prevPageBtn.addEventListener('click', function () {
        if (currentPage > 0) {
            currentPage--;
            const keyword = searchInput.value.trim();
            const selectedCounselState = getSelectedCounselState();
            search(keyword, currentPage, selectedCounselState);
        }
    });

    // 다음 페이지 버튼 클릭
    nextPageBtn.addEventListener('click', function () {
        currentPage++;
        const keyword = searchInput.value.trim();
        const selectedCounselState = getSelectedCounselState();
        search(keyword, currentPage, selectedCounselState);
    });

    // 검색 요청
    function search(keyword, page, counselState) {
        const url = new URL('/masterpage_sys/counsel/api/search', window.location.origin);
        url.searchParams.set('counselName', keyword);
        url.searchParams.set('counselState', counselState);
        url.searchParams.set('page', page);
        url.searchParams.set('size', pageSize);

        fetch(url.toString())
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
                console.error('검색 중 오류 발생:', error);
                displayErrorMessage('검색 중 문제가 발생했습니다. 다시 시도해주세요.');
            });
    }

    // 선택된 상담 상태 반환
    function getSelectedCounselState() {
        let selectedState = '0';
        counselStateRadios.forEach(radio => {
            if (radio.checked) {
                selectedState = radio.value; // value="0", "1", "2" 사용
            }
        });
        return selectedState;
    }


// 검색 결과 렌더링
function renderSearchResults(data, totalElements) {
    // 총 검색 결과 수를 렌더링
    const countDiv = document.querySelector('.count .Pretd_B');
    if (countDiv) {
        countDiv.textContent = totalElements; // 총 검색 결과 수를 설정
    }
        const tableBody = document.getElementById('tableBody');
        tableBody.innerHTML = ''; // 기존 데이터 초기화

        if (!data || data.length === 0) {
            tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="no-results">조회된 결과가 없습니다.</td>
            </tr>
            `;
            return;
        }

        data.forEach((item, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <!-- 체크박스 -->
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" class="select-item-checkbox" value="${item.counselIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>

            <!-- NO -->
            <td>${index + 1}</td>

            <!-- 제목 -->
            <td>${item.counselName || '제목 없음'}</td>

            <!-- 신청일 -->
            <td>${item.createdDate || '-'}</td>

            <!-- 상담자명 -->
            <td>${item.name || '-'}</td>

            <!-- 상담상태 -->
            <td>
                <a href="/masterpage_sys/counsel/${item.counselIdx}" class="jv-btn ${item.counselState === 1 ? 'label01' : 'label07'}">
                    ${item.counselState === 1 ? '상담대기' : '상담완료'}
                </a>
            </td>

            <!-- 최종 수정 시간 -->
            <td>${item.updateDate || '-'}</td>
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

    // 오류 메시지 표시
    function displayErrorMessage(message) {
        const errorDiv = document.querySelector('.error-message');
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';

            setTimeout(() => {
                errorDiv.style.display = 'none';
            }, 3000); // 3초 후 오류 메시지 숨김
        }
    }
});

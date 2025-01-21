/**
 * 검색 기능
 */
document.addEventListener('DOMContentLoaded', function () {
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageDisplay = document.getElementById('currentPage');
    const searchTypeSelect = document.getElementById('searchType'); // 제목 검색 방식
    const searchInput = document.getElementById('searchInput'); // 검색어
    const startDateInput = document.getElementById('startDate'); // 작성일 시작
    const endDateInput = document.getElementById('endDate'); // 작성일 종료
    const categorySelect = document.getElementById('category'); // 카테고리
    const searchButton = document.getElementById('searchButton');
    const tableBody = document.getElementById('tableBody');

    let currentPage = 0; // 현재 페이지 번호
    const pageSize = 20; // 페이지 크기

    // 초기 검색 실행
    search('', currentPage);

    // 검색 버튼 클릭
    searchButton.addEventListener('click', function () {
        const keyword = searchInput.value.trim();
        currentPage = 0; // 검색 시 항상 첫 페이지로 이동
        search(keyword, currentPage);
    });

    // 이전 페이지 버튼
    prevPageBtn.addEventListener('click', function () {
        if (currentPage > 0) {
            currentPage--;
            search(searchInput.value.trim(), currentPage);
        }
    });

    // 다음 페이지 버튼
    nextPageBtn.addEventListener('click', function () {
        currentPage++;
        search(searchInput.value.trim(), currentPage);
    });

    // 검색 요청
    function search(keyword, page) {
        const url = new URL('/masterpage_sys/board/api/search', window.location.origin);
        url.searchParams.set('title', keyword); // 검색어
        url.searchParams.set('boardType', 'MATERIAL'); // 고정된 게시판 타입
        url.searchParams.set('page', page);
        url.searchParams.set('size', pageSize);

        // 추가 검색 조건
        const searchType = searchTypeSelect.value; // 제목 검색 방식
        const startDate = startDateInput.value; // 작성일 시작
        const endDate = endDateInput.value; // 작성일 종료
        const category = categorySelect ? categorySelect.value : ''; // 카테고리 (조건부)

        if (searchType) {
            url.searchParams.set('searchType', searchType);
        }
        if (startDate) {
            url.searchParams.set('startDate', startDate);
        }
        if (endDate) {
            url.searchParams.set('endDate', endDate);
        }
        if (category) {
            url.searchParams.set('category', category);
        }

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
            });
    }

    // 검색 결과 렌더링
    function renderSearchResults(data, totalElements) {
        // 총 검색 결과 수를 렌더링
        const countDiv = document.querySelector('.count .Pretd_B');
        if (countDiv) {
            countDiv.textContent = totalElements; // 총 검색 결과 수를 설정
        }

        // 메시지 작성 버튼에 href 설정
        const saveForm = document.querySelector('.right .jv-btn');
        if (saveForm) {
            const dynamicUrl = '/masterpage_sys/board/data/save'; // 동적으로 설정할 URL
            saveForm.setAttribute('href', dynamicUrl); // href 속성 설정
        }

        tableBody.innerHTML = ''; // 기존 데이터 초기화

        if (!data || data.length === 0) {
            tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="no-results">조회된 결과가 없습니다.</td>
            </tr>
        `;
            return;
        }

        // 공지 사항을 상단으로 정렬
        data.sort((a, b) => b.chkTopFix.localeCompare(a.chkTopFix)); // 'Y'를 상단으로 정렬

        data.forEach((item, index) => {
            const row = document.createElement('tr');

            // 공지인 경우 추가 클래스 적용
            const rowClass = item.chkTopFix === 'Y' ? 'notice-row' : '';

            row.innerHTML = `
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" class="select-item-checkbox" value="${item.bbsIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>
            <td>${item.chkTopFix === 'Y' ? '공지' : index + 1}</td>
            <td>
                <a href="/masterpage_sys/board/example/${item.bbsIdx}" class="jv-btn underline01">${item.title || '제목 없음'}</a>
            </td>
            <td>${item.createdDate || '-'}</td>
            <td><a href="/masterpage_sys/board/example/${item.bbsIdx}" class="jv-btn fill04">수정하기</a></td>
            <td>${item.readNum || 0}</td>
        `;
            // 공지 행에 클래스 추가
            if (rowClass) {
                row.classList.add(rowClass);
            }
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

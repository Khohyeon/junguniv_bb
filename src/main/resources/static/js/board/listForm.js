/**
 * 검색 및 페이지네이션 기능 (공통화)
 */
document.addEventListener('DOMContentLoaded', function () {
    // 동적 boardType 설정 (예: 'notice', 'faq', 'qna')
    const boardType = document.body.getAttribute('data-board-type') || 'NOTICE'; // 기본값은 'notice'
    const urlType = document.body.getAttribute('data-url-type') || 'notice'; // body에 저장된 data-url-type 값

    // DOM 요소
    const pageSelect = document.getElementById('pageSelect');
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageDisplay = document.getElementById('currentPage');
    const searchTypeSelect = document.getElementById('searchType');
    const searchInput = document.getElementById('searchInput');
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const categorySelect = document.getElementById('category');
    const searchButton = document.getElementById('searchButton');
    const tableBody = document.getElementById('tableBody');


    // 검색 상태 관리
    let searchState = {
        keyword: '',
        searchType: '',
        startDate: '',
        endDate: '',
        category: '',
        currentPage: 0,
        pageSize: 20
    };

    // 초기 검색 실행
    performSearch();

    // 검색 버튼 클릭
    searchButton.addEventListener('click', function () {
        searchState.keyword = searchInput.value.trim();
        searchState.searchType = searchTypeSelect.value;
        searchState.startDate = startDateInput.value;
        searchState.endDate = endDateInput.value;
        searchState.category = categorySelect ? categorySelect.value : '';
        searchState.currentPage = 0; // 검색 시 첫 페이지로 이동
        performSearch();
    });

    // 이전 페이지 버튼
    prevPageBtn.addEventListener('click', function () {
        if (searchState.currentPage > 0) {
            searchState.currentPage--;
            updatePageSelect();
            performSearch();
        }
    });

    // 다음 페이지 버튼
    nextPageBtn.addEventListener('click', function () {
        searchState.currentPage++;
        updatePageSelect();
        performSearch();
    });

    // 페이지 선택 드롭다운
    pageSelect.addEventListener('change', function () {
        searchState.currentPage = parseInt(this.value, 10);
        performSearch();
    });

    // 검색 요청
    function performSearch() {
        const url = createSearchUrl();

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

    // 검색 URL 생성
    function createSearchUrl() {
        const url = new URL(`/masterpage_sys/board/api/search`, window.location.origin);
        url.searchParams.set('title', searchState.keyword);
        url.searchParams.set('boardType', boardType); // 동적 boardType
        url.searchParams.set('page', searchState.currentPage);
        url.searchParams.set('size', searchState.pageSize);

        if (searchState.searchType) {
            url.searchParams.set('searchType', searchState.searchType);
        }
        if (searchState.startDate) {
            url.searchParams.set('startDate', searchState.startDate);
        }
        if (searchState.endDate) {
            url.searchParams.set('endDate', searchState.endDate);
        }
        if (searchState.category) {
            url.searchParams.set('category', searchState.category);
        }

        return url;
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
            const dynamicUrl = '/masterpage_sys/board/'+urlType+'/save'; // 동적으로 설정할 URL
            saveForm.setAttribute('href', dynamicUrl); // href 속성 설정
        }

        tableBody.innerHTML = '';

        if (!data || data.length === 0) {
            tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="no-results">조회된 결과가 없습니다.</td>
            </tr>`;
            return;
        }

        data.forEach((item, index) => {
            const row = document.createElement('tr');


            // 공지인 경우 추가 클래스 적용
            const rowClass = item.chkTopFix === 'Y' ? 'notice-row' : '';

            // 비밀글 표시
            const secretLabel = item.pwd ? `<span class="secret jv-btn jv-label03-sm">비밀글</span>` : '';

            // 답글 표시
            const replyLabel = item.parentBbsIdx ? `<span class="jv-btn label05-sm">Re</span>` : '';

            // NEW 표시
            const newLabel = item.isNew ? `<span class="jv-btn label04-sm">NEW</span>` : '';

            // 제목과 댓글 수
            const commentCountLabel = item.commentCount > 0 ? `[${item.commentCount}]` : '';
            const title = item.title || '제목 없음';
            const titleWithCount = `${title} ${commentCountLabel}`;

            row.innerHTML = `
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" class="select-item-checkbox" value="${item.bbsIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>
            <td>${item.chkTopFix === 'Y' ? '공지' : index + 1}</td>
            <td>
                ${secretLabel}
                ${replyLabel}
                <a href="/masterpage_sys/board/${urlType}/${item.bbsIdx}" class="tit Pretd_SB"> ${titleWithCount} </a>
                ${newLabel}
            </td>
            <td>${item.createdDate || '-'}</td>
            <td><a href="/masterpage_sys/board/${urlType}/${item.bbsIdx}" class="jv-btn fill04">수정하기</a></td>
            <td>${item.readNum || 0}</td>
            `;

            if (rowClass) {
                row.classList.add(rowClass);
            }

            tableBody.appendChild(row);
        });
    }

    // 페이지네이션 업데이트
    function updatePagination(response) {
        const totalPages = response.totalPages;

        pageSelect.innerHTML = '';
        for (let i = 0; i < totalPages; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = i + 1;
            if (i === searchState.currentPage) {
                option.selected = true;
            }
            pageSelect.appendChild(option);
        }

        currentPageDisplay.textContent = response.pageable.pageNumber + 1;
        prevPageBtn.disabled = response.pageable.pageNumber === 0;
        nextPageBtn.disabled = response.pageable.pageNumber + 1 >= response.totalPages;
    }

    function updatePageSelect() {
        pageSelect.value = searchState.currentPage;
    }
});


// 초기 카테고리 설정
document.addEventListener('DOMContentLoaded', function () {
    const boardType = document.body.getAttribute('data-board-type') || 'NOTICE'; // 기본값은 'NOTICE'
    const categoryElement = document.getElementById('category'); // 카테고리 select 요소

    if (!categoryElement) {
        // category ID가 없으면 아무 작업도 하지 않음
        return;
    }
    getCategory(boardType);
});
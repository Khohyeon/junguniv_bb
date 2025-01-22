/**
 * 검색 기능
 */
document.addEventListener('DOMContentLoaded', function () {
    const pageSelect = document.getElementById('pageSelect'); // 페이지 선택 드롭다운
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageDisplay = document.getElementById('currentPage');
    const searchTypeSelect = document.getElementById('searchType'); // 제목 검색 방식
    const searchInput = document.getElementById('searchInput'); // 검색어
    const startDateInput = document.getElementById('startDate'); // 작성일 시작
    const endDateInput = document.getElementById('endDate'); // 작성일 종료
    const categorySelect = document.getElementById('category'); // 카테고리
    const searchButton = document.getElementById('searchButton');

    let searchState = {
        keyword: '',
        searchType: '',
        startDate: '',
        endDate: '',
        category: '',
        currentPage: 0, // 현재 페이지 번호
        pageSize: 20 // 페이지 크기
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
            updatePageSelect(); // 드롭다운 상태 업데이트
            performSearch();
        }
    });

    // 다음 페이지 버튼
    nextPageBtn.addEventListener('click', function () {
        searchState.currentPage++;
        updatePageSelect(); // 드롭다운 상태 업데이트
        performSearch();
    });

    // 페이지 선택 드롭다운 변경
    pageSelect.addEventListener('change', function () {
        searchState.currentPage = parseInt(this.value, 10); // 선택된 페이지 번호로 설정
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
        const url = new URL('/masterpage_sys/board/api/search', window.location.origin);
        url.searchParams.set('title', searchState.keyword);
        url.searchParams.set('boardType', 'NOTICE'); // 고정된 게시판 타입
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
            const dynamicUrl = '/masterpage_sys/board/notice/save'; // 동적으로 설정할 URL
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

            // 비밀글 표시
            const secretLabel = item.pwd ? `<span class="secret jv-btn jv-label03-sm">비밀글</span>` : '';

            // 답글 표시
            const replyLabel = item.parentBbsIdx ? `<span class="jv-btn label05-sm">Re</span>` : '';

            // NEW 표시
            const newLabel = item.isNew ? `<span class="jv-btn label04-sm">NEW</span>` : '';

            // 제목
            const title = item.title || '제목 없음';


            row.innerHTML = `
            <!-- 체크박스 -->
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" class="select-item-checkbox" value="${item.bbsIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>
            <!-- 번호 또는 공지 -->
            <td>${item.chkTopFix === 'Y' ? '공지' : index + 1}</td>
            <!-- 제목 -->
            <td>
                ${secretLabel}
                ${replyLabel}
                <a href="/masterpage_sys/board/notice/${item.bbsIdx}" class="jv-btn underline01">${title}</a>
                ${newLabel}
            </td>
            <!-- 작성일 -->
            <td>${item.createdDate || '-'}</td>
            <!-- 게시글 수정 -->
            <td><a href="/masterpage_sys/board/notice/${item.bbsIdx}" class="jv-btn fill04">수정하기</a></td>
            <!-- 조회수 -->
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
    function updatePagination(response) {
        const totalPages = response.totalPages;

        // 드롭다운 옵션 업데이트
        pageSelect.innerHTML = ''; // 기존 옵션 제거
        for (let i = 0; i < totalPages; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = i + 1;
            if (i === searchState.currentPage) {
                option.selected = true;
            }
            pageSelect.appendChild(option);
        }

        // 현재 페이지 상태 업데이트
        currentPageDisplay.textContent = response.pageable.pageNumber + 1;
        prevPageBtn.disabled = response.pageable.pageNumber === 0;
        nextPageBtn.disabled = response.pageable.pageNumber + 1 >= response.totalPages;
    }

    // 드롭다운 상태 업데이트
    function updatePageSelect() {
        pageSelect.value = searchState.currentPage;
    }
});

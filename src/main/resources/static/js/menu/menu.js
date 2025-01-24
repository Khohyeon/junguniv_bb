document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.forms['searchform'];
    const searchButton = document.getElementById('searchButton');
    const excelButton = document.getElementById('excelButton');
    const pageSizeSelect = document.getElementById('pageSizeSelect');

    // 페이지 로드 시 기본 검색 실행
    const defaultPageSize = pageSizeSelect.value || 20; // 기본 페이지 크기
    searchList(searchForm, 0, defaultPageSize); // 초기 검색 실행

    // 검색 버튼 클릭 이벤트
    searchButton.addEventListener('click', () => {
        searchList(searchForm);
    });

    // 엑셀 다운로드 버튼 클릭 이벤트
    excelButton.addEventListener('click', () => {
        const printLayer = document.getElementById('PrintLayer');
        DocumentExcelPrint(printLayer);
    });

    // 페이지 크기 변경 이벤트
    pageSizeSelect.addEventListener('change', () => {
        const selectedSize = pageSizeSelect.value || 20; // 기본값 20
        searchList(searchForm, 0, selectedSize);
    });
});

// 검색 요청 함수
async function searchList(form, page = 0, size = 20) {
    const menuName = form.menuName.value; // 메뉴/페이지명
    const chkUse = form.chkUse.value;     // 메뉴 사용 여부

    try {
        const response = await fetch(
            `/masterpage_sys/manager_menu/api/search?menuName=${encodeURIComponent(menuName)}&chkUse=${chkUse}&page=${page}&size=${size}`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );

        if (!response.ok) {
            throw new Error('Server response error');
        }

        const data = await response.json();

        if (data.success) {
            // 검색 결과 렌더링
            renderSearchResults(data.response.content);

            // 페이지네이션 갱신
            const pagination = document.getElementById('pagination');
            renderPagination(pagination, data.response, (newPage) => {
                searchList(form, newPage, size);
            });

            // 총 검색 결과 수 업데이트
            const resultCount = document.getElementById('resultCount');
            resultCount.textContent = data.response.totalElements;
        } else {
            alert('검색 결과를 가져오는 중 문제가 발생했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('검색 요청 중 문제가 발생했습니다.');
    }
}

// 검색 결과 렌더링 함수
function renderSearchResults(results) {
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = '';

    if (results.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align: center;">검색 결과가 없습니다.</td>
            </tr>
        `;
        return;
    }

    results.forEach((result, index) => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${result.menuIdx}</td>
            <td>${result.menuType}</td>
            <td>${result.menuName}</td>
            <td>${result.url}</td>
              <td>
                <label class="c-input ci-radio">
                    <input type="radio" name="change_chk_open${index}" value="Y" ${result.chkUse === 'Y' ? 'checked' : ''}> 사용
                    <div class="ci-show"></div>
                </label>
                <label class="c-input ci-radio">
                    <input type="radio" name="change_chk_open${index}" value="N" ${result.chkUse === 'N' ? 'checked' : ''}> 사용 안함
                    <div class="ci-show"></div>
                </label>
            </td>
        `;

        tbody.appendChild(row);
    });
}

// 페이지네이션 렌더링 함수
function renderPagination(pagination, data, callback) {
    if (!pagination) return;

    const { number: currentPage, totalPages } = data;
    let paginationHtml = '';

    // 이전 페이지 버튼
    if (currentPage > 0) {
        paginationHtml += `<a href="#" class="prev" data-page="${currentPage - 1}"></a>`;
    }

    // 페이지 번호
    for (let i = 0; i < totalPages; i++) {
        if (i === currentPage) {
            paginationHtml += `<a href="#" class="active" data-page="${i}">${i + 1}</a>`;
        } else {
            paginationHtml += `<a href="#" data-page="${i}">${i + 1}</a>`;
        }
    }

    // 다음 페이지 버튼
    if (currentPage < totalPages - 1) {
        paginationHtml += `<a href="#" class="next" data-page="${currentPage + 1}"></a>`;
    }

    pagination.innerHTML = paginationHtml;

    // 페이지네이션 클릭 이벤트
    pagination.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', async (e) => {
            e.preventDefault();
            const page = parseInt(link.dataset.page);
            if (!isNaN(page) && callback) {
                await callback(page);
            }
        });
    });
}

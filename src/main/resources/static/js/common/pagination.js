document.addEventListener('DOMContentLoaded', function () {
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const currentPageDisplay = document.getElementById('currentPage');
    let currentPage = 0;
    const pageSize = 20;

    // 초기 실행
    initializePagination();

    // 이전 페이지 버튼
    prevPageBtn.addEventListener('click', function () {
        if (currentPage > 0) {
            currentPage--;
            fetchPageData(currentPage);
        }
    });

    // 다음 페이지 버튼
    nextPageBtn.addEventListener('click', function () {
        currentPage++;
        fetchPageData(currentPage);
    });

    // 데이터 가져오기
    function fetchPageData(page) {
        fetch(`/api/data?page=${page}&size=${pageSize}`)
            .then(response => {
                if (!response.ok) throw new Error('페이지 로드 실패');
                return response.json();
            })
            .then(data => {
                renderData(data.content);
                updatePagination(data.pageable);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // 데이터 렌더링
    function renderData(content) {
        const tableBody = document.getElementById('dataTableBody');
        tableBody.innerHTML = '';
        content.forEach((item, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `<td>${index + 1}</td><td>${item.name}</td>`;
            tableBody.appendChild(row);
        });
    }

    // 페이지네이션 업데이트
    function updatePagination(pageable) {
        currentPage = pageable.number;
        currentPageDisplay.textContent = currentPage + 1;
        prevPageBtn.disabled = pageable.first;
        nextPageBtn.disabled = pageable.last;
    }

    // 초기화
    function initializePagination() {
        fetchPageData(currentPage);
    }
});

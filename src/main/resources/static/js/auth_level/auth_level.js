// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    // 초기화
    initializeAuthLevelList();
    // 초기 데이터 로드
    loadAuthLevelList();
});

// 권한레벨 리스트 초기화
function initializeAuthLevelList() {
    // 체크박스 전체 선택/해제
    const mainCheckbox = document.querySelector('thead input[type="checkbox"]');
    if (mainCheckbox) {
        mainCheckbox.addEventListener('change', function() {
            const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]');
            checkboxes.forEach(checkbox => {
                checkbox.checked = mainCheckbox.checked;
            });
        });
    }

    // 삭제 버튼 이벤트
    const deleteBtn = document.querySelector('.jv-btn.fill05');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', handleDelete);
    }

    // 권한생성 버튼 이벤트
    const createBtn = document.querySelector('.jv-btn.fill04');
    if (createBtn) {
        createBtn.addEventListener('click', () => {
            window.location.href = '/masterpage_sys/auth_level/save';
        });
    }

    // 페이지 사이즈 변경 이벤트
    const pageSizeSelect = document.querySelector('.page-view select');
    if (pageSizeSelect) {
        pageSizeSelect.addEventListener('change', function() {
            const pageSize = this.value;
            if (pageSize) {
                loadAuthLevelList(0, parseInt(pageSize));
            }
        });
    }
}

// 권한레벨 목록 로드
async function loadAuthLevelList(page = 0, size = 20, authLevelName = '') {
    try {
        const response = await fetch(`/masterpage_sys/auth_level/api?page=${page}&size=${size}&authLevelName=${authLevelName}`);
        const result = await response.json();

        if (result.success) {
            const data = {
                content: result.response.authLevelList,
                pageable: result.response.pageable,
                totalElements: result.response.pageable.totalElements,
                totalPages: result.response.pageable.totalPages,
                number: result.response.pageable.pageNumber
            };
            renderAuthLevelList(data);
            renderPagination(data);
            updateTotalCount(data.totalElements);
        } else {
            alert('권한레벨 목록을 불러오는데 실패했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('서버 오류가 발생했습니다.');
    }
}

// 권한레벨 목록 렌더링
function renderAuthLevelList(data) {
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = '';

    data.content.forEach((item, index) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" value="${item.authLevelIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>
            <td>${data.totalElements - (data.number * data.pageable.pageSize) - index}</td>
            <td>
                <a href="/masterpage_sys/auth_level/${item.authLevelIdx}" class="jv-btn underline01">${item.authLevelName}</a>
            </td>
            <td>
                <a href="/masterpage_sys/member/admin?authLevel=${item.authLevel}" class="jv-btn underline01">${item.linkedMemberCount}</a>
            </td>
            <td>${formatDate(item.createdDate)}</td>
        `;
        tbody.appendChild(tr);
    });
}

// 페이지네이션 렌더링
function renderPagination(data) {
    const pagination = document.getElementById('pagination');
    const totalPages = data.totalPages;
    const currentPage = data.number;
    
    let paginationHtml = '';
    
    // 이전 페이지 버튼
    if (currentPage > 0) {
        paginationHtml += `<a href="#" onclick="loadAuthLevelList(${currentPage - 1})" class="prev">이전</a>`;
    }
    
    // 페이지 번호
    for (let i = 0; i < totalPages; i++) {
        if (i === currentPage) {
            paginationHtml += `<a href="#" class="active">${i + 1}</a>`;
        } else {
            paginationHtml += `<a href="#" onclick="loadAuthLevelList(${i})">${i + 1}</a>`;
        }
    }
    
    // 다음 페이지 버튼
    if (currentPage < totalPages - 1) {
        paginationHtml += `<a href="#" onclick="loadAuthLevelList(${currentPage + 1})" class="next">다음</a>`;
    }
    
    pagination.innerHTML = paginationHtml;
}

// 총 건수 업데이트
function updateTotalCount(totalElements) {
    const countSpan = document.querySelector('.count .Pretd_B');
    if (countSpan) {
        countSpan.textContent = totalElements;
    }
}

// 삭제 처리
async function handleDelete() {
    const checkedBoxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
    const ids = Array.from(checkedBoxes).map(cb => parseInt(cb.value));
    
    if (ids.length === 0) {
        alert('삭제할 항목을 선택해주세요.');
        return;
    }
    
    if (!confirm('선택한 권한을 삭제하시겠습니까?')) {
        return;
    }
    
    try {
        const response = await fetch('/masterpage_sys/auth_level/api', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(ids)
        });
        
        const result = await response.json();
        
        if (result.success) {
            alert('삭제가 완료되었습니다.');
            loadAuthLevelList(); // 목록 새로고침
        } else {
            alert(result.error?.message || '삭제 처리 중 오류가 발생했습니다.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('서버 오류가 발생했습니다.');
    }
}

// 날짜 포맷 함수
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

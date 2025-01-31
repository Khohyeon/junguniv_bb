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

async function searchList(form, page = 0, size = 20) {
    const studyType = document.body.getAttribute('data-study-type');
    const refundPriceType = form.querySelector('input[name="refundPriceType"]:checked')
        ? form.querySelector('input[name="refundPriceType"]:checked').value
        : 'ALL'; // 환급 가격 유형

    const refundPriceName = form.refundPriceName.value; // 환급 가격 이름

    try {
        const response = await fetch(`/masterpage_sys/refund_price/api/search`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                refundPriceType, // DTO 필드에 맞게 매핑
                refundPriceName,
                studyType
            }),
        });

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

// 검색 결과를 테이블에 렌더링하는 함수
function renderSearchResults(results) {
    const tbody = document.querySelector('tbody');
    tbody.innerHTML = ''; // 기존 검색 결과 초기화

    if (results.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center;">검색 결과가 없습니다.</td>
            </tr>
        `;
        return;
    }

    results.forEach((result) => {
        const row = document.createElement('tr');

        let refundPriceTd = '';  // 환급 교육 정보 TD (studyType이 'normal'이 아닐 때만 추가)
        let refundPriceUrl = '/masterpage_sys/refund_price/normal';
        if (result.studyType !== 'normal') {
            refundPriceTd = `
                <td>
                    <span>${result.refundPriceType === 'saup' ? '사업주 환급 교육' :
                (result.refundPriceType === 'cardsil' ? '국민내일배움카드(실업자)' :
                    (result.refundPriceType === 'cardjae' ? '국민내일배움카드(재직자)' : '알 수 없음'))}</span>
                </td>
            `;
            refundPriceUrl = '/masterpage_sys/refund_price'
        }

        row.innerHTML = `
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" class="select-item-checkbox" value="${result.refundPriceIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>

            <td>${result.refundPriceIdx}</td>

            ${refundPriceTd} <!-- studyType이 'normal'이 아닐 때만 추가됨 -->

            <td>
                <a class="jv-btn" href="${refundPriceUrl}/${result.refundPriceIdx}">
                    ${result.refundPriceName}
                </a>
            </td>
            <td>${result.discountType === 'percent' ? '지원율(%)' : '할인율(%)'}</td>
            <td>${result.discountType === 'percent' ? result.refundRate + ' %' : result.refundRate + ' 원'}</td>
            <td>${result.chkUse}</td>
            <td>${result.sortno}</td>
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
        paginationHtml += `<a href="#" class="prev" data-page="${currentPage - 1}">이전</a>`;
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
        paginationHtml += `<a href="#" class="next" data-page="${currentPage + 1}">다음</a>`;
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

/**
 * 리스트 페이지에서 신규등록 클릭 시 기존 url에 '/save'를 추가하여 이동
 */
document.addEventListener('DOMContentLoaded', function () {
    const saveButton = document.getElementById('saveForm');

    if (saveButton) {
        saveButton.addEventListener('click', () => {
            // 현재 URL 가져오기
            let currentPath = window.location.pathname;

            // 이미 '/save'가 포함되어 있다면 추가하지 않음
            if (!currentPath.endsWith('/save')) {
                window.location.href = currentPath + '/save';
            }
        });
    }
});


/**
 * 지원금종류 저장
 */
document.addEventListener('DOMContentLoaded', function () {
    const studyType = document.body.getAttribute('data-study-type');
    const form = document.getElementById('refundForm');
    let refundPriceUrl = '/masterpage_sys/refund_price/normal';
    if (studyType !== 'normal') {
        refundPriceUrl = '/masterpage_sys/refund_price';
    }

    if (form) {  // 폼이 있을 때만 이벤트 리스너를 추가
        form.addEventListener('submit', function (e) {
            e.preventDefault(); // 기본 폼 제출 동작 방지

            const formData = new FormData(form);
            formData.append("studyType", studyType);

            fetch(form.action, {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        // 응답 헤더의 Content-Type이 JSON인지 확인
                        const contentType = response.headers.get('content-type');
                        if (contentType && contentType.includes('application/json')) {
                            // JSON 응답을 파싱하여 에러 메시지를 추출
                            return response.json().then(errorData => {
                                alert(errorData.error.message);
                            });
                        } else {
                            // JSON이 아닌 경우 일반 에러 메시지 표시
                            throw new Error('서버에 오류가 발생했습니다.');
                        }
                    }
                    return response.json();
                })
                .then(data => {
                    // 성공 응답 처리
                    alert(data.response);
                    window.location.href = refundPriceUrl; // 성공 시 리스트 페이지로 이동
                })
        });
    }
});

/**
 * 지원금종류 수정
 */
document.addEventListener('DOMContentLoaded', function () {
    const studyType = document.body.getAttribute('data-study-type');
    const form = document.getElementById('refundDetailForm');
    let refundPriceUrl = '/masterpage_sys/refund_price/normal';
    if (studyType !== 'normal') {
        refundPriceUrl = '/masterpage_sys/refund_price';
    }
    if (form) {  // 폼이 있을 때만 이벤트 리스너를 추가
        form.addEventListener('submit', function (e) {
            e.preventDefault(); // 기본 폼 제출 동작 방지

            const formData = new FormData(form);
            formData.append("studyType", studyType);

            fetch(form.action, {
                method: 'PUT',
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        // 응답 헤더의 Content-Type이 JSON인지 확인
                        const contentType = response.headers.get('content-type');
                        if (contentType && contentType.includes('application/json')) {
                            // JSON 응답을 파싱하여 에러 메시지를 추출
                            return response.json().then(errorData => {
                                alert(errorData.error.message);
                            });
                        } else {
                            // JSON이 아닌 경우 일반 에러 메시지 표시
                            throw new Error('서버에 오류가 발생했습니다.');
                        }
                    }
                    return response.json();
                })
                .then(data => {
                    // 성공 응답 처리
                    alert(data.response);
                    window.location.href = refundPriceUrl; // 성공 시 리스트 페이지로 이동
                })
        });
    }
});


/**
 * Save/Detail Form 에서 지원율/할인금액 라디오버튼 클릭 시 변화 주는 이벤트
 */
document.addEventListener('DOMContentLoaded', function () {
    const discountTypeRadios = document.querySelectorAll('input[name="discountType"]');
    const titleElement = document.getElementById('percent-name');
    const unitElement = document.getElementById('percent');
    const refundRateInput = document.getElementById('refundRate');

    discountTypeRadios.forEach(radio => {
        radio.addEventListener('change', function () {
            if (this.value === 'percent') {
                titleElement.textContent = '지원율(%)';
                unitElement.textContent = ' (%) ';
                refundRateInput.placeholder = '반드시 % 단위로 할인/지원율을 숫자로 기입해주세요';
            } else if (this.value === 'discount') {
                titleElement.textContent = '할인금액(원)';
                unitElement.textContent = ' (원) ';
                refundRateInput.placeholder = '할인 금액을 숫자로 입력해주세요';
            }
        });
    });
});

/**
 * 탭 클릭 시 활성화 변경
 */
document.addEventListener('DOMContentLoaded', function () {
    const tabs = document.querySelectorAll('.jv-tab.tab2 li');
    const currentUrl = window.location.pathname; // 현재 URL 가져오기

    // 모든 탭에서 'active' 클래스 제거
    tabs.forEach(tab => tab.classList.remove('active'));

    // URL에 따라 해당 탭에 'active' 클래스 추가
    if (currentUrl.includes('/masterpage_sys/refund_price/normal')) {
        document.querySelector('.jv-tab.tab2 li:nth-child(2)').classList.add('active'); // 일반교육 활성화
    } else if (currentUrl.includes('/masterpage_sys/refund_price')) {
        document.querySelector('.jv-tab.tab2 li:first-child').classList.add('active'); // 환급교육 활성화
    }
});

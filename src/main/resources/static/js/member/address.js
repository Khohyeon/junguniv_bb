const AddressModule = {
    // API 호출 함수들
    api: {
        // 주소록 목록 조회
        getList: async function(page = 0, size = 10) {
            try {
                const response = await fetch(`/masterpage_sys/member/api/address?page=${page}&size=${size}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Referer': window.location.href
                    }
                });

                if (!response.ok) {
                    throw new Error('주소록 목록을 불러오는데 실패했습니다.');
                }

                return await response.json();
            } catch (error) {
                console.error('주소록 목록 조회 에러:', error);
                throw error;
            }
        },

        // 주소록 검색
        search: async function(searchParams, page = 0, size = 10) {
            try {
                const queryString = new URLSearchParams({
                    ...searchParams,
                    page: page,
                    size: size
                }).toString();

                const response = await fetch(`/masterpage_sys/member/api/address/search?${queryString}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error('검색에 실패했습니다.');
                }

                return await response.json();
            } catch (error) {
                console.error('검색 에러:', error);
                throw error;
            }
        }
    },

    // UI 렌더링 함수들
    render: {
        // 페이지네이션 렌더링
        pagination: function(paginationDiv, pageInfo) {
            if (!paginationDiv) return;

            let html = '';
            
            // 이전 페이지 버튼
            html += `
                <button class="prev ${pageInfo.pageNumber === 0 ? 'disabled' : ''}" 
                        ${pageInfo.pageNumber === 0 ? 'disabled' : ''}>
                    이전
                </button>
            `;

            // 페이지 번호 (최대 10개씩 표시)
            const startPage = Math.floor(pageInfo.pageNumber / 10) * 10;
            const endPage = Math.min(startPage + 10, pageInfo.totalPages);

            // 시작 페이지가 0이 아니면 첫 페이지 버튼 표시
            if (startPage > 0) {
                html += `
                    <button class="page-number" data-page="0">1</button>
                    ${startPage > 1 ? '<span class="page-dots">...</span>' : ''}
                `;
            }

            // 페이지 번호
            for (let i = startPage; i < endPage; i++) {
                html += `
                    <button class="page-number ${pageInfo.pageNumber === i ? 'active' : ''}"
                            data-page="${i}">
                        ${i + 1}
                    </button>
                `;
            }

            // 마지막 페이지가 전체 페이지보다 작으면 마지막 페이지 버튼 표시
            if (endPage < pageInfo.totalPages) {
                html += `
                    ${endPage < pageInfo.totalPages - 1 ? '<span class="page-dots">...</span>' : ''}
                    <button class="page-number" data-page="${pageInfo.totalPages - 1}">
                        ${pageInfo.totalPages}
                    </button>
                `;
            }

            // 다음 페이지 버튼
            html += `
                <button class="next ${pageInfo.pageNumber === pageInfo.totalPages - 1 ? 'disabled' : ''}"
                        ${pageInfo.pageNumber === pageInfo.totalPages - 1 ? 'disabled' : ''}>
                    다음
                </button>
            `;

            paginationDiv.innerHTML = html;

            // 페이지네이션 이벤트 리스너 추가
            paginationDiv.querySelectorAll('button').forEach(button => {
                button.addEventListener('click', (e) => {
                    if (button.classList.contains('disabled')) return;
                    
                    if (button.classList.contains('prev')) {
                        AddressModule.state.currentPage = AddressModule.state.currentPage - 1;
                    } else if (button.classList.contains('next')) {
                        AddressModule.state.currentPage = AddressModule.state.currentPage + 1;
                    } else {
                        AddressModule.state.currentPage = parseInt(button.dataset.page);
                    }
                    AddressModule.handlers.search();
                });
            });
        }
    },

    // 상태 관리
    state: {
        currentPage: 0,
        pageSize: 10
    },

    // 이벤트 핸들러
    handlers: {
        // 검색 핸들러
        search: async function() {
            const tbody = document.querySelector('table tbody');
            const paginationDiv = document.getElementById('paginationMember');

            try {
                // 검색 파라미터 수집
                const email1 = document.querySelector('input[name="email1"]').value;
                const email2Select = document.querySelector('select[name="email2"]');
                const email2 = email2Select.value === '1' ? '' : email2Select.value;
                const email = email1 && email2 ? `${email1}@${email2}` : '';

                const searchParams = {
                    name: document.querySelector('input[name="name"]').value.trim(),
                    userId: document.querySelector('input[name="userid"]').value.trim(),
                    address: document.querySelector('input[name="address"]').value.trim(),
                    telMobile: document.querySelector('input[name="telMobile"]').value.trim(),
                    email: email.trim(),
                    jobName: document.querySelector('input[name="jobName"]').value.trim()
                };

                // 빈 값 제거
                Object.keys(searchParams).forEach(key =>
                    !searchParams[key] && delete searchParams[key]
                );

                // 검색 파라미터가 모두 비어있으면 기본 목록 조회
                const isEmptySearch = Object.keys(searchParams).length === 0;
                let data;

                if (isEmptySearch) {
                    data = await AddressModule.api.getList(AddressModule.state.currentPage, AddressModule.state.pageSize);
                } else {
                    data = await AddressModule.api.search(searchParams, AddressModule.state.currentPage, AddressModule.state.pageSize);
                }

                // 테이블 데이터 렌더링
                const rows = data.memberList.map((item, index) => {
                    // userType에 따른 상세보기 URL 생성
                    const detailUrl = `/masterpage_sys/member/${item.userType.toLowerCase()}/${item.memberIdx}`;
                    
                    return `
                    <tr>
                        <td>
                            <label class="c-input ci-check single">
                                <input type="checkbox" name="vidx[]" value="${item.memberIdx}">
                                <div class="ci-show"></div>
                            </label>
                        </td>
                        <td>${(AddressModule.state.currentPage * AddressModule.state.pageSize) + index + 1}</td>
                        <td>
                            <a href="${detailUrl}" class="jv-btn underline01">
                                ${item.name || ''}
                            </a>
                        </td>
                        <td>${item.userId || ''}</td>
                        <td>${item.telMobile || ''}</td>
                        <td>${item.zipcode || ''}</td>
                        <td>${(item.addr1 || '') + ' ' + (item.addr2 || '')}</td>
                    </tr>
                `}).join('');

                tbody.innerHTML = rows || '<tr><td colspan="7">검색 결과가 없습니다.</td></tr>';

                // 검색 결과 수 업데이트
                const countElement = document.querySelector('.result-count');
                if (countElement) {
                    countElement.textContent = data.pageable.totalElements || 0;
                }

                // 페이지네이션 렌더링
                AddressModule.render.pagination(paginationDiv, {
                    totalPages: data.pageable.totalPages,
                    pageNumber: data.pageable.pageNumber,
                    totalElements: data.pageable.totalElements
                });
            } catch (error) {
                console.error('검색 실패:', error);
                alert('검색에 실패했습니다.');
                tbody.innerHTML = '<tr><td colspan="7">데이터를 불러오는데 실패했습니다.</td></tr>';
            }
        },

        // 회사찾기 팝업 열기
        openCompanySearch: function() {
            const width = 1280;
            const height = 720;
            const left = (window.screen.width - width) / 2;
            const top = (window.screen.height - height) / 2;
            
            window.open('/masterpage_sys/member/company/searchForm', 'companySearch',
                `width=${width},height=${height},left=${left},top=${top},scrollbars=yes`);
        },

        // 회사 선택 처리 (팝업에서 호출됨)
        selectCompany: function(companyData) {
            const jobNameInput = document.querySelector('input[name="jobName"]');
            if (jobNameInput) {
                jobNameInput.value = companyData.jobName || '';
            }
        },

        // 전체 선택 체크박스 핸들러
        checkAll: function(checked) {
            const checkboxes = document.querySelectorAll('table tbody input[type="checkbox"]');
            checkboxes.forEach(cb => cb.checked = checked);
        },

        // 엑셀 다운로드 핸들러
        saveExcel: function() {
            // 엑셀 다운로드 로직 구현
            console.log('엑셀 다운로드');
        }
    },

    // 주소록 일괄 출력 관련 함수들
    printLabel: {
        // 라벨 당 표시할 항목 수
        LABELS_PER_PAGE: 18,

        // 라벨 HTML 요소 생성
        createLabelElement: function(label) {
            // null 체크 함수
            const safeStr = (value) => {
                if (value === null || value === undefined) return '';
                return String(value).trim();
            };

            const li = document.createElement('li');
            
            const address = document.createElement('div');
            address.className = 'address';
            
            const zipcode = document.createElement('div');
            zipcode.className = 'zipcode';
            zipcode.textContent = safeStr(label.zipcode);
            
            const addTxt = document.createElement('div');
            addTxt.className = 'add-txt';
            addTxt.textContent = `${safeStr(label.addr1)} ${safeStr(label.addr2)}`.trim();
            
            address.appendChild(zipcode);
            address.appendChild(addTxt);
            
            const recipient = document.createElement('div');
            recipient.className = 'recipient';
            
            const name = document.createElement('div');
            name.className = 'name';
            const nameText = [
                safeStr(label.jobName),
                safeStr(label.jobDept),
                safeStr(label.name)
            ].filter(Boolean).join(' ');
            name.textContent = nameText;
            
            const phone = document.createElement('div');
            phone.className = 'phone';
            phone.textContent = safeStr(label.telMobile);
            
            recipient.appendChild(name);
            recipient.appendChild(phone);
            
            li.appendChild(address);
            li.appendChild(recipient);
            
            return li;
        },

        // 프린트 창 생성 및 내용 구성
        createPrintWindow: function(labels) {
            const printWindow = window.open('', '_blank', 'width=800,height=600');
            
            printWindow.document.write(`
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>주소록 출력</title>
                    <style>
                        @media print {
                            @page {
                                size: A4;
                                margin: 14.5mm 5mm 12mm 4mm;
                            }
                            
                            body {
                                margin: 0;
                                padding: 0;
                            }
                            
                            .print-label {
                                width: 100%;
                            }
                            
                            table {
                                width: 100%;
                                border-collapse: collapse;
                                page-break-after: avoid; /* 테이블 내에서 페이지 나눔 방지 */
                            }
                            
                            td {
                                width: 50%;
                                height: 50mm; /* 높이 조정 */
                                padding: 5mm;
                                box-sizing: border-box;
                                vertical-align: top;
                            }
                            
                            .zipcode {
                                font-size: 16px;
                                font-weight: bold;
                                margin-bottom: 3mm;
                            }
                            
                            .add-txt {
                                font-size: 14px;
                                line-height: 1.6;
                                margin-bottom: 5mm;
                            }
                            
                            .name {
                                font-size: 16px;
                                font-weight: bold;
                                margin-bottom: 2mm;
                            }
                            
                            .phone {
                                font-size: 14px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="print-label">
            `);

            // 10개씩 그룹화 (페이지당 5쌍)
            for (let i = 0; i < labels.length; i += 10) {
                printWindow.document.write('<table>');
                // 5쌍의 행 생성
                for (let j = i; j < Math.min(i + 10, labels.length); j += 2) {
                    printWindow.document.write('<tr>');
                    
                    // 첫 번째 열
                    if (j < labels.length) {
                        printWindow.document.write(`
                            <td>
                                <div class="zipcode">[${labels[j].zipcode}]</div>
                                <div class="add-txt">${labels[j].addr1} ${labels[j].addr2}</div>
                                <div class="name">${labels[j].name}</div>
                                <div class="phone">${labels[j].telMobile}</div>
                            </td>
                        `);
                    } else {
                        printWindow.document.write('<td></td>');
                    }
                    
                    // 두 번째 열
                    if (j + 1 < labels.length) {
                        printWindow.document.write(`
                            <td>
                                <div class="zipcode">[${labels[j+1].zipcode}]</div>
                                <div class="add-txt">${labels[j+1].addr1} ${labels[j+1].addr2}</div>
                                <div class="name">${labels[j+1].name}</div>
                                <div class="phone">${labels[j+1].telMobile}</div>
                            </td>
                        `);
                    } else {
                        printWindow.document.write('<td></td>');
                    }
                    
                    printWindow.document.write('</tr>');
                }
                printWindow.document.write('</table>');
                
                // 마지막 테이블이 아닌 경우에만 페이지 나누기 추가
                if (i + 10 < labels.length) {
                    printWindow.document.write('<div style="page-break-after: always;"></div>');
                }
            }

            printWindow.document.write(`
                    </div>
                </body>
                </html>
            `);
            
            printWindow.document.close();
            return printWindow;
        },

        // 주소록 일괄 출력
        print: async function() {
            try {
                // 선택된 체크박스 가져오기
                const selectedCheckboxes = document.querySelectorAll('table tbody input[type="checkbox"]:checked');
                const memberIds = Array.from(selectedCheckboxes).map(cb => parseInt(cb.value));

                if (memberIds.length === 0) {
                    alert('출력할 회원을 선택해주세요.');
                    return;
                }

                // API 호출
                const response = await fetch('/masterpage_sys/member/api/address/print', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(memberIds)
                });

                if (!response.ok) {
                    throw new Error('주소록 데이터를 가져오는데 실패했습니다.');
                }

                const result = await response.json();
                
                // response 필드에서 라벨 데이터 가져오기
                if (!result.success || !result.response) {
                    throw new Error('주소록 데이터 형식이 올바르지 않습니다.');
                } 
                
                const labels = result.response;

                // 프린트 창 생성 및 내용 구성
                const printWindow = this.createPrintWindow(labels);
                
                // 프린트 창 포커스
                printWindow.focus();

                // 잠시 대기 후 프린트 다이얼로그 표시
                setTimeout(() => {
                    printWindow.print();
                }, 500);

            } catch (error) {
                console.error('주소록 출력 중 오류 발생:', error);
                alert('주소록 출력에 실패했습니다.');
            }
        }
    },

    // 이벤트 바인딩
    bindEvents: function() {
        // 검색 버튼 이벤트 리스너
        const searchBtn = document.getElementById('searchBtn');
        if (searchBtn) {
            searchBtn.addEventListener('click', () => {
                this.state.currentPage = 0;
                this.handlers.search();
            });
        }

        // 검색 입력 필드 엔터키 이벤트
        const searchInputs = document.querySelectorAll('.column-tc-wrap input[type="text"]');
        searchInputs.forEach(input => {
            input.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    this.state.currentPage = 0;
                    this.handlers.search();
                }
            });
        });

        // 페이지 크기 변경 이벤트
        const pageSizeSelect = document.querySelector('.page-view select');
        if (pageSizeSelect) {
            pageSizeSelect.addEventListener('change', (e) => {
                this.state.pageSize = parseInt(e.target.value) || 10;
                this.state.currentPage = 0;
                this.handlers.search();
            });
        }

        // 전체 선택 체크박스 이벤트
        const checkAll = document.querySelector('table thead input[type="checkbox"]');
        if (checkAll) {
            checkAll.addEventListener('change', (e) => this.handlers.checkAll(e.target.checked));
        }

        // 회사찾기 버튼 이벤트 리스너
        const companySearchBtn = document.getElementById('companySearchBtn');
        if (companySearchBtn) {
            companySearchBtn.addEventListener('click', this.handlers.openCompanySearch);
        }

        // 주소록 일괄 출력 버튼 이벤트 리스너
        const printLabelBtn = document.querySelector('.print-label-btn');
        if (printLabelBtn) {
            printLabelBtn.addEventListener('click', () => this.printLabel.print());
        }

        // 엑셀 다운로드 버튼 이벤트 리스너
        const excelBtn = document.querySelector('.icon-excel');
        if (excelBtn) {
            excelBtn.addEventListener('click', this.handlers.saveExcel);
        }
    },

    // 초기화
    init: function() {
        // 이벤트 바인딩
        this.bindEvents();

        // 초기 데이터 로드
        this.handlers.search();
    }
};

// 회사 선택 콜백 함수 (팝업에서 호출)
const handleCompanySelect = (companyData) => {
    AddressModule.handlers.selectCompany(companyData);
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    AddressModule.init();
});
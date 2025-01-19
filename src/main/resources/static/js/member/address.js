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

            const totalPages = pageInfo.totalPages;
            const currentPage = pageInfo.pageNumber;
            let html = '';

            // 이전 페이지 버튼
            if (currentPage > 0) {
                html += `<a href="javascript:" class="prev" data-page="${currentPage - 1}">이전</a>`;
            }

            // 페이지 번호
            for (let i = 0; i < totalPages; i++) {
                if (i === currentPage) {
                    html += `<a href="javascript:" class="active">${i + 1}</a>`;
                } else {
                    html += `<a href="javascript:" data-page="${i}">${i + 1}</a>`;
                }
            }

            // 다음 페이지 버튼
            if (currentPage < totalPages - 1) {
                html += `<a href="javascript:" class="next" data-page="${currentPage + 1}">다음</a>`;
            }

            paginationDiv.innerHTML = html;

            // 페이지네이션 클릭 이벤트 추가
            paginationDiv.querySelectorAll('a[data-page]').forEach(a => {
                a.addEventListener('click', () => {
                    const page = parseInt(a.dataset.page);
                    AddressModule.state.currentPage = page;
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
                const rows = data.content.map((item, index) => `
                    <tr>
                        <td>
                            <label class="c-input ci-check single">
                                <input type="checkbox" name="vidx[]" value="${item.memberIdx}">
                                <div class="ci-show"></div>
                            </label>
                        </td>
                        <td>${(AddressModule.state.currentPage * AddressModule.state.pageSize) + index + 1}</td>
                        <td>
                            <a href="/masterpage_sys/member/student/${item.memberIdx}" class="jv-btn underline01">
                                ${item.name || ''}
                            </a>
                        </td>
                        <td>${item.userId || ''}</td>
                        <td>${item.telMobile || ''}</td>
                        <td>${item.zipcode || ''}</td>
                        <td>${(item.addr1 || '') + ' ' + (item.addr2 || '')}</td>
                    </tr>
                `).join('');

                tbody.innerHTML = rows || '<tr><td colspan="7">검색 결과가 없습니다.</td></tr>';

                // 검색 결과 수 업데이트
                const countElement = document.querySelector('.count .Pretd_B');
                if (countElement) {
                    countElement.textContent = data.totalElements || 0;
                }

                // 페이지네이션 렌더링
                AddressModule.render.pagination(paginationDiv, {
                    totalPages: data.totalPages,
                    pageNumber: data.pageable.pageNumber,
                    totalElements: data.totalElements
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
            const printWindow = window.open('', '_blank');
            const doc = printWindow.document;
            
            // 기본 HTML 구조 생성
            doc.open();
            doc.write(`
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>주소록 출력</title>
                    <link rel="stylesheet" href="/css/member/member.css">
                </head>
                <body></body>
                </html>
            `);
            doc.close();
            
            // 페이지별로 라벨 추가
            for (let i = 0; i < labels.length; i += this.LABELS_PER_PAGE) {
                const pageLabels = labels.slice(i, i + this.LABELS_PER_PAGE);
                
                const printLabel = document.createElement('div');
                printLabel.className = 'print-label';
                
                const ul = document.createElement('ul');
                
                pageLabels.forEach(label => {
                    ul.appendChild(this.createLabelElement(label));
                });
                
                printLabel.appendChild(ul);
                doc.body.appendChild(printLabel);
            }
            
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
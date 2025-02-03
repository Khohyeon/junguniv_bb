// 회원 관리 모듈
const MemberModule = {
    // 상태 관리
    state: {
        members: [],
        totalPages: 0,
        currentPage: 0,
        size: 10,
        totalElements: 0,
        isIdCheckPassed: false, // 아이디 중복 확인 통과 여부
        pageType: '', // 'student', 'teacher', 'company', 'admin'
        fieldNames: {
            userId: '아이디',
            pwd: '비밀번호',
            name: '이름',
            birthday: '생년월일',
            telMobile: '휴대폰 번호',
            residentNumber: '주민등록번호',
            email: '이메일',
            jobName: '근무회사(위탁기업명)',
            jobNumber: '사업자번호',
            jobInsuranceNumber: '고용보험관리번호',
            trneeSe: '훈련생 구분',
            irglbrSe: '비정규직 구분',
            contractorName: '교육담당자 이름',
            telMobile1: '휴대폰 번호(앞자리)',
            telMobile2: '휴대폰 번호(중간자리)',
            telMobile3: '휴대폰 번호(뒷자리)',
            email1: '이메일(아이디)',
            email2: '이메일(도메인)',
            authLevel: '관리자 권한'
        }
    },

    // UUID 생성 함수 추가
    utils: {
        generateUUID: function() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                const r = Math.random() * 16 | 0;
                const v = c === 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        },

        getFileExtension: function(filename) {
            return filename.slice((filename.lastIndexOf(".") - 1 >>> 0) + 2);
        }
    },

    // API 호출 함수들
    api: {
        // 회원 목록 조회
        getMembers: async function(page = 0, size = 10) {
            try {
                // URL에서 authLevel 파라미터 확인
                const urlParams = new URLSearchParams(window.location.search);
                const authLevel = urlParams.get('authLevel');

                // 현재 페이지가 관리자 목록이고 authLevel이 있는 경우
                if (window.location.pathname.includes('/member/admin') && authLevel) {
                    return await this.searchAdmins({ authLevel }, page, size);
                }

                const response = await fetch(`/masterpage_sys/member/api?page=${page}&size=${size}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Referer': window.location.href
                    }
                });
                
                if (!response.ok) {
                    throw new Error('회원 목록을 불러오는데 실패했습니다.');
                }
                
                return await response.json();
            } catch (error) {
                console.error('회원 목록 조회 에러:', error);
                return null;
            }
        },

        // 회원 삭제
        deleteMembers: async function(ids) {
            try {
                const response = await fetch('/masterpage_sys/member/api', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(ids)
                });

                if (!response.ok) {
                    throw new Error('회원 삭제에 실패했습니다.');
                }

                return await response.json();
            } catch (error) {
                console.error('회원 삭제 에러:', error);
                throw error;
            }
        },

        // 회원 등록
        saveMember: async function(memberData) {
            try {
                const response = await fetch('/masterpage_sys/member/api', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(memberData)
                });

                if (!response.ok) {
                    throw new Error('회원 등록에 실패했습니다.');
                }

                return await response.json();
            } catch (error) {
                console.error('회원 등록 에러:', error);
                throw error;
            }
        },

        // 아이디 중복 확인
        checkDuplicateId: async function(userId) {
            try {
                const response = await fetch(`/masterpage_sys/member/api/idCheck?userId=${userId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                if (!response.ok) {
                    throw new Error('아이디 중복 확인에 실패했습니다.');
                }
                return await response.json();
            } catch (error) {
                console.error('아이디 중복 확인 에러:', error);
                throw error;
            }
        },

        // 관리자 검색
        searchAdmins: async function(searchParams, page = 0, size = 10) {
            try {
                const queryString = new URLSearchParams({
                    ...searchParams,
                    page: page,
                    size: size
                }).toString();

                const response = await fetch(`/masterpage_sys/member/api/admin/search?${queryString}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Referer': window.location.href
                    }
                });

                if (!response.ok) {
                    throw new Error('관리자 검색에 실패했습니다.');
                }

                const data = await response.json();
                return data;
            } catch (error) {
                console.error('관리자 검색 에러:', error);
                throw error;
            }
        },

        // 교강사 검색
        searchTeachers: async function(searchParams, page = 0, size = 10) {
            try {
                const queryString = new URLSearchParams({
                    ...searchParams,
                    page: page,
                    size: size
                }).toString();

                const response = await fetch(`/masterpage_sys/member/api/teacher/search?${queryString}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Referer': window.location.href
                    }
                });

                if (!response.ok) {
                    throw new Error('교강사 검색에 실패했습니다.');
                }

                const data = await response.json();
                return data;
            } catch (error) {
                console.error('교강사 검색 에러:', error);
                throw error;
            }
        },

        // 학생 검색
        searchStudents: async function(searchParams, page = 0, size = 10) {
            try {
                const queryString = new URLSearchParams({
                    ...searchParams,
                    page: page,
                    size: size
                }).toString();

                const response = await fetch(`/masterpage_sys/member/api/student/search?${queryString}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Referer': window.location.href
                    }
                });

                if (!response.ok) {
                    throw new Error('학생 검색에 실패했습니다.');
                }

                const data = await response.json();
                return data;
            } catch (error) {
                console.error('학생 검색 에러:', error);
                throw error;
            }
        },

        // 기업 검색
        searchCompanies: async function(searchParams, page = 0, size = 10) {
            try {
                const queryString = new URLSearchParams({
                    ...searchParams,
                    page: page,
                    size: size
                }).toString();

                const response = await fetch(`/masterpage_sys/member/api/company/search?${queryString}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Referer': window.location.href
                    }
                });

                if (!response.ok) {
                    throw new Error('기업 검색에 실패했습니다.');
                }

                const data = await response.json();
                return data;
            } catch (error) {
                console.error('기업 검색 에러:', error);
                throw error;
            }
        },

        // 파일 열람
        viewFile: async function(type, fileName) {
            if (!fileName) {
                alert('파일이 존재하지 않습니다.');
                return;
            }
            
            try {
                // 파일 다운로드 URL 생성
                const url = `/api/v1/files/download/${type}/${fileName}`;
                
                // 새 창에서 파일 열기
                window.open(url, '_blank');
            } catch (error) {
                console.error('파일 열람 에러:', error);
                alert('파일 열람에 실패했습니다.');
            }
        },
    },

    // UI 렌더링 함수들
    render: {
        // 회원 목록 테이블 렌더링
        memberTable: function(members) {
            const tbody = document.querySelector('table.table01 tbody');
            if (!tbody) return;

            if (!members || members.length === 0) {
                tbody.innerHTML = '<tr><td colspan="12">등록된 회원이 없습니다.</td></tr>';
                return;
            }

            // 역순 번호 계산을 위한 시작 번호
            const startNumber = MemberModule.state.totalElements - (MemberModule.state.currentPage * MemberModule.state.size);

            // 페이지 타입에 따른 테이블 렌더링
            switch (MemberModule.state.pageType) {
                case 'teacher':
                    tbody.innerHTML = members.map((member, index) => `
                        <tr>
                            <td>
                                <label class="c-input ci-check single">
                                    <input type="checkbox" value="${member.memberIdx || ''}">
                                    <div class="ci-show"></div>
                                </label>
                            </td>
                            <td>${startNumber - index}</td>
                            <td>
                                <a href="/masterpage_sys/member/teacher/${member.memberIdx}" class="jv-btn underline01">${member.name || '-'}</a>
                            </td>
                            <td>${member.userId || '-'}</td>
                            <td>
                                ${member.telMobile ? `<label class="c-input ci-check">
                                    <input type="checkbox" data-type="telMobile" data-id="${member.memberIdx}">
                                    ${member.telMobile}
                                    <div class="ci-show"></div>
                                </label>` : ''}
                            </td>
                            <td>
                                ${member.email ? `<label class="c-input ci-check">
                                    <input type="checkbox" data-type="email" data-id="${member.memberIdx}">
                                    ${member.email}
                                    <div class="ci-show"></div>
                                </label>` : ''}
                            </td>
                            <td>첨삭 참여 건수 계산 필요</td>
                            <td>${member.createdDate ? new Date(member.createdDate).toLocaleDateString() : '-'}</td>
                            <td>${member.agreeDate ? new Date(member.agreeDate).toLocaleDateString() : '-'}</td>
                        </tr>
                    `).join('');
                    break;

                case 'company':
                    tbody.innerHTML = members.map((member, index) => `
                        <tr>
                            <td>
                                <label class="c-input ci-check single">
                                    <input type="checkbox" value="${member.memberIdx || ''}">
                                    <div class="ci-show"></div>
                                </label>
                            </td>
                            <td>${startNumber - index}</td>
                            <td>
                                <a href="/masterpage_sys/member/company/${member.memberIdx}" class="jv-btn underline01">${member.jobName || '-'}</a>
                            </td>
                            <td>${member.userId || '-'}</td>
                            <td>${member.contractorName || '-'}</td>
                            <td>
                                ${member.contractorTel ? `<label class="c-input ci-check">
                                    <input type="checkbox" data-type="contractorTel" data-id="${member.memberIdx}">
                                    ${member.contractorTel}
                                    <div class="ci-show"></div>
                                </label>` : ''}
                            </td>
                            <td>
                                ${member.contractorEtc ? `<label class="c-input ci-check">
                                    <input type="checkbox" data-type="contractorEtc" data-id="${member.memberIdx}">
                                    ${member.contractorEtc}
                                    <div class="ci-show"></div>
                                </label>` : ''}
                            </td>
                            <td>수강생 계산 필요</td>
                            <td>${member.jobCeo || '-'}</td>
                            <td>
                                <a href="#" class="jv-btn" onclick="window.open('/company/${member.memberIdx || ''}', '_blank')">
                                    바로가기
                                </a>
                            </td>
                        </tr>
                    `).join('');
                    break;

                case 'companySearch':
                    tbody.innerHTML = members.map((member, index) => `
                        <tr data-job-number="${member.jobNumber || ''}" 
                            data-job-insurance-number="${member.jobInsuranceNumber || ''}">
                            <td>
                                <label class="c-input ci-check single">
                                    <input type="checkbox" value="${member.memberIdx || ''}">
                                    <div class="ci-show"></div>
                                </label>
                            </td>
                            <td>${startNumber - index}</td>
                            <td>${member.contractorName || '-'}</td>
                            <td>${member.userId || '-'}</td>
                            <td>${member.jobName || '-'}</td>
                            <td>${member.jobRegion || '-'}</td>
                            <td>${member.jobCeo || '-'}</td>
                            <td>${member.jobScale || '-'}</td>
                            <td>${member.contractorTel || '-'}</td>
                            <td>${member.contractorEtc || '-'}</td>
                            <td>-</td>
                            <td>
                                <button type="button" class="jv-btn select-company" data-member-idx="${member.memberIdx}">선택</button>
                            </td>
                        </tr>
                    `).join('');
                    break;

                case 'admin':
                    tbody.innerHTML = members.map((member, index) => `
                        <tr>
                            <td>
                                <label class="c-input ci-check single">
                                    <input type="checkbox" value="${member.memberIdx || ''}">
                                    <div class="ci-show"></div>
                                </label>
                            </td>
                            <td>${startNumber - index}</td>
                            <td>
                                <a href="/masterpage_sys/member/admin/${member.memberIdx}" class="jv-btn underline01">${member.name || '-'}</a>
                            </td>
                            <td>${member.userId || '-'}</td>
                            <td>
                                ${member.telMobile ? `
                                    <label class="c-input ci-check">
                                        <input type="checkbox" data-type="telMobile" data-id="${member.memberIdx}">
                                        ${member.telMobile}
                                        <div class="ci-show"></div>
                                    </label>
                                ` : ''}
                            </td>
                            <td>
                                ${member.email ? `
                                    <label class="c-input ci-check">
                                        <input type="checkbox" data-type="email" data-id="${member.memberIdx}">
                                        ${member.email}
                                        <div class="ci-show"></div>
                                    </label>
                                ` : ''}
                            </td>
                            <td>${(() => {
                                switch(member.jobCourseDuty) {
                                    case 'REFUND': return '환급';
                                    case 'NORMAL': return '일반';
                                    default: return '-';
                                }
                            })()}</td>
                            <td>${member.authLevelName || '-'}</td>
                            <td>${(() => {
                                switch(member.jobWorkState) {
                                    case '001': return '근무';
                                    case '002': return '휴직';
                                    case '003': return '기타'; 
                                    case '004': return '퇴사';
                                    default: return '-';
                                }
                            })()}</td>
                            <td>${member.createdDate ? new Date(member.createdDate).toLocaleDateString() : '-'}</td>
                        </tr>
                    `).join('');
                    break;

                default: // student
                    tbody.innerHTML = members.map((member, index) => `
                        <tr>
                            <td>
                                <label class="c-input ci-check single">
                                    <input type="checkbox" value="${member.memberIdx || ''}">
                                    <div class="ci-show"></div>
                                </label>
                            </td>
                            <td>${startNumber - index}</td>
                            <td>
                                <a href="/masterpage_sys/member/student/${member.memberIdx}" class="jv-btn underline01">${member.name || '-'}</a>
                            </td>
                            <td>${member.userId || '-'}</td>
                            <td>${member.birthday || '-'}</td>
                            <td>
                                ${member.telMobile ? `<label class="c-input ci-check">
                                    <input type="checkbox" data-type="telMobile" data-id="${member.memberIdx}">
                                    ${member.telMobile}
                                    <div class="ci-show"></div>
                                </label>` : ''}
                            </td>
                            <td>
                                ${member.email ? `<label class="c-input ci-check">
                                    <input type="checkbox" data-type="email" data-id="${member.memberIdx}">
                                    ${member.email}
                                    <div class="ci-show"></div>
                                </label>` : ''}
                            </td>
                            <td>${member.chkSmsReceive === 'Y' ? '수신' : '미수신'}</td>
                            <td>${member.chkMailReceive === 'Y' ? '수신' : '미수신'}</td>
                            <td>${member.createdDate ? new Date(member.createdDate).toLocaleDateString() : '-'}</td>
                            <td>${member.agreeDate ? new Date(member.agreeDate).toLocaleDateString() : '-'}</td>
                        </tr>
                    `).join('');
            }

            // 총 건수 업데이트
            const countElement = document.querySelector('.count .Pretd_B');
            if (countElement) {
                countElement.textContent = MemberModule.state.totalElements;
            }
        },

        // 페이지네이션 렌더링
        pagination: function(totalPages) {
            const pagination = document.getElementById('paginationMember');
            if (!pagination) return;

            let paginationHtml = '';
            
            // 이전 페이지 버튼
            if (MemberModule.state.currentPage > 0) {
                paginationHtml += `<a href="#" class="prev" data-page="${MemberModule.state.currentPage - 1}"></a>`;
            }
            
            // 페이지 번호
            for (let i = 0; i < totalPages; i++) {
                if (i === MemberModule.state.currentPage) {
                    paginationHtml += `<a href="#" class="active" data-page="${i}">${i + 1}</a>`;
                } else {
                    paginationHtml += `<a href="#" data-page="${i}">${i + 1}</a>`;
                }
            }
            
            // 다음 페이지 버튼
            if (MemberModule.state.currentPage < totalPages - 1) {
                paginationHtml += `<a href="#" class="next" data-page="${MemberModule.state.currentPage + 1}"></a>`;
            }
            
            pagination.innerHTML = paginationHtml;

            // 이벤트 리스너 추가
            pagination.querySelectorAll('a').forEach(link => {
                link.addEventListener('click', async (e) => {
                    e.preventDefault();
                    const page = parseInt(link.dataset.page);
                    if (!isNaN(page)) {
                        await MemberModule.handlers.changePage(page);
                    }
                });
            });
        }
    },

    // 이벤트 핸들러
    handlers: {
        // 페이지 변경 핸들러
        changePage: async function(page) {
            if (page < 0 || page >= MemberModule.state.totalPages) return;
            
            try {
                let response;
                const urlParams = new URLSearchParams(window.location.search);
                const authLevel = urlParams.get('authLevel');
                
                // 현재 검색 조건 수집
                const searchParams = {};
                const searchForm = document.querySelector('form.search-form');
                if (searchForm) {
                    const formData = new FormData(searchForm);
                    for (let [key, value] of formData.entries()) {
                        if (value) {
                            searchParams[key] = value;
                        }
                    }
                }
                
                // 현재 페이지 타입에 따라 적절한 API 호출
                if (window.location.pathname.includes('/member/admin')) {
                    if (authLevel) {
                        searchParams.authLevel = authLevel;
                    }
                    response = await MemberModule.api.searchAdmins(searchParams, page, MemberModule.state.size);
                } else if (window.location.pathname.includes('/member/teacher')) {
                    response = await MemberModule.api.searchTeachers(searchParams, page, MemberModule.state.size);
                } else if (window.location.pathname.includes('/member/company')) {
                    response = await MemberModule.api.searchCompanies(searchParams, page, MemberModule.state.size);
                } else {
                    response = await MemberModule.api.searchStudents(searchParams, page, MemberModule.state.size);
                }

                if (response) {
                    MemberModule.state.members = response.memberList || [];
                    MemberModule.state.currentPage = response.pageable?.pageNumber || 0;
                    MemberModule.state.totalPages = response.pageable?.totalPages || 0;
                    MemberModule.state.totalElements = response.pageable?.totalElements || 0;
                    
                    MemberModule.render.memberTable(MemberModule.state.members);
                    MemberModule.render.pagination(MemberModule.state.totalPages);
                } else {
                    console.error('응답이 없거나 잘못된 형식입니다:', response);
                    throw new Error('Invalid response format');
                }
            } catch (error) {
                console.error('페이지 로딩 에러:', error);
                alert('페이지 로딩에 실패했습니다.');
            }
        },

        // 회원 삭제 핸들러
        deleteMembers: async function() {
            const checkedBoxes = document.querySelectorAll('table.table01 input[type="checkbox"]:checked');
            const ids = Array.from(checkedBoxes)
                .map(cb => cb.value)
                .filter(id => id && id !== 'undefined' && id !== ''); // 유효한 ID만 필터링

            if (ids.length === 0) {
                alert('삭제할 회원을 선택해주세요.');
                return;
            }

            if (!confirm('선택한 회원을 삭제하시겠습니까?')) return;

            try {
                await MemberModule.api.deleteMembers(ids);
                alert('선택한 회원이 삭제되었습니다.');
                window.location.reload();
            } catch (error) {
                alert('회원 삭제에 실패했습니다.');
            }
        },

        // 전체 선택 핸들러
        toggleAll: function(e) {
            const checkboxes = document.querySelectorAll('table.table01 tbody input[type="checkbox"]:not([data-type])');
            checkboxes.forEach(cb => cb.checked = e.target.checked);
        },

        // 컬럼별 전체 선택 핸들러
        toggleColumn: function(e, type) {
            const checkboxes = document.querySelectorAll(`table.table01 tbody input[type="checkbox"][data-type="${type}"]`);
            checkboxes.forEach(cb => cb.checked = e.target.checked);
        },

        // 카카오 주소검색 API 호출
        searchAddress: function() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분
                    
                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('zipcode').value = data.zonecode;
                    document.getElementById('addr1').value = data.roadAddress || data.jibunAddress;
                    
                    // 커서를 상세주소 필드로 이동한다.
                    document.getElementById('addr2').focus();
                },
                width : '100%',
                height : '100%'
            }).open();
        },

        // 회원 등록 폼 제출 핸들러 (공통)
        submitMemberSaveForm: async function(event) {
            event.preventDefault();
            
            try {
                // 아이디 중복 확인 여부 체크
                if (!MemberModule.state.isIdCheckPassed) {
                    alert('아이디 중복 확인이 필요합니다.');
                    document.getElementById('userId')?.focus();
                    return;
                }

                const data = {
                    // 공통 필드
                    userType: document.getElementById('userType').value,
                    userId: document.getElementById('userId')?.value,
                    pwd: document.getElementById('pwd')?.value,
                    name: document.getElementById('name')?.value,
                    birthday: document.getElementById('birthday')?.value,
                    sex: document.querySelector('input[name="sex"]:checked')?.value,
                    chkDormant: document.getElementById('chkDormant')?.checked ? 'N' : 'Y',
                    memberState: document.getElementById('memberState')?.checked ? 'N' : 'Y',
                    engName: document.getElementById('engName')?.value,
                    chkForeigner: document.getElementById('chkForeigner')?.checked ? 'N' : 'Y',
                    agreeDate: document.getElementById('agreeDate')?.value,
                    realDate: document.getElementById('realDate')?.value,
                    jobDept: document.getElementById('jobDept')?.value,
                    
                    // 휴대폰
                    telMobile: document.getElementById('telMobile1')?.value && document.getElementById('telMobile2')?.value && document.getElementById('telMobile3')?.value
                        ? document.getElementById('telMobile1')?.value + '-' + document.getElementById('telMobile2')?.value + '-' + document.getElementById('telMobile3')?.value
                        : null,
                    
                    // 이메일
                    email: document.getElementById('email1')?.value && document.getElementById('email2')?.value
                        ? document.getElementById('email1')?.value + '@' + document.getElementById('email2')?.value
                        : null,
                    
                    // 주소
                    zipcode: document.getElementById('zipcode')?.value,
                    addr1: document.getElementById('addr1')?.value,
                    addr2: document.getElementById('addr2')?.value,
                    
                    // 계좌정보
                    bankName: document.getElementById('bankName')?.value,
                    bankNumber: document.getElementById('bankNumber')?.value,
                    
                    // 추가정보
                    applyUserId: document.getElementById('applyUserId')?.value,
                    loginMemberCount: document.getElementById('loginMemberCount')?.value,
                    
                    // 기업 전용 필드
                    jobName: document.getElementById('jobName')?.value,
                    jobCeo: document.getElementById('jobCeo')?.value,
                    jobScale: document.querySelector('input[name="jobScale"]:checked')?.value,
                    corporationCode: document.querySelector('input[name="corporationCode"]:checked')?.value,
                    masterId: document.getElementById('masterId')?.value,
                    contractorName: document.getElementById('contractorName')?.value,
                    jobPosition: document.getElementById('jobPosition')?.value,
                    
                    // 기업 전화번호
                    jobTelOffice: document.getElementById('jobTelOffice1')?.value && document.getElementById('jobTelOffice2')?.value && document.getElementById('jobTelOffice3')?.value
                        ? document.getElementById('jobTelOffice1')?.value + '-' + document.getElementById('jobTelOffice2')?.value + '-' + document.getElementById('jobTelOffice3')?.value
                        : null,
                    
                    // 사업자번호
                    jobNumber: document.getElementById('jobNumber1')?.value && document.getElementById('jobNumber2')?.value && document.getElementById('jobNumber3')?.value
                        ? document.getElementById('jobNumber1')?.value + '-' + document.getElementById('jobNumber2')?.value + '-' + document.getElementById('jobNumber3')?.value
                        : null,
                    
                    // 고용보험관리번호
                    jobInsuranceNumber: document.getElementById('jobInsuranceNumber1')?.value && document.getElementById('jobInsuranceNumber2')?.value && document.getElementById('jobInsuranceNumber3')?.value && document.getElementById('jobInsuranceNumber4')?.value
                        ? document.getElementById('jobInsuranceNumber1')?.value + '-' + document.getElementById('jobInsuranceNumber2')?.value + '-' + document.getElementById('jobInsuranceNumber3')?.value + '-' + document.getElementById('jobInsuranceNumber4')?.value
                        : null,

                    // 관리자 전용 필드
                    authLevel: document.getElementById('authLevel')?.value,
                    jobWorkState: document.getElementById('jobWorkState')?.value,
                    jobDuty: document.getElementById('jobDuty')?.value,
                    jobCourseDuty: document.querySelector('input[name="jobCourseDuty"]:checked')?.value,
                    loginDenyIp: document.getElementById('loginDenyIp')?.value,

                    // 주민등록번호 (학생 전용)
                    residentNumber: document.getElementById('residentNumber1')?.value && document.getElementById('residentNumber2')?.value && document.getElementById('residentNumber3')?.value
                        ? `${document.getElementById('residentNumber1')?.value}-${document.getElementById('residentNumber2')?.value}${document.getElementById('residentNumber3')?.value}`
                        : null,

                    // 훈련생 구분 (학생 전용)
                    trneeSe: document.getElementById('trneeSe')?.value,
                    irglbrSe: document.getElementById('irglbrSe')?.value,

                    // SMS/이메일 수신 여부
                    chkSmsReceive: document.querySelector('input[name="chkSmsReceive"]:checked')?.value,
                    chkMailReceive: document.querySelector('input[name="chkMailReceive"]:checked')?.value,

                    // 본인인증/비밀번호 변경 예외처리
                    chkIdentityVerification: document.querySelector('input[name="chkIdentityVerification"]:checked')?.value,
                    chkPwdChange: document.querySelector('input[name="chkPwdChange"]:checked')?.value,

                    // 기업 전용 홈페이지 설정
                    companyHomepageUse: document.querySelector('input[name="companyHomepageUse"]:checked')?.value,
                    companyUrl: document.getElementById('companyUrl')?.value,
                    counselNumber: document.getElementById('counselNumber')?.value,
                    counselTime: document.getElementById('counselTime1')?.value && document.getElementById('counselTime2')?.value
                        ? `${document.getElementById('counselTime1')?.value}-${document.getElementById('counselTime2')?.value}`
                        : null,

                    // 이미지 처리
                    mainImg: (() => {
                        const mainImgRadio = document.querySelector('input[name="mainImg"]:checked');
                        if (!mainImgRadio) return null;
                        if (mainImgRadio.value === 'custom') {
                            const value = document.getElementById('mainImgUserFile')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return mainImgRadio.value;
                    })(),
                    mainImgName: (() => {
                        const mainImgRadio = document.querySelector('input[name="mainImg"]:checked');
                        if (!mainImgRadio) return null;
                        if (mainImgRadio.value === 'custom') {
                            const value = document.getElementById('mainImgDisplay')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return mainImgRadio.value;
                    })(),
                    subImg: (() => {
                        const subImgRadio = document.querySelector('input[name="subImg"]:checked');
                        if (!subImgRadio) return null;
                        if (subImgRadio.value === 'custom') {
                            const value = document.getElementById('subImgUserFile')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return subImgRadio.value;
                    })(),
                    subImgName: (() => {
                        const subImgRadio = document.querySelector('input[name="subImg"]:checked');
                        if (!subImgRadio) return null;
                        if (subImgRadio.value === 'custom') {
                            const value = document.getElementById('subImgDisplay')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return subImgRadio.value;
                    })(),
                    fnameSaup: document.getElementById('fnameSaupUserFile')?.value || null,  // 명시적으로 null 설정
                    fnameSaupName: document.getElementById('fnameSaupDisplay')?.value || null,  // 명시적으로 null 설정
                    fnameLogo: document.getElementById('fnameLogoUserFile')?.value || null,  // 명시적으로 null 설정
                    fnameLogoName: document.getElementById('fnameLogoDisplay')?.value || null,  // 명시적으로 null 설정
                };

                // 필수 입력값 검증
                const requiredFields = ['userId', 'pwd'];
                
                // userType에 따른 추가 필수 필드
                switch(data.userType) {
                    case 'STUDENT':
                        requiredFields.push('name', 'birthday', 'residentNumber', 'jobName', 'trneeSe', 'irglbrSe');
                        break;
                    case 'TEACHER':
                        requiredFields.push('name');
                        break;
                    case 'COMPANY':
                        requiredFields.push('jobName', 'contractorName');
                        break;
                    case 'ADMIN':
                        requiredFields.push('name', 'authLevel');
                        break;
                }

                // 기본 필드 검증
                for (const field of requiredFields) {
                    if (!data[field]) {
                        const fieldName = MemberModule.state.fieldNames[field] || field;
                        alert(`${fieldName}은(는) 필수 입력 항목입니다.`);
                        if (field === 'residentNumber') {
                            if(document.getElementById('residentNumber1')?.value===''){
                                document.getElementById('residentNumber1')?.focus();
                            }else if(document.getElementById('residentNumber2')?.value===''){
                                document.getElementById('residentNumber2')?.focus();
                            }else if(document.getElementById('residentNumber3')?.value===''){
                                document.getElementById('residentNumber3')?.focus();
                            }
                        } else if(field === 'jobName'){
                            if(document.getElementById('jobName')?.value===''){
                                document.getElementById('jobName')?.focus();
                            }
                        } else {
                            document.getElementById(field)?.focus();
                        }
                        return;
                    }
                }

                // 휴대폰 번호 검증 (관리자 제외한 모든 회원 타입 공통)
                if (data.userType !== 'ADMIN') {
                    const telMobile2 = document.getElementById('telMobile2')?.value;
                    const telMobile3 = document.getElementById('telMobile3')?.value;
                    if (!telMobile2 || !telMobile3) {
                        alert('휴대폰 번호를 모두 입력해주세요.');
                        if (!telMobile2) document.getElementById('telMobile2')?.focus();
                        else document.getElementById('telMobile3')?.focus();
                        return;
                    }
                }

                // 이메일 검증 (관리자 제외한 모든 회원 타입 공통)
                if (data.userType !== 'ADMIN') {
                    const email1 = document.getElementById('email1')?.value;
                    const email2 = document.getElementById('email2')?.value;
                    if (!email1 || !email2) {
                        alert('이메일을 모두 입력해주세요.');
                        if (!email1) document.getElementById('email1')?.focus();
                        else document.getElementById('email2')?.focus();
                        return;
                    }
                }

                // 전화번호와 이메일 조합 (회원 타입에 따라 다른 필드에 저장)
                if (data.userType === 'COMPANY') {
                    // 기업 회원의 경우 교육담당자 연락처 필드에 저장
                    data.contractorTel = `${document.getElementById('telMobile1').value}-${document.getElementById('telMobile2').value}-${document.getElementById('telMobile3').value}`;
                    data.contractorEtc = `${document.getElementById('email1').value}@${document.getElementById('email2').value}`;
                } else if (data.userType == 'STUDENT' || data.userType == 'TEACHER') {
                    // 학생 또는 교강사 회원의 경우 일반 연락처 필드에 저장
                    data.telMobile = `${document.getElementById('telMobile1')?.value || ''}-${document.getElementById('telMobile2')?.value || ''}-${document.getElementById('telMobile3')?.value || ''}`;
                    data.email = `${document.getElementById('email1')?.value || ''}@${document.getElementById('email2')?.value || ''}`;
                } else {
                    // 관리자 회원의 경우 값이 없으면 빈 문자열로 저장
                    const telMobile1 = document.getElementById('telMobile1')?.value || '';
                    const telMobile2 = document.getElementById('telMobile2')?.value || '';
                    const telMobile3 = document.getElementById('telMobile3')?.value || '';
                    const email1 = document.getElementById('email1')?.value || '';
                    const email2 = document.getElementById('email2')?.value || '';

                    data.telMobile = telMobile1 || telMobile2 || telMobile3 ? `${telMobile1 || ''}-${telMobile2 || ''}-${telMobile3 || ''}` : '';
                    data.email = email1 || email2 ? `${email1 || ''}@${email2 || ''}` : '';
                }

                    // 이미지 파일 정보 추가
                    const mainImgFile = document.getElementById('mainImgFile');
                    const subImgFile = document.getElementById('subImgFile');
                    const logoFile = document.getElementById('fnameLogo');
    
                    // 메인 이미지 정보
                    if (mainImgFile && mainImgFile.dataset.uploadedFileName) {
                        data.mainImg = mainImgFile.dataset.uploadedFileName;
                        data.mainImgName = mainImgFile.dataset.originalFileName;
                    }
    
                    // 서브 이미지 정보
                    if (subImgFile && subImgFile.dataset.uploadedFileName) {
                        data.subImg = subImgFile.dataset.uploadedFileName;
                        data.subImgName = subImgFile.dataset.originalFileName;
                    }
    
                    // 로고 이미지 정보
                    if (logoFile && logoFile.dataset.uploadedFileName) {
                        data.fnameLogo = logoFile.dataset.uploadedFileName;
                        data.fnameLogoName = logoFile.dataset.originalFileName;
                    }
                
            
                // 빈 값이나 null 값을 가진 속성 제거하는 부분 수정
                Object.keys(data).forEach(key => {
                    // 파일 관련 필드는 명시적으로 null을 유지하고, 나머지는 기존처럼 처리
                    const isFileField = ['mainImg', 'mainImgName', 'subImg', 'subImgName', 
                                       'fnameSaup', 'fnameSaupName', 'fnameLogo', 'fnameLogoName'].includes(key);
                    if (!isFileField && (data[key] === null || data[key] === '' || data[key] === undefined)) {
                        delete data[key];
                    }
                });

                // API 호출
                const response = await fetch('/masterpage_sys/member/api', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                if (!response.ok) {
                    throw new Error('회원 등록에 실패했습니다.');
                }

                const result = await response.json();

                if (result.success) {
                    // 파일 이동 처리
                    const uploadedFiles = [];
                    const fileInputs = document.querySelectorAll('.hidden-file-input');
                    
                    // 새로 업로드된 파일만 이동 처리
                    fileInputs.forEach(input => {
                        // dataset에 uploadedFileName이 있는 경우에만 이동 대상에 추가
                        if (input.dataset.uploadedFileName) {
                            uploadedFiles.push(input.dataset.uploadedFileName);
                            console.log('이동할 파일:', input.dataset.uploadedFileName); // 디버깅용 로그
                        }
                    });

                    // 업로드된 파일들을 temp에서 member 폴더로 이동
                    for (const fileName of uploadedFiles) {
                        try {
                            console.log('파일 이동 시도:', fileName); // 디버깅용 로그
                            const moveResponse = await fetch(`/api/v1/files/move/member/${fileName}`, {
                                method: 'POST'
                            });

                            if (!moveResponse.ok) {
                                console.error(`파일 이동 실패: ${fileName}`);
                                throw new Error('파일 이동에 실패했습니다.');
                            }

                            const moveResult = await moveResponse.json();
                            if (!moveResult.success) {
                                throw new Error(moveResult.message || '파일 이동에 실패했습니다.');
                            }

                            console.log('파일 이동 성공:', fileName); // 디버깅용 로그

                            // 이동 성공 후 dataset 초기화
                            fileInputs.forEach(input => {
                                if (input.dataset.uploadedFileName === fileName) {
                                    delete input.dataset.uploadedFileName;
                                }
                            });
                        } catch (error) {
                            console.error('파일 이동 중 오류 발생:', error);
                            throw new Error('파일 이동 중 오류가 발생했습니다.');
                        }
                    }

                    const userTypeText = {
                        'STUDENT': '학생',
                        'TEACHER': '교강사',
                        'COMPANY': '기업',
                        'ADMIN': '관리자'
                    }[data.userType];
                    
                    alert(`${userTypeText} 등록 완료.`);
                    window.location.href = `/masterpage_sys/member/${data.userType.toLowerCase()}`;
                } else {
                    alert(result.message || '회원 등록에 실패했습니다.');
                }
            } catch (error) {
                console.error('회원 등록 에러:', error);
                alert(error.message || '회원 등록에 실패했습니다.');
            }
        },

        // 아이디 중복 확인 핸들러
        checkIdDuplication: async function() {
            const userId = document.getElementById('userId').value;
            if (!userId) {
                alert('아이디를 입력해주세요.');
                return;
            }

            try {
                const response = await MemberModule.api.checkDuplicateId(userId);
                if (response.response) {
                    alert('사용 가능한 아이디입니다.');
                    MemberModule.state.isIdCheckPassed = true; // 중복 확인 통과
                } else {
                    alert('이미 사용 중인 아이디입니다.');
                    MemberModule.state.isIdCheckPassed = false; // 중복 확인 실패
                }
            } catch (error) {
                alert(error.message);
                MemberModule.state.isIdCheckPassed = false; // 오류 발생 시 중복 확인 실패 처리
            }
        },

        // 회사찾기 팝업 열기
        openCompanySearch: function() {
            const popupWidth = 1200;
            const popupHeight = 800;
            const left = (window.screen.width - popupWidth) / 2;
            const top = (window.screen.height - popupHeight) / 2;
            
            window.open('/masterpage_sys/member/company/search', 'companySearch',
                `width=${popupWidth},height=${popupHeight},left=${left},top=${top}`);
        },

        // 회사 정보 설정 (팝업에서 호출됨)
        setCompanyInfo: function(companyInfo) {
            try {
                // 회사명
                const jobNameInput = document.getElementById('jobName');
                if (jobNameInput) {
                    jobNameInput.value = companyInfo.jobName || '';
                }
                
                // 사업자번호 설정
                if (companyInfo.jobNumber) {
                    const numbers = companyInfo.jobNumber.split('-');
                    if (numbers.length === 3) {
                        const jobNumber1 = document.getElementById('jobNumber1');
                        const jobNumber2 = document.getElementById('jobNumber2');
                        const jobNumber3 = document.getElementById('jobNumber3');
                        
                        if (jobNumber1) jobNumber1.value = numbers[0];
                        if (jobNumber2) jobNumber2.value = numbers[1];
                        if (jobNumber3) jobNumber3.value = numbers[2];
                    }
                }
                
                // 고용보험관리번호 설정
                if (companyInfo.jobInsuranceNumber) {
                    const numbers = companyInfo.jobInsuranceNumber.split('-');
                    if (numbers.length === 4) {
                        const jobInsuranceNumber1 = document.getElementById('jobInsuranceNumber1');
                        const jobInsuranceNumber2 = document.getElementById('jobInsuranceNumber2');
                        const jobInsuranceNumber3 = document.getElementById('jobInsuranceNumber3');
                        const jobInsuranceNumber4 = document.getElementById('jobInsuranceNumber4');
                        
                        if (jobInsuranceNumber1) jobInsuranceNumber1.value = numbers[0];
                        if (jobInsuranceNumber2) jobInsuranceNumber2.value = numbers[1];
                        if (jobInsuranceNumber3) jobInsuranceNumber3.value = numbers[2];
                        if (jobInsuranceNumber4) jobInsuranceNumber4.value = numbers[3];
                    }
                }
            } catch (error) {
                console.error('회사 정보 설정 중 오류 발생:', error);
            }
        },

        // 기업 검색 팝업에서 기업 선택
        selectCompany: function(memberIdx) {
            try {
                const selectedRow = document.querySelector(`input[type="checkbox"][value="${memberIdx}"]`).closest('tr');
                if (!selectedRow) {
                    console.error('선택된 행을 찾을 수 없습니다.');
                    return;
                }

                const companyInfo = {
                    jobName: selectedRow.querySelector('td:nth-child(5)').textContent.trim(),
                    jobNumber: selectedRow.getAttribute('data-job-number'),
                    jobInsuranceNumber: selectedRow.getAttribute('data-job-insurance-number')
                };

                // 부모 창이 존재하는지 확인
                if (!window.opener) {
                    console.error('부모 창을 찾을 수 없습니다.');
                    return;
                }

                // 부모 창의 필드에 직접 값 설정
                const parentDocument = window.opener.document;
                
                // 회사명 설정
                const jobNameInput = parentDocument.getElementById('jobName');
                if (jobNameInput) {
                    jobNameInput.value = companyInfo.jobName || '';
                }
                
                // 사업자번호 설정
                if (companyInfo.jobNumber) {
                    const numbers = companyInfo.jobNumber.split('-');
                    if (numbers.length === 3) {
                        const jobNumber1 = parentDocument.getElementById('jobNumber1');
                        const jobNumber2 = parentDocument.getElementById('jobNumber2');
                        const jobNumber3 = parentDocument.getElementById('jobNumber3');
                        
                        if (jobNumber1) jobNumber1.value = numbers[0];
                        if (jobNumber2) jobNumber2.value = numbers[1];
                        if (jobNumber3) jobNumber3.value = numbers[2];
                    }
                }
                
                // 고용보험관리번호 설정
                if (companyInfo.jobInsuranceNumber) {
                    const numbers = companyInfo.jobInsuranceNumber.split('-');
                    if (numbers.length === 4) {
                        const jobInsuranceNumber1 = parentDocument.getElementById('jobInsuranceNumber1');
                        const jobInsuranceNumber2 = parentDocument.getElementById('jobInsuranceNumber2');
                        const jobInsuranceNumber3 = parentDocument.getElementById('jobInsuranceNumber3');
                        const jobInsuranceNumber4 = parentDocument.getElementById('jobInsuranceNumber4');
                        
                        if (jobInsuranceNumber1) jobInsuranceNumber1.value = numbers[0];
                        if (jobInsuranceNumber2) jobInsuranceNumber2.value = numbers[1];
                        if (jobInsuranceNumber3) jobInsuranceNumber3.value = numbers[2];
                        if (jobInsuranceNumber4) jobInsuranceNumber4.value = numbers[3];
                    }
                }

                // 팝업 창 닫기
                window.close();
            } catch (error) {
                console.error('기업 선택 중 오류 발생:', error);
            }
        },

        // 검색 핸들러
        search: async function(e) {
            e.preventDefault();

            // URL에서 authLevel 파라미터 확인
            const urlParams = new URLSearchParams(window.location.search);
            const authLevel = urlParams.get('authLevel');

            // 검색 파라미터 수집
            const searchParams = {};

            // 페이지 타입에 따른 검색 파라미터 설정
            if (MemberModule.state.pageType === 'admin') {
                // 관리자 검색 파라미터
                searchParams.name = document.getElementById('name')?.value || null;
                searchParams.userId = document.getElementById('userId')?.value || null;
                searchParams.jobCourseDuty = document.getElementById('jobCourseDuty')?.value || null;
                if (authLevel) {
                    searchParams.authLevel = authLevel;
                }
            } else if (MemberModule.state.pageType === 'teacher') {
                // 교강사 검색 파라미터
                searchParams.name = document.getElementById('name')?.value || null;
                searchParams.userId = document.getElementById('userId')?.value || null;
                searchParams.jobEmployeeType = document.querySelector('input[name="jobEmployeeType"]:checked')?.value || null;
                searchParams.telMobile = document.getElementById('telMobile')?.value || null;
                searchParams.email = document.getElementById('email')?.value || null;
            } else if (MemberModule.state.pageType === 'company') {
                // 기업 검색 파라미터
                searchParams.jobName = document.getElementById('jobName')?.value || null;
                searchParams.userId = document.getElementById('userId')?.value || null;
                searchParams.jobNumber = document.getElementById('jobNumber')?.value || null;
                searchParams.contractorName = document.getElementById('contractorName')?.value || null;
                searchParams.contractorTel = document.getElementById('contractorTel')?.value || null;
                searchParams.contractorEtc = document.getElementById('contractorEtc')?.value || null;
                searchParams.jobScale = document.querySelector('input[name="company"]:checked')?.value || null;
            } else {
                // 학생 검색 파라미터
                searchParams.name = document.getElementById('name')?.value || null;
                searchParams.userId = document.getElementById('userId')?.value || null;
                searchParams.birthYear = document.getElementById('birthYear')?.value || null;
                searchParams.birthMonth = document.getElementById('birthMonth')?.value || null;
                searchParams.birthDay = document.getElementById('birthDay')?.value || null;
                searchParams.telMobile = document.getElementById('telMobile')?.value || null;
                searchParams.email = document.getElementById('email')?.value || null;
                searchParams.chkDormant = document.querySelector('input[name="chkDormant"]:checked')?.value || null;
                searchParams.loginPass = document.querySelector('input[name="loginPass"]:checked')?.value || null;
                searchParams.chkForeigner = document.querySelector('input[name="chkForeigner"]:checked')?.value || null;
                searchParams.sex = document.querySelector('input[name="sex"]:checked')?.value || null;
                searchParams.jobName = document.getElementById('jobName')?.value || null;
                searchParams.jobWorkState = document.getElementById('jobWorkState')?.value || null;
                searchParams.jobDept = document.getElementById('jobDept')?.value || null;
                searchParams.chkSmsReceive = document.querySelector('input[name="chkSmsReceive"]:checked')?.value || null;
                searchParams.chkMailReceive = document.querySelector('input[name="chkMailReceive"]:checked')?.value || null;
                searchParams.chkIdentityVerification = document.querySelector('input[name="chkIdentityVerification"]:checked')?.value || null;
                searchParams.loginClientIp = document.getElementById('loginClientIp')?.value || null;
            }

            // 빈 값이나 null 값을 가진 속성 제거
            Object.keys(searchParams).forEach(key => {
                if (searchParams[key] === null || searchParams[key] === '') {
                    delete searchParams[key];
                }
            });

            try {
                let response;
                if (MemberModule.state.pageType === 'teacher') {
                    response = await MemberModule.api.searchTeachers(searchParams, 0, MemberModule.state.size);
                } else if (MemberModule.state.pageType === 'company') {
                    response = await MemberModule.api.searchCompanies(searchParams, 0, MemberModule.state.size);
                } else if (MemberModule.state.pageType === 'admin') {
                    response = await MemberModule.api.searchAdmins(searchParams, 0, MemberModule.state.size);
                } else {
                    response = await MemberModule.api.searchStudents(searchParams, 0, MemberModule.state.size);
                }

                // 응답 구조 확인 및 데이터 추출
                const responseData = response.response || response;
                const memberList = responseData.memberList;
                const pageable = responseData.pageable;

                if (!memberList || !pageable) {
                    throw new Error('Invalid response format');
                }

                MemberModule.state.members = memberList;
                MemberModule.state.currentPage = pageable.pageNumber;
                MemberModule.state.totalPages = pageable.totalPages;
                MemberModule.state.totalElements = pageable.totalElements;

                MemberModule.render.memberTable(MemberModule.state.members);
                MemberModule.render.pagination(MemberModule.state.totalPages);
            } catch (error) {
                console.error('검색 에러:', error);
                alert('검색에 실패했습니다.');
            }
        },

        // 상세검색 토글
        toggleDetailSearch: function() {
            const hiddenArea = document.querySelector('.column-tc-wrap.hidden');
            const toggleBtn = document.querySelector('.search-more-btn');
            if (hiddenArea && toggleBtn) {
                hiddenArea.classList.toggle('show');
                toggleBtn.classList.toggle('active');
            }
        },

        // 회원 수정 폼 제출 핸들러 (공통)
        submitMemberUpdateForm: async function(event) {
            event.preventDefault();
            
            try {
                const data = {
                    // 공통 필드
                    memberIdx: document.getElementById('memberIdx')?.value,
                    userType: document.getElementById('userType')?.value,
                    userId: document.getElementById('userId')?.value,
                    name: document.getElementById('name')?.value,
                    birthday: document.getElementById('birthday')?.value,
                    sex: document.querySelector('input[name="sex"]:checked')?.value,
                    chkDormant: document.getElementById('chkDormant')?.checked ? 'N' : 'Y',
                    memberState: document.getElementById('memberState')?.checked ? 'N' : 'Y',
                    engName: document.getElementById('engName')?.value,
                    chkForeigner: document.getElementById('chkForeigner')?.checked ? 'N' : 'Y',
                    agreeDate: document.getElementById('agreeDate')?.value,
                    realDate: document.getElementById('realDate')?.value,
                    applyUserId: document.getElementById('applyUserId')?.value,
                    jobDept: document.getElementById('jobDept')?.value,

                    // 주민등록번호 (학생 전용)
                    residentNumber: document.getElementById('residentNumber1')?.value && document.getElementById('residentNumber2')?.value && document.getElementById('residentNumber3')?.value
                        ? `${document.getElementById('residentNumber1')?.value}-${document.getElementById('residentNumber2')?.value}${document.getElementById('residentNumber3')?.value}`
                        : null,

                    // 휴대폰
                    telMobile: document.getElementById('telMobile1')?.value && document.getElementById('telMobile2')?.value && document.getElementById('telMobile3')?.value
                        ? document.getElementById('telMobile1')?.value + '-' + document.getElementById('telMobile2')?.value + '-' + document.getElementById('telMobile3')?.value
                        : null,

                    // 이메일
                    email: document.getElementById('email1')?.value && document.getElementById('email2')?.value
                        ? document.getElementById('email1')?.value + '@' + document.getElementById('email2')?.value
                        : null,

                    // 계좌정보
                    bankName: document.getElementById('bankName')?.value,
                    bankNumber: document.getElementById('bankNumber')?.value,

                    // 관리자 전용 필드
                    authLevel: document.getElementById('authLevel')?.value,
                    jobWorkState: document.getElementById('jobWorkState')?.value,
                    jobDuty: document.getElementById('jobDuty')?.value,
                    jobCourseDuty: document.querySelector('input[name="jobCourseDuty"]:checked')?.value,
                    loginDenyIp: document.getElementById('loginDenyIp')?.value,
                    loginMemberCount: document.getElementById('loginMemberCount')?.value,
                    jobPosition: document.getElementById('jobPosition')?.value,

                    // 기업 전용 필드
                    jobName: document.getElementById('jobName')?.value,
                    jobCeo: document.getElementById('jobCeo')?.value,
                    jobScale: document.querySelector('input[name="jobScale"]:checked')?.value,
                    corporationCode: document.querySelector('input[name="corporationCode"]:checked')?.value,
                    masterId: document.getElementById('masterId')?.value,
                    contractorName: document.getElementById('contractorName')?.value,

                    // 기업 전화번호
                    jobTelOffice: document.getElementById('jobTelOffice1')?.value && document.getElementById('jobTelOffice2')?.value && document.getElementById('jobTelOffice3')?.value
                        ? document.getElementById('jobTelOffice1')?.value + '-' + document.getElementById('jobTelOffice2')?.value + '-' + document.getElementById('jobTelOffice3')?.value
                        : null,

                    // 사업자번호
                    jobNumber: document.getElementById('jobNumber1')?.value && document.getElementById('jobNumber2')?.value && document.getElementById('jobNumber3')?.value
                        ? document.getElementById('jobNumber1')?.value + '-' + document.getElementById('jobNumber2')?.value + '-' + document.getElementById('jobNumber3')?.value
                        : null,

                    // 고용보험관리번호
                    jobInsuranceNumber: document.getElementById('jobInsuranceNumber1')?.value && document.getElementById('jobInsuranceNumber2')?.value && document.getElementById('jobInsuranceNumber3')?.value && document.getElementById('jobInsuranceNumber4')?.value
                        ? document.getElementById('jobInsuranceNumber1')?.value + '-' + document.getElementById('jobInsuranceNumber2')?.value + '-' + document.getElementById('jobInsuranceNumber3')?.value + '-' + document.getElementById('jobInsuranceNumber4')?.value
                        : null,

                    // 주소
                    zipcode: document.getElementById('zipcode')?.value,
                    addr1: document.getElementById('addr1')?.value,
                    addr2: document.getElementById('addr2')?.value,

                    // 기업 전용 홈페이지 설정
                    companyHomepageUse: document.querySelector('input[name="companyHomepageUse"]:checked')?.value,
                    companyUrl: document.getElementById('companyUrl')?.value,
                    counselNumber: document.getElementById('counselNumber')?.value,
                    counselTime: document.getElementById('counselTime1')?.value && document.getElementById('counselTime2')?.value
                        ? `${document.getElementById('counselTime1')?.value}-${document.getElementById('counselTime2')?.value}`
                        : null,

                    // 이미지 정보
                    mainImg: (() => {
                        const mainImgRadio = document.querySelector('input[name="mainImg"]:checked');
                        if (!mainImgRadio) return null;
                        if (mainImgRadio.value === 'custom') {
                            const value = document.getElementById('mainImgUserFile')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return mainImgRadio.value;
                    })(),
                    mainImgName: (() => {
                        const mainImgRadio = document.querySelector('input[name="mainImg"]:checked');
                        if (!mainImgRadio) return null;
                        if (mainImgRadio.value === 'custom') {
                            const value = document.getElementById('mainImgDisplay')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return mainImgRadio.value;
                    })(),
                    subImg: (() => {
                        const subImgRadio = document.querySelector('input[name="subImg"]:checked');
                        if (!subImgRadio) return null;
                        if (subImgRadio.value === 'custom') {
                            const value = document.getElementById('subImgUserFile')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return subImgRadio.value;
                    })(),
                    subImgName: (() => {
                        const subImgRadio = document.querySelector('input[name="subImg"]:checked');
                        if (!subImgRadio) return null;
                        if (subImgRadio.value === 'custom') {
                            const value = document.getElementById('subImgDisplay')?.value;
                            return value || null;  // 값이 없으면 명시적으로 null 반환
                        }
                        return subImgRadio.value;
                    })(),
                    fnameSaup: document.getElementById('fnameSaupUserFile')?.value || null,  // 명시적으로 null 설정
                    fnameSaupName: document.getElementById('fnameSaupDisplay')?.value || null,  // 명시적으로 null 설정
                    fnameLogo: document.getElementById('fnameLogoUserFile')?.value || null,  // 명시적으로 null 설정
                    fnameLogoName: document.getElementById('fnameLogoDisplay')?.value || null,  // 명시적으로 null 설정

                    // 교육담당자 연락처 (기업 회원)
                    contractorTel: document.getElementById('telMobile1')?.value && document.getElementById('telMobile2')?.value && document.getElementById('telMobile3')?.value
                        ? `${document.getElementById('telMobile1')?.value}-${document.getElementById('telMobile2')?.value}-${document.getElementById('telMobile3')?.value}`
                        : null,
                    contractorEtc: document.getElementById('email1')?.value && document.getElementById('email2')?.value
                        ? `${document.getElementById('email1')?.value}@${document.getElementById('email2')?.value}`
                        : null,

                    // 학생 전용 필드
                    trneeSe: document.getElementById('trneeSe')?.value,
                    irglbrSe: document.getElementById('irglbrSe')?.value,

                    // SMS/이메일 수신 여부
                    chkSmsReceive: document.querySelector('input[name="chkSmsReceive"]:checked')?.value,
                    chkMailReceive: document.querySelector('input[name="chkMailReceive"]:checked')?.value,

                    // 본인인증/비밀번호 변경 예외처리
                    chkIdentityVerification: document.querySelector('input[name="chkIdentityVerification"]:checked')?.value,
                    chkPwdChange: document.querySelector('input[name="chkPwdChange"]:checked')?.value
                };

                // 비밀번호가 입력된 경우에만 추가
                const pwdValue = document.getElementById('pwd')?.value;
                if (pwdValue && pwdValue.trim() !== '') {
                    data.pwd = pwdValue;
                }

                // 필수 입력값 검증
                const requiredFields = ['memberIdx', 'userId'];
                
                // userType에 따른 추가 필수 필드
                switch(data.userType) {
                    case 'ADMIN':
                        requiredFields.push('name', 'authLevel');
                        break;
                    case 'COMPANY':
                        requiredFields.push('jobName', 'contractorName');
                        break;
                }

                // 기본 필드 검증
                for (const field of requiredFields) {
                    if (!data[field]) {
                        const fieldName = MemberModule.state.fieldNames[field] || field;
                        alert(`${fieldName}은(는) 필수 입력 항목입니다.`);
                        document.getElementById(field)?.focus();
                        return;
                    }
                }

                // 빈 값이나 null 값을 가진 속성 제거하는 부분 수정
                Object.keys(data).forEach(key => {
                    // 파일 관련 필드는 명시적으로 null을 유지하고, 나머지는 기존처럼 처리
                    const isFileField = ['mainImg', 'mainImgName', 'subImg', 'subImgName', 
                                       'fnameSaup', 'fnameSaupName', 'fnameLogo', 'fnameLogoName'].includes(key);
                    if (!isFileField && (data[key] === null || data[key] === '' || data[key] === undefined)) {
                        delete data[key];
                    }
                });

                // API 호출
                const response = await fetch(`/masterpage_sys/member/api/${data.memberIdx}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                if (!response.ok) {
                    throw new Error('회원 수정에 실패했습니다.');
                }

                const result = await response.json();

                if (result.success) {
                    // 파일 이동 처리
                    const uploadedFiles = [];
                    const fileInputs = document.querySelectorAll('.hidden-file-input');
                    
                    // 새로 업로드된 파일만 이동 처리
                    fileInputs.forEach(input => {
                        if (input.dataset.uploadedFileName) {
                            uploadedFiles.push(input.dataset.uploadedFileName);
                            console.log('이동할 파일:', input.dataset.uploadedFileName); // 디버깅용 로그
                        }
                    });

                    // 업로드된 파일들을 temp에서 member 폴더로 이동
                    for (const fileName of uploadedFiles) {
                        try {
                            console.log('파일 이동 시도:', fileName); // 디버깅용 로그
                            const moveResponse = await fetch(`/api/v1/files/move/member/${fileName}`, {
                                method: 'POST'
                            });

                            if (!moveResponse.ok) {
                                console.error(`파일 이동 실패: ${fileName}`);
                                throw new Error('파일 이동에 실패했습니다.');
                            }

                            const moveResult = await moveResponse.json();
                            if (!moveResult.success) {
                                throw new Error(moveResult.message || '파일 이동에 실패했습니다.');
                            }

                            console.log('파일 이동 성공:', fileName); // 디버깅용 로그

                            // 이동 성공 후 dataset 초기화
                            fileInputs.forEach(input => {
                                if (input.dataset.uploadedFileName === fileName) {
                                    delete input.dataset.uploadedFileName;
                                }
                            });
                        } catch (error) {
                            console.error('파일 이동 중 오류 발생:', error);
                            throw new Error('파일 이동 중 오류가 발생했습니다.');
                        }
                    }

                    const userTypeText = {
                        'STUDENT': '학생',
                        'TEACHER': '교강사',
                        'COMPANY': '기업',
                        'ADMIN': '관리자'
                    }[data.userType];

                    alert(`${userTypeText} 수정이 완료되었습니다.`);
                    // window.location.reload();
                    window.location.href = `/masterpage_sys/member/${data.userType.toLowerCase()}`;

                } else {
                    alert(result.message || '회원 수정에 실패했습니다.');
                }
            } catch (error) {
                console.error('회원 수정 에러:', error);
                alert(error.message || '회원 수정에 실패했습니다.');
            }
        },

        // 파일 열람 버튼 클릭 이벤트 핸들러
        onViewFileClick: function(event) {
            const button = event.target;
            const type = 'member'; // 회원 관련 파일은 모두 member 타입
            const fileName = button.dataset.filename;

            if (!fileName) {
                alert('파일이 존재하지 않습니다.');
                return;
            }

            MemberModule.api.viewFile(type, fileName);
        },
    },

    // 파일 업로드 관련 핸들러 추가
    fileHandlers: {
        // 메인 이미지 파일 업로드
        handleMainImageUpload: async function(e) {
            const file = e.target.files[0];
            if (!file) return;

            if (!file.type.startsWith('image/')) {
                alert('이미지 파일만 업로드 가능합니다.');
                e.target.value = '';
                return;
            }

            if (file.size > 10 * 1024 * 1024) {
                alert('파일 크기는 10MB를 초과할 수 없습니다.');
                e.target.value = '';
                return;
            }

            const formData = new FormData();
            formData.append('file', file);

            try {
                const response = await fetch('/api/v1/files/upload/temp/member', {
                    method: 'POST',
                    body: formData
                });

                if (!response.ok) {
                    throw new Error('파일 업로드에 실패했습니다.');
                }

                const data = await response.json();
                // 기존 데이터 초기화 후 새로운 데이터 설정
                e.target.value = '';  // 파일 input 초기화
                e.target.dataset.uploadedFileName = data.fileName;
                e.target.dataset.originalFileName = file.name;

                // 파일명 표시 업데이트
                const fileNameDisplay = e.target.parentElement.querySelector('.selected-file');
                if (fileNameDisplay) {
                    fileNameDisplay.textContent = file.name;
                }

                console.log('메인 이미지 업로드 성공:', data);
            } catch (error) {
                console.error('메인 이미지 업로드 중 오류 발생:', error);
                alert('파일 업로드에 실패했습니다.');
                e.target.value = '';
            }
        },

        // 서브 이미지 파일 업로드
        handleSubImageUpload: async function(e) {
            const file = e.target.files[0];
            if (!file) return;

            if (!file.type.startsWith('image/')) {
                alert('이미지 파일만 업로드 가능합니다.');
                e.target.value = '';
                return;
            }

            if (file.size > 10 * 1024 * 1024) {
                alert('파일 크기는 10MB를 초과할 수 없습니다.');
                e.target.value = '';
                return;
            }

            const formData = new FormData();
            formData.append('file', file);

            try {
                const response = await fetch('/api/v1/files/upload/temp/member', {
                    method: 'POST',
                    body: formData
                });

                if (!response.ok) {
                    throw new Error('파일 업로드에 실패했습니다.');
                }

                const data = await response.json();
                // 기존 데이터 초기화 후 새로운 데이터 설정
                e.target.value = '';  // 파일 input 초기화
                e.target.dataset.uploadedFileName = data.fileName;
                e.target.dataset.originalFileName = file.name;

                // 파일명 표시 업데이트
                const fileNameDisplay = e.target.parentElement.querySelector('.selected-file');
                if (fileNameDisplay) {
                    fileNameDisplay.textContent = file.name;
                }

                console.log('서브 이미지 업로드 성공:', data);
            } catch (error) {
                console.error('서브 이미지 업로드 중 오류 발생:', error);
                alert('파일 업로드에 실패했습니다.');
                e.target.value = '';
            }
        },

        // 로고 파일 업로드
        handleLogoUpload: async function(e) {
            const file = e.target.files[0];
            if (!file) return;

            if (!file.type.startsWith('image/')) {
                alert('이미지 파일만 업로드 가능합니다.');
                e.target.value = '';
                return;
            }

            if (file.size > 10 * 1024 * 1024) {
                alert('파일 크기는 10MB를 초과할 수 없습니다.');
                e.target.value = '';
                return;
            }

            const formData = new FormData();
            formData.append('file', file);

            try {
                const response = await fetch('/api/v1/files/upload/temp/member', {
                    method: 'POST',
                    body: formData
                });

                if (!response.ok) {
                    throw new Error('파일 업로드에 실패했습니다.');
                }

                const data = await response.json();
                // 기존 데이터 초기화 후 새로운 데이터 설정
                e.target.value = '';  // 파일 input 초기화
                e.target.dataset.uploadedFileName = data.fileName;
                e.target.dataset.originalFileName = file.name;

                // 파일명 표시 업데이트
                const fileNameDisplay = e.target.parentElement.querySelector('.selected-file');
                if (fileNameDisplay) {
                    fileNameDisplay.textContent = file.name;
                }

                console.log('로고 이미지 업로드 성공:', data);
            } catch (error) {
                console.error('로고 이미지 업로드 중 오류 발생:', error);
                alert('파일 업로드에 실패했습니다.');
                e.target.value = '';
            }
        }
    },

    // 초기화 함수
    init: async function() {
        try {
            // 페이지 타입 설정
            const currentPath = window.location.pathname;
            if (currentPath.includes('/member/teacher')) {
                this.state.pageType = 'teacher';
            } else if (currentPath.includes('/member/company/search')) {
                this.state.pageType = 'companySearch';
            } else if (currentPath.includes('/member/company')) {
                this.state.pageType = 'company';
            } else if (currentPath.includes('/member/admin')) {
                this.state.pageType = 'admin';
            } else if (currentPath.includes('/member/student')) {
                this.state.pageType = 'student';
            }

            // 초기 데이터 로드
            const response = await this.api.getMembers();
            if (response && response.response) {
                const responseData = response.response;
                this.state.members = responseData.memberList || [];
                this.state.currentPage = responseData.pageable?.pageNumber || 0;
                this.state.totalPages = responseData.pageable?.totalPages || 0;
                this.state.totalElements = responseData.pageable?.totalElements || 0;
            } else {
                this.state.members = response.memberList || [];
                this.state.currentPage = response.pageable?.pageNumber || 0;
                this.state.totalPages = response.pageable?.totalPages || 0;
                this.state.totalElements = response.pageable?.totalElements || 0;
            }

            // 초기 렌더링
            this.render.memberTable(this.state.members);
            this.render.pagination(this.state.totalPages);

            // 이벤트 리스너 등록
            const headerCheckbox = document.querySelector('table.table01 thead input[type="checkbox"]:not([data-type])');
            if (headerCheckbox) {
                headerCheckbox.addEventListener('change', this.handlers.toggleAll);
            }

            // 휴대폰 컬럼 체크박스
            const telCheckbox = document.querySelector('table.table01 thead input[type="checkbox"][data-type="telMobile"]');
            if (telCheckbox) {
                telCheckbox.addEventListener('change', (e) => this.handlers.toggleColumn(e, 'telMobile'));
            }

            // 이메일 컬럼 체크박스
            const emailCheckbox = document.querySelector('table.table01 thead input[type="checkbox"][data-type="email"]');
            if (emailCheckbox) {
                emailCheckbox.addEventListener('change', (e) => this.handlers.toggleColumn(e, 'email'));
            }

            // 검색 버튼 클릭 이벤트
            const searchBtn = document.getElementById('searchBtn');
            if (searchBtn) {
                searchBtn.addEventListener('click', this.handlers.search);
            }

            // 검색 입력 필드에 엔터키 이벤트 추가
            const searchInputs = document.querySelectorAll('.column-tc-wrap input[type="text"]');
            searchInputs.forEach(input => {
                input.addEventListener('keypress', (e) => {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        this.handlers.search(e);
                    }
                });
            });

            const deleteBtn = document.querySelector('.jv-btn.fill05');
            if (deleteBtn) {
                deleteBtn.addEventListener('click', this.handlers.deleteMembers);
            }

            // 페이지 사이즈 변경 이벤트
            const pageSizeSelect = document.querySelector('.page-view select');
            if (pageSizeSelect) {
                pageSizeSelect.addEventListener('change', (e) => {
                    const newSize = parseInt(e.target.value);
                    if (!isNaN(newSize)) {
                        this.state.size = newSize;
                        this.handlers.changePage(0);
                    }
                });
            }

            // 회원 등록 폼 제출 이벤트 리스너 (모든 타입 공통)
            const forms = ['studentSaveForm', 'teacherSaveForm', 'companySaveForm', 'adminSaveForm'];
            forms.forEach(formId => {
                const form = document.getElementById(formId);
                if (form) {
                    form.addEventListener('submit', this.handlers.submitMemberSaveForm.bind(this));
                }
            });

            // 저장 버튼 클릭 이벤트 리스너 (폼 제출 대체)
            const btnSave = document.getElementById('btnSave');
            const btnSave1 = document.getElementById('btnSave1');
            if (btnSave || btnSave1) {
                btnSave.addEventListener('click', (e) => {
                    e.preventDefault();
                    const form = document.querySelector('form[id$="SaveForm"]');
                    if (form) {
                        form.dispatchEvent(new Event('submit'));
                    }
                });
            }

            // 우편번호 찾기 버튼 이벤트 리스너
            const btnZipcode = document.getElementById('btnZipcode');
            if (btnZipcode) {
                btnZipcode.addEventListener('click', this.handlers.searchAddress);
            }

            // 이메일 도메인 선택 이벤트 리스너
            const email2Select = document.getElementById('email2Select');
            if (email2Select) {
                email2Select.addEventListener('change', function(e) {
                    const email2Input = document.getElementById('email2');
                    if (e.target.value === '') {
                        // 직접입력 선택 시
                        email2Input.value = '';
                        email2Input.readOnly = false;
                        email2Input.focus();
                    } else {
                        // 도메인 선택 시
                        email2Input.value = e.target.value;
                        email2Input.readOnly = true;
                    }
                });

                // 페이지 로드 시 초기 상태 설정
                const email2Input = document.getElementById('email2');
                if (email2Input) {
                    email2Input.readOnly = email2Select.value !== '';
                }
            }

            // 아이디 중복 확인 버튼 이벤트 리스너
            const checkDuplicateIdButton = document.getElementById('checkDuplicateId');
            if (checkDuplicateIdButton) {
                checkDuplicateIdButton.addEventListener('click', this.handlers.checkIdDuplication);
            }

            // 회사찾기 버튼 이벤트 리스너
            const companySearchBtn = document.getElementById('companySearchBtn');
            if (companySearchBtn) {
                companySearchBtn.addEventListener('click', this.handlers.openCompanySearch);
            }

            // 기업 검색 팝업에서 선택 버튼 이벤트 리스너
            if (this.state.pageType === 'companySearch') {
                const table = document.querySelector('table.table01 tbody');
                if (table) {
                    table.addEventListener('click', (e) => {
                        const selectBtn = e.target.closest('.jv-btn.select-company');
                        if (selectBtn) {
                            const memberIdx = selectBtn.getAttribute('data-member-idx');
                            if (memberIdx) {
                                MemberModule.handlers.selectCompany(memberIdx);
                            }
                        }
                    });
                }
            }

            // 상세검색 토글 버튼 이벤트 리스너
            const searchMoreBtn = document.querySelector('.search-more-btn');
            if (searchMoreBtn) {
                searchMoreBtn.addEventListener('click', this.handlers.toggleDetailSearch);
            }

            // 일괄등록 버튼 이벤트 리스너
            const bulkRegisterBtn = document.getElementById('bulkRegisterBtn');
            if (bulkRegisterBtn) {
                bulkRegisterBtn.addEventListener('click', function() {
                    alert('일괄 등록은 준비중입니다.');
                });
            }

            // 파일 업로드 이벤트 리스너 등록
            const mainImgFile = document.getElementById('mainImgFile');
            const subImgFile = document.getElementById('subImgFile');
            const logoFile = document.getElementById('fnameLogo');

            if (mainImgFile) {
                mainImgFile.removeEventListener('change', this.fileHandlers.handleMainImageUpload);
            }
            if (subImgFile) {
                subImgFile.removeEventListener('change', this.fileHandlers.handleSubImageUpload);
            }
            if (logoFile) {
                logoFile.removeEventListener('change', this.fileHandlers.handleLogoUpload);
            }

            // 회원 수정 폼 제출 이벤트 리스너 (모든 타입 공통)
            const updateForms = ['studentDetailForm', 'teacherDetailForm', 'companyDetailForm', 'adminDetailForm'];
            updateForms.forEach(formId => {
                const form = document.getElementById(formId);
                if (form) {
                    form.addEventListener('submit', this.handlers.submitMemberUpdateForm.bind(this));
                }
            });

            // 수정 버튼 클릭 이벤트 리스너 (폼 제출 대체)
            const btnUpdate = document.getElementById('btnUpdate');
            const btnUpdate1 = document.getElementById('btnUpdate1');
            if (btnUpdate || btnUpdate1) {
                btnUpdate.addEventListener('click', (e) => {
                    e.preventDefault();
                    const form = document.querySelector('form[id$="DetailForm"]');
                    if (form) {
                        form.dispatchEvent(new Event('submit'));
                    }
                });
            }

            // 파일 열람 버튼 이벤트 리스너
            document.querySelectorAll('.btn-view').forEach(button => {
                button.addEventListener('click', this.handlers.onViewFileClick);
            });

            // 파일 이동 처리
            const moveFile = async (fileName) => {
                if (!fileName || fileName.startsWith('sample')) {
                    return; // 기본 이미지인 경우 이동 처리 스킵
                }
                try {
                    const response = await fetch(`/api/v1/files/move/member/${fileName}`, {
                        method: 'POST'
                    });
                    if (!response.ok) {
                        throw new Error(`파일 이동 실패: ${fileName}`);
                    }
                } catch (error) {
                    console.error(`파일 이동 실패: ${fileName}`);
                    throw new Error('파일 이동에 실패했습니다.');
                }
            };

            // 업로드된 파일들 이동
            try {
                const fileInputs = document.querySelectorAll('.hidden-file-input');
                for (const input of fileInputs) {
                    const uploadedFileName = input.dataset.uploadedFileName;
                    if (uploadedFileName) {
                        await moveFile(uploadedFileName);
                    }
                }
            } catch (error) {
                console.error('파일 이동 중 오류 발생:', error);
                throw new Error('파일 이동 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('초기화 에러:', error);
            alert('데이터를 불러오는데 실패했습니다.');
        }
    }
};

// 모듈 초기화
document.addEventListener('DOMContentLoaded', () => {
    MemberModule.init();
});

// 파일 선택 시 파일명 표시
document.addEventListener('DOMContentLoaded', function() {
    // 메인 이미지 파일 선택 시
    const mainImgFile = document.getElementById('mainImgFile');
    if(mainImgFile) {
        mainImgFile.addEventListener('change', function(e) {
            const fileName = e.target.files[0]?.name;
            const fileLabel = document.createElement('div');
            fileLabel.className = 'selected-file';
            fileLabel.textContent = '선택된 파일: ' + (fileName || '없음');

            // 이전에 표시된 선택 파일명이 있다면 제거
            const prevLabel = this.parentElement.querySelector('.selected-file');
            if(prevLabel) {
                prevLabel.remove();
            }

            // 새로운 파일명 표시
            this.parentElement.appendChild(fileLabel);
        });
    }

    // 서브 이미지 파일 선택 시
    const subImgFile = document.getElementById('subImgFile');
    if(subImgFile) {
        subImgFile.addEventListener('change', function(e) {
            const fileName = e.target.files[0]?.name;
            const fileLabel = document.createElement('div');
            fileLabel.className = 'selected-file';
            fileLabel.textContent = '선택된 파일: ' + (fileName || '없음');

            // 이전에 표시된 선택 파일명이 있다면 제거
            const prevLabel = this.parentElement.querySelector('.selected-file');
            if(prevLabel) {
                prevLabel.remove();
            }

            // 새로운 파일명 표시
            this.parentElement.appendChild(fileLabel);
        });
    }

    // 로고 파일 선택 시
    const logoFile = document.getElementById('fnameLogo');
    if(logoFile) {
        logoFile.addEventListener('change', function(e) {
            const fileName = e.target.files[0]?.name;
            const fileLabel = document.createElement('div');
            fileLabel.className = 'selected-file';
            fileLabel.textContent = '선택된 파일: ' + (fileName || '없음');

            // 이전에 표시된 선택 파일명이 있다면 제거
            const prevLabel = this.parentElement.querySelector('.selected-file');
            if(prevLabel) {
                prevLabel.remove();
            }

            // 새로운 파일명 표시
            this.parentElement.appendChild(fileLabel);
        });
    }

    // 사업자등록증 파일 선택 시
    const saupFile = document.getElementById('fnameSaup');
    if(saupFile) {
        saupFile.addEventListener('change', function(e) {
            const fileName = e.target.files[0]?.name;
            const fileLabel = document.createElement('div');
            fileLabel.className = 'selected-file';
            fileLabel.textContent = '선택된 파일: ' + (fileName || '없음');

            // 이전에 표시된 선택 파일명이 있다면 제거
            const prevLabel = this.parentElement.querySelector('.selected-file');
            if(prevLabel) {
                prevLabel.remove();
            }

            // 새로운 파일명 표시
            this.parentElement.appendChild(fileLabel);
        });
    }
});

// 파일 관련 이벤트 핸들러 추가 (하나의 이벤트 리스너로 통합)
document.addEventListener('DOMContentLoaded', function() {
    // 파일 업로드 버튼 클릭 이벤트
    document.querySelectorAll('.btn-upload').forEach(button => {
        button.addEventListener('click', function() {
            const target = this.dataset.target;
            const fileInput = document.getElementById(target + 'File');
            if (fileInput) {
                fileInput.click();
            }
        });
    });

    // 파일 선택 시 이벤트 (통합된 핸들러)
    document.querySelectorAll('.hidden-file-input').forEach(input => {
        input.addEventListener('change', async function(e) {
            const file = e.target.files[0];
            if (!file) return;

            const target = this.id.replace('File', '');
            const displayInput = document.getElementById(target + 'Display');
            const userFileInput = document.getElementById(target + 'UserFile');

            if (!file.type.startsWith('image/')) {
                alert('이미지 파일만 업로드 가능합니다.');
                this.value = '';
                return;
            }

            if (file.size > 10 * 1024 * 1024) {
                alert('파일 크기는 10MB를 초과할 수 없습니다.');
                this.value = '';
                return;
            }

            const formData = new FormData();
            formData.append('file', file);
            formData.append('type', 'member');

            try {
                const response = await fetch('/api/v1/files/upload/temp/member', {
                    method: 'POST',
                    body: formData
                });

                if (!response.ok) throw new Error('파일 업로드에 실패했습니다.');

                const data = await response.json();

                // 파일 정보 저장
                if (displayInput) displayInput.value = file.name;
                if (userFileInput) userFileInput.value = data.fileName;

                // dataset에 업로드된 파일 정보 저장
                this.dataset.uploadedFileName = data.fileName;
                this.dataset.originalFileName = file.name;

                // mainImg나 subImg인 경우 라디오 버튼 체크
                if (target === 'mainImg' || target === 'subImg') {
                    const radio = document.querySelector(`input[name="${target}"][value="custom"]`);
                    if (radio) radio.checked = true;
                }

                // 파일명 표시 업데이트
                const fileNameDisplay = this.parentElement.querySelector('.selected-file');
                if (fileNameDisplay) {
                    fileNameDisplay.textContent = '현재 파일: ' + file.name;
                } else {
                    const newFileNameDisplay = document.createElement('div');
                    newFileNameDisplay.className = 'selected-file';
                    newFileNameDisplay.textContent = '현재 파일: ' + file.name;
                    this.parentElement.appendChild(newFileNameDisplay);
                }

                console.log('파일 업로드 성공:', data);
            } catch (error) {
                console.error('파일 업로드 중 오류 발생:', error);
                alert('파일 업로드에 실패했습니다.');
                this.value = '';
            }
        });
    });

    // 파일 삭제 버튼 클릭 이벤트
    document.querySelectorAll('.btn-delete').forEach(button => {
        button.addEventListener('click', async function() {
            const target = this.dataset.target;
            const fileInput = document.getElementById(target + 'File');
            const displayInput = document.getElementById(target + 'Display');
            const userFileInput = document.getElementById(target + 'UserFile');

            if (!displayInput.value) {
                alert('삭제할 파일이 없습니다.');
                return;
            }

            if (confirm('파일을 삭제하시겠습니까?')) {
                try {
                    // 서버에 삭제 요청
                    const response = await fetch(`/api/v1/files/member/${userFileInput.value}`, {
                        method: 'DELETE'
                    });

                    if (!response.ok) throw new Error('파일 삭제에 실패했습니다.');

                    // UI 초기화
                    fileInput.value = '';
                    displayInput.value = '';
                    userFileInput.value = '';

                    // dataset 초기화
                    fileInput.dataset.uploadedFileName = '';
                    fileInput.dataset.originalFileName = '';

                    // 파일명 표시 제거
                    const fileNameDisplay = this.parentElement.querySelector('.selected-file');
                    if (fileNameDisplay) {
                        fileNameDisplay.remove();
                    }

                    // mainImg나 subImg인 경우 라디오 버튼 초기화
                    if (target === 'mainImg' || target === 'subImg') {
                        const defaultRadio = document.querySelector(`input[name="${target}"][value="sample01.jpg"]`);
                        if (defaultRadio) defaultRadio.checked = true;
                    }

                    alert('파일이 삭제되었습니다.');
                } catch (error) {
                    console.error('파일 삭제 중 오류 발생:', error);
                    alert('파일 삭제에 실패했습니다.');
                }
            }
        });
    });
});


document.addEventListener("DOMContentLoaded", function() {

    const state = {
        menuIdx: "5",  // 숫자든 문자열이든 원하는 형태로 지정하세요.
        depth3Menus: [
            {
                url: "http://localhost:8080/masterpage_sys/member/student",
                target: "_self",
                name: "수강생"
            },
            {
                url: "http://localhost:8080/masterpage_sys/member/teacher",
                target: "_self",
                name: "교강사(튜터)"
            },
            {
                url: "http://localhost:8080/masterpage_sys/member/company",
                target: "_self",
                name: "위탁기업"
            },
            {
                url: "http://localhost:8080/masterpage_sys/member/admin",
                target: "_self",
                name: "LMS관리자"
            }
        ]
    };

    // state 객체를 JSON 문자열로 변환하여 localStorage에 저장
    localStorage.setItem('tabState', JSON.stringify(state));

    // 복원 함수 호출 (구현되어 있다면)
    restoreTabState();
});

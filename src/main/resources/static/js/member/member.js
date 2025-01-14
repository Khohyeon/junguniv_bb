// 회원 관리 모듈
const MemberModule = {
    // 상태 관리
    state: {
        members: [],
        totalPages: 0,
        currentPage: 0,
        size: 10,
        totalElements: 0,
        pageType: '' // 'student', 'teacher', 'company', 'admin'
    },

    // API 호출 함수들
    api: {
        // 회원 목록 조회
        getMembers: async function(page = 0, size = 10) {
            try {
                const response = await fetch(`/masterpage_sys/member/api/?page=${page}&size=${size}`, {
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
                throw error;
            }
        },

        // 회원 삭제
        deleteMembers: async function(ids) {
            try {
                const response = await fetch('/masterpage_sys/member/api/', {
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
        }
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
                            <td>${MemberModule.state.currentPage * MemberModule.state.size + index + 1}</td>
                            <td>${member.name || '-'}</td>
                            <td>${member.userId || '-'}</td>
                            <td>${member.telMobile || '-'}</td>
                            <td>${member.email || '-'}</td>
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
                            <td>${MemberModule.state.currentPage * MemberModule.state.size + index + 1}</td>
                            <td>${member.jobName || '-'}</td>
                            <td>${member.userId || '-'}</td>
                            <td>${member.contractorName || '-'}</td>
                            <td>${member.contractorTel || '-'}</td>
                            <td>${member.contractorEtc || '-'}</td>
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

                case 'admin':
                    tbody.innerHTML = members.map((member, index) => `
                        <tr>
                            <td>
                                <label class="c-input ci-check single">
                                    <input type="checkbox" value="${member.memberIdx || ''}">
                                    <div class="ci-show"></div>
                                </label>
                            </td>
                            <td>${MemberModule.state.currentPage * MemberModule.state.size + index + 1}</td>
                            <td>${member.name || '-'}</td>
                            <td>${member.userId || '-'}</td>
                            <td>${member.telMobile || '-'}</td>
                            <td>${member.email || '-'}</td>
                            <td>'필드 추가 필요'</td>
                            <td>${member.jobWorkState || '-'}</td>
                            <td>${member.authLevel || '-'}</td>
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
                            <td>${MemberModule.state.currentPage * MemberModule.state.size + index + 1}</td>
                            <td>${member.name || '-'}</td>
                            <td>${member.userId || '-'}</td>
                            <td>${member.birthday || '-'}</td>
                            <td>${member.telMobile || '-'}</td>
                            <td>${member.email || '-'}</td>
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

            let html = '';
            
            // 이전 페이지 버튼
            html += `
                <button class="prev ${MemberModule.state.currentPage === 0 ? 'disabled' : ''}" 
                        ${MemberModule.state.currentPage === 0 ? 'disabled' : ''}>
                    이전
                </button>
            `;

            // 페이지 번호
            for (let i = 0; i < totalPages; i++) {
                html += `
                    <button class="page-number ${MemberModule.state.currentPage === i ? 'active' : ''}"
                            data-page="${i}">
                        ${i + 1}
                    </button>
                `;
            }

            // 다음 페이지 버튼
            html += `
                <button class="next ${MemberModule.state.currentPage === totalPages - 1 ? 'disabled' : ''}"
                        ${MemberModule.state.currentPage === totalPages - 1 ? 'disabled' : ''}>
                    다음
                </button>
            `;

            pagination.innerHTML = html;

            // 페이지네이션 이벤트 리스너 추가
            pagination.querySelectorAll('button').forEach(button => {
                button.addEventListener('click', (e) => {
                    if (button.classList.contains('disabled')) return;
                    
                    if (button.classList.contains('prev')) {
                        MemberModule.handlers.changePage(MemberModule.state.currentPage - 1);
                    } else if (button.classList.contains('next')) {
                        MemberModule.handlers.changePage(MemberModule.state.currentPage + 1);
                    } else {
                        const page = parseInt(button.dataset.page);
                        MemberModule.handlers.changePage(page);
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
                const response = await MemberModule.api.getMembers(page, MemberModule.state.size);
                MemberModule.state.members = response.memberList;
                MemberModule.state.currentPage = response.pageable.pageNumber;
                MemberModule.state.totalPages = response.pageable.totalPages;
                MemberModule.state.totalElements = response.pageable.totalElements;
                
                MemberModule.render.memberTable(MemberModule.state.members);
                MemberModule.render.pagination(MemberModule.state.totalPages);
            } catch (error) {
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
                MemberModule.handlers.changePage(MemberModule.state.currentPage);
            } catch (error) {
                alert('회원 삭제에 실패했습니다.');
            }
        },

        // 전체 선택 핸들러
        toggleAll: function(e) {
            const checkboxes = document.querySelectorAll('table.table01 tbody input[type="checkbox"]');
            checkboxes.forEach(cb => cb.checked = e.target.checked);
        }
    },

    // 초기화 함수
    init: async function() {
        try {
            // 페이지 타입 설정
            const currentPath = window.location.pathname;
            if (currentPath.includes('/teacher/')) {
                this.state.pageType = 'teacher';
            } else if (currentPath.includes('/company/')) {
                this.state.pageType = 'company';
            } else if (currentPath.includes('/admin/')) {
                this.state.pageType = 'admin';
            } else {
                this.state.pageType = 'student';
            }

            // 초기 데이터 로드
            const response = await this.api.getMembers();
            this.state.members = response.memberList;
            this.state.currentPage = response.pageable.pageNumber;
            this.state.totalPages = response.pageable.totalPages;
            this.state.totalElements = response.pageable.totalElements;
            
            // 초기 렌더링
            this.render.memberTable(this.state.members);
            this.render.pagination(this.state.totalPages);

            // 이벤트 리스너 등록
            const headerCheckbox = document.querySelector('table.table01 thead input[type="checkbox"]');
            if (headerCheckbox) {
                headerCheckbox.addEventListener('change', this.handlers.toggleAll);
            }

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
        } catch (error) {
            console.error('초기화 에러:', error);
            alert('데이터를 불러오는데 실패했습니다.');
        }
    }
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    MemberModule.init();
});

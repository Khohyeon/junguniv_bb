// 상태 관리
const state = {
    size: 20 // 기본 페이지 크기
};

// 상수 정의
const MESSAGES = {
    NO_DATA: '등록된 권한이 없습니다.',
    CONFIRM_DELETE: '선택한 권한을 삭제하시겠습니까?',
    SELECT_ITEMS: '삭제할 항목을 선택해주세요.',
    DELETE_SUCCESS: '삭제가 완료되었습니다.',
    DELETE_ERROR: '삭제 처리 중 오류가 발생했습니다.',
    LOAD_ERROR: '권한레벨 목록을 불러오는데 실패했습니다.',
    SERVER_ERROR: '서버 오류가 발생했습니다.'
};

// 권한레벨 관련 공통 기능 모듈
const AuthLevelUtils = {
    // 권한레벨 API 엔드포인트
    API_ENDPOINT: '/masterpage_sys/auth_level/api',

    // 권한레벨 목록 조회
    async getAuthLevels(page = 0, size = 20) {
        try {
            const response = await fetch(`${this.API_ENDPOINT}?page=${page}&size=${size}`);
            if (!response.ok) throw new Error('권한레벨 목록을 불러오는데 실패했습니다.');
            return await response.json();
        } catch (error) {
            console.error('Error:', error);
            throw error;
        }
    },

    // 권한레벨 삭제
    async deleteAuthLevels(ids) {
        try {
            const response = await fetch(this.API_ENDPOINT, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(ids)
            });
            return await response.json();
        } catch (error) {
            console.error('Error:', error);
            throw error;
        }
    },

    // 권한레벨별 관리자 수 조회 URL 생성
    getAdminListUrl(authLevel) {
        return `/masterpage_sys/member/admin?authLevel=${authLevel}`;
    },

    // 권한레벨 상세 URL 생성
    getDetailUrl(authLevelIdx) {
        return `/masterpage_sys/auth_level/${authLevelIdx}`;
    }

};

// 권한레벨 리스트 초기화
function initializeAuthLevelList() {
    // 체크박스 전체 선택/해제
    const mainCheckbox = document.querySelector('thead input[type="checkbox"]');
    if (mainCheckbox) {
        mainCheckbox.addEventListener('change', function() {
            const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]');
            checkboxes.forEach(checkbox => checkbox.checked = mainCheckbox.checked);
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
        state.size = parseInt(pageSizeSelect.value) || 20;
        
        pageSizeSelect.addEventListener('change', function(e) {
            const newSize = parseInt(e.target.value);
            if (!isNaN(newSize) && newSize !== state.size) {
                state.size = newSize;
                loadAuthLevelList(0);
            }
        });
    }
}

// 권한레벨 목록 로드
async function loadAuthLevelList(page = 0) {
    try {
        const data = await AuthLevelUtils.getAuthLevels(page, state.size);
        if (data.success && data.response) {
            renderAuthLevelList(data.response);
            renderPagination(data.response);
            updateTotalCount(data.response.pageable.totalElements);
        }
    } catch (error) {
        alert('권한레벨 목록을 불러오는데 실패했습니다.');
    }
}

// 권한레벨 목록 렌더링
function renderAuthLevelList(data) {
    const tbody = document.querySelector('tbody');
    if (!tbody) return;

    const { authLevelList = [], pageable = {} } = data;
    const { totalElements = 0, pageNumber = 0, pageSize = state.size } = pageable;

    if (authLevelList.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="text-center">${MESSAGES.NO_DATA}</td></tr>`;
        return;
    }

    tbody.innerHTML = authLevelList.map((item, index) => `
        <tr>
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" value="${item.authLevelIdx}">
                    <div class="ci-show"></div>
                </label>
            </td>
            <td>${totalElements - (pageNumber * pageSize) - index}</td>
            <td>
                <a href="${AuthLevelUtils.getDetailUrl(item.authLevelIdx)}" class="jv-btn underline01">${item.authLevelName} | ${item.authLevel}</a>
            </td>
            <td>
                <a href="${AuthLevelUtils.getAdminListUrl(item.authLevel)}" class="jv-btn underline01">${item.linkedMemberCount}</a>
            </td>
            <td>${formatDate(item.createdDate)}</td>
        </tr>
    `).join('');
}

// 공통 유틸리티 모듈
const CommonUtils = {
// 페이지네이션 렌더링
    renderPagination(pagination, data, callback) {
    if (!pagination) return;

        const { pageNumber: currentPage, totalPages } = data;
    
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
};

// 권한레벨 목록 페이지 관련 함수들
function renderPagination(data) {
    const pagination = document.getElementById('pagination');
    CommonUtils.renderPagination(pagination, data.pageable, loadAuthLevelList);
}

// 총 건수 업데이트
function updateTotalCount(totalElements) {
    const countSpan = document.querySelector('.count .Pretd_B');
    if (countSpan) {
        countSpan.textContent = `${totalElements}건 검색되었습니다.`;
    }
}

// 삭제 처리
async function handleDelete() {
    const checkedBoxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
    const ids = Array.from(checkedBoxes).map(cb => parseInt(cb.value)).filter(Boolean);
    
    if (ids.length === 0) {
        alert(MESSAGES.SELECT_ITEMS);
        return;
    }
    
    if (!confirm(MESSAGES.CONFIRM_DELETE)) return;
    
    try {
        const result = await AuthLevelUtils.deleteAuthLevels(ids);
        if (result.success) {
            alert(MESSAGES.DELETE_SUCCESS);
            loadAuthLevelList(0);
        } else {
            alert(result.error?.message || MESSAGES.DELETE_ERROR);
        }
    } catch (error) {
        alert(MESSAGES.SERVER_ERROR);
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

// 권한 저장 폼 관련 모듈
const AuthLevelSaveForm = {
    // 상태 관리
    state: {
        menuList: [],
        currentMenuType: 'ADMIN_REFUND',
        allMenuAuths: new Map(),
        initialized: false  // 초기화 여부를 추적하는 플래그 추가
    },

    // API 관련 메서드
    api: {
        async fetchAllMenuList() {
            try {
                const response = await fetch('/masterpage_sys/manager_menu/api/all');
                if (!response.ok) throw new Error('메뉴 목록을 불러오는데 실패했습니다.');
                const data = await response.json();
                if (!data.success) throw new Error(data.error?.message || '메뉴 목록을 불러오는데 실패했습니다.');
                return data.response.managerMenuList;
            } catch (error) {
                console.error('Error:', error);
                throw error;
            }
        },

        async fetchMenuList(page = 0, size = 20) {
            try {
                const response = await fetch(`/masterpage_sys/manager_menu/api?page=${page}&size=${size}`);
                if (!response.ok) throw new Error('메뉴 목록을 불러오는데 실패했습니다.');
                const data = await response.json();
                if (!data.success) throw new Error(data.error?.message || '메뉴 목록을 불러오는데 실패했습니다.');
                return {
                    menuList: data.response.managerMenuList,
                    pageable: data.response.pageable
                };
            } catch (error) {
                console.error('Error:', error);
                throw error;
            }
        },

        async saveAuthLevel(data) {
            try {
                const response = await fetch('/masterpage_sys/auth_level/api', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                return await response.json();
            } catch (error) {
                console.error('Error:', error);
                throw error;
            }
        },

        async saveManagerAuths(data) {
            try {
                const response = await fetch('/masterpage_sys/manager_auth/api', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                return await response.json();
            } catch (error) {
                console.error('Error:', error);
                throw error;
            }
        }
    },

    // DOM 이벤트 핸들러
    handlers: {
        // 탭 메뉴 클릭 이벤트
        handleTabClick(e) {
            e.preventDefault();
            const menuType = e.target.dataset.menuType;
            if (menuType) {
                this.state.currentMenuType = menuType;
                this.view.updateActiveTab(menuType);
                this.view.filterMenuTable(menuType);
            }
        },

        // 전체 체크박스 클릭 이벤트 (수정)
        handleAllCheck(type, checked) {
            const self = this;
            const visibleRows = Array.from(document.querySelectorAll('#managerAuthTable tr'))
                .filter(row => row.style.display !== 'none');

            visibleRows.forEach(row => {
                const checkbox = row.querySelector(`input[name^="${type}Auth_"]`);
                if (checkbox) {
                    checkbox.checked = checked;
                    const menuIdx = checkbox.dataset.menuidx;
                    const authType = checkbox.dataset.authtype;
                    const currentAuth = self.state.allMenuAuths.get(parseInt(menuIdx)) || {
                        menuReadAuth: 1,
                        menuWriteAuth: 1,
                        menuModifyAuth: 1,
                        menuDeleteAuth: 1
                    };
                    currentAuth[authType] = checked ? 1 : 0;
                    self.state.allMenuAuths.set(parseInt(menuIdx), {...currentAuth});
                }
            });
        },

        // 저장 버튼 클릭 이벤트
        async handleSave() {
            try {
                const authLevelName = document.getElementById('authLevelName').value;
                const authLevel = document.getElementById('authLevel').value;

                if (!authLevelName) {
                    alert('권한명을 입력해주세요.');
                    return;
                }

                if (!authLevel) {
                    alert('권한레벨을 입력해주세요.');
                    return;
                }

                // 전체 메뉴 목록을 가져옴 (페이지네이션 없이)
                const allMenuResponse = await fetch('/masterpage_sys/manager_menu/api/all');
                const allMenuData = await allMenuResponse.json();
                
                if (!allMenuData.success) {
                    throw new Error('메뉴 목록을 가져오는데 실패했습니다.');
                }

                // 전체 메뉴에 대한 권한 데이터 수집
                const menuAuths = allMenuData.response.map(menu => ({
                    menuIdx: menu.menuIdx,
                    authLevel: authLevel,
                    menuReadAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuReadAuth ?? 1,
                    menuWriteAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuWriteAuth ?? 1,
                    menuModifyAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuModifyAuth ?? 1,
                    menuDeleteAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuDeleteAuth ?? 1
                }));

                if (menuAuths.length === 0) {
                    alert('최소한 하나의 메뉴에 대한 권한을 설정해주세요.');
                    return;
                }

                // 권한 레벨 저장
                const authLevelResult = await this.api.saveAuthLevel({
                    authLevelName,
                    authLevel
                });

                if (authLevelResult.success) {
                    // 메뉴 권한 저장
                    const managerAuthResult = await this.api.saveManagerAuths(menuAuths);
                    if (managerAuthResult.success) {
                        alert('권한이 성공적으로 저장되었습니다.');
                        window.location.href = '/masterpage_sys/auth_level';
                    }
                }
            } catch (error) {
                console.error('Error:', error);
                alert('권한 저장 중 오류가 발생했습니다.');
            }
        }
    },

    // 뷰 관련 메서드
    view: {
        // 탭 활성화 상태 업데이트
        updateActiveTab(menuType) {
            document.querySelectorAll('.jv-tab li').forEach(li => {
                const link = li.querySelector('a');
                if (link.dataset.menuType === menuType) {
                    li.classList.add('active');
                } else {
                    li.classList.remove('active');
                }
            });
        },

        // 메뉴 테이블 필터링
        filterMenuTable(menuType) {
            const tbody = document.getElementById('managerAuthTable');
            if (!tbody) return;

            const rows = tbody.getElementsByTagName('tr');
            for (let row of rows) {
                const menuTypeCell = row.querySelector('[data-menu-type]');
                if (menuTypeCell) {
                    row.style.display = menuTypeCell.dataset.menuType === menuType ? '' : 'none';
                }
            }
        },

        // 메뉴 권한 테이블 렌더링 (수정)
        renderMenuAuthTable(menuList) {
            const tbody = document.getElementById('managerAuthTable');
            if (!tbody) return;

            const getMenuType = this.parent.utils.getMenuType.bind(this.parent.utils);
            const self = this;

            tbody.innerHTML = menuList.map((menu, index) => {
                const menuIdx = parseInt(menu.menuIdx);
                const currentAuth = this.parent.state.allMenuAuths.get(menuIdx) || {
                    menuReadAuth: 1,
                    menuWriteAuth: 1,
                    menuModifyAuth: 1,
                    menuDeleteAuth: 1
                };

                // 상태 저장
                this.parent.state.allMenuAuths.set(menuIdx, {...currentAuth});

                return `
                    <tr data-menu-type="${getMenuType(menu)}">
                        <td>${index + 1}</td>
                        <td>${menu.parent ? `${menu.parent.menuName} > ` : ''}${menu.menuName}</td>
                        <td><a href="${menu.url || '#'}" class="jv-btn outline01-sm">화면접속</a></td>
                        <td>${menu.chkUse === 'Y' ? 'Y' : 'N'}</td>
                        <td>
                            <label class="c-input ci-check single">
                                <input type="checkbox" 
                                    id="readAuth_${menuIdx}"
                                    name="readAuth_${menuIdx}"
                                    data-menuidx="${menuIdx}"
                                    data-authtype="menuReadAuth"
                                    ${currentAuth.menuReadAuth === 1 ? 'checked' : ''}>
                                <div class="ci-show"></div>
                            </label>
                        </td>
                        <td>
                            <label class="c-input ci-check single">
                                <input type="checkbox" 
                                    id="writeAuth_${menuIdx}"
                                    name="writeAuth_${menuIdx}"
                                    data-menuidx="${menuIdx}"
                                    data-authtype="menuWriteAuth"
                                    ${currentAuth.menuWriteAuth === 1 ? 'checked' : ''}>
                                <div class="ci-show"></div>
                            </label>
                        </td>
                        <td>
                            <label class="c-input ci-check single">
                                <input type="checkbox" 
                                    id="modifyAuth_${menuIdx}"
                                    name="modifyAuth_${menuIdx}"
                                    data-menuidx="${menuIdx}"
                                    data-authtype="menuModifyAuth"
                                    ${currentAuth.menuModifyAuth === 1 ? 'checked' : ''}>
                                <div class="ci-show"></div>
                            </label>
                        </td>
                        <td>
                            <label class="c-input ci-check single">
                                <input type="checkbox" 
                                    id="deleteAuth_${menuIdx}"
                                    name="deleteAuth_${menuIdx}"
                                    data-menuidx="${menuIdx}"
                                    data-authtype="menuDeleteAuth"
                                    ${currentAuth.menuDeleteAuth === 1 ? 'checked' : ''}>
                                <div class="ci-show"></div>
                            </label>
                        </td>
                    </tr>
                `;
            }).join('');

            // 체크박스 이벤트 리스너 추가
            tbody.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
                checkbox.addEventListener('change', function(e) {
                    const menuIdx = parseInt(this.dataset.menuidx);
                    const authType = this.dataset.authtype;
                    const currentAuth = self.parent.state.allMenuAuths.get(menuIdx) || {
                        menuReadAuth: 1,
                        menuWriteAuth: 1,
                        menuModifyAuth: 1,
                        menuDeleteAuth: 1
                    };
                    currentAuth[authType] = this.checked ? 1 : 0;
                    self.parent.state.allMenuAuths.set(menuIdx, {...currentAuth});
                    self.parent.updateAllCheckboxStates();
                });
            });
        },

        // 페이지네이션 렌더링 (공통 유틸리티 사용)
        renderPagination(pageable) {
            const pagination = document.getElementById('pagination');
            CommonUtils.renderPagination(pagination, pageable, 
                (page) => this.parent.loadMenuList(page));
        },

        // 초기화 메서드
        init(parent) {
            this.parent = parent;
        }
    },

    // 유틸리티 메서드
    utils: {
        parent: null,  // parent 참조를 저장할 속성 추가

        // 초기화 메서드 추가
        init(parent) {
            this.parent = parent;
        },

        // 메뉴 권한 데이터 수집 (수정)
        collectMenuAuths() {
            const menuAuths = [];
            this.parent.state.allMenuAuths.forEach((auth, menuIdx) => {
                menuAuths.push({
                    menuIdx: parseInt(menuIdx),
                    ...auth
                });
            });
            return menuAuths;
        },

        // 체크박스 상태 변경 시 권한 정보 업데이트 (수정)
        updateMenuAuth(menuIdx, authType, checked) {
            if (!menuIdx || !authType || !this.parent || !this.parent.state) return;
            
            const currentAuth = this.parent.state.allMenuAuths.get(menuIdx) || {
                menuReadAuth: 1,
                menuWriteAuth: 1,
                menuModifyAuth: 1,
                menuDeleteAuth: 1
            };

            // 권한 값 업데이트
            currentAuth[authType] = checked ? 1 : 0;
            
            // Map에 업데이트된 권한 정보 저장
            this.parent.state.allMenuAuths.set(menuIdx, {...currentAuth});
            
            // 전체 선택 체크박스 상태 업데이트
            this.parent.updateAllCheckboxStates();
        },

        // 메뉴 타입 결정
        getMenuType(menu) {
            // URL 기반으로 메뉴 타입 결정
            const url = menu.url || '';
            if (url.includes('/refund/')) return 'ADMIN_REFUND';
            if (url.includes('/admin/')) return 'ADMIN_NORMAL';
            if (url.includes('/teacher/')) return 'TEACHER';
            if (url.includes('/company/')) return 'COMPANY';
            return 'ADMIN_REFUND'; // 기본값
        }
    },

    // 이벤트 리스너 설정을 별도 메서드로 분리
    setupEventListeners() {
        // 탭 메뉴 이벤트 리스너
        document.querySelectorAll('.jv-tab a').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                this.handlers.handleTabClick.call(this, e);
            });
        });

        // 전체 선택 체크박스 이벤트 리스너
        const self = this;
        ['Read', 'Write', 'Modify', 'Delete'].forEach(type => {
            const checkAllElement = document.getElementById(`checkAll${type}`);
            if (checkAllElement) {
                checkAllElement.addEventListener('change', function(e) {
                    self.handlers.handleAllCheck.call(self, type.toLowerCase(), e.target.checked);
                });
            }
        });

        // 저장 버튼 이벤트 리스너
        const saveBtn = document.getElementById('saveBtn');
        const saveBtn1 = document.getElementById('saveBtn1');
        if (saveBtn || saveBtn1) {
            saveBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.handlers.handleSave.call(this);
            });
        }

        // 목록 버튼 이벤트 리스너
        const listBtn = document.getElementById('listBtn');
        const listBtn1 = document.getElementById('listBtn1');
        if (listBtn || listBtn1) {
            listBtn.addEventListener('click', (e) => {
                e.preventDefault();
                window.location.href = '/masterpage_sys/auth_level';
            });
        }
    },

    // 초기화 메서드 (수정)
    async init() {
        try {
            if (this.state.initialized) return;

            // CSP 설정
            const meta = document.createElement('meta');
            meta.httpEquiv = 'Content-Security-Policy';
            meta.content = "default-src 'self'; script-src 'self' 'nonce-random123'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;";
            document.head.appendChild(meta);

            // view와 utils 초기화
            this.view.init(this);
            this.utils.init(this);
            
            // 전체 메뉴 목록을 먼저 가져와서 권한 정보 초기화
            const response = await this.api.fetchAllMenuList();
            if (response?.success && response?.response) {
                response.response.forEach(menu => {
                    if (!this.state.allMenuAuths.has(menu.menuIdx)) {
                        this.state.allMenuAuths.set(menu.menuIdx, {
                            menuReadAuth: 1,
                            menuWriteAuth: 1,
                            menuModifyAuth: 1,
                            menuDeleteAuth: 1
                        });
                    }
                });
            }

            // 페이징된 메뉴 목록 로드
            await this.loadMenuList();

            // 이벤트 리스너 설정
            this.setupEventListeners();
            
            this.state.initialized = true;
        } catch (error) {
            console.error('초기화 중 오류 발생:', error);
            alert('메뉴 데이터를 불러오는데 실패했습니다.');
        }
    },

    // 메뉴 목록 로드 (수정)
    async loadMenuList(page = 0) {
        try {
            const data = await this.api.fetchMenuList(page);
            if (data.menuList) {
                this.state.menuList = data.menuList;
                
                // UI 업데이트
                this.view.renderMenuAuthTable(data.menuList);
                this.view.renderPagination(data.pageable);
                this.view.filterMenuTable(this.state.currentMenuType);
                this.updateAllCheckboxStates();
            }
        } catch (error) {
            console.error('메뉴 목록 로드 중 오류:', error);
            alert('메뉴 목록을 불러오는데 실패했습니다.');
        }
    },

    // 전체 선택 체크박스 상태 업데이트 메서드 추가
    updateAllCheckboxStates() {
        const authTypes = ['read', 'write', 'modify', 'delete'];
        const visibleRows = Array.from(document.querySelectorAll('#managerAuthTable tr'))
            .filter(row => row.style.display !== 'none');

        authTypes.forEach(type => {
            const allCheckbox = document.getElementById(`checkAll${type.charAt(0).toUpperCase() + type.slice(1)}`);
            if (!allCheckbox) return;

            // 현재 보이는 행들의 체크박스 상태 확인
            const visibleCheckboxes = visibleRows.map(row => 
                row.querySelector(`input[name="${type}Auth_${row.querySelector('input[type="checkbox"]')?.dataset.menuidx}"]`)
            ).filter(Boolean);

            // 모든 체크박스가 체크되어 있는지 확인
            const allChecked = visibleCheckboxes.every(checkbox => checkbox.checked);
            allCheckbox.checked = allChecked;
        });
    },
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    
    if (currentPath.includes('/auth_level/save')) {
        AuthLevelSaveForm.init();
    } else {
    initializeAuthLevelList();
    loadAuthLevelList();
    }
});

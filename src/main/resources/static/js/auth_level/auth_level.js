// 상태 관리
const state = {
    size: 20 // 기본 페이지 크기
};

// 상수 정의
const CONSTANTS = {
    PAGE_SIZE: 10,
    DEFAULT_MENU_TYPE: 'ADMIN_REFUND',
    API_ENDPOINTS: {
        AUTH_LEVEL: '/masterpage_sys/auth_level/api',
        MENU_LIST: '/masterpage_sys/manager_menu/api/all',
        MANAGER_AUTH: '/masterpage_sys/manager_auth/api'
    },
    MESSAGES: {
        NO_DATA: '등록된 권한이 없습니다.',
        CONFIRM_DELETE: '선택한 권한을 삭제하시겠습니까?',
        SELECT_ITEMS: '삭제할 항목을 선택해주세요.',
        DELETE_SUCCESS: '삭제가 완료되었습니다.',
        DELETE_ERROR: '삭제 처리 중 오류가 발생했습니다.',
        LOAD_ERROR: '권한레벨 목록을 불러오는데 실패했습니다.',
        SERVER_ERROR: '서버 오류가 발생했습니다.',
        SAVE_SUCCESS: '저장되었습니다.',
        SAVE_ERROR: '저장에 실패했습니다.',
        UPDATE_SUCCESS: '수정되었습니다.',
        UPDATE_ERROR: '수정에 실패했습니다.',
        INPUT_REQUIRED: '권한명과 권한레벨을 입력해주세요.'
    }
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
        tbody.innerHTML = `<tr><td colspan="5" class="text-center">${CONSTANTS.MESSAGES.NO_DATA}</td></tr>`;
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
    // API 호출 유틸리티
    async fetchAPI(url, options = {}) {
        try {
            const response = await fetch(url, {
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                },
                ...options
            });
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

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
    },

    // 날짜 포맷팅
    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
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
    const checkedRows = Array.from(checkedBoxes).map(cb => cb.closest('tr'));
    
    // 선택된 항목이 없는 경우
    if (checkedRows.length === 0) {
        alert(CONSTANTS.MESSAGES.SELECT_ITEMS);
        return;
    }
    
    // 연결된 관리자 수 확인
    const hasLinkedManagers = checkedRows.some(row => {
        const linkedCountCell = row.querySelector('td:nth-child(4)');
        const linkedCount = parseInt(linkedCountCell.textContent);
        return linkedCount > 0;
    });
    
    if (hasLinkedManagers) {
        alert('연결된 관리자가 있는 권한은 삭제할 수 없습니다.\n관리자 연결을 먼저 해제해주세요.');
        return;
    }
    
    if (!confirm(CONSTANTS.MESSAGES.CONFIRM_DELETE)) return;
    
    const ids = checkedRows.map(row => {
        const checkbox = row.querySelector('input[type="checkbox"]');
        return parseInt(checkbox.value);
    }).filter(Boolean);
    
    try {
        const result = await AuthLevelUtils.deleteAuthLevels(ids);
        if (result.success) {
            alert(CONSTANTS.MESSAGES.DELETE_SUCCESS);
            loadAuthLevelList(0);
        } else {
            alert(result.error?.message || CONSTANTS.MESSAGES.DELETE_ERROR);
        }
    } catch (error) {
        alert(CONSTANTS.MESSAGES.SERVER_ERROR);
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

// 권한 관리 기본 클래스
class AuthLevelBase {
    constructor() {
        this.state = {
            menuList: [],
            currentMenuType: CONSTANTS.DEFAULT_MENU_TYPE,
            allMenuAuths: new Map(),
            initialized: false,
            currentPage: 0,
            pageSize: CONSTANTS.PAGE_SIZE
        };
    }

    // API 호출 메서드
    async fetchMenuList(menuType) {
        const type = menuType || CONSTANTS.DEFAULT_MENU_TYPE;
        const data = await CommonUtils.fetchAPI(`${CONSTANTS.API_ENDPOINTS.MENU_LIST}?menuType=${type}`);
        if (!data.success) {
            throw new Error(data.error?.message || CONSTANTS.MESSAGES.LOAD_ERROR);
        }
        return data.response;
    }

    // 메뉴 권한 테이블 렌더링
    renderMenuAuthTable(menuList) {
        const tbody = document.getElementById('managerAuthTable');
        if (!tbody) return;

        const start = this.state.currentPage * this.state.pageSize;
        const end = start + this.state.pageSize;
        const pagedMenuList = menuList.slice(start, end);

        tbody.innerHTML = pagedMenuList.map((menu, index) => this.renderMenuRow(menu, index, start)).join('');
        this.attachMenuRowEventListeners();
        this.renderPagination(menuList.length);
    }

    // 메뉴 행 렌더링
    renderMenuRow(menu, index, start) {
        const menuIdx = parseInt(menu.menuIdx);
        const currentAuth = this.state.allMenuAuths.get(menuIdx) || {
            menuReadAuth: 1,
            menuWriteAuth: 1,
            menuModifyAuth: 1,
            menuDeleteAuth: 1
        };

        return `
            <tr>
                <td>${start + index + 1}</td>
                <td>${menu.parent ? `${menu.parent.menuName} > ` : ''}${menu.menuName}</td>
                <td><a href="${menu.url || '#'}" class="jv-btn outline01-sm">화면접속</a></td>
                <td>${menu.chkUse === 'Y' ? 'Y' : 'N'}</td>
                ${this.renderAuthCheckboxes(menuIdx, currentAuth)}
            </tr>
        `;
    }

    // 권한 체크박스 렌더링
    renderAuthCheckboxes(menuIdx, currentAuth) {
        const authTypes = ['Read', 'Write', 'Modify', 'Delete'];
        return authTypes.map(type => `
            <td>
                <label class="c-input ci-check single">
                    <input type="checkbox" 
                        id="${type.toLowerCase()}Auth_${menuIdx}"
                        name="${type.toLowerCase()}Auth_${menuIdx}"
                        data-menuidx="${menuIdx}"
                        data-authtype="menu${type}Auth"
                        ${currentAuth[`menu${type}Auth`] === 1 ? 'checked' : ''}>
                    <div class="ci-show"></div>
                </label>
            </td>
        `).join('');
    }

    // 이벤트 리스너 설정
    setupEventListeners() {
        // 탭 메뉴 이벤트
        document.querySelectorAll('.jv-tab li a').forEach(tab => {
            tab.addEventListener('click', async (e) => {
                e.preventDefault();
                document.querySelector('.jv-tab li.active')?.classList.remove('active');
                e.target.closest('li').classList.add('active');
                const menuType = e.target.dataset.menuType;
                this.state.currentMenuType = menuType;
                this.state.currentPage = 0; // 탭 전환시 페이지 초기화
                await this.loadMenuList(menuType);
            });
        });

        // 전체 선택 체크박스 이벤트
        ['Read', 'Write', 'Modify', 'Delete'].forEach(type => {
            const checkAllElement = document.getElementById(`checkAll${type}`);
            if (checkAllElement) {
                checkAllElement.addEventListener('change', (e) => {
                    this.handleAllCheck(type.toLowerCase(), e.target.checked);
                });
            }
        });
    }

    // 전체 체크박스 핸들러
    handleAllCheck(type, checked) {
        const visibleRows = Array.from(document.querySelectorAll('#managerAuthTable tr'))
            .filter(row => row.style.display !== 'none');

        visibleRows.forEach(row => {
            const checkbox = row.querySelector(`input[name^="${type}Auth_"]`);
            if (checkbox) {
                checkbox.checked = checked;
                const menuIdx = parseInt(checkbox.dataset.menuidx);
                const authType = checkbox.dataset.authtype;
                const currentAuth = this.state.allMenuAuths.get(menuIdx) || {
                    menuReadAuth: 1,
                    menuWriteAuth: 1,
                    menuModifyAuth: 1,
                    menuDeleteAuth: 1
                };
                currentAuth[authType] = checked ? 1 : 0;
                this.state.allMenuAuths.set(menuIdx, {...currentAuth});
            }
        });
    }

    // 페이지네이션 렌더링
    renderPagination(totalElements) {
        const pagination = document.getElementById('pagination');
        if (!pagination) return;

        const totalPages = Math.ceil(totalElements / this.state.pageSize);
        CommonUtils.renderPagination(pagination, {
            pageNumber: this.state.currentPage,
            totalPages: totalPages
        }, (page) => {
            this.state.currentPage = page;
            this.renderMenuAuthTable(this.state.menuList);
        });
    }

    // 메뉴 행 이벤트 리스너 추가
    attachMenuRowEventListeners() {
        document.querySelectorAll('#managerAuthTable input[type="checkbox"]').forEach(checkbox => {
            checkbox.addEventListener('change', (e) => {
                const menuIdx = parseInt(e.target.dataset.menuidx);
                const authType = e.target.dataset.authtype;
                const currentAuth = this.state.allMenuAuths.get(menuIdx) || {
                    menuReadAuth: 1,
                    menuWriteAuth: 1,
                    menuModifyAuth: 1,
                    menuDeleteAuth: 1
                };
                currentAuth[authType] = e.target.checked ? 1 : 0;
                this.state.allMenuAuths.set(menuIdx, {...currentAuth});
            });
        });
    }

    // 메뉴 권한 데이터 수집
    collectMenuAuths(menuData, authLevel) {
        return menuData.map(menu => ({
            menuIdx: menu.menuIdx,
            authLevel: authLevel,
            menuReadAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuReadAuth ?? 1,
            menuWriteAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuWriteAuth ?? 1,
            menuModifyAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuModifyAuth ?? 1,
            menuDeleteAuth: this.state.allMenuAuths.get(menu.menuIdx)?.menuDeleteAuth ?? 1
        }));
    }

    async loadMenuList(menuType) {
        try {
            const menuData = await this.fetchMenuList(menuType);
            this.state.menuList = menuData;
            this.renderMenuAuthTable(this.state.menuList);
        } catch (error) {
            console.error('메뉴 목록 로드 중 오류:', error);
            alert(CONSTANTS.MESSAGES.LOAD_ERROR);
        }
    }
}

// 권한 목록 관리 클래스
class AuthLevelList {
    constructor() {
        this.state = {
            size: 20
        };
    }

    async init() {
        this.setupEventListeners();
        await this.loadAuthLevelList();
    }

    setupEventListeners() {
        // 체크박스 전체 선택/해제
        const mainCheckbox = document.querySelector('thead input[type="checkbox"]');
        if (mainCheckbox) {
            mainCheckbox.addEventListener('change', () => {
                const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]');
                checkboxes.forEach(checkbox => checkbox.checked = mainCheckbox.checked);
            });
        }

        // 삭제 버튼 이벤트
        const deleteBtn = document.querySelector('.jv-btn.fill05');
        if (deleteBtn) {
            deleteBtn.addEventListener('click', () => this.handleDelete());
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
            this.state.size = parseInt(pageSizeSelect.value) || 20;
            pageSizeSelect.addEventListener('change', (e) => {
                const newSize = parseInt(e.target.value);
                if (!isNaN(newSize) && newSize !== this.state.size) {
                    this.state.size = newSize;
                    this.loadAuthLevelList(0);
                }
            });
        }
    }

    async loadAuthLevelList(page = 0) {
        try {
            const data = await AuthLevelUtils.getAuthLevels(page, this.state.size);
            if (data.success && data.response) {
                this.renderAuthLevelList(data.response);
                this.renderPagination(data.response);
                this.updateTotalCount(data.response.pageable.totalElements);
            }
        } catch (error) {
            alert(CONSTANTS.MESSAGES.LOAD_ERROR);
        }
    }

    renderAuthLevelList(data) {
        const tbody = document.querySelector('tbody');
        if (!tbody) return;

        const { authLevelList = [], pageable = {} } = data;
        const { totalElements = 0, pageNumber = 0, pageSize = this.state.size } = pageable;

        if (authLevelList.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center">${CONSTANTS.MESSAGES.NO_DATA}</td></tr>`;
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
                    <a href="${AuthLevelUtils.getDetailUrl(item.authLevelIdx)}" class="jv-btn underline01">
                        ${item.authLevelName} | ${item.authLevel}
                    </a>
                </td>
                <td>
                    <a href="${AuthLevelUtils.getAdminListUrl(item.authLevel)}" class="jv-btn underline01">
                        ${item.linkedMemberCount}
                    </a>
                </td>
                <td>${CommonUtils.formatDate(item.createdDate)}</td>
            </tr>
        `).join('');
    }

    renderPagination(data) {
        const pagination = document.getElementById('pagination');
        CommonUtils.renderPagination(pagination, data.pageable, 
            (page) => this.loadAuthLevelList(page));
    }

    updateTotalCount(totalElements) {
        const countSpan = document.querySelector('.count .Pretd_B');
        if (countSpan) {
            countSpan.textContent = `${totalElements}건 검색되었습니다.`;
        }
    }

    async handleDelete() {
        const checkedBoxes = document.querySelectorAll('tbody input[type="checkbox"]:checked');
        const checkedRows = Array.from(checkedBoxes).map(cb => cb.closest('tr'));
        
        // 선택된 항목이 없는 경우
        if (checkedRows.length === 0) {
            alert(CONSTANTS.MESSAGES.SELECT_ITEMS);
            return;
        }
        
        // 연결된 관리자 수 확인
        const hasLinkedManagers = checkedRows.some(row => {
            const linkedCountCell = row.querySelector('td:nth-child(4)');
            const linkedCount = parseInt(linkedCountCell.textContent);
            return linkedCount > 0;
        });
        
        if (hasLinkedManagers) {
            alert('연결된 관리자가 있는 권한은 삭제할 수 없습니다.\n관리자 연결을 먼저 해제해주세요.');
            return;
        }
        
        if (!confirm(CONSTANTS.MESSAGES.CONFIRM_DELETE)) return;
        
        const ids = checkedRows.map(row => {
            const checkbox = row.querySelector('input[type="checkbox"]');
            return parseInt(checkbox.value);
        }).filter(Boolean);
        
        try {
            const result = await AuthLevelUtils.deleteAuthLevels(ids);
            if (result.success) {
                alert(CONSTANTS.MESSAGES.DELETE_SUCCESS);
                this.loadAuthLevelList(0);
            } else {
                alert(result.error?.message || CONSTANTS.MESSAGES.DELETE_ERROR);
            }
        } catch (error) {
            alert(CONSTANTS.MESSAGES.SERVER_ERROR);
        }
    }
}

// 권한 저장 폼 클래스
class AuthLevelSaveForm extends AuthLevelBase {
    async init() {
        if (this.state.initialized) return;

        this.setupEventListeners();
        this.setupSaveFormEventListeners();

        const firstTab = document.querySelector('.jv-tab li a');
        const defaultMenuType = firstTab ? firstTab.dataset.menuType : CONSTANTS.DEFAULT_MENU_TYPE;
        firstTab?.closest('li')?.classList.add('active');
        
        await this.loadMenuList(defaultMenuType);
        
        this.state.initialized = true;
    }

    setupSaveFormEventListeners() {
        // 저장 버튼 이벤트
        const saveBtn = document.getElementById('saveBtn');
        if (saveBtn) {
            saveBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.handleSave();
            });
        }

        // 목록 버튼 이벤트
        const listBtn = document.getElementById('listBtn');
        if (listBtn) {
            listBtn.addEventListener('click', (e) => {
                e.preventDefault();
                window.location.href = '/masterpage_sys/auth_level';
            });
        }
    }

    async handleSave() {
        try {
            const authLevelName = document.getElementById('authLevelName').value;
            const authLevel = document.getElementById('authLevel').value;

            if (!authLevelName || !authLevel) {
                alert(CONSTANTS.MESSAGES.INPUT_REQUIRED);
                return;
            }

            const activeTab = document.querySelector('.jv-tab li.active a');
            const menuType = activeTab?.dataset.menuType;
            
            if (!menuType) {
                alert('선택된 메뉴 탭이 없습니다.');
                return;
            }

            const menuData = await this.fetchMenuList(menuType);
            
            if (!menuData || menuData.length === 0) {
                alert('현재 선택된 메뉴 타입에 대한 메뉴가 없습니다.');
                return;
            }

            const menuAuths = this.collectMenuAuths(menuData, authLevel);

            // 권한 레벨 저장
            const authLevelResult = await CommonUtils.fetchAPI(CONSTANTS.API_ENDPOINTS.AUTH_LEVEL, {
                method: 'POST',
                body: JSON.stringify({ authLevelName, authLevel })
            });

            if (authLevelResult.success) {
                // 메뉴 권한 저장
                const managerAuthResult = await CommonUtils.fetchAPI(CONSTANTS.API_ENDPOINTS.MANAGER_AUTH, {
                    method: 'POST',
                    body: JSON.stringify(menuAuths)
                });

                if (managerAuthResult.success) {
                    alert(CONSTANTS.MESSAGES.SAVE_SUCCESS);
                    window.location.href = '/masterpage_sys/auth_level';
                } else {
                    throw new Error(managerAuthResult.error?.message || CONSTANTS.MESSAGES.SAVE_ERROR);
                }
            } else {
                throw new Error(authLevelResult.error?.message || CONSTANTS.MESSAGES.SAVE_ERROR);
            }
        } catch (error) {
            console.error('Error:', error);
            alert(error.message || CONSTANTS.MESSAGES.SAVE_ERROR);
        }
    }
}

// 권한 상세 폼 클래스
class AuthLevelDetailForm extends AuthLevelBase {
    async init() {
        if (this.state.initialized) return;

        this.setupEventListeners();
        this.setupDetailFormEventListeners();
        await this.initializeData();
        
        this.state.initialized = true;
    }

    setupDetailFormEventListeners() {
        // 저장 버튼 이벤트
        const saveBtn = document.getElementById('saveBtn');
        if (saveBtn) {
            saveBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.handleUpdate();
            });
        }

        // 목록 버튼 이벤트
        const listBtn = document.getElementById('listBtn');
        if (listBtn) {
            listBtn.addEventListener('click', (e) => {
                e.preventDefault();
                window.location.href = '/masterpage_sys/auth_level';
            });
        }

        // 수정 버튼 이벤트
        const updateBtn = document.getElementById('updateBtn');
        if (updateBtn) {
            updateBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.handleUpdate();
            });
        }
    }

    async initializeData() {
        if (!window.authLevelDetail || !window.menuList || !window.managerAuthMap) {
            console.error('필요한 데이터가 없습니다.');
            return;
        }

        const authLevelDetail = window.authLevelDetail;
        document.getElementById('authLevelName').value = authLevelDetail.authLevelName;
        document.getElementById('authLevel').value = authLevelDetail.authLevel;

        const managerAuthMap = window.managerAuthMap;
        Object.entries(managerAuthMap).forEach(([menuIdx, auth]) => {
            this.state.allMenuAuths.set(parseInt(menuIdx), {
                menuReadAuth: auth.menuReadAuth,
                menuWriteAuth: auth.menuWriteAuth,
                menuModifyAuth: auth.menuModifyAuth,
                menuDeleteAuth: auth.menuDeleteAuth
            });
        });

        // 관리자(환급) 탭을 기본으로 선택
        const adminRefundTab = document.querySelector('.jv-tab li a[data-menu-type="ADMIN_REFUND"]');
        if (adminRefundTab) {
            adminRefundTab.closest('li').classList.add('active');
            this.state.currentMenuType = 'ADMIN_REFUND';
            await this.loadMenuList('ADMIN_REFUND');
        }
    }

    async handleUpdate() {
        try {
            const authLevelIdx = document.getElementById('authLevelIdx').value;
            const authLevelName = document.getElementById('authLevelName').value;
            const authLevel = document.getElementById('authLevel').value;

            if (!authLevelName || !authLevel) {
                alert(CONSTANTS.MESSAGES.INPUT_REQUIRED);
                return;
            }

            const activeTab = document.querySelector('.jv-tab li.active a');
            const menuType = activeTab?.dataset.menuType;
            
            if (!menuType) {
                alert('선택된 메뉴 탭이 없습니다.');
                return;
            }

            const menuData = await this.fetchMenuList(menuType);
            
            if (!menuData || menuData.length === 0) {
                alert('현재 선택된 메뉴 타입에 대한 메뉴가 없습니다.');
                return;
            }

            const menuAuths = this.collectMenuAuths(menuData, authLevel);

            // 권한 레벨 수정
            const authLevelResult = await CommonUtils.fetchAPI(
                `${CONSTANTS.API_ENDPOINTS.AUTH_LEVEL}/${authLevelIdx}`,
                {
                    method: 'PUT',
                    body: JSON.stringify({ authLevelName, authLevel })
                }
            );

            if (authLevelResult.success) {
                // 각 메뉴 권한을 개별적으로 upsert
                const upsertPromises = menuAuths.map(auth => 
                    CommonUtils.fetchAPI(
                        `${CONSTANTS.API_ENDPOINTS.MANAGER_AUTH}/upsert`,
                        {
                            method: 'PUT',
                            body: JSON.stringify({
                                menuIdx: auth.menuIdx,
                                authLevel: auth.authLevel,
                                menuReadAuth: auth.menuReadAuth,
                                menuWriteAuth: auth.menuWriteAuth,
                                menuModifyAuth: auth.menuModifyAuth,
                                menuDeleteAuth: auth.menuDeleteAuth
                            })
                        }
                    )
                );

                try {
                    const results = await Promise.all(upsertPromises);
                    const hasError = results.some(result => !result.success);
                    
                    if (!hasError) {
                        alert(CONSTANTS.MESSAGES.UPDATE_SUCCESS);
                        window.location.href = '/masterpage_sys/auth_level';
                    } else {
                        throw new Error('일부 메뉴 권한 업데이트에 실패했습니다.');
                    }
                } catch (error) {
                    throw new Error(error.message || CONSTANTS.MESSAGES.UPDATE_ERROR);
                }
            } else {
                throw new Error(authLevelResult.error?.message || CONSTANTS.MESSAGES.UPDATE_ERROR);
            }
        } catch (error) {
            console.error('Error:', error);
            alert(error.message || CONSTANTS.MESSAGES.UPDATE_ERROR);
        }
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    
    if (currentPath.includes('/auth_level/save')) {
        const saveForm = new AuthLevelSaveForm();
        saveForm.init();
    } else if (currentPath.match(/\/auth_level\/\d+$/)) {
        const detailForm = new AuthLevelDetailForm();
        detailForm.init();
    } else {
        const listForm = new AuthLevelList();
        listForm.init();
    }
});

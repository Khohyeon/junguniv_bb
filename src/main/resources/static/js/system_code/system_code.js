// 기본 설정 관련 상수
const BASIC_SETTINGS = {
    LOGIN_SESSION: '로그인 세션관리',
    COURSE_DEADLINE: '수강신청 마감시간',
    ADMIN_TITLE: '관리자모드 제목',
    CERTIFICATE_FOOTER: '증명서별 하단 기관명',
    EVAL_NOTIFICATION: '평가알림기능',
    AUTO_MESSAGE: '회원가입 안내 자동문자/메일 사용여부',
    ADMIN_LOGO: '관리자페이지 로고 이미지'
};

// API 엔드포인트 상수
const API = {
    BASIC_SETTINGS: '/masterpage_sys/settings/basic/api/settings',
    LOGO_UPLOAD: '/masterpage_sys/settings/basic/api/logo'
};

// 공통 fetch 에러 처리 함수
async function fetchWithErrorHandling(url, options = {}) {
    try {
        const response = await fetch(url, options);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// 기본 설정 저장 함수
async function saveBasicSetting(type, value) {
    try {
        const data = await fetchWithErrorHandling(API.BASIC_SETTINGS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ type, value })
        });

        if (data.success) {
            alert('저장되었습니다.');
        } else {
            alert('저장에 실패했습니다: ' + (data.error?.message || '알 수 없는 오류'));
        }
    } catch (error) {
        alert('저장 중 오류가 발생했습니다.');
    }
}

// 각 설정 저장 함수들
const saveLoginSession = value => saveBasicSetting(BASIC_SETTINGS.LOGIN_SESSION, value);
const saveCourseDeadline = value => saveBasicSetting(BASIC_SETTINGS.COURSE_DEADLINE, value);
const saveAdminTitle = value => saveBasicSetting(BASIC_SETTINGS.ADMIN_TITLE, value);
const saveCertificateFooter = value => saveBasicSetting(BASIC_SETTINGS.CERTIFICATE_FOOTER, value);

// 이벤트 핸들러 등록 함수
function addSaveButtonEventListener(buttonClass, saveFunction) {
    const button = document.querySelector(buttonClass);
    if (button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const value = this.previousElementSibling.value;
            saveFunction(value);
        });
    }
}

// 파일 선택시 temp 디렉토리에 업로드
async function handleFileSelect(file) {
    if (!file) return;
    
    if (!file.type.startsWith('image/')) {
        alert('이미지 파일만 선택 가능합니다.');
        return;
    }

    // 미리보기 표시
    const reader = new FileReader();
    reader.onload = function(e) {
        document.getElementById('previewImage').innerHTML = 
            `<img src="${e.target.result}" alt="미리보기 이미지" style="max-width: 200px;">`;
    };
    reader.readAsDataURL(file);

    // temp 디렉토리에 업로드
    const formData = new FormData();
    formData.append('file', file);

    try {
        const uploadResponse = await fetch('/api/v1/files/upload/temp/system-code', {
            method: 'POST',
            body: formData
        });

        const uploadData = await uploadResponse.json();
        if (!uploadData.fileName) {
            throw new Error('파일 업로드에 실패했습니다.');
        }

        // 파일명을 hidden input에 저장
        const fileNameInput = document.getElementById('tempFileName');
        if (fileNameInput) {
            fileNameInput.value = uploadData.fileName;
            console.log('임시 파일명 저장됨:', uploadData.fileName); // 디버깅용 로그
        } else {
            console.error('tempFileName input을 찾을 수 없습니다.'); // 디버깅용 로그
        }
    } catch (error) {
        console.error('Error:', error);
        alert('파일 업로드 중 오류가 발생했습니다: ' + error.message);
    }
}

// 관리자 로고 이미지 저장
async function uploadAdminLogo() {
    const fileNameInput = document.getElementById('tempFileName');
    const fileName = fileNameInput?.value;
    const fileInput = document.getElementById('adminLogoFile');
    const originalFileName = fileInput?.files[0]?.name;

    if (!fileName) {
        alert('업로드할 파일이 없습니다.');
        return;
    }

    try {
        // 0. 기존 파일 정보 조회
        const existingData = await fetchWithErrorHandling(API.BASIC_SETTINGS, {
            method: 'GET'
        });
        const existingFileName = existingData.success && existingData.response['관리자페이지 로고 이미지'];

        // 1. temp에서 실제 디렉토리로 이동
        const moveResponse = await fetch(`/api/v1/files/move/system-code/${fileName}`, {
            method: 'POST'
        });

        const moveData = await moveResponse.json();
        if (!moveData.success) {
            throw new Error(moveData.error || '파일 이동에 실패했습니다.');
        }

        // 2. 기존 파일이 있다면 삭제
        if (existingFileName && existingFileName !== fileName) {
            try {
                // 실제 파일 삭제
                const deleteResponse = await fetch(`/api/v1/files/system-code/${existingFileName}`, {
                    method: 'DELETE'
                });
                
                if (!deleteResponse.ok) {
                    console.error('기존 파일 삭제 실패');
                }

                // temp 폴더의 파일도 삭제 시도
                try {
                    await fetch(`/api/v1/files/system-code/temp/${existingFileName}`, {
                        method: 'DELETE'
                    });
                } catch (error) {
                    console.error('temp 파일 삭제 실패:', error);
                }
            } catch (error) {
                console.error('기존 파일 삭제 중 오류 발생:', error);
            }
        }

        // 3. 시스템 코드에 파일명 저장
        const saveResponse = await fetch(API.BASIC_SETTINGS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                type: BASIC_SETTINGS.ADMIN_LOGO,
                value: fileName
            })
        });

        const saveData = await saveResponse.json();
        if (!saveData.success) {
            throw new Error(saveData.error?.message || '시스템 코드 저장에 실패했습니다.');
        }

        // 4. 원본 파일명 저장
        if (originalFileName) {
            const saveOriginalResponse = await fetch(API.BASIC_SETTINGS, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    type: '관리자페이지 로고 이미지 원본명',
                    value: originalFileName
                })
            });

            if (!saveOriginalResponse.ok) {
                console.error('원본 파일명 저장 실패');
            }
        }

        alert('로고 이미지가 성공적으로 저장되었습니다.');
        // 저장 성공 후 실제 이미지로 미리보기 업데이트
        document.getElementById('previewImage').innerHTML =
            `<img src="/api/v1/files/download/system-code/${fileName}" alt="관리자 로고" style="max-width: 200px;">`;
        // 입력 필드 초기화
        fileNameInput.value = '';
        fileInput.value = '';

        // 기존 파일명 라벨 제거
        const existingLabel = document.querySelector('.file-name-label');
        if (existingLabel) {
            existingLabel.remove();
        }
    } catch (error) {
        alert('저장 중 오류가 발생했습니다: ' + error.message);
        console.error('Error:', error);
    }
}

// 체크박스 상태 변경 이벤트 핸들러
function handleCheckboxChange(checkboxId, settingType) {
    const checkbox = document.getElementById(checkboxId);
    if (checkbox) {
        checkbox.addEventListener('change', function() {
            saveBasicSetting(settingType, this.checked ? 'Y' : 'N');
        });
    }
}

// 체크박스 초기 상태 설정
function setCheckboxState(checkboxId, value) {
    const checkbox = document.getElementById(checkboxId);
    if (checkbox) {
        checkbox.checked = value === 'Y';
    }
}

// 기본 설정 로드
async function loadBasicSettings() {
    try {
        const data = await fetchWithErrorHandling(API.BASIC_SETTINGS, {
            method: 'GET'
        });
        if (data.success) {
            Object.keys(data.response).forEach(key => {
                const input = document.querySelector(`input[data-setting="${key}"]`);
                if (input) {
                    input.value = data.response[key];
                }
                
                // 로그인 세션 슬라이더 값 업데이트
                if (key === '로그인 세션관리') {
                    const value = parseInt(data.response[key]) || 60;
                    $("#range").slider("value", value);
                    $(".ui-slider-handle").html(value);
                }
                
                // 체크박스 상태 설정
                if (key === '평가알림기능') {
                    setCheckboxState('switch1', data.response[key]);
                }
                if (key === '회원가입 안내 자동문자/메일 사용여부') {
                    setCheckboxState('switch2', data.response[key]);
                }
                // 로고 이미지 설정
                if (key === '관리자페이지 로고 이미지' && data.response[key]) {
                    const fileName = data.response[key];
                    const originalFileName = data.response[key + '_original'] || fileName;
                    
                    // 이미지 미리보기 업데이트
                    document.getElementById('previewImage').innerHTML =
                        `<img src="/api/v1/files/download/system-code/${fileName}" alt="관리자 로고" style="max-width: 200px;">`;
                    
                    // 파일명 표시를 위한 컨테이너 생성 또는 업데이트
                    const fileInput = document.getElementById('adminLogoFile');
                    if (fileInput) {
                        // 기존 파일명 표시 요소가 있다면 제거
                        const existingLabel = document.querySelector('.file-name-label');
                        if (existingLabel) {
                            existingLabel.remove();
                        }

                        // 새로운 파일명 표시 요소 생성
                        const fileNameLabel = document.createElement('div');
                        fileNameLabel.className = 'file-name-label';
                        fileNameLabel.textContent = originalFileName;
                        
                        // 파일 입력 필드 다음에 추가
                        fileInput.parentNode.insertBefore(fileNameLabel, fileInput.nextSibling);
                    }
                }
            });
        }
    } catch (error) {
        console.error('기본 설정을 불러오는 중 오류가 발생했습니다.');
    }
}

// 계정 관리 관련 함수들
async function loadAccounts(type) {
    try {
        const response = await fetch(`/masterpage_sys/settings/basic/api/accounts/${type}`);
        const data = await response.json();
        
        if (data.success) {
            const accountList = document.querySelector(`.${type}-accounts ul`);
            accountList.innerHTML = '';
            
            data.response.forEach(account => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <span class="txt">${account}</span>
                    <a href="#none" onclick="deleteAccount('${type}', '${account}')"></a>
                `;
                accountList.appendChild(li);
            });
        } else {
            alert('계정 목록을 불러오는데 실패했습니다.');
        }
    } catch (error) {
        console.error('계정 목록 조회 중 오류 발생:', error);
        alert('계정 목록을 불러오는데 실패했습니다.');
    }
}

async function addAccount(type) {
    const input = document.querySelector(`.${type}-accounts input[type="text"]`);
    const account = input.value.trim();
    
    if (!account) {
        alert('계정 ID를 입력해주세요.');
        return;
    }
    
    try {
        const response = await fetch(`/masterpage_sys/settings/basic/api/accounts/${type}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ account })
        });
        
        const data = await response.json();
        
        if (data.success) {
            input.value = '';
            await loadAccounts(type);
        } else {
            alert(data.error.message || '계정 추가에 실패했습니다.');
        }
    } catch (error) {
        console.error('계정 추가 중 오류 발생:', error);
        alert('계정 추가에 실패했습니다.');
    }
}

async function deleteAccount(type, account) {
    if (!confirm(`${account} 계정을 삭제하시겠습니까?`)) {
        return;
    }
    
    try {
        const response = await fetch(`/masterpage_sys/settings/basic/api/accounts/${type}/${account}`, {
            method: 'DELETE'
        });
        
        const data = await response.json();
        
        if (data.success) {
            await loadAccounts(type);
        } else {
            alert(data.error.message || '계정 삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('계정 삭제 중 오류 발생:', error);
        alert('계정 삭제에 실패했습니다.');
    }
}

// 페이지 초기화
document.addEventListener('DOMContentLoaded', () => {
    // CSS 스타일 동적 추가
    const style = document.createElement('style');
    style.textContent = `
        .file-name-label {
            margin-top: 5px;
            color: #666;
            font-size: 14px;
        }
        #range .ui-slider-handle {
            text-align: center;
            text-decoration: none;
        }
    `;
    document.head.appendChild(style);

    let isInitializing = true;  // 초기화 중임을 나타내는 플래그

    // 로그인 세션 슬라이더 초기화
    $("#range").slider({
        range: "min",
        min: 0,
        max: 120,
        value: 60,
        slide: function(e, ui) {
            $(".ui-slider-handle").html(ui.value);
            return true;
        },
        change: async function(e, ui) {
            // 초기화 중에는 저장 함수를 호출하지 않음
            if (isInitializing) return;
            
            try {
                await saveBasicSetting('로그인 세션관리', ui.value.toString());
            } catch (error) {
                console.error('로그인 세션 설정 저장 중 오류 발생:', error);
            }
        }
    });
    $(".ui-slider-handle").html("60");

    // 기본 설정 로드 후 초기화 완료 플래그 설정
    loadBasicSettings().then(() => {
        isInitializing = false;
    });

    // 저장 버튼 이벤트 리스너 등록
    addSaveButtonEventListener('.save-course-deadline', saveCourseDeadline);
    addSaveButtonEventListener('.save-admin-title', saveAdminTitle);
    addSaveButtonEventListener('.save-certificate-footer', saveCertificateFooter);

    // 체크박스 이벤트 리스너 등록
    handleCheckboxChange('switch1', BASIC_SETTINGS.EVAL_NOTIFICATION);
    handleCheckboxChange('switch2', BASIC_SETTINGS.AUTO_MESSAGE);

    // hidden input 추가 (파일 업로드 영역 바로 다음에 추가)
    const fileInput = document.getElementById('adminLogoFile');
    if (fileInput) {
        // 이미 존재하는 hidden input이 있다면 제거
        const existingHiddenInput = document.getElementById('tempFileName');
        if (existingHiddenInput) {
            existingHiddenInput.remove();
        }

        // 새로운 hidden input 생성 및 추가
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.id = 'tempFileName';
        hiddenInput.name = 'tempFileName';
        // fileInput 바로 다음에 hidden input 추가
        fileInput.parentNode.insertBefore(hiddenInput, fileInput.nextSibling);

        // 파일 선택 이벤트 리스너
        fileInput.addEventListener('change', function(e) {
            handleFileSelect(this.files[0]);
        });
    }

    // 관리자 로고 저장 버튼 - 실제 업로드 수행
    const logoButton = document.querySelector('.save-admin-logo');
    if (logoButton) {
        logoButton.addEventListener('click', function(e) {
            e.preventDefault();
            uploadAdminLogo();
        });
    }

    // 계정 목록 로드
    (async function initializeAccounts() {
        await loadAccounts('monitoring');
        await loadAccounts('forbidden');
        await loadAccounts('review');
    })();
    
    // 계정 추가 버튼 이벤트 리스너 등록
    document.querySelectorAll('.list-wrap .con01 .jv-btn').forEach(button => {
        const type = button.closest('.cont-wrap').parentElement.querySelector('.form-tit').textContent.includes('모니터링') ? 'monitoring' :
                    button.closest('.cont-wrap').parentElement.querySelector('.form-tit').textContent.includes('가입불가') ? 'forbidden' : 'review';
        
        button.addEventListener('click', () => addAccount(type));
    });
});

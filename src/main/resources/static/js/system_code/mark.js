// API 엔드포인트 상수
const API_ENDPOINT = '/masterpage_sys/mark/refund/api/settings';

// 환급교육 설정 관련 상수
const REFUND_SETTINGS = {
    // 체크박스 설정
    checkboxes: [
        { id: 'sequentialLearning', name: '순차학습 여부', rule: 'Y/N' },
        { id: 'motp', name: 'MOTP 사용 여부', rule: 'Y/N' },
        { id: 'identityVerification', name: '본인인증 사용 여부', rule: 'Y/N' },
        { id: 'per', name: '50% 학습 설정', rule: 'Y/N' },
        { id: 'lcmsLearningRestrict', name: '차시 학습 제한 여부', rule: 'Y/N' },
        { id: 'monitoringOtpAuth', name: '모니터링 전송 여부', rule: 'Y/N' },
        { id: 'courseBoardShare', name: '개설과정 게시판 공유 여부', rule: 'Y/N' },
        { id: 'autoSmsMail', name: '자동문자/메일 사용 여부', rule: 'Y/N' }
    ],
    // 라디오 버튼 설정
    radioGroups: [
        { 
            name: 'reviewGiganChktype', 
            groupName: '복습 기간 유형', 
            rule: 'end/suryo',
            options: [
                { value: 'end', inputId: 'reviewGiganChktype1', label: '교육종료일로부터' },
                { value: 'suryo', inputId: 'reviewGiganChktype2', label: '수료일로부터' }
            ]
        },
        { 
            name: 'reviewTarget', 
            groupName: '복습 대상', 
            rule: 'target_suryo/target_all' 
        },
        { 
            name: 'lcmsResultFinishChktype', 
            groupName: '수료기준 종합점수 유형', 
            rule: 'OutOf100/each',
            options: [
                { value: 'OutOf100', inputId: 'lcmsResultFinishChktype1', label: '100점 만점 기준' },
                { value: 'each', inputId: 'lcmsResultFinishChktype2', label: '평가 구분/항목별 점수 기준' }
            ]
        }
    ],
    // 텍스트 입력 설정
    textInputs: [
        { id: 'lcmsProgressFinishPercent', name: '단원수업 최소 학습율', rule: '퍼센트' },
        { id: 'lcmsProgress1dayPercent', name: '1일 최대 진도율', rule: '퍼센트' },
        { id: 'lcmsLimitUnitPercent', name: '수료기준 진도율', rule: '퍼센트' }
    ],
    // 연관된 값 입력 설정
    relatedInputs: [
        { 
            radioName: 'reviewGiganChktype',
            name: '복습 기간 값',
            rule: '일수',
            valueKey: 'reviewGiganChktypeValue',
            options: {
                end: { inputId: 'reviewGiganChktype1' },
                suryo: { inputId: 'reviewGiganChktype2' }
            }
        },
        {
            radioName: 'lcmsResultFinishChktype',
            name: '수료기준 종합점수 값',
            rule: '점수',
            valueKey: 'lcmsResultFinishChktypeValue',
            options: {
                OutOf100: { inputId: 'lcmsResultFinishChktype1' },
                each: { inputId: 'lcmsResultFinishChktype2' }
            }
        }
    ]
};

// 공통 유틸리티 함수
const utils = {
    csrf: {
        getToken() {
            const metaTag = document.querySelector('meta[name="_csrf"]');
            return metaTag ? metaTag.content : '';
        },
        getHeader() {
            const metaTag = document.querySelector('meta[name="_csrf_header"]');
            return metaTag ? metaTag.content : '';
        }
    },
    validation: {
        isInvalid(value) {
            return value === undefined || value === 'undefined' || value === null || value === 'null' || value === '';
        },
        getValidValue(value, defaultValue = '') {
            return this.isInvalid(value) ? defaultValue : value;
        }
    },
    dom: {
        setCheckboxState(id, value) {
            const checkbox = document.getElementById(id);
            if (checkbox) {
                checkbox.checked = !utils.validation.isInvalid(value) && value === 'Y';
            }
        },
        setRadioState(name, value) {
            if (!utils.validation.isInvalid(value)) {
                const radio = document.querySelector(`input[name="${name}"][value="${value}"]`);
                if (radio) {
                    radio.checked = true;
                }
            }
        },
        setTextInputValue(id, value) {
            const input = document.getElementById(id);
            if (input) {
                input.value = utils.validation.getValidValue(value);
            }
        }
    }
};

// API 호출 함수
async function fetchWithErrorHandling(url, options = {}) {
    try {
        const csrfToken = utils.csrf.getToken();
        const csrfHeader = utils.csrf.getHeader();
        
        if (csrfToken && csrfHeader) {
            options.headers = {
                ...options.headers,
                [csrfHeader]: csrfToken
            };
        }

        const response = await fetch(url, options);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// 설정값 저장 함수
async function saveSetting(type, value) {
    if (utils.validation.isInvalid(value)) {
        return null;
    }

    const data = await fetchWithErrorHandling(API_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ type, value })
    });

    if (!data.success) {
        throw new Error(data.error?.message || '알 수 없는 오류');
    }
    return data;
}

// 설정값 수집 함수
function collectCurrentSettings() {
    const settings = {};

    // 체크박스 값 수집
    REFUND_SETTINGS.checkboxes.forEach(({ id }) => {
        const checkbox = document.getElementById(id);
        if (checkbox) {
            settings[id] = checkbox.checked ? 'Y' : 'N';
        }
    });

    // 라디오 버튼 값 수집
    REFUND_SETTINGS.radioGroups.forEach(({ name }) => {
        const checkedRadio = document.querySelector(`input[name="${name}"]:checked`);
        if (checkedRadio) {
            settings[name] = checkedRadio.value;
        }
    });

    // 텍스트 입력 값 수집
    REFUND_SETTINGS.textInputs.forEach(({ id }) => {
        const input = document.getElementById(id);
        if (input && input.value.trim()) {
            settings[id] = input.value.trim();
        }
    });

    // 연관된 입력 값 수집
    REFUND_SETTINGS.relatedInputs.forEach(({ radioName, valueKey, options }) => {
        const checkedRadio = document.querySelector(`input[name="${radioName}"]:checked`);
        if (checkedRadio && options[checkedRadio.value]) {
            const input = document.getElementById(options[checkedRadio.value].inputId);
            if (input && input.value.trim()) {
                settings[valueKey] = input.value.trim();
            }
        }
    });

    return settings;
}

// 설정값 로드 함수
async function loadSettings() {
    try {
        const data = await fetchWithErrorHandling(API_ENDPOINT, {
            method: 'GET'
        });

        if (data.success) {
            // 체크박스 설정
            REFUND_SETTINGS.checkboxes.forEach(({ id }) => {
                utils.dom.setCheckboxState(id, data.response[id]);
            });

            // 라디오 버튼 설정
            REFUND_SETTINGS.radioGroups.forEach(({ name }) => {
                utils.dom.setRadioState(name, data.response[name]);
            });

            // 텍스트 입력 설정
            REFUND_SETTINGS.textInputs.forEach(({ id }) => {
                utils.dom.setTextInputValue(id, data.response[id]);
            });

            // 연관된 입력 값 설정
            REFUND_SETTINGS.relatedInputs.forEach(({ radioName, valueKey, options }) => {
                const checkedRadio = document.querySelector(`input[name="${radioName}"]:checked`);
                if (checkedRadio && options[checkedRadio.value]) {
                    const input = document.getElementById(options[checkedRadio.value].inputId);
                    if (input) {
                        utils.dom.setTextInputValue(input.id, data.response[valueKey]);
                    }
                }
            });
        }
    } catch (error) {
        console.error('설정을 불러오는 중 오류가 발생했습니다:', error);
        alert('설정을 불러오는 중 오류가 발생했습니다.');
    }
}

// 저장 버튼 클릭 이벤트 핸들러
function handleSaveButtonClick() {
    const saveButton = document.getElementById('saveButton');
    if (saveButton) {
        saveButton.addEventListener('click', async function(e) {
            e.preventDefault();
            
            try {
                const settings = collectCurrentSettings();
                const savePromises = [];

                for (const [type, value] of Object.entries(settings)) {
                    if (!utils.validation.isInvalid(value)) {
                        savePromises.push(saveSetting(type, value));
                    }
                }

                const results = await Promise.all(savePromises);
                const validResults = results.filter(result => result !== null);
                
                if (validResults.length > 0) {
                    alert('모든 설정이 저장되었습니다.');
                    window.location.reload();
                } else {
                    alert('저장할 설정이 없습니다.');
                }
            } catch (error) {
                console.error('설정 저장 중 오류 발생:', error);
                alert('설정 저장 중 오류가 발생했습니다.');
            }
        });
    }
}

// 라디오 버튼 변경 이벤트 핸들러 추가
function handleRadioChange() {
    REFUND_SETTINGS.relatedInputs.forEach(({ radioName, options }) => {
        const radios = document.getElementsByName(radioName);
        radios.forEach(radio => {
            radio.addEventListener('change', function() {
                // 모든 관련 입력 필드 초기화
                Object.values(options).forEach(({ inputId }) => {
                    const input = document.getElementById(inputId);
                    if (input) {
                        input.value = '';
                        input.disabled = true;
                    }
                });

                // 선택된 라디오 버튼에 해당하는 입력 필드 활성화
                if (this.checked && options[this.value]) {
                    const input = document.getElementById(options[this.value].inputId);
                    if (input) {
                        input.disabled = false;
                    }
                }
            });
        });
    });
}

// 페이지 초기화
document.addEventListener('DOMContentLoaded', () => {
    loadSettings();
    handleSaveButtonClick();
    handleRadioChange();
});

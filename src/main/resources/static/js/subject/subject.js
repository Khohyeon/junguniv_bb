/**
 * 상세 검색 클릭 시 토글 변경되면서 검색창 변경
 */
document.addEventListener('DOMContentLoaded', function () {
    const searchMoreBtn = document.getElementById('searchMoreBtn');
    const columnTcWrap = document.getElementById('columnTcWrap');
    const formWrap = document.getElementById('formWrap');

    if (searchMoreBtn) {
        searchMoreBtn.addEventListener('click', function () {
            // display 속성 변경
            if (columnTcWrap.style.display === 'none' || columnTcWrap.style.display === '') {
                columnTcWrap.style.display = 'flex';
            } else {
                columnTcWrap.style.display = 'none';
            }

            // form-wrap에 active 클래스 추가/제거
            formWrap.classList.toggle('active');
        });
    }
});


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
 * 과정등록 중 단계별 탭 전환 기능
 */
document.addEventListener('DOMContentLoaded', function () {
    const navLinks = document.querySelectorAll('.nav-link');
    const tabPanes = document.querySelectorAll('.tab-pane');

    // 초기 설정: 모든 tab-pane 숨기고 첫 번째만 보이게
    tabPanes.forEach((tab, index) => {
        tab.style.display = index === 0 ? 'block' : 'none';
    });

    navLinks.forEach(link => {
        link.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 앵커 이벤트 막기

            // 현재 클릭한 탭의 ID 가져오기
            const targetId = this.getAttribute('href').replace('#', ''); // step-0, step-1 등

            // 모든 탭에서 active 클래스 제거 & tab-pane 숨기기
            navLinks.forEach(nav => nav.classList.remove('active'));
            tabPanes.forEach(tab => tab.style.display = 'none');

            // 클릭한 탭과 해당하는 콘텐츠 활성화
            this.classList.add('active');
            document.getElementById(targetId).style.display = 'block';
        });
    });
});

/**
 * 환급과정 클릭 시 메뉴 자동 설정
 */
document.addEventListener("DOMContentLoaded", function() {

    const state = {
        menuIdx: "51",  // 숫자든 문자열이든 원하는 형태로 지정하세요.
        depth3Menus: [
            {
                url: "http://localhost:8080/masterpage_pro/subject",
                target: "_self",
                name: "과정정보등록"
            },
            {
                url: "#",
                target: "_self",
                name: "콘텐츠차시설정"
            },
            {
                url: "#",
                target: "_self",
                name: "평가문제관리"
            },
            {
                url: "#",
                target: "_self",
                name: "수강정보설정"
            },
            {
                url: "#",
                target: "_self",
                name: "역량평가등록"
            },
            {
                url: "#",
                target: "_self",
                name: "설문평가등록"
            },
        ]
    };

    localStorage.setItem('tabState', JSON.stringify(state));

    restoreTabState();
});

// document.addEventListener("DOMContentLoaded", function() {
//     // 폼 요소와 "저장하기" 버튼 선택
//     const mainForm = document.getElementById("mainform");
//     // "저장하기" 버튼의 경우 div.btn-wrap.mt50 요소를 선택합니다.
//     // 만약 해당 요소가 클릭 가능한 요소라면, 버튼 역할을 할 수 있도록 tabindex나 click 이벤트가 적용되어야 합니다.
//     const saveButton = document.querySelector('.btn-wrap.mt50');
//
//     // 저장하기 버튼 클릭 이벤트 처리
//     if (saveButton) {
//         saveButton.addEventListener('click', function(e) {
//             e.preventDefault();
//             saveFormData();
//         });
//     }
//
//     // 폼 데이터를 AJAX로 전송하는 함수
//     function saveFormData() {
//         // FormData 객체 생성 (enctype="multipart/form-data" 지원)
//         const formData = new FormData(mainForm);
//
//         // fetch API를 이용한 POST 요청
//         fetch(mainForm.action, {
//             method: 'POST',
//             body: formData
//         })
//             .then(response => {
//                 // 응답이 정상인지 체크
//                 if (!response.ok) {
//                     throw new Error('네트워크 응답이 올바르지 않습니다.');
//                 }
//                 // JSON 형식 응답을 파싱 (서버가 JSON을 반환한다고 가정)
//                 return response.json();
//             })
//             .then(data => {
//                 console.log('저장 성공:', data);
//                 // 저장 성공 후 추가 처리 (예: 알림창 표시, 페이지 이동 등)
//                 alert('저장이 완료되었습니다.');
//             })
//             .catch(error => {
//                 console.error('저장 중 오류 발생:', error);
//                 alert('저장 중 오류가 발생하였습니다. 콘솔을 확인하세요.');
//             });
//     }
// });

document.querySelectorAll('.jv-tab-list li').forEach(tab => {
    tab.addEventListener('click', function() {
        // 모든 탭에서 active 클래스 제거
        document.querySelectorAll('.jv-tab-list li').forEach(t => t.classList.remove('active'));
        this.classList.add('active');

        // 모든 탭 컨텐츠 숨김 처리
        document.querySelectorAll('.jv-tab-cont').forEach(content => content.style.display = 'none');

        // 선택한 탭의 컨텐츠 표시
        const rel = this.getAttribute('rel'); // 예: tab1, tab2, ...
        const activeContent = document.getElementById(rel);
        if (activeContent) {
            activeContent.style.display = 'block';

            // 동적으로 id 조정: 모든 textarea에서 'editor' id 제거 후,
            // 활성 컨텐츠의 textarea에 id="editor"를 추가
            document.querySelectorAll('.jv-tab-cont textarea').forEach(textarea => {
                if (textarea.id === 'editor') {
                    textarea.id = textarea.getAttribute('data-original-id');
                }
            });

            // 만약 활성 컨텐츠의 textarea에 data-original-id가 없다면, 저장해두기
            const textarea = activeContent.querySelector('textarea');
            if (textarea) {
                if (!textarea.getAttribute('data-original-id')) {
                    textarea.setAttribute('data-original-id', textarea.id);
                }
                // Remove current id and assign "editor"
                textarea.id = 'editor';
            }
        }
    });
});

document.addEventListener("DOMContentLoaded", function() {
    // 모든 nav-item li 요소 선택
    const navItems = document.querySelectorAll('.nav .nav-item');

    navItems.forEach(function(item) {
        item.addEventListener('click', function(event) {
            // 기본 동작(예: 앵커 태그의 href 이동)이 필요하다면 아래 주석 처리된 부분은 주석처리하거나 제거하세요.
            // event.preventDefault();

            // 모든 nav-item에서 active 클래스 제거
            navItems.forEach(el => el.classList.remove('active'));

            // 클릭한 li 요소에 active 클래스 추가
            this.classList.add('active');

            // 만약 내부의 <a> 태그에도 active 클래스가 있다면 함께 처리
            // 예:
            document.querySelectorAll('.nav .nav-link').forEach(link => link.classList.remove('active'));
            this.querySelector('.nav-link')?.classList.add('active');
        });
    });
});

document.addEventListener("DOMContentLoaded", function() {
    // 전역 변수에 모든 지원금 데이터를 저장 (초기에는 빈 배열)
    window.refundData = [];

    // 페이지 로드시 DB에서 모든 지원금 데이터를 받아오기
    fetch('/masterpage_pro/subject/api/refundData')
        .then(response => {
            if (!response.ok) {
                throw new Error("네트워크 응답이 올바르지 않습니다.");
            }
            return response.json();
        })
        .then(data => {
            if (data.success && Array.isArray(data.response)) {
                window.refundData = data.response;
                // 초기 선택된 라디오 버튼의 값으로 테이블 렌더링
                const selectedType = document.querySelector('input[name="refundType"]:checked').value;
                renderRefundTable(window.refundData, selectedType);
            } else {
                console.error("서버 에러:", data.error);
            }
        })
        .catch(error => {
            console.error("데이터 로드 오류:", error);
        });

    // 모든 refund_type 라디오 버튼에 change 이벤트 핸들러 추가 (onchange 대신 이벤트 리스너 사용)
    const refundRadios = document.querySelectorAll('input[name="refundType"]');
    refundRadios.forEach(function(radio) {
        radio.addEventListener('change', function() {
            changeEmpInsFlag(this.value);
        });
    });

    // 수강료 입력 필드 이벤트 등록
    const coursePricesInput = document.getElementById("course_prices");
    if (coursePricesInput) {
        coursePricesInput.addEventListener("keyup", updateJiPrices);
        coursePricesInput.addEventListener("blur", updateJiPrices);
    }
});

/**
 * 선택한 지원금 유형(refund_type)에 따라 테이블을 업데이트하는 함수
 * @param {string} selectedType - 선택된 지원금 유형 ("saup", "cardsil", "cardjae")
 */
function changeEmpInsFlag(selectedType) {
    console.log("선택된 지원금 유형: " + selectedType);
    if (typeof renderRefundTable === "function") {
        renderRefundTable(window.refundData, selectedType);
    } else {
        console.warn("renderRefundTable 함수가 정의되어 있지 않습니다.");
    }
}

/**
 * 전체 데이터와 선택된 refund_type에 따라 테이블 내용을 렌더링하는 함수
 * @param {Array} data - 전체 데이터 배열
 * @param {string} selectedType - 선택된 지원금 유형
 */
function renderRefundTable(data, selectedType) {
    const tbody = document.getElementById("refundTableBody");
    tbody.innerHTML = ""; // 기존 내용을 초기화

    // studyType이 "refund" 인 항목 중 refundPriceType이 선택된 값과 일치하는 데이터 필터링
    const filteredData = data.filter(item =>
        item.studyType === "refund" && item.refundPriceType === selectedType
    );

    if (filteredData.length === 0) {
        const tr = document.createElement("tr");
        tr.innerHTML = `<td colspan="4">해당 유형의 지원금 데이터가 없습니다.</td>`;
        tbody.appendChild(tr);
        return;
    }

    // 각 데이터 항목을 테이블 행으로 생성 (data-discount-type 속성 포함)
    filteredData.forEach(item => {
        const tr = document.createElement("tr");
        tr.setAttribute("data-discount-type", item.discountType);

        const isChecked = item.chkUse === "Y" ? "checked" : "";
        const discountUnit = (item.discountType === "percent") ? "%" : "원";

        tr.innerHTML = `
            <td>
                <input class="switch" type="checkbox" name="chk_use_${item.refundPriceIdx}" id="chk_use_${item.refundPriceIdx}" value="Y" ${isChecked}>
                <label for="chk_use_${item.refundPriceIdx}" class="switch"></label>
            </td>
            <td>${item.refundPriceName}</td>
            <td>
                <div class="flex-center">
                    <input class="w50p" name="discount_rate_${item.refundPriceIdx}" id="discount_rate_${item.refundPriceIdx}" type="text" value="${item.refundRate}" style="text-align:right">
                    <span id="chkper_${item.refundPriceIdx}">${discountUnit}</span>
                </div>
            </td>
            <td>
                <div class="flex-center">
                    <input class="ji_price" name="ji_price_${item.refundPriceIdx}" id="ji_price_${item.refundPriceIdx}" type="text" value="0" style="text-align:right" readonly>
                    <span>&nbsp;원</span>
                </div>
            </td>
        `;
        tbody.appendChild(tr);
    });

    // 테이블 렌더링 후 현재 수강료에 따라 자부담금 업데이트
    updateJiPrices();
}

/**
 * 수강료(#course_prices)의 값과 각 행의 할인 정보에 따라 자부담금(지원금 적용 수강료)을 업데이트하는 함수
 */
function updateJiPrices() {
    let coursePrice = parseFloat(document.getElementById("course_prices").value);
    if (isNaN(coursePrice)) coursePrice = 0;

    const tbody = document.getElementById("refundTableBody");
    const rows = tbody.querySelectorAll("tr");
    rows.forEach(row => {
        const discountType = row.getAttribute("data-discount-type");
        const discountInput = row.querySelector("input[id^='discount_rate_']");
        if (!discountInput) return;
        let discountValue = parseFloat(discountInput.value);
        if (isNaN(discountValue)) discountValue = 0;

        let newPrice = 0;
        if (discountType === "percent") {
            newPrice = coursePrice - (coursePrice * (discountValue / 100));
        } else if (discountType === "discount") {
            newPrice = coursePrice - discountValue;
        }
        newPrice = newPrice < 0 ? 0 : newPrice;

        const jiPriceInput = row.querySelector("input[id^='ji_price_']");
        if (jiPriceInput) {
            jiPriceInput.value = newPrice;
        }
    });
}

/**
 * Step.1 저장
 */
document.addEventListener("DOMContentLoaded", function() {
    // step-0의 저장 및 다음단계 이동 버튼에 대해 attachSaveButton 호출
    attachSaveButton(
        "step1Form",         // 현재 단계 form id
        "saveButton1",       // 저장 버튼 id
        { subjectName: "subject_name", subjectCode: "subject_code" }, // 이전 단계 데이터 (step-0)
        { subjectName: "subject_name_step1", subjectCode: "subject_code_step1" }, // 다음 단계 대상 (step-1)
        goToNextTab          // 저장 성공 시 호출할 탭 전환 함수 (예: goToNextTab)
    );
});

/**
 * Step.2 저장
 */
document.addEventListener("DOMContentLoaded", function() {
    // step-0의 저장 및 다음단계 이동 버튼에 대해 attachSaveButton 호출
    attachSaveButton(
        "step2Form",         // 현재 단계 form id
        "saveButton2",       // 저장 버튼 id
        { subjectName: "subject_name", subjectCode: "subject_code" }, // 이전 단계 데이터 (step-0)
        { subjectName: "subject_name_step1", subjectCode: "subject_code_step1" }, // 다음 단계 대상 (step-1)
        goToNextTab          // 저장 성공 시 호출할 탭 전환 함수 (예: goToNextTab)
    );
});

/**
 * Step.3 저장
 */
document.addEventListener("DOMContentLoaded", function() {
    // step-0의 저장 및 다음단계 이동 버튼에 대해 attachSaveButton 호출
    attachSaveButton(
        "step3Form",         // 현재 단계 form id
        "saveButton3",       // 저장 버튼 id
        { subjectName: "subject_name", subjectCode: "subject_code" }, // 이전 단계 데이터 (step-0)
        { subjectName: "subject_name_step1", subjectCode: "subject_code_step1" }, // 다음 단계 대상 (step-1)
        goToNextTab          // 저장 성공 시 호출할 탭 전환 함수 (예: goToNextTab)
    );
});

/**
 * 지정된 form 데이터를 저장하고, 저장 성공 시 지정된 다음 단계로 탭 전환 및 데이터 전달하는 함수
 * @param {string} formId - 저장할 form의 id
 * @param {string} saveButtonId - 저장 버튼의 id
 * @param {Object} sourceDataIds - 이전 단계에서 읽어올 데이터 id들 { subjectName: "subject_name", subjectCode: "subject_code" }
 * @param {Object} targetDataIds - 다음 단계에 채워 넣을 데이터 id들 { subjectName: "subject_name_step1", subjectCode: "subject_code_step1" }
 * @param {Function} nextTabCallback - 저장 성공 시 호출할 탭 전환 콜백 함수, subjectIdx를 인자로 전달 (예: goToNextTab)
 */
function attachSaveButton(formId, saveButtonId, sourceDataIds, targetDataIds, nextTabCallback) {
    const formElement = document.getElementById(formId);
    const saveButton = document.getElementById(saveButtonId);

    if (!formElement || !saveButton) {
        console.error("폼 또는 버튼 요소를 찾을 수 없습니다.");
        return;
    }

    saveButton.addEventListener("click", function (e) {
        e.preventDefault();

        // 확인창: 사용자가 저장 후 다음 단계로 이동할지 확인
        if (!confirm("저장 후 이동하시겠습니까?")) {
            return;
        }

        // 이전 단계에서 데이터를 읽어 다음 단계에 채워 넣기
        if (sourceDataIds && targetDataIds) {
            const subjectName = document.getElementById(sourceDataIds.subjectName).value;
            const subjectCode = document.getElementById(sourceDataIds.subjectCode).value;
            document.getElementById(targetDataIds.subjectName).value = subjectName;
            document.getElementById(targetDataIds.subjectCode).value = subjectCode;
        }

        const formData = new FormData(formElement);

        // Ajax POST 요청
        fetch(formElement.action, {
            method: "POST",
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    const contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json().then(errorData => {
                            alert(errorData.error.message || "서버에 오류가 발생했습니다.");
                            throw new Error("서버 오류 발생");
                        });
                    } else {
                        throw new Error("서버에 오류가 발생했습니다.");
                    }
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // data.response.subjectIdx가 있으면 step-2의 숨겨진 필드에 저장
                    if (data.response) {
                        const subjectIdxInput = document.getElementById("subject_idx_step2");
                        if (subjectIdxInput) {
                            subjectIdxInput.value = data.response;
                        }
                    }
                    alert(data.response.message || "저장이 완료되었습니다.");
                    // 저장 성공 시 다음 단계 탭 전환 (subjectIdx를 인자로 전달할 수도 있음)
                    if (typeof nextTabCallback === "function") {
                        nextTabCallback(data.response);
                    }
                } else {
                    alert("저장 실패: " + (data.error || "알 수 없는 에러"));
                }
            })
            .catch(error => {
                console.error("저장 중 오류 발생:", error);
                alert("저장하는 동안 오류가 발생했습니다. 콘솔을 확인하세요.");
            });
    });
}


function goToNextTab(subjectIdx) {
    // 예를 들어, subjectIdx를 콘솔에 출력하거나 step-2의 추가 작업에 활용
    console.log("이동할 때 전달된 subjectIdx:", subjectIdx);

    // 모든 nav-link 요소를 배열로 변환
    const navLinks = Array.from(document.querySelectorAll('.nav-link'));
    const activeIndex = navLinks.findIndex(link => link.classList.contains('active'));

    // 모든 탭에서 active 클래스 제거 및 콘텐츠 숨기기
    navLinks.forEach(link => link.classList.remove('active'));
    document.querySelectorAll('.tab-pane').forEach(pane => pane.style.display = 'none');

    // 다음 탭이 존재하면 활성화
    if (activeIndex !== -1 && activeIndex < navLinks.length - 1) {
        const nextLink = navLinks[activeIndex + 1];
        nextLink.classList.add('active');
        const targetId = nextLink.getAttribute('href').replace('#', '');
        document.getElementById(targetId).style.display = 'block';
    }
}


/**
 * 분야(select#firstChoice) 변경 시 호출되는 함수
 * @param {string} collegeId - 선택된 분야의 ID
 */
function collegeChanged(collegeId) {
    // 선택된 collegeIdx를 hidden 필드에 업데이트 (필요한 경우)
    document.getElementById('college_idx').value = collegeId;

    // major select 박스를 초기화 (기본 option만 남김)
    const majorSelect = document.getElementById('secondChoice');
    majorSelect.innerHTML = '<option value="0">분류 선택</option>';

    // AJAX 호출: 예를 들어, /api/majorList?collegeIdx=선택된값 에서 major 목록을 JSON 형식으로 받아온다고 가정
    fetch('/masterpage_pro/major/api/' + encodeURIComponent(collegeId))
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 응답이 올바르지 않습니다.');
            }
            return response.json();
        })
        .then(data => {
            // data가 majorList 배열이라고 가정 (예: [{id:1, name:"전공1"}, {id:2, name:"전공2"}, ...])
            data.response.forEach(function(major) {
                const option = document.createElement('option');
                option.value = major.majorIdx;
                option.textContent = major.majorName;
                majorSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Major 목록 로드 오류:", error);
        });
}


document.addEventListener('DOMContentLoaded', function () {
    // 모든 평가 구분 체크박스 선택 (class="check_evaluation_code")
    const checkBoxes = document.querySelectorAll('.check_evaluation_code');

    checkBoxes.forEach(function (checkbox) {
        // 초기 상태에 따라 해당 행 보이기/숨기기
        toggleEvaluationRow(checkbox);

        // change 이벤트 핸들러 추가
        checkbox.addEventListener('change', function () {
            toggleEvaluationRow(checkbox);
        });
    });
});

/**
 * 체크박스 상태에 따라 해당 평가방식 텍스트 영역(테이블 행)을 보였다 숨기는 함수
 * @param {HTMLInputElement} checkbox - 평가 구분 체크박스
 */
function toggleEvaluationRow(checkbox) {
    const dataId = checkbox.getAttribute('data-id'); // 예: "result1_number"
    if (!dataId) return;

    // 대응하는 행의 클래스는 "evaluation_input view_" + dataId
    const row = document.querySelector('.evaluation_input.view_' + dataId);
    if (!row) return;

    // 체크되면 보여주고, 아니면 숨김 처리
    row.style.display = checkbox.checked ? '' : 'none';
}

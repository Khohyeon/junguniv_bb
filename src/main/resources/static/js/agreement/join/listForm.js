
document.addEventListener('DOMContentLoaded', function () {
    const save = document.getElementById('save');

    if (save) {
        save.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 동작(링크 이동) 방지

            let trainingCenterName = document.getElementById("trainingCenterName").value;
            let trainingCenterUrl = document.getElementById("trainingCenterUrl").value;

            // 서버로 전송할 데이터 생성
            const requestData = {
                trainingCenterName: trainingCenterName,
                trainingCenterUrl: trainingCenterUrl
            };

            // PUT 요청 전송
            fetch('/masterpage_sys/agreement/api/join/save', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData) // 데이터를 JSON으로 변환하여 전송
            })
                .then(response => {
                    if (!response.ok) {
                        // 응답 헤더의 Content-Type이 JSON인지 확인
                        const contentType = response.headers.get('content-type');
                        if (contentType && contentType.includes('application/json')) {
                            // JSON 응답을 파싱하여 에러 메시지를 추출
                            return response.json().then(errorData => {
                                console.log("errorData : "+ errorData);
                                alert(errorData.error.message);
                                throw new Error('서버에 오류가 발생했습니다.');
                            });
                        } else {
                            // JSON이 아닌 경우 일반 에러 메시지 표시
                            throw new Error('서버에 오류가 발생했습니다.');
                        }
                    }
                    return response.json();
                })

                .then(data => {
                    alert(data.response);
                    window.location.href = '/masterpage_sys/agreement/join';
                })
        });
    } else {
        console.error('Element with id "detailForm.html" not found.');
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const chkAll = document.querySelector('.c-input input[type="checkbox"]'); // 전체공개설정 체크박스
    const openYnCheckboxes = document.querySelectorAll('.agree-join-sect input[type="checkbox"]'); // 공개설정 체크박스들

    // 전체공개설정 클릭 시 개별 체크박스 상태 변경
    chkAll.addEventListener('click', function () {
        const isChecked = chkAll.checked; // 전체공개설정 체크 상태
        openYnCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked; // 개별 체크박스 상태 변경
        });
    });

    // 개별 체크박스 상태 변경 시 전체공개설정 상태 동기화
    openYnCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('click', function () {
            const totalCheckboxes = openYnCheckboxes.length; // 전체 체크박스 개수
            const checkedCheckboxes = document.querySelectorAll('.agree-join-sect input[type="checkbox"]:checked').length; // 체크된 체크박스 개수

            // 모두 체크된 경우 전체공개설정 체크, 그렇지 않으면 해제
            chkAll.checked = totalCheckboxes === checkedCheckboxes;
        });
    });
});

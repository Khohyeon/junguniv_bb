document.addEventListener('DOMContentLoaded', function () {
    const detailForm = document.getElementById('detailForm');
    if (detailForm) {
        detailForm.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 동작(링크 이동) 방지
            // 이동할 URL 설정
            const targetUrl = '/masterpage_sys/agreement/refund/detail';
            window.location.href = targetUrl; // URL로 이동
        });
    } else {
        console.error('Element with id "detailForm" not found.');
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const save = document.getElementById('save');

    if (save) {
        save.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 동작(링크 이동) 방지

            // 라디오 버튼의 선택된 값 가져오기
            const selectedOpenYn = document.querySelector('input[name="openYn"]:checked');
            if (!selectedOpenYn) {
                alert('사용 여부를 선택해주세요.');
                return;
            }

            // 선택된 값
            const openYnValue = selectedOpenYn.value;

            // 서버로 전송할 데이터 생성
            const requestData = {
                openYn: openYnValue,
                agreementType: 'REFUND'
            };

            // PUT 요청 전송
            fetch('/masterpage_sys/agreement/api/save', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData) // 데이터를 JSON으로 변환하여 전송
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('서버 요청 실패');
                    }
                    return response.json();
                })
                .then(data => {
                    alert(data.response);
                    window.location.href = '/masterpage_sys/agreement/course';
                })
                .catch(error => {
                    console.error('저장 중 오류 발생:', error);
                    alert('저장 중 오류가 발생했습니다.');
                });
        });
    } else {
        console.error('Element with id "detailForm" not found.');
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('counselForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault(); // 기본 폼 제출 동작 방지

        const formData = new FormData(form);

        fetch(form.action, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    // 응답 헤더의 Content-Type이 JSON인지 확인
                    const contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        // JSON 응답을 파싱하여 에러 메시지를 추출
                        return response.json().then(errorData => {
                            alert(errorData.error.message);
                        });
                    } else {
                        // JSON이 아닌 경우 일반 에러 메시지 표시
                        throw new Error('서버에 오류가 발생했습니다.');
                    }
                }
                return response.json();
            })
            .then(data => {
                // 성공 응답 처리
                alert(data.response);
                window.location.href = '/masterpage_sys/counsel'; // 성공 시 리스트 페이지로 이동
            })

    });
});

// 카카오 주소 검색 API 호출
document.addEventListener('DOMContentLoaded', function () {
    const btnZipcode = document.getElementById('btnZipcode');
    if (btnZipcode) {
        btnZipcode.addEventListener('click', function () {
            new daum.Postcode({
                oncomplete: function (data) {
                    // 주소 데이터를 우편번호와 주소 필드에 입력
                    document.getElementById('zipcode').value = data.zonecode; // 우편번호
                    document.getElementById('addr1').value = data.roadAddress || data.jibunAddress; // 도로명 주소 또는 지번 주소

                    // 상세주소 필드로 포커스 이동
                    document.getElementById('addr2').focus();
                },
                width: '100%',
                height: '100%'
            }).open();
        });
    } else {
        console.error('Element with id "btnZipcode" not found.');
    }
});

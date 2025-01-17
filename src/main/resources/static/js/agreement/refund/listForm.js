document.addEventListener('DOMContentLoaded', function () {
    const detailForm = document.getElementById('detailForm');
    if (detailForm) {
        detailForm.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 동작(링크 이동) 방지
            // 이동할 URL 설정
            const targetUrl = '/masterpage_sys/agreement/refund/detailForm';
            window.location.href = targetUrl; // URL로 이동
        });
    } else {
        console.error('Element with id "detailForm" not found.');
    }
});

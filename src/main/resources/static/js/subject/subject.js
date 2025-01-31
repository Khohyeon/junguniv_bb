/**
 * 상세 검색 클릭 시 토글 변경되면서 검색창 변경
 */
document.addEventListener('DOMContentLoaded', function () {
    const searchMoreBtn = document.getElementById('searchMoreBtn');
    const columnTcWrap = document.getElementById('columnTcWrap');
    const formWrap = document.getElementById('formWrap');

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
});

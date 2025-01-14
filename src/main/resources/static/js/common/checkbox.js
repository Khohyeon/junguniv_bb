/**
 * 상위 체크박스 선택 시 하위 체크박스 일괄 선택
 */
document.addEventListener('DOMContentLoaded', function () {
    const selectAllCheckbox = document.querySelector('.select-all-checkbox'); // 전체 선택 체크박스
    const tableBody = document.getElementById('tableBody'); // 테이블 바디

    // 전체 선택 체크박스 클릭 이벤트
    selectAllCheckbox.addEventListener('change', function () {
        const isChecked = selectAllCheckbox.checked;

        // 모든 개별 체크박스 상태를 전체 선택 체크박스 상태로 변경
        const itemCheckboxes = document.querySelectorAll('.select-item-checkbox');
        itemCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
    });

    // 이벤트 위임: 개별 체크박스 상태 변경
    tableBody.addEventListener('change', function (event) {
        if (event.target.classList.contains('select-item-checkbox')) {
            const itemCheckboxes = document.querySelectorAll('.select-item-checkbox');
            const allChecked = Array.from(itemCheckboxes).every(cb => cb.checked);
            const noneChecked = Array.from(itemCheckboxes).every(cb => !cb.checked);

            // 모든 항목이 선택되면 전체 선택 체크박스도 체크
            if (allChecked) {
                selectAllCheckbox.checked = true;
                selectAllCheckbox.indeterminate = false;
            }
            // 아무 것도 선택되지 않으면 전체 선택 체크박스를 해제
            else if (noneChecked) {
                selectAllCheckbox.checked = false;
                selectAllCheckbox.indeterminate = false;
            }
            // 일부만 선택된 경우 전체 선택 체크박스를 indeterminate 상태로 설정
            else {
                selectAllCheckbox.checked = false;
                selectAllCheckbox.indeterminate = true;
            }
        }
    });
});

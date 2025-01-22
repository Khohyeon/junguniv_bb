document.addEventListener('DOMContentLoaded', function () {
    const ulList = document.getElementById('ul_list');
    const addRowButton = document.getElementById('addRow');
    const saveFormButton = document.getElementById('saveForm');

    // 항목 추가 기능
    addRowButton.addEventListener('click', function () {
        const newIndex = ulList.children.length; // 현재 항목 개수로 인덱스 설정
        const newRow = document.createElement('li');
        newRow.innerHTML = `
            <input type="hidden" name="agreementUpdateReqDTOList[${newIndex}].agreementIdx" value="">
            <input type="text" class="re_agree_data" name="agreementUpdateReqDTOList[${newIndex}].agreementContents" value="" placeholder="새 항목">
            <button type="button" class="deleteRow jv-btn fill05-icon-trash">삭제</button>
        `;
        ulList.appendChild(newRow);

        // 삭제 이벤트 바인딩
        const deleteButton = newRow.querySelector('.deleteRow');
        deleteButton.addEventListener('click', function () {
            newRow.remove();
        });
    });

    // 기존 항목 삭제 기능
    ulList.addEventListener('click', function (event) {
        if (event.target.classList.contains('deleteRow')) {
            const row = event.target.closest('li');
            if (row) {
                const agreementIdx = row.querySelector('input[name^="agreementUpdateReqDTOList"][type="hidden"]').value;

                // ID가 있는 경우 서버와 통신
                if (agreementIdx) {
                    const confirmDelete = confirm("삭제 시 복원할 수 없습니다. 정말 삭제하시겠습니까?");
                    if (!confirmDelete) {
                        return; // 사용자가 취소를 선택하면 삭제 중단
                    }

                    // 서버에 삭제 요청
                    fetch(`/masterpage_sys/agreement/api/delete/${agreementIdx}`, {
                        method: 'DELETE'
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('서버 요청 실패');
                            }
                            return response.json();
                        })
                        .then(data => {
                            alert(data.message || '항목이 삭제되었습니다.');
                            row.remove(); // 성공 시 항목 삭제
                        })
                        .catch(error => {
                            console.error('삭제 중 오류 발생:', error);
                            alert('삭제 중 오류가 발생했습니다.');
                        });
                } else {
                    // ID가 없는 새 항목인 경우, 확인 없이 바로 삭제
                    row.remove();
                }
            }
        }
    });

    // 저장 버튼 클릭 시 PUT 요청 처리
    saveFormButton.addEventListener('click', function () {
        const items = [];
        const rows = ulList.querySelectorAll('li');

        // 각 항목 데이터를 수집
        rows.forEach(row => {
            const agreementIdx = row.querySelector('input[type="hidden"]').value;
            const agreementContents = row.querySelector('.re_agree_data').value;

            items.push({
                agreementIdx: agreementIdx || null, // 새 항목일 경우 null
                agreementContents: agreementContents,
                agreementType: 'COURSE' // 예시 타입, 필요에 따라 수정
            });
        });

        // PUT 요청 전송
        fetch('/masterpage_sys/agreement/api/update2', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(items) // 데이터를 JSON으로 변환하여 전송
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

});

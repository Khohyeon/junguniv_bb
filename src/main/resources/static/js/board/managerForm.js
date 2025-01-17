document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('submitBtn').addEventListener('click', function () {
        // 폼 데이터 수집
        const bbsGroupName = document.getElementById('bbsGroupName').value.trim();
        const bbsId = document.getElementById('bbsId').value.trim();

        if (!bbsGroupName || !bbsId) {
            alert('모든 필드를 입력해주세요.');
            return;
        }

        // 데이터 생성
        const requestData = {
            bbsGroupName: bbsGroupName,
            bbsId: bbsId
        };

        // AJAX 요청
        fetch('/masterpage_sys/bbsGroup/api/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.response); // 성공 메시지 출력
                    window.location.reload();
                } else {
                    alert('게시판 등록 실패: ' + data.error);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('요청 중 오류가 발생했습니다.');
            });
    });
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('updateBtn').addEventListener('click', function () {
    // 테이블의 모든 행 가져오기
    const rows = document.querySelectorAll('tbody tr');

    const tableData = Array.from(rows).map(row => {
        // 행 ID 가져오기
        const rowId = row.getAttribute('id').split('-')[1];

        // 필드 값 가져오기
        const bbsGroupName = row.querySelector('.bbsGroupName').value;
        const bbsId = row.querySelector('.bbsId').value;
        const category = row.querySelector('.category').value;
        const fileNum = row.querySelector('.fileNum').value;
        const skin = row.querySelector('.skin').value;
        const allowReply = row.querySelector('.allowReply').checked;
        const allowSecret = row.querySelector('.allowSecret').checked;
        const allowComment = row.querySelector('.allowComment').checked;


        // 권한 데이터 수집
        const authCheckboxes = row.querySelectorAll('.auth-checkbox');
        const authData = {
            readAuth: '',
            writeAuth: '',
            commentAuth: '',
            replyAuth: ''
        };

        authCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const authType = checkbox.getAttribute('data-auth'); // readAuth, writeAuth, etc.
                const group = checkbox.getAttribute('data-group'); // guest, member, etc.

                // 각 authType에 따라 데이터를 누적
                if (!authData[authType]) {
                    authData[authType] = ''; // 초기값 설정
                }
                authData[authType] += `@${group}`;
            }
        });

        // 반환할 데이터 객체
        return {
            bbsGroupIdx: rowId,
            bbsGroupName: bbsGroupName,
            bbsId: bbsId,
            category: category,
            fileNum: fileNum,
            skin: skin,
            optionSecretAuth: allowSecret === false ? 'N' : 'Y',
            optionReplyAuth: allowReply === false ? 'N' : 'Y',
            optionCommentAuth: allowComment === false ? 'N' : 'Y',
            readAuth: authData.readAuth,
            writeAuth: authData.writeAuth,
            commentAuth: authData.commentAuth,
            replyAuth: authData.replyAuth,

        };
    });

    // 서버로 전송
    fetch('/masterpage_sys/bbsGroup/api/update', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(tableData) // 전체 테이블 데이터를 JSON으로 변환하여 전송
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.response);
                location.reload(); // 새로고침하여 변경 사항 반영
            } else {
                alert('저장 실패: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('저장 중 오류가 발생했습니다.');
        });
    });
});

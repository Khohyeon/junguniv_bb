document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('saveForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 제출 방지

        const formData = new FormData(form); // 폼 데이터 생성
        const boardType = 'CONSULTING';
        formData.append('boardType', boardType);

        fetch('/masterpage_sys/board/api/save', {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('저장 실패');
                }
                return response.json();
            })
            .then(data => {
                alert(data.response);
                window.location.href = '/masterpage_sys/board/consulting'; // 성공 후 목록으로 이동
            })
            .catch(error => {
                console.error('저장 중 오류 발생:', error);
                alert('저장 중 문제가 발생했습니다.');
            });
    });
});


document.addEventListener('DOMContentLoaded', function () {
    const searchMemberButton = document.getElementById('searchMember');
    const modal = document.getElementById('memberModal');
    const closeModalButton = document.getElementById('closeModal');
    const searchButton = document.getElementById('searchButton');
    const memberList = document.getElementById('memberList');
    const recipientInput = document.getElementById('recipient');

    // 모달 열기
    searchMemberButton.addEventListener('click', function () {
        modal.style.display = 'block';
    });

    // 모달 닫기
    closeModalButton.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    // 회원 검색
    searchButton.addEventListener('click', function () {
        const searchInput = document.getElementById('searchInput').value;

        // 서버에서 회원 검색 (예제 API 호출)
        fetch(`/api/members?name=${encodeURIComponent(searchInput)}`)
            .then(response => response.json())
            .then(data => {
                memberList.innerHTML = '';

                if (data.length === 0) {
                    memberList.textContent = '검색 결과가 없습니다.';
                } else {
                    data.forEach(member => {
                        const memberItem = document.createElement('div');
                        memberItem.textContent = `${member.name} (${member.id})`;
                        memberItem.classList.add('member-item');
                        memberItem.dataset.name = member.name; // 데이터 저장
                        memberItem.addEventListener('click', function () {
                            selectMember(member.name);
                        });
                        memberList.appendChild(memberItem);
                    });
                }
            })
            .catch(error => {
                console.error('Error fetching members:', error);
                memberList.textContent = '회원 검색에 실패했습니다.';
            });
    });

    // 회원 선택
    function selectMember(name) {
        recipientInput.value = name;
        modal.style.display = 'none';
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const modal = document.querySelector('.modal');
    const overlay = document.querySelector('.modal-overlay');
    const openButton = document.getElementById('searchMember'); // 버튼 ID
    const closeButton = document.querySelector('.modal .close');

    // 모달 열기
    openButton.addEventListener('click', function () {
        overlay.style.display = 'block';
        modal.style.display = 'block';
    });

    // 모달 닫기
    closeButton.addEventListener('click', function () {
        overlay.style.display = 'none';
        modal.style.display = 'none';
    });

    // 모달 외부 클릭 시 닫기
    overlay.addEventListener('click', function () {
        overlay.style.display = 'none';
        modal.style.display = 'none';
    });
});
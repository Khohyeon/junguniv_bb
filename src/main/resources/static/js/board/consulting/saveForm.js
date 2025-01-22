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
    const modal = document.getElementById('memberModal');
    const overlay = document.querySelector('.modal-overlay'); // If overlay is used
    const closeModalButton = document.getElementById('closeModal');
    const searchMemberButton = document.getElementById('searchMember');
    const searchButton = document.getElementById('searchButton');
    const searchInput = document.getElementById('searchInput');
    const memberList = document.getElementById('memberList');
    const confirmButton = document.getElementById('confirmSelection');
    const parentInput = document.getElementById('selectedMemberInput');
    const hiddenNameInput = document.getElementById('recipientName');
    const hiddenIdInput = document.getElementById('recipientId');

    let selectedMember = null;

    function openModal() {
        if (overlay) overlay.style.display = 'block';
        modal.style.display = 'block';
    }

    function closeModal() {
        if (overlay) overlay.style.display = 'none';
        modal.style.display = 'none';
    }

    searchMemberButton.addEventListener('click', openModal);
    closeModalButton.addEventListener('click', closeModal);

    if (overlay) {
        overlay.addEventListener('click', closeModal);
    }

    searchButton.addEventListener('click', function () {
        const query = searchInput.value.trim();

        if (!query) {
            alert('검색어를 입력해주세요.');
            return;
        }

        fetch(`/masterpage_sys/member/api/search?name=${encodeURIComponent(query)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('멤버 조회 중 오류가 발생했습니다.');
                }
                return response.json();
            })
            .then(data => {
                displayMembers(data);
            })
            .catch(error => {
                console.error(error);
                alert('멤버 조회 중 오류가 발생했습니다.');
            });
    });

    function displayMembers(members) {
        memberList.innerHTML = '';

        if (members.length === 0) {
            memberList.innerHTML = '<p>검색 결과가 없습니다.</p>';
            return;
        }

        const ul = document.createElement('ul');
        members.forEach(member => {
            const li = document.createElement('li');
            li.textContent = `${member.name} (${member.userId}) [${member.birthday}]`;
            li.dataset.name = member.name;
            li.dataset.userId = member.userId;
            li.dataset.birthday = member.birthday;
            li.style.cursor = 'pointer';

            li.addEventListener('click', function () {
                const previouslySelected = memberList.querySelector('li.selected');
                if (previouslySelected) {
                    previouslySelected.classList.remove('selected');
                }
                li.classList.add('selected');
                selectedMember = {
                    name: li.dataset.name,
                    userId: li.dataset.userId,
                    birthday: li.dataset.birthday
                };
            });

            ul.appendChild(li);
        });

        memberList.appendChild(ul);
    }

    confirmButton.addEventListener('click', function () {
        if (!selectedMember) {
            alert('멤버를 선택해주세요.');
            return;
        }

        // 부모 페이지의 input 요소에 값 삽입
        parentInput.value = `${selectedMember.name} (${selectedMember.userId}) [${selectedMember.birthday}]`;

        // 숨겨진 필드에 개별 데이터 저장
        hiddenNameInput.value = selectedMember.name;
        hiddenIdInput.value = selectedMember.userId;

        // 모달 닫기
        closeModal();
    });
});

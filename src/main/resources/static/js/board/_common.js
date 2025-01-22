/**
 * 페이지 로드 시 게시판에 있는 카테고리를 select - option 에 ,로 구분하여 넣어주는 함수
 */
function getCategory(boardType) {
    const categorySelect = document.getElementById('category');
    // 카테고리 데이터를 서버에서 가져오기
    fetch(`/masterpage_sys/board/api/categories?boardType=${encodeURIComponent(boardType)}`)
        .then(response => response.json())
        .then(data => {
            const categories = data.response.split(','); // ,로 구분하여 배열로 변환
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.trim();
                option.textContent = category.trim();
                categorySelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading categories:', error));
}


document.addEventListener('DOMContentLoaded', () => {
    const contentContainer = document.querySelector('.page-wrap .inner');
    if (!contentContainer) {
        console.error('Error: .inner element not found');
    } else {
        console.log('Found .inner element:', contentContainer);
    }
});
const observer = new MutationObserver((mutationsList, observer) => {
    const contentContainer = document.querySelector('.page-wrap .inner');
    if (contentContainer) {
        console.log('Found .inner element:', contentContainer);
        observer.disconnect(); // .inner를 찾으면 감지 중지
    }
});

observer.observe(document.body, { childList: true, subtree: true });
function initializeContentLoader() {
    const contentContainer = document.querySelector('.page-wrap .inner');
    if (!contentContainer) {
        console.error('Error: .inner element not found');
        return;
    }
    console.log('Content loader initialized with .inner:', contentContainer);
}

// 본문 로드 이후 실행
setTimeout(initializeContentLoader, 0); // 약간의 지연 추가
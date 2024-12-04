document.addEventListener("DOMContentLoaded", async () => {
  const searchInput = document.getElementById("searchInput");
  const productTable = document.getElementById("productTable");
  const totalCount = document.getElementById("totalCount");
  const prevPage = document.getElementById("prevPage");
  const nextPage = document.getElementById("nextPage");
  const currentPage = document.getElementById("currentPage");

  let products = [];
  let filteredProducts = [];
  let page = 1;
  const itemsPerPage = 5;

  // 4번 방식: API 호출로 데이터 가져오기
  async function fetchProducts() {
    try {
      const response = await fetch("/api/v1/products");
      const data = await response.json();
      if (data && data.success && Array.isArray(data.data)) {
        // 반환된 데이터 매핑
        return data.data.map((product) => ({
          id: product.id,
          name: product.name,
          category: product.category || "카테고리 없음",
          description: product.description,
          image: product.imageFiles.length > 0
              ? product.imageFiles[0].imageUrl
              : "/default-image.png", // 기본 이미지 경로 설정
          date: new Date().toLocaleDateString(), // 임시 등록일
        }));
      } else {
        console.error("상품 데이터 형식 오류:", data);
        return [];
      }
    } catch (error) {
      console.error("상품 데이터를 가져오는 중 오류 발생:", error);
      return [];
    }
  }

  // 테이블 업데이트 함수
  function updateTable() {
    productTable.innerHTML = ""; // 기존 행 초기화
    const start = (page - 1) * itemsPerPage;
    const end = page * itemsPerPage;

    filteredProducts.slice(start, end).forEach((product) => {
      const row = `
        <tr>
          <td class="px-6 py-4 whitespace-nowrap">
            <img src="${product.image}" alt="상품 이미지" class="h-12 w-12 object-cover rounded" />
          </td>
          <td class="px-6 py-4 whitespace-nowrap">${product.name}</td>
          <td class="px-6 py-4 whitespace-nowrap">${product.category}</td>
          <td class="px-6 py-4 whitespace-nowrap">${product.date}</td>
          <td class="px-6 py-4 whitespace-nowrap">
            <div class="flex space-x-2">
              <button class="p-1 text-blue-600 hover:bg-blue-50 rounded">
                <i class="icon-edit h-4 w-4"></i>
              </button>
              <button class="p-1 text-red-600 hover:bg-red-50 rounded">
                <i class="icon-trash h-4 w-4"></i>
              </button>
            </div>
          </td>
        </tr>
      `;
      productTable.innerHTML += row;
    });

    totalCount.textContent = `총 ${filteredProducts.length}개의 상품`;
    currentPage.textContent = page;
    prevPage.disabled = page === 1;
    nextPage.disabled = page * itemsPerPage >= filteredProducts.length;
  }

  // 검색 입력 이벤트
  searchInput.addEventListener("input", (e) => {
    const searchTerm = e.target.value.toLowerCase();
    filteredProducts = products.filter((product) =>
        product.name.toLowerCase().includes(searchTerm)
    );
    page = 1;
    updateTable();
  });

  // 이전 페이지 버튼
  prevPage.addEventListener("click", () => {
    if (page > 1) {
      page--;
      updateTable();
    }
  });

  // 다음 페이지 버튼
  nextPage.addEventListener("click", () => {
    if (page * itemsPerPage < filteredProducts.length) {
      page++;
      updateTable();
    }
  });

  // 초기 데이터 로드
  products = await fetchProducts();
  filteredProducts = products; // 초기 필터링 데이터 설정
  updateTable();
});

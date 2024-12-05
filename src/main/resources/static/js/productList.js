document.addEventListener("DOMContentLoaded", async () => {
  const searchInput = document.getElementById("searchInput");
  const productTable = document.getElementById("productTable");
  const totalCount = document.getElementById("totalCount");
  const paginationContainer = document.getElementById("paginationContainer");

  let products = [];
  let filteredProducts = [];
  let page = 1;
  const itemsPerPage = 10;
  let total = 0;

  // API 호출로 데이터 가져오기
  async function fetchProducts(pageNumber = 1) {
    try {
      const response = await fetch(`/api/v1/products?page=${pageNumber}&size=${itemsPerPage}`);
      const data = await response.json();
      if (data && data.success && Array.isArray(data.data)) {
        return {
          products: data.data.map((product) => ({
            id: product.id,
            name: product.name,
            category: product.category || "카테고리 없음",
            description: product.description,
            image: product.imageFiles.length > 0
                ? product.imageFiles[0].imageUrl
                : "/default-image.png", // 기본 이미지 경로 설정
            date: new Date().toLocaleDateString(), // 임시 등록일
          })),
          total: data.pageInfo.totalElements,
        };
      } else {
        console.error("상품 데이터 형식 오류:", data);
        return { products: [], total: 0 };
      }
    } catch (error) {
      console.error("상품 데이터를 가져오는 중 오류 발생:", error);
      return { products: [], total: 0 };
    }
  }

  // 페이지네이션 버튼 생성 함수
  async function createPaginationButtons() {
    paginationContainer.innerHTML = ""; // 기존 버튼 초기화
    const totalPages = Math.ceil(total / itemsPerPage);

    // 이전 버튼
    const prevButton = document.createElement("button");
    prevButton.textContent = "이전";
    prevButton.disabled = page === 1;
    prevButton.className = "px-4 py-2 border rounded mx-1";
    prevButton.addEventListener("click", async () => {
      if (page > 1) {
        page--;
        await loadPageData(); // 이전 페이지 데이터 로드
      }
    });
    paginationContainer.appendChild(prevButton);

    // 페이지 번호 버튼
    for (let i = 1; i <= totalPages; i++) {
      const pageButton = document.createElement("button");
      pageButton.textContent = i;
      pageButton.className = `px-4 py-2 border rounded mx-1 ${i === page ? "bg-blue-500 text-white" : ""}`;
      pageButton.addEventListener("click", async () => {
        if (i !== page) {
          page = i;
          await loadPageData(); // 선택한 페이지 데이터 로드
        }
      });
      paginationContainer.appendChild(pageButton);
    }

    // 다음 버튼
    const nextButton = document.createElement("button");
    nextButton.textContent = "다음";
    nextButton.disabled = page === totalPages;
    nextButton.className = "px-4 py-2 border rounded mx-1";
    nextButton.addEventListener("click", async () => {
      if (page < totalPages) {
        page++;
        await loadPageData(); // 다음 페이지 데이터 로드
      }
    });
    paginationContainer.appendChild(nextButton);
  }

  // 테이블 업데이트 함수
  function updateTable() {
    productTable.innerHTML = ""; // 기존 행 초기화
    filteredProducts.forEach((product) => {
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

    totalCount.textContent = `총 ${total}개의 상품`;
  }

  // 페이지 데이터 로드 함수
  async function loadPageData() {
    const fetchedData = await fetchProducts(page);
    products = fetchedData.products;
    filteredProducts = products;
    total = fetchedData.total;
    updateTable();
    createPaginationButtons();
  }

  // 검색 입력 이벤트
  searchInput.addEventListener("input", async (e) => {
    const searchTerm = e.target.value.toLowerCase();
    const fetchedData = await fetchProducts(1); // 검색 시 첫 페이지로 초기화
    products = fetchedData.products;
    filteredProducts = products.filter((product) =>
        product.name.toLowerCase().includes(searchTerm)
    );
    total = filteredProducts.length;
    page = 1;
    updateTable();
    createPaginationButtons();
  });

  // 초기 데이터 로드
  await loadPageData();
});

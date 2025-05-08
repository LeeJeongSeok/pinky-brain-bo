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

  function viewProductDetail(productId) {
    window.location.href = `/product/${productId}`;
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
              <button class="p-1 text-blue-600 hover:bg-blue-50 rounded view-detail-btn" data-id="${product.id}">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                  <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
                </svg>
              </button>
              <button class="p-1 text-red-600 hover:bg-red-50 rounded view-delete-btn" data-id="${product.id}">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                  <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                </svg>
              </button>
            </div>
          </td>
        </tr>
      `;
      productTable.innerHTML += row;
    });

    // 수정 버튼 이벤트 리스너 추가
    document.querySelectorAll(".view-detail-btn").forEach((button) => {
      button.addEventListener("click", () => {
        const productId = button.getAttribute("data-id");
        window.location.href = `/product/${productId}`;
      });
    });

    // 삭제 버튼 이벤트 리스너 추가
    document.querySelectorAll(".view-delete-btn").forEach((button) => {
      button.addEventListener("click", async () => {
        const productId = button.getAttribute("data-id");

        if (confirm("정말로 이 상품을 삭제하시겠습니까?")) {
          try {
            const response = await fetch(`/api/v1/products/${productId}`, {
              method: "DELETE",
              headers: {
                "Content-Type": "application/json",
              },
            });

            if (!response.ok) {
              throw new Error("상품 삭제에 실패했습니다.");
            }

            alert("상품이 성공적으로 삭제되었습니다!");
            // 페이지 새로고침 또는 목록 갱신
            window.location.reload();
          } catch (error) {
            console.error(error);
            alert("상품 삭제 중 오류가 발생했습니다.");
          }
        }
      });
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
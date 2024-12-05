document.addEventListener("DOMContentLoaded", async () => {
  const form = document.getElementById("productForm");
  const nameInput = document.getElementById("name");
  const categorySelect = document.getElementById("category");
  const descriptionTextarea = document.getElementById("description");
  const previewContainer = document.getElementById("previewContainer");

  // 현재 URL에서 상품 ID 추출
  const productId = window.location.pathname.split("/").pop();

  // API 호출로 상품 상세 정보 가져오기
  async function fetchProductDetail() {
    try {
      const response = await fetch(`/api/v1/products/${productId}`);
      if (!response.ok) {
        throw new Error("상품 정보를 가져오는 데 실패했습니다.");
      }
      const result = await response.json();

      // 데이터 추출
      const product = result.data;

      // 폼에 데이터 바인딩
      nameInput.value = product.name || "";
      categorySelect.value = product.category || "";
      descriptionTextarea.value = product.description || "";

      // 이미지 미리보기
      previewContainer.innerHTML = "";
      if (product.imageFiles && product.imageFiles.length > 0) {
        product.imageFiles.forEach((file) => {
          const previewElement = document.createElement("div");
          previewElement.classList.add("relative");

          const img = document.createElement("img");
          img.src = file.imageUrl;
          img.alt = "상품 이미지";
          img.classList.add("w-full", "h-32", "object-cover", "rounded");

          previewElement.appendChild(img);
          previewContainer.appendChild(previewElement);
        });
      }
    } catch (error) {
      console.error(error);
      alert("상품 정보를 가져오는 중 오류가 발생했습니다.");
    }
  }


  // 수정 폼 제출 처리
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(form);

    try {
      const response = await fetch(`/api/v1/products/${productId}`, {
        method: "PUT",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("상품 정보를 수정하는 데 실패했습니다.");
      }

      alert("상품 정보가 수정되었습니다!");
      window.location.href = "/products";
    } catch (error) {
      console.error(error);
      alert("상품 정보를 수정하는 중 오류가 발생했습니다.");
    }
  });

  // 데이터 로드
  await fetchProductDetail();
});

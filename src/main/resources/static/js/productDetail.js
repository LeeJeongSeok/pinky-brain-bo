document.addEventListener("DOMContentLoaded", async () => {
  const form = document.getElementById("productForm");
  const nameInput = document.getElementById("name");
  const categorySelect = document.getElementById("category");
  const descriptionTextarea = document.getElementById("description");
  const previewContainer = document.getElementById("previewContainer");
  const dropArea = document.getElementById("dropArea");
  const fileInput = document.getElementById("image");

  const productId = window.location.pathname.split("/").pop();

  let images = []; // 새로운 이미지
  let existingImages = []; // 서버에서 로드된 기존 이미지
  let imagesToDelete = []; // 삭제할 이미지 목록

  // API 호출로 상품 상세 정보 가져오기
  async function fetchProductDetail() {
    try {
      const response = await fetch(`/api/v1/products/${productId}`);
      if (!response.ok) {
        throw new Error("상품 정보를 가져오는 데 실패했습니다.");
      }
      const result = await response.json();

      const product = result.data;

      // 폼에 데이터 바인딩
      nameInput.value = product.name || "";
      categorySelect.value = product.category || "";
      descriptionTextarea.value = product.description || "";

      // 기존 이미지 로드 및 미리보기 생성
      existingImages = product.imageFiles.map((file) => ({
        id: file.id,
        imageUrl: file.imageUrl,
        imageOrder: file.imageOrder,
      }));
      updatePreview();
    } catch (error) {
      console.error(error);
      alert("상품 정보를 가져오는 중 오류가 발생했습니다.");
    }
  }

  // 이미지 미리보기 업데이트
  function updatePreview() {
    previewContainer.innerHTML = "";

    // 기존 이미지
    existingImages.forEach((image, index) => {
      const previewElement = document.createElement("div");
      previewElement.classList.add("relative");

      const img = document.createElement("img");
      img.src = image.imageUrl;
      img.alt = `기존 이미지 ${index + 1}`;
      img.classList.add("w-full", "h-32", "object-cover", "rounded");

      const deleteButton = document.createElement("button");
      deleteButton.type = "button";
      deleteButton.classList.add(
          "absolute",
          "top-1",
          "right-1",
          "bg-red-500",
          "text-white",
          "rounded-full",
          "w-6",
          "h-6",
          "flex",
          "items-center",
          "justify-center"
      );
      deleteButton.textContent = "×";
      deleteButton.addEventListener("click", () => removeExistingImage(index));

      previewElement.appendChild(img);
      previewElement.appendChild(deleteButton);
      previewContainer.appendChild(previewElement);
    });

    // 새로 추가된 이미지
    images.forEach((image, index) => {
      const previewElement = document.createElement("div");
      previewElement.classList.add("relative");

      const img = document.createElement("img");
      img.src = image.preview;
      img.alt = `새 이미지 ${index + 1}`;
      img.classList.add("w-full", "h-32", "object-cover", "rounded");

      const deleteButton = document.createElement("button");
      deleteButton.type = "button";
      deleteButton.classList.add(
          "absolute",
          "top-1",
          "right-1",
          "bg-red-500",
          "text-white",
          "rounded-full",
          "w-6",
          "h-6",
          "flex",
          "items-center",
          "justify-center"
      );
      deleteButton.textContent = "×";
      deleteButton.addEventListener("click", () => removeImage(index));

      previewElement.appendChild(img);
      previewElement.appendChild(deleteButton);
      previewContainer.appendChild(previewElement);
    });
  }

  // 기존 이미지 삭제
  function removeExistingImage(index) {
    imagesToDelete.push(existingImages[index].id);
    existingImages.splice(index, 1);
    updatePreview();
  }

  // 새로운 이미지 추가
  function addImages(files) {
    images = [
      ...images,
      ...files.map((file) => ({
        file,
        preview: URL.createObjectURL(file),
      })),
    ];
    updatePreview();
  }

  // 새로운 이미지 삭제
  function removeImage(index) {
    URL.revokeObjectURL(images[index].preview);
    images.splice(index, 1);
    updatePreview();
  }

  // 드래그 앤 드롭 처리
  dropArea.addEventListener("dragover", (e) => e.preventDefault());
  dropArea.addEventListener("drop", (e) => {
    e.preventDefault();
    const files = Array.from(e.dataTransfer.files).filter((file) =>
        file.type.startsWith("image/")
    );
    addImages(files);
  });

  // 파일 선택 처리
  fileInput.addEventListener("change", (e) => {
    const files = Array.from(e.target.files).filter((file) =>
        file.type.startsWith("image/")
    );
    addImages(files);
  });

  // 폼 제출 처리
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(form);

    // 삭제된 이미지 추가
    if (imagesToDelete.length > 0) {
      imagesToDelete.forEach((imageId) => formData.append("imagesToDelete", imageId));
    }

    // 새로 추가된 이미지 추가
    images.forEach((image) => formData.append("newImages", image.file));

    try {
      const response = await fetch(`/api/v1/products/${productId}`, {
        method: "PATCH",
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

  // 초기 데이터 로드
  await fetchProductDetail();
});

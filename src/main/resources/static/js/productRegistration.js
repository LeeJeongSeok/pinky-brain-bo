document.addEventListener("DOMContentLoaded", () => {
  const dropArea = document.getElementById("dropArea");
  const fileInput = document.getElementById("image");
  const previewContainer = document.getElementById("previewContainer");
  const form = document.getElementById("productForm");

  let images = [];

  // Handle file drop
  dropArea.addEventListener("dragover", (e) => e.preventDefault());
  dropArea.addEventListener("drop", (e) => {
    e.preventDefault();
    const files = Array.from(e.dataTransfer.files).filter((file) =>
        file.type.startsWith("image/")
    );
    addImages(files);
  });

  // Handle file select
  fileInput.addEventListener("change", (e) => {
    const files = Array.from(e.target.files).filter((file) =>
        file.type.startsWith("image/")
    );
    addImages(files);
  });

  // Add images to preview
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

  // Update image previews
  function updatePreview() {
    previewContainer.innerHTML = "";
    images.forEach((image, index) => {
      const previewElement = document.createElement("div");
      previewElement.classList.add("relative");

      const img = document.createElement("img");
      img.src = image.preview;
      img.alt = `Preview ${index + 1}`;
      img.classList.add("w-full", "h-32", "object-cover", "rounded");

      const button = document.createElement("button");
      button.type = "button";
      button.classList.add(
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
      button.textContent = "×";
      button.addEventListener("click", () => removeImage(index));

      previewElement.appendChild(img);
      previewElement.appendChild(button);
      previewContainer.appendChild(previewElement);
    });
  }

  // Remove image
  function removeImage(index) {
    URL.revokeObjectURL(images[index].preview);
    images.splice(index, 1);
    updatePreview();
  }

  // Handle form submission
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(form);

    // 파일 입력 필드로 이미 추가된 이미지를 제거
    images.forEach((image) => {
      if (!formData.getAll("imageFiles").includes(image.file)) {
        formData.append("imageFiles", image.file);
      }
    });

    fetch("/api/v1/products", {
      method: "POST",
      body: formData,
    })
    .then((response) => {
      if (!response.ok) {
        throw new Error("서버 응답 에러");
      }
      return response.json();
    })
    .then((data) => {
      alert("상품 등록 완료!");
      window.location.href = "/products"; // 특정 페이지로 이동 및 새로고침
    })
    .catch((error) => console.error("Error:", error));
  });
});
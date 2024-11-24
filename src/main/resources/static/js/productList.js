document.addEventListener("DOMContentLoaded", () => {
  const searchInput = document.getElementById("searchInput");
  const productTable = document.getElementById("productTable");
  const totalCount = document.getElementById("totalCount");
  const prevPage = document.getElementById("prevPage");
  const nextPage = document.getElementById("nextPage");
  const currentPage = document.getElementById("currentPage");

  let products = Array.from(productTable.querySelectorAll("tr"));
  let filteredProducts = products;
  let page = 1;
  const itemsPerPage = 5;

  function updateTable() {
    products.forEach((product) => (product.style.display = "none"));
    const start = (page - 1) * itemsPerPage;
    const end = page * itemsPerPage;
    filteredProducts.slice(start, end).forEach((product) => (product.style.display = ""));
    totalCount.textContent = `총 ${filteredProducts.length}개의 상품`;
    currentPage.textContent = page;
    prevPage.disabled = page === 1;
    nextPage.disabled = page * itemsPerPage >= filteredProducts.length;
  }

  searchInput.addEventListener("input", (e) => {
    const searchTerm = e.target.value.toLowerCase();
    filteredProducts = products.filter((product) =>
        product.textContent.toLowerCase().includes(searchTerm)
    );
    page = 1;
    updateTable();
  });

  prevPage.addEventListener("click", () => {
    if (page > 1) {
      page--;
      updateTable();
    }
  });

  nextPage.addEventListener("click", () => {
    if (page * itemsPerPage < filteredProducts.length) {
      page++;
      updateTable();
    }
  });

  updateTable();
});
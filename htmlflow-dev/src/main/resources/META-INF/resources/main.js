document.addEventListener("turbo:submit-end", (event) => {
    if (event.target.id === "cart_frame" && event.detail.success) {
        Turbo.visit("/bikes", { frame: "test_bikes_frame", action: "replace" });
        Turbo.visit("/cart", { frame: "test_cart_frame", action: "replace" });
        Turbo.visit("/order/history", { frame: "test_transaction_frame", action: "replace" });
    }
});

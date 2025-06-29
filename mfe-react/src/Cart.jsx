import React from "react";
import CartItem from "./CartItem";

const Cart = ({ cart, onDelete, onIncrease, onDecrease, onCheckout }) => {
    if (!cart) {
        return <div>Loading cart...</div>;
    }

    return (
        <>
            <div className="cart-container">
                {cart.cartItems.map((item) => (
                    <CartItem
                        key={item.id}
                        item={item}
                        onDelete={onDelete}
                        onIncrease={onIncrease}
                        onDecrease={onDecrease}
                    />
                ))}
            </div>

            <div className="cart-summary">
                <h3>Total: {cart.cartTotal.toFixed(2)} €</h3>
                <button
                    className="btn btn-checkout"
                    onClick={onCheckout}
                    disabled={cart.cartItems.length === 0}
                >
                    Checkout
                </button>
            </div>
        </>
    );
};

export default Cart;
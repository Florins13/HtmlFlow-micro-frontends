import React from "react";

const CartItem = ({ item, onDelete, onIncrease, onDecrease }) => {
    const { id, quantity, bike } = item;
    const { model, imageSource, stock, details, isElectric, price } = bike;

    return (
        <div className="cart-item">
            <div className="bike-box">
                <button className="btn btn-delete" onClick={() => onDelete(id)}>
                    X
                </button>

                <h4>Model: {model}</h4>

                <img src={imageSource} className="bike-img" alt={`Bike: ${model}`} />

                <span>Stock: {stock}</span>
                <span>Details: {details}</span>
                <span>Electric: {isElectric ? "Yes" : "No"}</span>
                <span>Price: {price}€</span>

                <div className="quantity-group">
                    <button className="btn btn-qty" onClick={() => onDecrease(id)}>
                        Decrease -
                    </button>

                    <p>{quantity}</p>

                    <button className="btn btn-qty" onClick={() => onIncrease(id)}>
                        Increase +
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CartItem;
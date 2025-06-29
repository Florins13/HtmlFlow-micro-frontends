import './App.css'
import Cart from "./Cart.jsx";
import {useEffect, useState} from "react";

const App = () => {
    const [cart, setCart] = useState(null);

    const mfeTriggerOrderEvent = "triggerOrderEvent";
    const mfeListenAddEvent = 'triggerAddEvent';

    useEffect(() => {
        const handler = (e) => {
            if (e.detail?.payload?.type === "add") {
                handleAddBike(e.detail.payload.id);
            }
        };

        window.addEventListener(mfeListenAddEvent, handler);

        return () => {
            window.removeEventListener(mfeListenAddEvent, handler);
        };
    }, []);

    const dispatchAddEvent = () => {

        const event = new CustomEvent(mfeListenAddEvent, {
            detail: {
                payload: {
                    type: "add",
                    id: 1,
                },
            },
        });

        window.dispatchEvent(event);
    };

    const handleAddBike = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/cart/add/${id}`, { method: "POST" });

            if (!response.ok) throw new Error("Failed to add bike");

            // No JSON response, so refetch the full cart state:
            const cartResponse = await fetch("http://localhost:8080/cart");
            if (!cartResponse.ok) throw new Error("Failed to fetch cart");

            const updatedCart = await cartResponse.json();
            setCart(updatedCart);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        // Replace with your actual endpoint
        fetch("http://localhost:8080/cart")
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Failed to fetch cart data");
                }
                return res.json();
            })
            .then((data) => {
                setCart(data);
            })
            .catch((error) => {
                console.error("Error fetching cart:", error);
            });
    }, []);// initially null

    const deleteCartItem = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/cart/deleteItem/${id}`, { method: "POST" });
            if (!response.ok) {
                throw new Error("Failed to delete item");
            }
            // Update state by filtering out deleted item
            const updatedItems = cart.cartItems.filter(item => item.id !== id);

            // Recalculate total price
            const newTotal = updatedItems.reduce((sum, item) => sum + item.quantity * item.bike.price, 0);

            setCart({
                cartItems: updatedItems,
                cartTotal: newTotal,
                cartIsEmpty: function () {
                    return this.cartItems.length === 0;
                }
            });
        } catch (error) {
            console.error("Delete error:", error);
        }
    };

    const handleUpdateQuantity = async (id, type) => {
        // type can be "increase" or "decrease"
        try {
            const response = await fetch(`http://localhost:8080/cart/updateQuantity/${id}/${type}`, {
                method: "POST",
            });

            if (!response.ok) {
                throw new Error("Failed to update quantity");
            }

            // Option 1: If the response returns the updated cart, use it to set state
            // const updatedCart = await response.json();
            // setCart(updatedCart);

            // Option 2: If not, update state locally like before:
            const updatedItems = cart.cartItems.map(item => {
                if (item.id === id) {
                    const newQuantity = type === "increase" ? item.quantity + 1 : item.quantity - 1;
                    return {
                        ...item,
                        quantity: newQuantity > 0 ? newQuantity : 0,
                    };
                }
                return item;
            }).filter(item => item.quantity > 0);

            const newTotal = updatedItems.reduce(
                (sum, item) => sum + item.quantity * item.bike.price,
                0
            );

            setCart({
                cartItems: updatedItems,
                cartTotal: newTotal,
                cartIsEmpty: function () {
                    return this.cartItems.length === 0;
                },
            });

        } catch (error) {
            console.error("Update quantity error:", error);
        }
    };

    const handleCheckout = async () => {
        try {
            const response = await fetch("http://localhost:8080/order/finalise", {
                method: "POST",
            });
            setCart({
                cartItems: [],
                cartTotal: 0,
                cartIsEmpty: true
            });
            const event = new CustomEvent(mfeTriggerOrderEvent, {
                detail: { message: 'post success', type: 'reload' },
                bubbles: true,
                composed: true
            });
            dispatchEvent(event);

        }
        catch (error) {
            console.error("Checkout error:", error);
        }
    }
    return (
        <div>
            <Cart
                cart={cart}
                onDelete={deleteCartItem}
                onDecrease={id => handleUpdateQuantity(id, "decrease")}
                onIncrease={id => handleUpdateQuantity(id, "increase")}
                onCheckout={handleCheckout}
            />
        </div>
    );
}

export default App
// <button onClick={dispatchAddEvent}>Add Random Bike</button>
package com.dev.shop.repository;

import com.dev.shop.model.CartItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CartItemRepository implements PanacheRepository<CartItem> {

    public void deleteCartItemById(Long id){
        this.delete("cart_id", id);
    }

    public void deleteWhereCartIdAndCartItemId(Long cartItemId, Long cartId){
        this.delete("id = ?1 and cart_id = ?2", cartItemId, cartId);
    }

}

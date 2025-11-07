package com.example.gio_hang.entity;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<Product,Integer> products = new HashMap<>();

    public Cart() {
    }

    public Cart(Map<Product,Integer> products) {
        this.products = products;
    }

    public Map<Product,Integer> getProducts() {
        return products;
    }

    private boolean checkItemInCart(Product product){
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            if(entry.getKey().getId().equals(product.getId())){
                return true;
            }
        }
        return false;
    }

    private Map.Entry<Product, Integer> selectItemInCart(Product product){
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            if(entry.getKey().getId().equals(product.getId())){
                return entry;
            }
        }
        return null;
    }

    public void addProduct(Product product){
        if (!checkItemInCart(product)){
            products.put(product,1);
        } else {
            Map.Entry<Product, Integer> itemEntry = selectItemInCart(product);
            Integer newQuantity = itemEntry.getValue() + 1;
            products.replace(itemEntry.getKey(),newQuantity);
        }
    }

    public Integer countProductQuantity(){
        Integer productQuantity = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            productQuantity += entry.getValue();
        }
        return productQuantity;
    }

    public Integer countItemQuantity(){
        return products.size();
    }

    public Float countTotalPayment(){
        float payment = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            payment += entry.getKey().getPrice() * (float) entry.getValue();
        }
        return payment;
    }

    public void updateQuantity(Product product, int quantity) {
        Map.Entry<Product, Integer> itemEntry = selectItemInCart(product);
        if (itemEntry != null) {
            if (quantity <= 0) {
                // Nếu số lượng <= 0, xóa sản phẩm khỏi giỏ hàng
                products.remove(itemEntry.getKey());
            } else {
                // Ngược lại, cập nhật số lượng
                products.replace(itemEntry.getKey(), quantity);
            }
        }
    }

    public void removeProduct(Product product) {
        Map.Entry<Product, Integer> itemEntry = selectItemInCart(product);
        if (itemEntry != null) {
            products.remove(itemEntry.getKey());
        }
    }


    public void clearCart() {
        products.clear();
    }
}
package com.sasidharanmart.shop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * One line item inside a placed Order. Snapshots the product name and price
 * at purchase time, so later price changes don't rewrite historical orders.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 150)
    private String productName; // snapshot

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase; // snapshot

    @Column(nullable = false)
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(Product product, String productName, BigDecimal priceAtPurchase, Integer quantity) {
        this.product = product;
        this.productName = productName;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
    }

    // ----- Getters and setters -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
    }
}

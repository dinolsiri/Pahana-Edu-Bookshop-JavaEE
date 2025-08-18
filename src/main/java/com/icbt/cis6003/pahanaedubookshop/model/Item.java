package com.icbt.cis6003.pahanaedubookshop.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Item entity representing a bookshop inventory item
 */
public class Item {
    private Long id;
    private String code;
    private String name;
    private ItemCategory category;
    private BigDecimal price;
    private Integer stock;
    private Integer minStock;
    private String description;

    // Default constructor
    public Item() {
        this.minStock = 5; // Default minimum stock level
    }

    // Constructor with required fields
    public Item(String code, String name, ItemCategory category, BigDecimal price, Integer stock) {
        this();
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Full constructor
    public Item(Long id, String code, String name, ItemCategory category, 
               BigDecimal price, Integer stock, Integer minStock, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.minStock = minStock;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Validation methods
    public boolean isValid() {
        return code != null && !code.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               category != null &&
               price != null && price.compareTo(BigDecimal.ZERO) >= 0 &&
               stock != null && stock >= 0;
    }

    // Business methods
    public boolean isInStock() {
        return stock != null && stock > 0;
    }

    public boolean isLowStock() {
        return stock != null && minStock != null && stock <= minStock;
    }

    public BigDecimal getTotalValue() {
        if (price == null || stock == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(stock));
    }

    public void updateStock(int quantity) {
        if (stock == null) {
            stock = 0;
        }
        stock += quantity;
        if (stock < 0) {
            stock = 0;
        }
    }

    public boolean canFulfillOrder(int requestedQuantity) {
        return stock != null && stock >= requestedQuantity;
    }

    public String getDisplayName() {
        return name + " (" + code + ")";
    }

    public ItemStatus getStatus() {
        if (!isInStock()) {
            return ItemStatus.OUT_OF_STOCK;
        } else if (isLowStock()) {
            return ItemStatus.LOW_STOCK;
        } else {
            return ItemStatus.IN_STOCK;
        }
    }

    // Override methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
               Objects.equals(code, item.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", stock=" + stock +
                ", minStock=" + minStock +
                ", description='" + description + '\'' +
                '}';
    }

    // Enums
    public enum ItemCategory {
        TEXTBOOK("Textbook"),
        REFERENCE("Reference Book"),
        STATIONERY("Stationery"),
        DIGITAL("Digital Resource");

        private final String displayName;

        ItemCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum ItemStatus {
        IN_STOCK("In Stock"),
        LOW_STOCK("Low Stock"),
        OUT_OF_STOCK("Out of Stock");

        private final String displayName;

        ItemStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}

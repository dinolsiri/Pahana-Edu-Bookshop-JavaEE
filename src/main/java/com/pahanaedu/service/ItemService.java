package com.pahanaedu.service;

import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.model.Item;

import java.util.List;

public class ItemService {
    
    private ItemDAO itemDAO;

    public ItemService() {
        this.itemDAO = new ItemDAO();
    }

    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    public Item getItemById(int id) {
        return itemDAO.getItemById(id);
    }

    public boolean addItem(Item item) {
        return itemDAO.addItem(item);
    }

    public boolean updateItem(Item item) {
        return itemDAO.updateItem(item);
    }

    public boolean deleteItem(int id) {
        return itemDAO.deleteItem(id);
    }

    public int getItemCount() {
        return itemDAO.getItemCount();
    }

    public boolean updateStock(int itemId, int newQuantity) {
        return itemDAO.updateStock(itemId, newQuantity);
    }
}
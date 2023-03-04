/**
 * Class SaleListingDAOInMemory
 * Author: John Salame
 * CSCI 5673 Distributed Systems
 * Assignment 2 - RPC
 * Description: Provide access to the sale listings (item + quantity) database.
 */

package com.jsala.db.product;

import com.jsala.common.*;
import com.jsala.dao.ItemDAO;
import com.jsala.dao.SaleListingDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SaleListingDAOInMemory implements SaleListingDAO {
    static int nextId = 1;
    private List<SaleListing> saleListings = null;
    private ItemDAO itemDAO; // later on, turn this into a CustomerDAOFactory instead

    // CONSTRUCTOR
    public SaleListingDAOInMemory(ItemDAO itemDAO) {
        saleListings = new ArrayList<SaleListing>();
        this.itemDAO = itemDAO;
    }

    /**
     * Generate chooses the next unique user id
     * Currently not thread-safe.
     */
    private int generateUniqueId() {
        int currId = this.nextId;
        this.nextId++;
        return currId;
    }

    @Override
    public SaleListingId putItemOnSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, IllegalArgumentException, UnsupportedOperationException {
        // first, verify that this seller can put the item on sale
        Item item = itemDAO.getItemById(itemId);
        if(item.getSellerId() != sellerId) {
            throw new UnsupportedOperationException("Wrong seller is trying to sell item");
        }
        SaleListing saleListing = new SaleListing(itemId, quantity);
        // after all the validation, finally generate a database id
        int dbId = this.generateUniqueId();
        saleListing.setDbId(dbId);
        try {
            this.saleListings.add(saleListing);
        } catch (Exception e) {
            throw new IOException("Error adding seller to database");
        }
        return saleListing.getId();
    }
    @Override
    public void removeItemFromSale(int sellerId, ItemId itemId, int quantity) throws IOException, NoSuchElementException, UnsupportedOperationException {
        // first, verify that this seller can remove the item
        Item item = itemDAO.getItemById(itemId); // NoSuchElementException
        if(item.getSellerId() != sellerId) {
            throw new UnsupportedOperationException("Wrong seller is trying to remove item from sale");
        }

        SaleListingId saleListingId = null;
        try {
            saleListingId = new SaleListingId(itemId, quantity);
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(e.getMessage());
        }
        // remove the first item you find with the quantity
        // TO-DO: If data structure becomes unordered, use database id for determinism
        try {
            for(SaleListing saleListing : saleListings) {
                if(saleListingId.equals(saleListing.getId()) && saleListing.isAvailable()) {
                    saleListing.removeListing(); // mark it as removed from sale
                    return;
                }
            }
        } catch (Exception e) {
            throw new IOException("Error searching for sale listing");
        }
        // if no available sale listing is found matching the item id and quantity, throw NoSuchElementException
        throw new NoSuchElementException("Could not remove item from sale -- does not exist");
    }

    @Override
    // can return an empty array
    public SaleListing[] getSaleListingsBySeller(int sellerId) throws IOException {
        Item[] itemsBySeller = itemDAO.getItemsBySeller(sellerId);
        List<SaleListing> salesList = new ArrayList<SaleListing>();
        try {
            for(SaleListing saleListing : saleListings) {
                for (Item item : itemsBySeller){
                    if (saleListing.getItemId().equals(item.getId())) {
                        salesList.add(saleListing);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("Error iterating through sales listings");
        }
        SaleListing[] ret = new SaleListing[0];
        ret = salesList.toArray(ret);
        return ret;
    }

    @Override
    public DetailedSaleListing[] getDetailedSaleListingsBySeller(int sellerId) throws IOException {
        SaleListing[] saleListings = this.getSaleListingsBySeller(sellerId);
        int numListings = saleListings.length;
        DetailedSaleListing[] dsl = new DetailedSaleListing[numListings];
        int numElements = 0;
        DetailedSaleListing[] ret = dsl;
        try {
            // I could optimize later on by using batch retrievals
            for (int i = 0; i < numListings; ++i) {
                SaleListing saleListing = saleListings[i];
                dsl[i] = new DetailedSaleListing(saleListing, this.itemDAO.getItemById(saleListing.getItemId()));
                ++numElements;
            }
        } catch (Exception e) {
            // copy only the detailed sale listings which did not throw an Exception during creation
            ret = new DetailedSaleListing[numElements];
            for(int i = 0; i < numElements; ++i) {
                ret[i] = dsl[i];
            }
        }
        return ret;
    }

    @Override
    public void closeConnection() throws IOException {
        itemDAO.closeConnection();
    }
}

package org.tucn.pt.model;

public record Bill(int orderId, String clientName, String productName, int orderedQuantity, double totalPrice) {
}
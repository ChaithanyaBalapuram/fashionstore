package com.fashionstore.model;

import java.sql.Timestamp;

public class OrderTracking {

    private int trackingId;
    private int orderId;
    private String status;
    private String remarks;
    private String updatedBy;
    private Timestamp updatedAt;

    public int getTrackingId() { return trackingId; }
    public void setTrackingId(int trackingId) { this.trackingId = trackingId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}

package com.webmyne.kidscrown.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vatsaldesai on 25-01-2017.
 */

public class OrderHistoryModel implements Serializable{

    /**
     * BillingAddressDC : {"Address1":"String content","Address2":"String content","City":"String content","EmailID":"String content","IsShipping":true,"MobileNo":"String content","PinCode":"String content","StateName":"String content"}
     * InvoiceNumber : String content
     * OrderDate : String content
     * OrderID : 9223372036854775807
     * OrderNumber : String content
     * ShippingAddressDC : {"Address1":"String content","Address2":"String content","City":"String content","EmailID":"String content","IsShipping":true,"MobileNo":"String content","PinCode":"String content","StateName":"String content"}
     * ShippingCost : 1.2678967543233E7
     * Status : String content
     * StatusID : 9223372036854775807
     * SubTotal : 1.2678967543233E7
     * TotalAmount : 1.2678967543233E7
     * YouSaved : 1.2678967543233E7
     * lstOrderProduct : [{"ImagePath":"String content","ProductID":9223372036854775807,"ProductName":"String content","ProductPrice":1.2678967543233E7,"ProductSpecification":"String content","Quantity":9223372036854775807,"UnitPrice":1.2678967543233E7}]
     */

    private BillingAddressDCBean BillingAddressDC;
    private String InvoiceNumber;
    private String OrderDate;
    private long OrderID;
    private String OrderNumber;
    private ShippingAddressDCBean ShippingAddressDC;
    private double ShippingCost;
    private String Status;
    private long StatusID;
    private double SubTotal;
    private double TotalAmount;
    private double YouSaved;
    private List<LstOrderProductBean> lstOrderProduct;

    public BillingAddressDCBean getBillingAddressDC() {
        return BillingAddressDC;
    }

    public void setBillingAddressDC(BillingAddressDCBean BillingAddressDC) {
        this.BillingAddressDC = BillingAddressDC;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String InvoiceNumber) {
        this.InvoiceNumber = InvoiceNumber;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public long getOrderID() {
        return OrderID;
    }

    public void setOrderID(long OrderID) {
        this.OrderID = OrderID;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String OrderNumber) {
        this.OrderNumber = OrderNumber;
    }

    public ShippingAddressDCBean getShippingAddressDC() {
        return ShippingAddressDC;
    }

    public void setShippingAddressDC(ShippingAddressDCBean ShippingAddressDC) {
        this.ShippingAddressDC = ShippingAddressDC;
    }

    public double getShippingCost() {
        return ShippingCost;
    }

    public void setShippingCost(double ShippingCost) {
        this.ShippingCost = ShippingCost;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public long getStatusID() {
        return StatusID;
    }

    public void setStatusID(long StatusID) {
        this.StatusID = StatusID;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double SubTotal) {
        this.SubTotal = SubTotal;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public double getYouSaved() {
        return YouSaved;
    }

    public void setYouSaved(double YouSaved) {
        this.YouSaved = YouSaved;
    }

    public List<LstOrderProductBean> getLstOrderProduct() {
        return lstOrderProduct;
    }

    public void setLstOrderProduct(List<LstOrderProductBean> lstOrderProduct) {
        this.lstOrderProduct = lstOrderProduct;
    }

    public static class BillingAddressDCBean {
        /**
         * Address1 : String content
         * Address2 : String content
         * City : String content
         * EmailID : String content
         * IsShipping : true
         * MobileNo : String content
         * PinCode : String content
         * StateName : String content
         */

        private String Address1;
        private String Address2;
        private String City;
        private String EmailID;
        private boolean IsShipping;
        private String MobileNo;
        private String PinCode;
        private String StateName;

        public String getAddress1() {
            return Address1;
        }

        public void setAddress1(String Address1) {
            this.Address1 = Address1;
        }

        public String getAddress2() {
            return Address2;
        }

        public void setAddress2(String Address2) {
            this.Address2 = Address2;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getEmailID() {
            return EmailID;
        }

        public void setEmailID(String EmailID) {
            this.EmailID = EmailID;
        }

        public boolean isIsShipping() {
            return IsShipping;
        }

        public void setIsShipping(boolean IsShipping) {
            this.IsShipping = IsShipping;
        }

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String MobileNo) {
            this.MobileNo = MobileNo;
        }

        public String getPinCode() {
            return PinCode;
        }

        public void setPinCode(String PinCode) {
            this.PinCode = PinCode;
        }

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String StateName) {
            this.StateName = StateName;
        }
    }

    public static class ShippingAddressDCBean {
        /**
         * Address1 : String content
         * Address2 : String content
         * City : String content
         * EmailID : String content
         * IsShipping : true
         * MobileNo : String content
         * PinCode : String content
         * StateName : String content
         */

        private String Address1;
        private String Address2;
        private String City;
        private String EmailID;
        private boolean IsShipping;
        private String MobileNo;
        private String PinCode;
        private String StateName;

        public String getAddress1() {
            return Address1;
        }

        public void setAddress1(String Address1) {
            this.Address1 = Address1;
        }

        public String getAddress2() {
            return Address2;
        }

        public void setAddress2(String Address2) {
            this.Address2 = Address2;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getEmailID() {
            return EmailID;
        }

        public void setEmailID(String EmailID) {
            this.EmailID = EmailID;
        }

        public boolean isIsShipping() {
            return IsShipping;
        }

        public void setIsShipping(boolean IsShipping) {
            this.IsShipping = IsShipping;
        }

        public String getMobileNo() {
            return MobileNo;
        }

        public void setMobileNo(String MobileNo) {
            this.MobileNo = MobileNo;
        }

        public String getPinCode() {
            return PinCode;
        }

        public void setPinCode(String PinCode) {
            this.PinCode = PinCode;
        }

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String StateName) {
            this.StateName = StateName;
        }
    }

    public static class LstOrderProductBean {
        /**
         * ImagePath : String content
         * ProductID : 9223372036854775807
         * ProductName : String content
         * ProductPrice : 1.2678967543233E7
         * ProductSpecification : String content
         * Quantity : 9223372036854775807
         * UnitPrice : 1.2678967543233E7
         */

        private String ImagePath;
        private long ProductID;
        private String ProductName;
        private double ProductPrice;
        private String ProductSpecification;
        private long Quantity;
        private double UnitPrice;

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public long getProductID() {
            return ProductID;
        }

        public void setProductID(long ProductID) {
            this.ProductID = ProductID;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public double getProductPrice() {
            return ProductPrice;
        }

        public void setProductPrice(double ProductPrice) {
            this.ProductPrice = ProductPrice;
        }

        public String getProductSpecification() {
            return ProductSpecification;
        }

        public void setProductSpecification(String ProductSpecification) {
            this.ProductSpecification = ProductSpecification;
        }

        public long getQuantity() {
            return Quantity;
        }

        public void setQuantity(long Quantity) {
            this.Quantity = Quantity;
        }

        public double getUnitPrice() {
            return UnitPrice;
        }

        public void setUnitPrice(double UnitPrice) {
            this.UnitPrice = UnitPrice;
        }
    }
}
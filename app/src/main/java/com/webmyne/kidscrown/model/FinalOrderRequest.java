package com.webmyne.kidscrown.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagartahelyani on 01-02-2017.
 */

public class FinalOrderRequest {

    /**
     * MobileOS : String content
     * UserID : 9223372036854775807
     * billingAddressDC : {"Address1":"String content","Address2":"String content","BillingAddressID":9223372036854775807,"City":"String content","Country":"String content","Email":"String content","IsUpdated":true,"MobileNo":"String content","PinCode":"String content"}
     * placeOrderCalculationDC : {"DeliveryCharge":1.2678967543233E7,"InvoiceDiscount":1.2678967543233E7,"InvoiceDiscountID":9223372036854775807,"PayableAmount":1.2678967543233E7,"ProductDiscount":1.2678967543233E7,"TotalAmount":1.2678967543233E7,"productCalculationDCs":[{"Discount":1.2678967543233E7,"IsSingle":true,"Price":1.2678967543233E7,"PriceID":9223372036854775807,"ProductID":9223372036854775807,"ProductName":"String content","Quntity":2147483647,"TotalPrice":1.2678967543233E7,"placeOrderRiffileDC":[{"ProductSpecID":9223372036854775807,"Quntity":2147483647,"RiffleName":"String content"}]}]}
     * placeOrderProductDC : [{"ProductID":9223372036854775807,"Quntity":2147483647,"placeOrderRiffileDC":[{"ProductSpecID":9223372036854775807,"Quntity":2147483647,"RiffleName":"String content"}]}]
     * shippingAddressDC : {"Address1":"String content","Address2":"String content","City":"String content","Country":"String content","Email":"String content","IsUpdated":true,"MobileNo":"String content","PinCode":"String content","ShippingAddressID":9223372036854775807}
     */

    private String MobileOS = "A";
    private long UserID;
    private PlaceOrderRequest.BillingAddressDCBean billingAddressDC;
    private PlaceOrderResponse.DataBean.PlaceOrderCalculationDCBean placeOrderCalculationDC;
    private PlaceOrderRequest.ShippingAddressDCBean shippingAddressDC;
    private List<PlaceOrderRequest.PlaceOrderProductDCBean> placeOrderProductDC;

    public String getMobileOS() {
        return MobileOS;
    }

    public void setMobileOS(String MobileOS) {
        this.MobileOS = MobileOS;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long UserID) {
        this.UserID = UserID;
    }

    public PlaceOrderRequest.BillingAddressDCBean getBillingAddressDC() {
        return billingAddressDC;
    }

    public void setBillingAddressDC(PlaceOrderRequest.BillingAddressDCBean billingAddressDC) {
        this.billingAddressDC = billingAddressDC;
    }

    public PlaceOrderResponse.DataBean.PlaceOrderCalculationDCBean getPlaceOrderCalculationDC() {
        return placeOrderCalculationDC;
    }

    public void setPlaceOrderCalculationDC(PlaceOrderResponse.DataBean.PlaceOrderCalculationDCBean placeOrderCalculationDC) {
        this.placeOrderCalculationDC = placeOrderCalculationDC;
    }

    public PlaceOrderRequest.ShippingAddressDCBean getShippingAddressDC() {
        return shippingAddressDC;
    }

    public void setShippingAddressDC(PlaceOrderRequest.ShippingAddressDCBean shippingAddressDC) {
        this.shippingAddressDC = shippingAddressDC;
    }

    public List<PlaceOrderRequest.PlaceOrderProductDCBean> getPlaceOrderProductDC() {
        return placeOrderProductDC;
    }

    public void setPlaceOrderProductDC(List<PlaceOrderRequest.PlaceOrderProductDCBean> placeOrderProductDC) {
        this.placeOrderProductDC = placeOrderProductDC;
    }

    public static class BillingAddressDCBean {
        public BillingAddressDCBean() {
            Address1 = "";
            Address2 = "";
            BillingAddressID = 0;
            City = "";
            Email = "";
            IsUpdated = false;
            MobileNo = "";
            PinCode = "";
        }



        /**
         * Address1 : String content
         * Address2 : String content
         * BillingAddressID : 9223372036854775807
         * City : String content
         * Country : String content
         * Email : String content
         * IsUpdated : true
         * MobileNo : String content
         * PinCode : String content
         */


        private String Address1;
        private String Address2;
        private long BillingAddressID;
        private String City;
        private String Country;
        private String Email;
        private boolean IsUpdated;
        private String MobileNo;
        private String PinCode;

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

        public long getBillingAddressID() {
            return BillingAddressID;
        }

        public void setBillingAddressID(long BillingAddressID) {
            this.BillingAddressID = BillingAddressID;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public boolean isIsUpdated() {
            return IsUpdated;
        }

        public void setIsUpdated(boolean IsUpdated) {
            this.IsUpdated = IsUpdated;
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
    }

    public static class PlaceOrderCalculationDCBean {
        public PlaceOrderCalculationDCBean() {
            DeliveryCharge = 0.0;
            InvoiceDiscount = 0.0;
            InvoiceDiscountID = 0;
            PayableAmount = 0.0;
            ProductDiscount = 0.0;
            TotalAmount = 0.0;
            productCalculationDCs = new ArrayList<>();
            productCalculationDCs.add(new PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean());
        }

        /**
         * DeliveryCharge : 1.2678967543233E7
         * InvoiceDiscount : 1.2678967543233E7
         * InvoiceDiscountID : 9223372036854775807
         * PayableAmount : 1.2678967543233E7
         * ProductDiscount : 1.2678967543233E7
         * TotalAmount : 1.2678967543233E7
         * productCalculationDCs : [{"Discount":1.2678967543233E7,"IsSingle":true,"Price":1.2678967543233E7,"PriceID":9223372036854775807,"ProductID":9223372036854775807,"ProductName":"String content","Quntity":2147483647,"TotalPrice":1.2678967543233E7,"placeOrderRiffileDC":[{"ProductSpecID":9223372036854775807,"Quntity":2147483647,"RiffleName":"String content"}]}]
         */


        private double DeliveryCharge;
        private double InvoiceDiscount;
        private long InvoiceDiscountID;
        private double PayableAmount;
        private double ProductDiscount;
        private double TotalAmount;
        private List<PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean> productCalculationDCs;

        public double getDeliveryCharge() {
            return DeliveryCharge;
        }

        public void setDeliveryCharge(double DeliveryCharge) {
            this.DeliveryCharge = DeliveryCharge;
        }

        public double getInvoiceDiscount() {
            return InvoiceDiscount;
        }

        public void setInvoiceDiscount(double InvoiceDiscount) {
            this.InvoiceDiscount = InvoiceDiscount;
        }

        public long getInvoiceDiscountID() {
            return InvoiceDiscountID;
        }

        public void setInvoiceDiscountID(long InvoiceDiscountID) {
            this.InvoiceDiscountID = InvoiceDiscountID;
        }

        public double getPayableAmount() {
            return PayableAmount;
        }

        public void setPayableAmount(double PayableAmount) {
            this.PayableAmount = PayableAmount;
        }

        public double getProductDiscount() {
            return ProductDiscount;
        }

        public void setProductDiscount(double ProductDiscount) {
            this.ProductDiscount = ProductDiscount;
        }

        public double getTotalAmount() {
            return TotalAmount;
        }

        public void setTotalAmount(double TotalAmount) {
            this.TotalAmount = TotalAmount;
        }

        public List<PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean> getProductCalculationDCs() {
            return productCalculationDCs;
        }

        public void setProductCalculationDCs(List<PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean> productCalculationDCs) {
            this.productCalculationDCs = productCalculationDCs;
        }

        public static class ProductCalculationDCsBean {
            public ProductCalculationDCsBean() {
                Discount = 0.0;
                IsSingle = false;
                Price = 0.0;
                PriceID = 0;
                ProductID = 0;
                ProductName = "";
                Quntity = 0;
                TotalPrice = 0.0;
                placeOrderRiffileDC = new ArrayList<>();
                placeOrderRiffileDC.add(new PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean.PlaceOrderRiffileDCBean());
            }

            /**
             * Discount : 1.2678967543233E7
             * IsSingle : true
             * Price : 1.2678967543233E7
             * PriceID : 9223372036854775807
             * ProductID : 9223372036854775807
             * ProductName : String content
             * Quntity : 2147483647
             * TotalPrice : 1.2678967543233E7
             * placeOrderRiffileDC : [{"ProductSpecID":9223372036854775807,"Quntity":2147483647,"RiffleName":"String content"}]
             */


            private double Discount;
            private boolean IsSingle;
            private double Price;
            private long PriceID;
            private long ProductID;
            private String ProductName;
            private int Quntity;
            private double TotalPrice;
            private List<PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean.PlaceOrderRiffileDCBean> placeOrderRiffileDC;

            public double getDiscount() {
                return Discount;
            }

            public void setDiscount(double Discount) {
                this.Discount = Discount;
            }

            public boolean isIsSingle() {
                return IsSingle;
            }

            public void setIsSingle(boolean IsSingle) {
                this.IsSingle = IsSingle;
            }

            public double getPrice() {
                return Price;
            }

            public void setPrice(double Price) {
                this.Price = Price;
            }

            public long getPriceID() {
                return PriceID;
            }

            public void setPriceID(long PriceID) {
                this.PriceID = PriceID;
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

            public int getQuntity() {
                return Quntity;
            }

            public void setQuntity(int Quntity) {
                this.Quntity = Quntity;
            }

            public double getTotalPrice() {
                return TotalPrice;
            }

            public void setTotalPrice(double TotalPrice) {
                this.TotalPrice = TotalPrice;
            }

            public List<PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean.PlaceOrderRiffileDCBean> getPlaceOrderRiffileDC() {
                return placeOrderRiffileDC;
            }

            public void setPlaceOrderRiffileDC(List<PlaceOrderRequest.PlaceOrderCalculationDCBean.ProductCalculationDCsBean.PlaceOrderRiffileDCBean> placeOrderRiffileDC) {
                this.placeOrderRiffileDC = placeOrderRiffileDC;
            }

            public static class PlaceOrderRiffileDCBean {
                public PlaceOrderRiffileDCBean() {
                    ProductSpecID = 0;
                    Quntity = 0;
                    RiffleName = "";
                }

                /**
                 * ProductSpecID : 9223372036854775807
                 * Quntity : 2147483647
                 * RiffleName : String content
                 */

                private long ProductSpecID;
                private int Quntity;
                private String RiffleName;

                public long getProductSpecID() {
                    return ProductSpecID;
                }

                public void setProductSpecID(long ProductSpecID) {
                    this.ProductSpecID = ProductSpecID;
                }

                public int getQuntity() {
                    return Quntity;
                }

                public void setQuntity(int Quntity) {
                    this.Quntity = Quntity;
                }

                public String getRiffleName() {
                    return RiffleName;
                }

                public void setRiffleName(String RiffleName) {
                    this.RiffleName = RiffleName;
                }
            }
        }
    }

    public static class ShippingAddressDCBean {

        public ShippingAddressDCBean() {
            Address1 = "";
            Address2 = "";
            City = "";
            IsUpdated = false;
            MobileNo = "";
            PinCode = "";
            ShippingAddressID = 0;
        }

        @Override
        public String toString() {
            return Address1 + ", " + Address2 + ", " + City + ", " + PinCode;
        }

        /**
         * Address1 : String content
         * Address2 : String content
         * City : String content
         * Country : String content
         * Email : String content
         * IsUpdated : true
         * MobileNo : String content
         * PinCode : String content
         * ShippingAddressID : 9223372036854775807
         */


        private String Address1;
        private String Address2;
        private String City;
        private String Country;
        private String Email;
        private boolean IsUpdated;
        private String MobileNo;
        private String PinCode;
        private long ShippingAddressID;

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

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public boolean isIsUpdated() {
            return IsUpdated;
        }

        public void setIsUpdated(boolean IsUpdated) {
            this.IsUpdated = IsUpdated;
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

        public long getShippingAddressID() {
            return ShippingAddressID;
        }

        public void setShippingAddressID(long ShippingAddressID) {
            this.ShippingAddressID = ShippingAddressID;
        }
    }

    public static class PlaceOrderProductDCBean {
        /**
         * ProductID : 9223372036854775807
         * Quntity : 2147483647
         * placeOrderRiffileDC : [{"ProductSpecID":9223372036854775807,"Quntity":2147483647,"RiffleName":"String content"}]
         */

        private int ProductID;
        private int Quntity;
        private List<PlaceOrderRequest.PlaceOrderProductDCBean.PlaceOrderRiffileDCBeanX> placeOrderRiffileDC;

        public int getProductID() {
            return ProductID;
        }

        public void setProductID(int ProductID) {
            this.ProductID = ProductID;
        }

        public int getQuntity() {
            return Quntity;
        }

        public void setQuntity(int Quntity) {
            this.Quntity = Quntity;
        }

        public List<PlaceOrderRequest.PlaceOrderProductDCBean.PlaceOrderRiffileDCBeanX> getPlaceOrderRiffileDC() {
            return placeOrderRiffileDC;
        }

        public void setPlaceOrderRiffileDC(List<PlaceOrderRequest.PlaceOrderProductDCBean.PlaceOrderRiffileDCBeanX> placeOrderRiffileDC) {
            this.placeOrderRiffileDC = placeOrderRiffileDC;
        }

        public static class PlaceOrderRiffileDCBeanX {
            /**
             * ProductSpecID : 9223372036854775807
             * Quntity : 2147483647
             * RiffleName : String content
             */

            private long ProductSpecID;
            private int Quntity;
            private String RiffleName;

            public long getProductSpecID() {
                return ProductSpecID;
            }

            public void setProductSpecID(long ProductSpecID) {
                this.ProductSpecID = ProductSpecID;
            }

            public int getQuntity() {
                return Quntity;
            }

            public void setQuntity(int Quntity) {
                this.Quntity = Quntity;
            }

            public String getRiffleName() {
                return RiffleName;
            }

            public void setRiffleName(String RiffleName) {
                this.RiffleName = RiffleName;
            }
        }
    }
}

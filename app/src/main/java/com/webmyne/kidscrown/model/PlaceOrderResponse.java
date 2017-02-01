package com.webmyne.kidscrown.model;

import java.util.List;

/**
 * Created by sagartahelyani on 27-01-2017.
 */

public class PlaceOrderResponse {

    /**
     * Data : {"UserID":9223372036854775807,"billingAddressDC":{"Address1":"String content","Address2":"String content","BillingAddressID":9223372036854775807,"City":"String content","Country":"String content","Email":"String content","IsUpdated":true,"MobileNo":"String content","PinCode":"String content"},"placeOrderCalculationDC":{"DeliveryCharge":1.2678967543233E7,"InvoiceDiscount":1.2678967543233E7,"InvoiceDiscountID":9223372036854775807,"PayableAmount":1.2678967543233E7,"ProductDiscount":1.2678967543233E7,"TotalAmount":1.2678967543233E7,"productCalculationDCs":[{"Discount":1.2678967543233E7,"IsSingle":true,"Price":1.2678967543233E7,"PriceID":9223372036854775807,"ProductID":9223372036854775807,"ProductName":"String content","Quntity":2147483647,"TotalPrice":1.2678967543233E7,"placeOrderRiffileDC":[{"ProductSpecID":9223372036854775807,"Quntity":2147483647,"RiffleName":"String content"}]}]},"placeOrderSuccessDetailsDC":{"BankDetails":"String content","InvoiceNumber":"String content"},"shippingAddressDC":{"Address1":"String content","Address2":"String content","City":"String content","Country":"String content","Email":"String content","IsUpdated":true,"MobileNo":"String content","PinCode":"String content","ShippingAddressID":9223372036854775807}}
     * Response : {"ResponseCode":2147483647,"ResponseMsg":"String content"}
     */

    private DataBean Data;
    private ResponseBean Response;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public static class DataBean {
        /**
         * UserID : 9223372036854775807
         * billingAddressDC : {"Address1":"String content","Address2":"String content","BillingAddressID":9223372036854775807,"City":"String content","Country":"String content","Email":"String content","IsUpdated":true,"MobileNo":"String content","PinCode":"String content"}
         * placeOrderCalculationDC : {"DeliveryCharge":1.2678967543233E7,"InvoiceDiscount":1.2678967543233E7,"InvoiceDiscountID":9223372036854775807,"PayableAmount":1.2678967543233E7,"ProductDiscount":1.2678967543233E7,"TotalAmount":1.2678967543233E7,"productCalculationDCs":[{"Discount":1.2678967543233E7,"IsSingle":true,"Price":1.2678967543233E7,"PriceID":9223372036854775807,"ProductID":9223372036854775807,"ProductName":"String content","Quntity":2147483647,"TotalPrice":1.2678967543233E7,"placeOrderRiffileDC":[{"ProductSpecID":9223372036854775807,"Quntity":2147483647,"RiffleName":"String content"}]}]}
         * placeOrderSuccessDetailsDC : {"BankDetails":"String content","InvoiceNumber":"String content"}
         * shippingAddressDC : {"Address1":"String content","Address2":"String content","City":"String content","Country":"String content","Email":"String content","IsUpdated":true,"MobileNo":"String content","PinCode":"String content","ShippingAddressID":9223372036854775807}
         */

        private long UserID;
        private BillingAddressDCBean billingAddressDC;
        private PlaceOrderCalculationDCBean placeOrderCalculationDC;
        private PlaceOrderSuccessDetailsDCBean placeOrderSuccessDetailsDC;
        private ShippingAddressDCBean shippingAddressDC;

        public long getUserID() {
            return UserID;
        }

        public void setUserID(long UserID) {
            this.UserID = UserID;
        }

        public BillingAddressDCBean getBillingAddressDC() {
            return billingAddressDC;
        }

        public void setBillingAddressDC(BillingAddressDCBean billingAddressDC) {
            this.billingAddressDC = billingAddressDC;
        }

        public PlaceOrderCalculationDCBean getPlaceOrderCalculationDC() {
            return placeOrderCalculationDC;
        }

        public void setPlaceOrderCalculationDC(PlaceOrderCalculationDCBean placeOrderCalculationDC) {
            this.placeOrderCalculationDC = placeOrderCalculationDC;
        }

        public PlaceOrderSuccessDetailsDCBean getPlaceOrderSuccessDetailsDC() {
            return placeOrderSuccessDetailsDC;
        }

        public void setPlaceOrderSuccessDetailsDC(PlaceOrderSuccessDetailsDCBean placeOrderSuccessDetailsDC) {
            this.placeOrderSuccessDetailsDC = placeOrderSuccessDetailsDC;
        }

        public ShippingAddressDCBean getShippingAddressDC() {
            return shippingAddressDC;
        }

        public void setShippingAddressDC(ShippingAddressDCBean shippingAddressDC) {
            this.shippingAddressDC = shippingAddressDC;
        }

        public static class BillingAddressDCBean {
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
            private List<ProductCalculationDCsBean> productCalculationDCs;

            public double getTotalDiscount() {
                return InvoiceDiscount + ProductDiscount;
            }

            public void setTotalDiscount(double totalDiscount) {
                TotalDiscount = totalDiscount;
            }

            private double TotalDiscount = 0;

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

            public List<ProductCalculationDCsBean> getProductCalculationDCs() {
                return productCalculationDCs;
            }

            public void setProductCalculationDCs(List<ProductCalculationDCsBean> productCalculationDCs) {
                this.productCalculationDCs = productCalculationDCs;
            }

            public static class ProductCalculationDCsBean {
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
                private List<PlaceOrderRiffileDCBean> placeOrderRiffileDC;

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

                public List<PlaceOrderRiffileDCBean> getPlaceOrderRiffileDC() {
                    return placeOrderRiffileDC;
                }

                public void setPlaceOrderRiffileDC(List<PlaceOrderRiffileDCBean> placeOrderRiffileDC) {
                    this.placeOrderRiffileDC = placeOrderRiffileDC;
                }

                public static class PlaceOrderRiffileDCBean {
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

        public static class PlaceOrderSuccessDetailsDCBean {
            /**
             * BankDetails : String content
             * InvoiceNumber : String content
             */

            private String BankDetails;
            private String InvoiceNumber;

            public String getBankDetails() {
                return BankDetails;
            }

            public void setBankDetails(String BankDetails) {
                this.BankDetails = BankDetails;
            }

            public String getInvoiceNumber() {
                return InvoiceNumber;
            }

            public void setInvoiceNumber(String InvoiceNumber) {
                this.InvoiceNumber = InvoiceNumber;
            }
        }

        public static class ShippingAddressDCBean {
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
    }

    public static class ResponseBean {
        /**
         * ResponseCode : 2147483647
         * ResponseMsg : String content
         */

        private int ResponseCode;
        private String ResponseMsg;

        public int getResponseCode() {
            return ResponseCode;
        }

        public void setResponseCode(int ResponseCode) {
            this.ResponseCode = ResponseCode;
        }

        public String getResponseMsg() {
            return ResponseMsg;
        }

        public void setResponseMsg(String ResponseMsg) {
            this.ResponseMsg = ResponseMsg;
        }
    }
}

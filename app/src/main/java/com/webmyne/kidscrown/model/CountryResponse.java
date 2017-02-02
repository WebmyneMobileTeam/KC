package com.webmyne.kidscrown.model;

import java.util.List;

/**
 * Created by sagartahelyani on 02-02-2017.
 */

public class CountryResponse {

    /**
     * Data : [{"StateID":1267,"StateName":"Andaman and Nicobar"},{"StateID":1268,"StateName":"Andhra Pradesh"},{"StateID":1269,"StateName":"Arunachal Pradesh"},{"StateID":1270,"StateName":"Assam"},{"StateID":1271,"StateName":"Bihar"},{"StateID":1272,"StateName":"Chandigarh"},{"StateID":1273,"StateName":"Chhattisgarh"},{"StateID":1274,"StateName":"Dadra and Nagar Haveli"},{"StateID":1275,"StateName":"Daman and Diu"},{"StateID":1276,"StateName":"Delhi"},{"StateID":1277,"StateName":"Goa"},{"StateID":1278,"StateName":"Gujarat"},{"StateID":1279,"StateName":"Haryana"},{"StateID":1280,"StateName":"Himachal Pradesh"},{"StateID":1281,"StateName":"Jammu and Kashmir"},{"StateID":1282,"StateName":"Jharkand"},{"StateID":1283,"StateName":"Karnataka"},{"StateID":1284,"StateName":"Kerala"},{"StateID":1285,"StateName":"Lakshadeep"},{"StateID":1286,"StateName":"Madhya Pradesh"},{"StateID":1287,"StateName":"Maharashtra"},{"StateID":1288,"StateName":"Manipur"},{"StateID":1289,"StateName":"Meghalaya"},{"StateID":1290,"StateName":"Mizoram"},{"StateID":1291,"StateName":"Nagaland"},{"StateID":1292,"StateName":"Orissa"},{"StateID":1293,"StateName":"Pondicherry"},{"StateID":1294,"StateName":"Punjab"},{"StateID":1295,"StateName":"Rajasthan"},{"StateID":1296,"StateName":"Sikkim"},{"StateID":1297,"StateName":"Tamil Nadu"},{"StateID":1298,"StateName":"Tripura"},{"StateID":1299,"StateName":"Uttar Pradesh"},{"StateID":1300,"StateName":"Uttaranchal"},{"StateID":1301,"StateName":"West Bengal"},{"StateID":1302,"StateName":"Others"}]
     * Response : {"ResponseCode":1,"ResponseMsg":"Data send successfully"}
     */

    private ResponseBean Response;
    private List<DataBean> Data;

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class ResponseBean {
        /**
         * ResponseCode : 1
         * ResponseMsg : Data send successfully
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

    public static class DataBean {
        /**
         * StateID : 1267
         * StateName : Andaman and Nicobar
         */

        private int StateID;
        private String StateName;

        public int getStateID() {
            return StateID;
        }

        public void setStateID(int StateID) {
            this.StateID = StateID;
        }

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String StateName) {
            this.StateName = StateName;
        }
    }
}

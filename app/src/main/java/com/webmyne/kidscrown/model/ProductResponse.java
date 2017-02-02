package com.webmyne.kidscrown.model;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 23-01-2017.
 */

public class ProductResponse {

    private BaseResponse Response;

    public ArrayList<Product> getData() {
        return Data;
    }

    public void setData(ArrayList<Product> data) {
        Data = data;
    }

    private ArrayList<Product> Data;

    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }

    public Banner getRootImage() {
        return RootImage;
    }

    public void setRootImage(Banner rootImage) {
        RootImage = rootImage;
    }

    public Banner RootImage;

    public class Banner {
        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            Image = image;
        }

        private String Image;
    }
}

package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 19-01-2017.
 */

public class AboutUsResponseModel {

    private String Description;
    private String HeaderImage;
    private BaseResponse Response;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getHeaderImage() {
        return HeaderImage;
    }

    public void setHeaderImage(String headerImage) {
        HeaderImage = headerImage;
    }

    public BaseResponse getResponse() {
        return Response;
    }

    public void setResponse(BaseResponse response) {
        Response = response;
    }
}

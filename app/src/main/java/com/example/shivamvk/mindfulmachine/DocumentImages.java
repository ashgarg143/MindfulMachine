package com.example.shivamvk.mindfulmachine;

public class DocumentImages {
    public String pancardfront,pancardback,aadharcardfront,aadharcardback,visitingcardfront,visitingcardback;

    public DocumentImages() {
    }

    public DocumentImages(String pancardfront, String pancardback, String aadharcardfront, String aadharcardback, String visitingcardfront, String visitingcardback) {
        this.pancardfront = pancardfront;
        this.pancardback = pancardback;
        this.aadharcardfront = aadharcardfront;
        this.aadharcardback = aadharcardback;
        this.visitingcardfront = visitingcardfront;
        this.visitingcardback = visitingcardback;
    }

    public String getPancardfront() {
        return pancardfront;
    }

    public String getPancardback() {
        return pancardback;
    }

    public String getAadharcardfront() {
        return aadharcardfront;
    }

    public String getAadharcardback() {
        return aadharcardback;
    }

    public String getVisitingcardfront() {
        return visitingcardfront;
    }

    public String getVisitingcardback() {
        return visitingcardback;
    }
}

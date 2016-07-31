package com.akramamirza.photobabble;

/**
 * Created by jeet on 18.6.16.
 */
public class MyStory {
String description;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    MyStory(String description,String url){

        this.url=url;
        this.description=description;
}
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

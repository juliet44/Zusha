package com.ck.zusha.models;



public class Post {
    String postId;
    String senderEmail;
    String imagedownloadURI;
    String description;
    String location;


    public Post(){

    }
    public Post(String postId ,String imagedownloadURI, String description,String location) {

      if (description.trim().equals("")){
          description="no description";
      }
      if(location.trim().equals("")){
          location="no location";
      }
        this.location=location;
        this.imagedownloadURI = imagedownloadURI;
        this.description = description;
        this.postId=postId;

    }



    public String getImagedownloadURI() {
        return imagedownloadURI;
    }

    public void setImagedownloadURI(String imagedownloadURI) {
        this.imagedownloadURI = imagedownloadURI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

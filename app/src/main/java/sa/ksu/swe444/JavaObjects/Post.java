package sa.ksu.swe444.JavaObjects;

import com.google.firebase.database.Exclude;

public class Post {
    private String name;
    private String imageURL;
    private String key;
    private String description;
    private int position;
    String classId;
    String postId;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Post(String name, String imageURL, String description, String classId, String postId) {
        this.name = name;
        this.imageURL = imageURL;
        this.description = description;
        this.classId = classId;
        this.postId=postId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Post() {
        //empty constructor needed
    }
    public Post (int position){
        this.position = position;
    }
    public Post(String name, String imageUrl ,String Des) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageURL = imageUrl;
        this.description = Des;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageURL;
    }
    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}

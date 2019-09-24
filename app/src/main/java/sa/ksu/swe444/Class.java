package sa.ksu.swe444;


public class Class {
    private String name;

    private int img;
    private String nameAr;

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Class() {
    }

    public Class(String name, int thumbnail) {
        this.name = name;

        this.img = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

}
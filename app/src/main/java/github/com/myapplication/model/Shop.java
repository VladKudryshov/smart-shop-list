package github.com.myapplication.model;

public class Shop {

    private String nameShop;
    private Location location;

    public Shop() {
    }

    public Shop(String nameShop, Location location) {
        this.nameShop = nameShop;
        this.location = location;
    }

    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

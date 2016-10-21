package example.mks.swiggy.network.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh on 19/10/16.
 */

public class Restaurant {
    private String name;
    private String city;
    private String area;
    private String avg_rating;
    private String cid;
    private List<String> cuisine = new ArrayList<String>();
    private String costForTwo;
    private Integer deliveryTime;
    private List<Chain> chain = new ArrayList<Chain>();
    private Boolean closed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<String> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<String> cuisine) {
        this.cuisine = cuisine;
    }

    public String getCostForTwo() {
        return costForTwo;
    }

    public void setCostForTwo(String costForTwo) {
        this.costForTwo = costForTwo;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<Chain> getChain() {
        return chain;
    }

    public void setChain(List<Chain> chain) {
        this.chain = chain;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }
}

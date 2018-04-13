package github.com.myapplication.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Владислав on 10.02.2018.
 */

public class Docket implements Serializable {

    private String uid;
    private String docketName;
    private String docket_info_uid;
    private String user;
    private int quantity;

    public Docket() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDocketName() {
        return docketName;
    }

    public void setDocketName(String docketName) {
        this.docketName = docketName;
    }

    public String getDocket_info_uid() {
        return docket_info_uid;
    }

    public void setDocket_info_uid(String docket_info_uid) {
        this.docket_info_uid = docket_info_uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

package Home.DTO;

import java.util.Date;

public class History {
    private String id;
    private String idProudct;
    private String lastUpdate;
    private int price;

    public String getIdProudct() {
        return idProudct;
    }

    public void setIdProudct(String idProudct) {
        this.idProudct = idProudct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}

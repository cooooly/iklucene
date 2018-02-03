package lucenemanager.po;

/**
 * Created by cooooly on 2018/2/3.
 */
public class Product {
    private String id;
    private String p_name;
    private Float price;
    private String description;
    /*无参*/
    public Product() {}
    /*有参*/
    public Product(String id, String p_name, Float price, String description) {
        this.id = id;
        this.p_name = p_name;
        this.price = price;
        this.description = description;
    }
    /*setter/getter方法*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package Home.Model;

import Home.DTO.Category;
import Home.DTO.CommentDTO;
import Home.DTO.History;
import Home.DTO.Page;
import Home.NameProduct;
import Home.Product;

import java.util.ArrayList;

public class Singleton {
    public ArrayList<Category> categories = null;
    public ArrayList<Product> products = null;
    public Product product;
    public ArrayList<History> histories = null;
    public ArrayList<CommentDTO> commentDTOS = null;
    public ArrayList<NameProduct> nameProducts = null;
    public static String key = null;

    public Page page;

    private static Singleton instance;
    private Singleton(){
        categories = new ArrayList<>();
        products = new ArrayList<>();
        histories = new ArrayList<>();
        product = new Product();
        page = new Page();
        commentDTOS = new ArrayList<>();
        nameProducts = new ArrayList<>();
    }
    public static Singleton getInstance() {
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }
}

package Home.Model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import Home.DTO.Category;
import Home.CommentDTO;
import Home.DTO.History;
import Home.NameProduct;
import Home.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Client {
    private Socket socket = null;
    static BufferedWriter out = null;
    static BufferedReader in = null;
    static BufferedReader stdIn = null;
    static Singleton SINGLETON = Singleton.getInstance();
    private static Security sc = new Security();
    public Client(){ }
    public boolean startClient(String address, int port) {
        boolean con = false;
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            con = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
    void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    public static void senData(String action,String param){
        String key = "action="+action+"&"+param;
        String keys = "";
        if(action.equals("keyPRP")){
            keys = key;
        }
        else{
            keys = sc.encrypt(key,SINGLETON.key);
            //keys = key;
        }
//        else{
//            keys = key;

//        }
        //System.out.println(key);
        //System.out.println(keys);
        try {
            out.write(keys);

            out.newLine();
            out.flush();
            String line = in.readLine();
            if(action.equals("keyPRP")){
                SINGLETON.key = RSA.decryt(line);
                //keys = sc.encrypt(RSA.decryt(line),SINGLETON.key);
                System.out.println("Key là"+SINGLETON.key);
            }
            else{
                checkResult(line,action);
            }
            //checkResult(line,action);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void checkResult(String line,String action){

        String data = null;
        if(action.equals("getAllCategory")){
            System.out.println(line);
            data = sc.decrypt(line,SINGLETON.key);
            SINGLETON.categories = resutlDataCategory(data);
            //SINGLETON.nameProducts = resutlNameProduct(line);
        }
        if(action.equals("search")){
            if(SINGLETON.products != null){
                SINGLETON.products.clear();
            }
            data = sc.decrypt(line,SINGLETON.key);
            SINGLETON.products.addAll(resultDataProduct(data));
            //System.out.println(SINGLETON.products);
        }
        if(action.equals("detailProduct")){
            data = sc.decrypt(line,SINGLETON.key);
            reustlDetailProduct(data);
        }
        if(action.equals("fillter_history")){
            data = sc.decrypt(line,SINGLETON.key);
            if(SINGLETON.histories != null){
                SINGLETON.histories.clear();
            }
            SINGLETON.histories.addAll(resultHistoryUpdate(data));
        }
    }
    static ArrayList<Category> resutlDataCategory(String data) {
        ArrayList<Category> arr = new ArrayList<>();
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(data);
            JSONArray json = jsons.getJSONArray("data");
            //SINGLETON.key = jsons.getString("key");
            for(int i = 0 ; i < json.length() ;i++){
                Category  category = new Category();
                JSONObject jsonObj = json.getJSONObject(i);
                category.setId(jsonObj.getString("id"));
                category.setName(jsonObj.getString("name"));
                category.setImage(jsonObj.getString("image"));
                arr.add(category);
            }
            ArrayList<NameProduct> arrName = new ArrayList<>();
            try {
                JSONArray jsonw = jsons.getJSONArray("name");
                jsonw.forEach(x -> {
                    NameProduct  name = new NameProduct();
                    name.setName(x.toString());
                    arrName.add(name);
                });
                SINGLETON.nameProducts = arrName;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
    static ArrayList<Product> resultDataProduct(String data){
        ArrayList<Product> arr = new ArrayList<>();
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(data);
            //System.out.println(data);
            SINGLETON.page.setCurrent_page(jsons.getInt("current_page"));
            SINGLETON.page.setTotal(jsons.getInt("total"));
            SINGLETON.page.setTotal_page(jsons.getInt("last_page"));
            JSONArray json = jsons.getJSONArray("data");
            for(int i = 0 ; i < json.length() ;i++){
                Product  product = new Product();
                JSONObject jsonObj = json.getJSONObject(i);
                product.setId(jsonObj.getString("id"));
                product.setName(jsonObj.getString("name"));
                product.setImage(jsonObj.getString("image"));
                product.setPrice(jsonObj.getInt("price"));
                product.setId_item(jsonObj.getString("id_item"));
                arr.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
    static void reustlDetailProduct(String data){
        JSONObject jsons = new JSONObject(data);
        SINGLETON.product.setName(jsons.getJSONObject("data").getString("name"));
        SINGLETON.product.setImage(jsons.getJSONObject("data").getString("image"));
        SINGLETON.product.setPrice(jsons.getJSONObject("data").getInt("price"));
        SINGLETON.product.setId_item(jsons.getJSONObject("data").getString("id_item"));
        SINGLETON.product.setMax_price(jsons.getInt("max_price"));
        SINGLETON.product.setMin_price(jsons.getInt("min_price"));
        SINGLETON.product.setRating(jsons.getJSONObject("data").getFloat("rating_average"));
        SINGLETON.product.setReview_count(jsons.getJSONObject("data").getInt("review_count"));
        JSONArray jsonComment = jsons.getJSONObject("data").getJSONArray("commentDTOS");
        JSONArray jsonHistory = jsons.getJSONObject("data").getJSONArray("historyDTOS");
        if(SINGLETON.histories != null){
            SINGLETON.histories.clear();
        }
        if(SINGLETON.commentDTOS !=null){
            SINGLETON.commentDTOS.clear();
        }
        SINGLETON.histories.addAll(resultHistory(jsonHistory));
        SINGLETON.commentDTOS.addAll(resultComment(jsonComment));
    }
    static ArrayList<History> resultHistory(JSONArray json){
        ArrayList<History> arr = new ArrayList<>();
        try {
            for(int i = 0 ; i < json.length() ;i++){
                History  history = new History();
                //DateFormat formatS = new SimpleDateFormat("MM-dd-yyyy");
                JSONObject jsonObj = json.getJSONObject(i);
                //Date day = formatS.parse();
                history.setLastUpdate(jsonObj.getString("last_update"));
                history.setPrice(jsonObj.getInt("current_price"));
                arr.add(history);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
    static ArrayList<History> resultHistoryUpdate(String line){
        ArrayList<History> arr = new ArrayList<>();
        JSONObject jsonObjs = new JSONObject(line);
        SINGLETON.product.setMax_price(jsonObjs.getInt("max_price"));
        SINGLETON.product.setMin_price(jsonObjs.getInt("min_price"));
        JSONArray json = jsonObjs.getJSONArray("data");
        try {
            for(int i = 0 ; i < json.length() ;i++){
                History  history = new History();
                //DateFormat formatS = new SimpleDateFormat("MM-dd-yyyy");
                JSONObject jsonObj = json.getJSONObject(i);
                //Date day = formatS.parse();
                history.setLastUpdate(jsonObj.getString("last_update"));
                history.setPrice(jsonObj.getInt("current_price"));
                arr.add(history);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
    static ArrayList<CommentDTO> resultComment(JSONArray arrCommnent){
        ArrayList<CommentDTO> arr = new ArrayList<>();
        try {
            for(int i = 0 ; i < arrCommnent.length() ;i++){
                JSONObject jsonObj = arrCommnent.getJSONObject(i);
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setId_product(jsonObj.getString("id_product"));
                commentDTO.setFull_name(jsonObj.getString("full_name"));
                commentDTO.setPurchased_at(jsonObj.getString("purchased_at"));
                commentDTO.setRating(jsonObj.getInt("rating"));
                commentDTO.setTitle(jsonObj.getString("title"));
                commentDTO.setContent(jsonObj.getString("content"));
                commentDTO.setThank_count(jsonObj.getInt("thank_count"));
                arr.add(commentDTO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
}

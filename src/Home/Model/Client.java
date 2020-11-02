package Home.Model;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Home.DTO.Category;
import Home.DTO.CommentDTO;
import Home.DTO.History;
import Home.DTO.Page;
import Home.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Client {
    private static Socket socket = null;
    static BufferedWriter out = null;
    static BufferedReader in = null;
    static BufferedReader stdIn = null;
    static Singleton SINGLETON = Singleton.getInstance();
    public Client(String address,int port) throws IOException {
        startClient(address,port);
    }
    void startClient(String address, int port) throws IOException {
        socket = new Socket(address, port);
        System.out.println("Connected");
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }
    void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    public static void senData(String action,String param){
        String key = "action="+action+"&"+param;
        System.out.println(key);
        try {
            out.write(key);
            out.newLine();
            out.flush();
            String line = in.readLine();
            checkResult(line,action);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void checkResult(String line,String action){
        if(action.equals("getAllCategory")){
            SINGLETON.categories = resutlDataCategory(line);
        }
        if(action.equals("search")){
            if(SINGLETON.products != null){
                SINGLETON.products.clear();
            }
            SINGLETON.products.addAll(resultDataProduct(line));
            //System.out.println(SINGLETON.products);
        }
        if(action.equals("detailProduct")){
            reustlDetailProduct(line);
        }
    }
    static ArrayList<Category> resutlDataCategory(String data) {
        ArrayList<Category> arr = new ArrayList<>();
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(data);
            JSONArray json = jsons.getJSONArray("data");
            for(int i = 0 ; i < json.length() ;i++){
                Category  category = new Category();
                JSONObject jsonObj = json.getJSONObject(i);
                category.setId(jsonObj.getString("id"));
                category.setName(jsonObj.getString("name"));
                category.setImage(jsonObj.getString("image"));
                arr.add(category);
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
    static ArrayList<CommentDTO> resultComment(JSONArray arrCommnent){
        ArrayList<CommentDTO> arr = new ArrayList<>();
        try {
            for(int i = 0 ; i < arrCommnent.length() ;i++){
                JSONObject jsonObj = arrCommnent.getJSONObject(i);
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setId_product(jsonObj.getString("id_product"));
                commentDTO.setFull_name(jsonObj.getString("full_name"));
                commentDTO.setPurchased_at(jsonObj.getInt("purchased_at"));
                commentDTO.setRating(jsonObj.getInt("rating"));
                commentDTO.setTitle(jsonObj.getString("title"));
                commentDTO.setContent(jsonObj.getString("content"));
                arr.add(commentDTO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }
}

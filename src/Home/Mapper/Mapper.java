package Home.Mapper;

import org.json.JSONArray;

import java.util.ArrayList;

public interface Mapper<T> {
     T MapRow(JSONArray arr, ArrayList<T> arrayList);
}

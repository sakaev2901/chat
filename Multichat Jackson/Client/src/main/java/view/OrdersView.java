package view;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class OrdersView {
    public void openOrders(ArrayList<LinkedHashMap<String, Object>> data) {
        System.out.println("=== Orders ===");
        int i = 0;
        for (Object item:
             data) {
            i++;
            LinkedHashMap<String, Object> mapItem = (LinkedHashMap<String, Object>) item;
            LinkedHashMap<String, String> info = (LinkedHashMap<String, String>)mapItem.get("info");
            ArrayList<LinkedHashMap<String, String>> products = (ArrayList<LinkedHashMap<String, String>>)mapItem.get("products");
            System.out.println(i + ". " + info.get("id") + " " + info.get("time"));
            for (LinkedHashMap<String, String> product:
                 products) {
                System.out.println(" - " + String.valueOf(product.get("id")) + " " + product.get("name") + " " + String.valueOf(product.get("price")));
            }
        }
    }
}

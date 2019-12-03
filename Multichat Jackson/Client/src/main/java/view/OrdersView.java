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
            ArrayList<LinkedHashMap<String, String>> products = (ArrayList<LinkedHashMap<String, String>>)mapItem.get("products");
            System.out.println(i + ". " + mapItem.get("id") + " " + mapItem.get("date"));
            for (LinkedHashMap<String, String> product:
                 products) {
                System.out.println(" - " + product.get("id") + " " + product.get("name") + " " + product.get("price"));
            }
        }
    }
}

package view;

import net.Session;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ShopView {
    public void doShop(ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> products) {
        Session session = Session.getInstance();
        for (LinkedHashMap<String, LinkedHashMap<String, String>> item:
             products) {
            System.out.println(item.get("id")+ " " + item.get("name") + " " + item.get("price"));
        }

    }
}

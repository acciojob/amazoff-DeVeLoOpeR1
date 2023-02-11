package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String,Order> orderDb;
    private HashMap<String,DeliveryPartner> deliveryPartnerDb;

    private HashMap<String, List<String>> partnerPairDb;

    private HashMap<String,String> orderPairDb;

    public OrderRepository() {
        orderDb = new HashMap<String,Order>();
        deliveryPartnerDb = new HashMap<String,DeliveryPartner>();
        partnerPairDb = new HashMap<String,List<String>>();
        orderPairDb =  new HashMap<String, String>();

    }

    public void addOrder(Order order) {
      orderDb.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner dp = new DeliveryPartner(partnerId);
        deliveryPartnerDb.put(partnerId,dp);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderPairDb.put(orderId,partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return deliveryPartnerDb.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {

        List<String> orders = partnerPairDb.getOrDefault(partnerId,new ArrayList<>());
        for(String you : orderPairDb.keySet()){
            if(orderPairDb.get(you).equals(partnerId)){
                orders.add(you);
            }
        }

        return orders;
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<String>();
        for(String i : orderDb.keySet()){
            orders.add(i);
        }
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
       return (orderDb.size()-orderPairDb.size());
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        if(partnerPairDb.containsKey(partnerId)){
            return 0;
        }
        String[] arr = time.split(":");

        int HH = Integer.parseInt(arr[0]);
        int MM = Integer.parseInt(arr[1]);

        int givenTime = (HH*60) + MM;
        int count = 0;
        List<String> l1 = partnerPairDb.get(partnerId);
        for(String h : l1){
            int temp = orderDb.get(h).getDeliveryTime();
            if(temp > givenTime)
                count++;

        }
        return count;
    }

    public void deleteOrderById(String orderId) {
        orderPairDb.remove(orderId);
        orderDb.remove(orderId);
    }

    public void deletePartnerById(String partnerId) {
    partnerPairDb.get(partnerId);
    deliveryPartnerDb.get(partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orders = partnerPairDb.get(partnerId);
        int time = 0;
        int max = 0;
        for(String you : orderPairDb.keySet()){
            time = orderDb.get(you).getDeliveryTime();
            if(time>max)
                max = time;
        }

        return (String.valueOf(max));
    }
}

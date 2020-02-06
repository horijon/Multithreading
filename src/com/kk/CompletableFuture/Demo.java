package com.kk.CompletableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        //        ResponseDTO responseDTO = new ResponseDTO();

        List<Runnable> tasks = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("info", "info");
        Runnable infoTask = () -> data.put("info", "Hello");
        Runnable sleepTask = () -> data.put("sleep", sleepForSomeTime());
        Runnable firstNameTask = () -> data.put("firstName", "kshitij");
        Runnable lastNameTask = () -> data.put("lastName", "khanal");
        tasks.add(infoTask);
        tasks.add(firstNameTask);
        tasks.add(sleepTask);
        tasks.add(lastNameTask);
        ThreadUtils.executeInParallel(tasks);
        data.forEach((datumKey, datumValue) -> {
            System.out.println(datumKey);
            System.out.println(datumValue);
        });
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------------------");
        data.forEach((datumKey, datumValue) -> {
            System.out.println(datumKey);
            System.out.println(datumValue);
        });
        //        responseDTO.setData(data);
        //        return responseDTO;

    }

    private static String sleepForSomeTime() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Slept for 5 seconds";
    }

}

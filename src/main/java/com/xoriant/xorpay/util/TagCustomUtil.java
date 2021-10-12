package com.xoriant.xorpay.util;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TagCustomUtil {
  
  public boolean checkCustomization(Map<String,List<String>> map,List<String> value) {
    for(List<String> list:map.values()) {
      if(list.containsAll(value)) 
        return true;
    }
    return false;
  }
  
  public boolean tagLevelCustomization(Map<String,List<String>> map,String value) {
    for(List<String> list:map.values()) {
      if(list.contains(value)) 
        return true;
    }
    return false;
  }
  
  

}

package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import java.util.*;

public class RuntimeDictValue extends RuntimeValue {

  HashMap<String, RuntimeValue> dictMap;

  public RuntimeDictValue() {
    dictMap = new HashMap<>();
  }

  public void addElement(String name, RuntimeValue value) {
    dictMap.put(name, value);
  }

  @Override
  public String showInfo() {
    String toReturn = "{";

    for(String name : dictMap.keySet()) {
      String key = name.toString();
      String value = dictMap.get(name).showInfo();

      toReturn += key + " : " + value + ", ";
    }
    //To remove last ,
    toReturn = toReturn.substring(0, toReturn.length()-2);
    return toReturn += "}";
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      String subscription = v.getStringValue("Dict subscription", where);
      RuntimeValue value = dictMap.get(subscription);
      if(value != null) {
        return value;
      }
      runtimeError("Can't find value with string " + subscription, where);
      return null;
    }
    runtimeError("Unexpected value for subscription", where);
    return null;
  }

  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
    if(inx instanceof RuntimeStringValue) {
      if(dictMap.containsKey(inx.toString())) {
        dictMap.replace(inx.toString(), val);
      } else {
        runtimeError("Key " + inx.toString() + "was not found", where);
      }
    } else {
      runtimeError("Expected String-value, but found " + inx.typeName(), where);
    }
  }

  @Override
  protected String typeName() {
    return "dict value";
  }

  @Override
  public String toString() {
    return "";
  }
}

package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.*;
import java.util.*;

public class RuntimeListValue extends RuntimeValue {

  public ArrayList<RuntimeValue> listValues;

  public RuntimeListValue() {
    listValues = new ArrayList<>();
  }

  public void addValue(RuntimeValue v) {
    listValues.add(v);
  }

  public ArrayList<RuntimeValue> getValues() {
    return listValues;
  }

  @Override
  public String showInfo() {
    String toReturn = "[";
    RuntimeValue v = null;

    for(int i = 0; i < listValues.size(); i++) {
      v = listValues.get(i);

      if(v instanceof RuntimeListValue || v instanceof RuntimeStringValue) {
        toReturn += v.showInfo();
        if(i != listValues.size()-1) toReturn += ", ";
      }
      else {
        if(i == listValues.size()-1) toReturn += (v.toString());
        else toReturn += (v.toString() + ", ");
      }
    }

    return toReturn += "]";
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
        int timesToAdd = (int)v.getIntValue("Multiply list", where);
        ArrayList<RuntimeValue> copy = listValues;
        ArrayList<RuntimeValue> empty = new ArrayList<>();

        for(int i = timesToAdd; i > 0; i--) {
          empty.addAll(copy);
        }

        listValues = empty;
      } else {
      Main.panic("Unexpected value");
    }

    return this;
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      int i = (int)v.getIntValue("list subscription", where);
      if(i >= listValues.size()) {
        runtimeError("IndexOutOfBoundsException", where);
      } else {
        v = listValues.get(i);
      }
    } else {
      Main.panic("Subscription must contain int");
    }
    return v;
  }

  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where){
    int index = (int) inx.getIntValue("evalAssignElem", where);
    listValues.set(index, val);
  }

  @Override
  protected String typeName() {
    return "list value";
  }

  @Override
  public String toString(){
      String toRet = "[";
      int nPrinted = 0;

      for(RuntimeValue v : listValues){
        if(nPrinted > 0) toRet += ", ";
        toRet += v.toString(); nPrinted++;
      }

      return toRet+="]";
    }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(listValues.isEmpty()) return false;
    else return true;
  }

  @Override
  public RuntimeValue evalLen(AspSyntax where) {
    RuntimeValue v = new RuntimeIntValue(listValues.size());
    return v;
  }
}

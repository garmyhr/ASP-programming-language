package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
  String stringValue;

  public RuntimeStringValue(String s) {
    stringValue = s;
  }

  @Override
  protected String typeName() {
    return "string value";
  }

  @Override
  public String toString() {
    return stringValue;
  }

  @Override
  public String showInfo() {
    if(stringValue.indexOf('\'') >= 0)
      return '"' + stringValue + '"';
    else
      return "'" + stringValue + "'";
  }

  //operations
  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    this.stringValue += v.toString();
    return this;
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    int i = (int)v.getIntValue("* operand", where);
    String temp = "";
    while(i > 0) {
      temp += stringValue;
      i--;
    }

    this.stringValue = temp;
    return this;
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
    if(v.getBoolValue("> operand", where) && getBoolValue("> operand", where)) return new RuntimeBoolValue(true);
    else return new RuntimeBoolValue(false);
  }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      if(v.toString().equals(stringValue)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    return new RuntimeBoolValue(false);
  }

  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      if(!v.toString().equals(stringValue)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    return new RuntimeBoolValue(false);
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      int i = (int)v.getIntValue("string subscription", where);
      if(i >= stringValue.length()) {
        runtimeError("IndexOutOfBoundsException", where);
        return null;
      } else {
        String newString = "";
        char value = stringValue.charAt(i);
        newString += value;
        RuntimeValue toReturn = new RuntimeStringValue(newString);
        return toReturn;
      }
    } else {
      runtimeError("Unexpected value, must be integer", where);
      return null;
    }
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(stringValue == "") return false;
    else return true;
  }

  @Override
  public String getStringValue(String what, AspSyntax where) {
    return stringValue;
  }

  @Override
  public long getIntValue(String what, AspSyntax where) {
	try {
	   long toReturn = Long.parseLong(stringValue);
	   return toReturn;
	 } catch(NumberFormatException e) {
		 runtimeError("String doesn't contain a legal int value", where);
		 return 0;
	 }
  }
  
  @Override
  public double getFloatValue(String what, AspSyntax where) {
    try{
      double toReturn = Double.parseDouble(stringValue);
      return toReturn;
    } catch(NumberFormatException e) {
      runtimeError("String doesn't contain a legal float value", where);
      return 0.0;
    }
  }
}

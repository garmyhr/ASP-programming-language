package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {
  double floatValue;

  public RuntimeFloatValue(double v) {
    floatValue = v;
  }

  @Override
  protected String typeName() {
    return "float value";
  }

  @Override
  public String toString() {
    return Double.toString(floatValue);
  }


  // Operations
  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue(floatValue + v.getFloatValue(" + operand", where));
    } else if (v instanceof RuntimeIntValue) {
      return new RuntimeIntValue((long)floatValue + v.getIntValue(" + operand", where));
    }

    runtimeError("Type error for +.", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue(floatValue - v.getFloatValue(" + operand", where));
    } else if (v instanceof RuntimeIntValue) {
      return new RuntimeIntValue((long)floatValue - v.getIntValue(" + operand", where));
    }

    runtimeError("Type error for +.", where);
    return null;
  }

  @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
      if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue)
        return new RuntimeFloatValue(floatValue / v.getFloatValue("/ operand", where));
      runtimeError("Type error for '/'.", where);
      return null;
    }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue(floatValue * v.getFloatValue(" + operand", where));
    } else if (v instanceof RuntimeIntValue) {
      return new RuntimeIntValue((long)floatValue * v.getIntValue(" + operand", where));
    }

    runtimeError("Type error for +.", where);
    return null;
  }

  @Override
  public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where)*Math.floor(floatValue/v.getFloatValue("% operand", where)));
    } else if (v instanceof RuntimeIntValue) {
      return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where)*Math.floor(floatValue/v.getFloatValue("% operand", where)));
    }

    runtimeError("Type error for %.", where);
    return null;
  }

  @Override
  public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue)
      return new RuntimeFloatValue(Math.floor(floatValue / v.getIntValue("// operand", where)));
    runtimeError("Type error for //.", where);
    return null;
  }


  //comparisons
  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if((long)floatValue == v.getIntValue("== operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if(floatValue == v.getFloatValue("== operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for ==.", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if((long)floatValue > v.getIntValue("> operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if(floatValue > v.getFloatValue("> operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for >.", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if((long)floatValue >= v.getIntValue(">= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if(floatValue >= v.getFloatValue(">= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for >=.", where);
    return null;
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if((long)floatValue < v.getIntValue("< operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if(floatValue < v.getFloatValue("< operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for <.", where);
    return null;
  }

  @Override
  public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if((long)floatValue <= v.getIntValue("<= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if(floatValue <= v.getFloatValue("<= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for <=.", where);
    return null;
  }

  @Override
  public RuntimeValue evalNegate(AspSyntax where) {
    this.floatValue = floatValue * -1;
    return this;
  }

  @Override
  public long getIntValue(String what, AspSyntax where) {
    return (long)floatValue;
  }

  @Override
  public double getFloatValue(String what, AspSyntax where) {
    return floatValue;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(floatValue == 0.0) return false;
    else return true;
  }

  @Override
  public String getStringValue(String what, AspSyntax where) {
    return Double.toString(floatValue);
  }
}

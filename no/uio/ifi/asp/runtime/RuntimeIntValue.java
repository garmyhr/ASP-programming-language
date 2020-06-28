package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import java.lang.Math;

public class RuntimeIntValue extends RuntimeValue {
  long intValue;

  public RuntimeIntValue(long v) {
    intValue = v;
  }

  @Override
  protected String typeName() {
    return "int value";
  }

  @Override
  public String toString() {
    return Long.toString(intValue);
  }

  //operations
  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      return new RuntimeIntValue(intValue + v.getIntValue(" + operand", where));
    } else if (v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue((double)intValue + v.getFloatValue(" + operand", where));
    }

    runtimeError("Type error for +.", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      return new RuntimeIntValue(intValue - v.getIntValue("- operand", where));
    } else if (v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue((double)intValue - v.getFloatValue("- operand", where));
    }

    runtimeError("Type error for -.", where);
    return null;
  }

  @Override
  public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
		return new RuntimeIntValue(intValue / v.getIntValue("/ operand", where));
    } else if(v instanceof RuntimeFloatValue) {
		return new RuntimeFloatValue((double)intValue / v.getFloatValue("/ operand", where));
    }
	runtimeError("Type error for /.", where);
	return null;
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      return new RuntimeIntValue(intValue * v.getIntValue("* operand", where));
    } else if(v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue((double)intValue * v.getFloatValue("* operand", where));
    }
    runtimeError("Type error for *.", where);
    return null;
  }

  @Override
  public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      return new RuntimeIntValue(Math.floorMod(intValue, v.getIntValue("% operand", where)));
    } else if(v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue((double)intValue - v.getFloatValue("% operand", where)
                                    * Math.floor((double)intValue/v.getFloatValue("% operand", where)));
    }
    runtimeError("Type error for %.", where);
    return null;
  }

  @Override
  public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      return new RuntimeIntValue(Math.floorDiv(intValue, v.getIntValue("// operand", where)));
    } else if(v instanceof RuntimeFloatValue) {
      return new RuntimeFloatValue(Math.floor((double)intValue / v.getFloatValue("// operand", where)));
    }
    runtimeError("Type error for //.", where);
    return null;
  }

  //Comparisons
  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
		if(intValue == v.getIntValue("== operand", where)) return new RuntimeBoolValue(true);
        else return new RuntimeBoolValue(false);
    }
	if(v instanceof RuntimeFloatValue) {
		if((double)intValue == v.getFloatValue("== operand", where)) return new RuntimeBoolValue(true);
		else return new RuntimeBoolValue(false);
    }
	runtimeError("Type error for ==.", where);
	return null;
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if(intValue > v.getIntValue("> operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if((double)intValue > v.getFloatValue("> operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for >.", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if(intValue >= v.getIntValue(">= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if((double)intValue >= v.getFloatValue(">= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for >=.", where);
    return null;
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if(intValue < v.getIntValue("< operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if((double)intValue < v.getFloatValue("< operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for <.", where);
    return null;
  }

  @Override
  public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if(intValue <= v.getIntValue("<= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if((double)intValue <= v.getFloatValue("<= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for <=.", where);
    return null;
  }

  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if(intValue != v.getIntValue("!= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    if(v instanceof RuntimeFloatValue) {
      if((double)intValue != v.getFloatValue("!= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }

    runtimeError("Type error for !=.", where);
    return null;
  }

  @Override
  public RuntimeValue evalNegate(AspSyntax where) {
    this.intValue = intValue * -1;
    return this;
  }

  @Override
  public RuntimeValue evalPositive(AspSyntax where) {
    this.intValue = Math.abs(intValue);
    return this;
  }

  @Override
  public long getIntValue(String what, AspSyntax where) {
    return intValue;
  }

  @Override
  public double getFloatValue(String what, AspSyntax where) {
    return (double)intValue;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(intValue == 0) return false;
    else return true;
  }

  @Override
  public String getStringValue(String what, AspSyntax where) {
	  return Long.toString(intValue);
  }
}

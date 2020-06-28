package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.*;
import java.util.*;

public class RuntimeFunc extends RuntimeValue {
  public AspFuncDef def;
  public RuntimeScope defScope;
  public int nrOfFormalParams;
  String name;

  public RuntimeFunc(String name) {
    this.name = name;
  }

  public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
    RuntimeScope newScope = new RuntimeScope(defScope);

    if(actualParams.size() != nrOfFormalParams) {
      runtimeError("Wrong number of paramaters.", def);
      return null;
    } else {
      for(int i = 0; i < actualParams.size(); i++) {
        String formalParam = def.params.get(i).name;
        RuntimeValue actualParam = actualParams.get(i);

        newScope.assign(formalParam, actualParam);
      }

      try {
        def.body.eval(newScope);
      } catch(RuntimeReturnValue rrv) {
        return rrv.value;
      }
      return new RuntimeNoneValue();
    }
  }

  @Override
  protected String typeName() {
    return "func value";
  }

  @Override
  public String toString() {
    return name;
  }
}

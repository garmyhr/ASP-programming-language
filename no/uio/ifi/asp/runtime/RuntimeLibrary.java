package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {

    //len
    assign("len", new RuntimeFunc("len") {
      @Override
      public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
                                      ,AspSyntax where) {
      checkNumParams(actualParams, 1, "len", where);
      return actualParams.get(0).evalLen(where);
    }});

    //print
    assign("print", new RuntimeFunc("print") {
      @Override
      public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
                                      ,AspSyntax where) {
      for(int i = 0; i < actualParams.size(); i++) {
        if(i > 0) System.out.print(" ");
          System.out.print(actualParams.get(i).toString());
      }
      System.out.println();
      return new RuntimeNoneValue();
    }});

		//int
		assign("int", new RuntimeFunc("int") {
			@Override
			public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
											,AspSyntax where) {
			checkNumParams(actualParams, 1, "int()", where);
			RuntimeValue v = actualParams.get(0);
			if(v instanceof RuntimeStringValue) {
				return new RuntimeIntValue(v.getIntValue("Function call", where));
			} else if(v instanceof RuntimeFloatValue) {
				return new RuntimeIntValue(v.getIntValue("Function call", where));
			} else if(v instanceof RuntimeIntValue) {
				return v;
			} else {
				runtimeError("Illegal paramater type for int()", where);
				return null;
			}
		}});

		//float
		assign("float", new RuntimeFunc("float") {
			@Override
			public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
											,AspSyntax where) {
			checkNumParams(actualParams, 1, "float()", where);
			RuntimeValue v = actualParams.get(0);

			if(v instanceof RuntimeStringValue) {
				return new RuntimeFloatValue(v.getFloatValue("Function call", where));
			} else if(v instanceof RuntimeFloatValue) {
				return v;
			} else if(v instanceof RuntimeIntValue) {
				return new RuntimeFloatValue(v.getFloatValue("Function call", where));
			} else {
				runtimeError("Illegal paramater type for float()", where);
				return null;
			}
		}});

		//input
		assign("input", new RuntimeFunc("input") {
			@Override
			public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
											,AspSyntax where) {
			checkNumParams(actualParams, 1, "input()", where);
			RuntimeValue v = actualParams.get(0);
			if(v instanceof RuntimeStringValue) {
        RuntimeStringValue string = (RuntimeStringValue) v;
				System.out.print(string.toString());
				return new RuntimeStringValue(keyboard.next());
			} else {
				runtimeError("Illegal paramater type for input()", where);
				return null;
			}
		}});

    //range
    assign("range", new RuntimeFunc("range"){
    @Override
    public RuntimeValue evalFuncCall(
    ArrayList<RuntimeValue> actualParams,
    AspSyntax where){

    checkNumParams(actualParams, 2, "range", where);
    RuntimeListValue toRet = new RuntimeListValue();
    RuntimeValue p1 = actualParams.get(0);
    RuntimeValue p2 = actualParams.get(1);
    if(p1 instanceof RuntimeIntValue && p2 instanceof RuntimeIntValue){
        int start = (int) p1.getIntValue("range", where);
        int end = (int) p2.getIntValue("range", where);

        if (start < end){
            for (int i=start; i<end; i++){
                toRet.addValue(new RuntimeIntValue(i));
            }
            return toRet;
        } else if (end < start){
             for (int i=end; i>=start; i--){
                 toRet.addValue(new RuntimeIntValue(i));
              }
              return toRet;
        } else {
              toRet.addValue(new RuntimeIntValue(start));
              return toRet;
        }
      } else {
          RuntimeValue.runtimeError("Wrong type of parameters for range().", where);
          return null;
      }
    }});

		//str
		assign("str", new RuntimeFunc("str") {
			@Override
			public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
											,AspSyntax where) {
			checkNumParams(actualParams, 1, "str()", where);
			RuntimeValue v = actualParams.get(0);
			return new RuntimeStringValue(v.toString());
		}});
    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
		                            int nCorrect, String id, AspSyntax where) {
	  if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}

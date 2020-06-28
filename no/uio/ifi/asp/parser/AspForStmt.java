package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspStmt {
  AspName name;
  AspExpr test;
  AspSuite body;

  AspForStmt(int n) {
    super(n);
  }

  static AspForStmt parse(Scanner s) {
    enterParser("for stmt");

    AspForStmt afs = new AspForStmt(s.curLineNum());
    skip(s, forToken);
    afs.name = AspName.parse(s);
    skip(s, inToken);
    afs.test = AspExpr.parse(s);
    skip(s, colonToken);
    afs.body = AspSuite.parse(s);

    leaveParser("for stmt");
    return afs;
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeValue t = test.eval(curScope);
     if (t instanceof RuntimeListValue){
       RuntimeListValue list = (RuntimeListValue) t;
       ArrayList<RuntimeValue> loop = list.getValues();

       for(int i=0; i<loop.size(); i++){
         trace("for #" + (i+1) + ": " + name.name + " = " + loop.get(i));
         curScope.assign(name.name, loop.get(i));
         body.eval(curScope);
       }

     }else{
       t.runtimeError(t.showInfo() + "is not iterable", this);
     }
     return null;
  }
  @Override
  void prettyPrint() {
    Main.log.prettyWrite("for ");
    name.prettyPrint();
    Main.log.prettyWrite(" in ");
    test.prettyPrint();
    Main.log.prettyWrite(":");
    body.prettyPrint();
  }
}

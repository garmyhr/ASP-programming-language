package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspStmt {

  AspExpr assignedValue;
  AspName name;
  ArrayList<AspSubscription> subscriptions = new ArrayList<>();

  AspAssignment(int n) {
    super(n);
  }

  static AspAssignment parse(Scanner s) {
    enterParser("assignment");
    AspAssignment aa = new AspAssignment(s.curLineNum());
    aa.name = AspName.parse(s);
    if(s.curToken().kind != equalToken) {
      while(true) {
        aa.subscriptions.add(AspSubscription.parse(s));
        if(s.curToken().kind == equalToken) break;
      }
    }

    test(s, equalToken); skip(s, equalToken);
    aa.assignedValue = AspExpr.parse(s);
    skip(s, newLineToken);

    leaveParser("assignment");
    return aa;
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    String varName = name.name;
    RuntimeValue to = assignedValue.eval(curScope);
    if (subscriptions.isEmpty()){
      trace(varName + " = " + to);
      curScope.assign(varName, to);
    }
    else if (subscriptions.size() == 1){

      RuntimeValue v = curScope.find(name.name, this);
      RuntimeValue sub = subscriptions.get(0).eval(curScope);

      trace(varName + "[" + sub + "]" + " = " + to.showInfo());
      v.evalAssignElem(sub, to, this);

    }else if (subscriptions.size() > 1){
      RuntimeValue v = curScope.find(name.name, this);
      for (int i=0; i<subscriptions.size()-1; i++){
        RuntimeValue sub = subscriptions.get(i).eval(curScope);
        v = v.evalSubscription(sub, this);
      }
      RuntimeValue lastSub = subscriptions.get(subscriptions.size()-1).eval(curScope);
      trace("multilength subscription: " + varName + " to " + to);
      v.evalAssignElem(lastSub, to, this);

    }else RuntimeValue.runtimeError("Subscription error", this);
    return null;
  }

  @Override
  void prettyPrint() {
    name.prettyPrint();

    for(AspSubscription as : subscriptions) {
      as.prettyPrint();
    }
    Main.log.prettyWrite(" = ");
    assignedValue.prettyPrint();
    Main.log.prettyWriteLn();
  }
}

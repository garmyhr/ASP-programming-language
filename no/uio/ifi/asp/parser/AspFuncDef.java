package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspStmt {
  AspExpr test;
  AspName name;
  public AspSuite body;
  public ArrayList<AspName> params = new ArrayList<>();

  AspFuncDef(int n) {
    super(n);
  }

  static AspFuncDef parse(Scanner s) {
    enterParser("func def");

    AspFuncDef afd = new AspFuncDef(s.curLineNum());
    skip(s, defToken); afd.name = AspName.parse(s);
    skip(s, leftParToken);

    while(s.curToken().kind != rightParToken) {
      afd.params.add(AspName.parse(s));
      if(s.curToken().kind == commaToken) skip(s, commaToken);
      else break;
    }
    skip(s, rightParToken);
    test(s, colonToken); skip(s, colonToken);
    afd.body = AspSuite.parse(s);

    leaveParser("func def");
    return afd;
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	
	   trace("def " + name.name);
     RuntimeFunc function = new RuntimeFunc(name.name);
	   function.def = this;
	   function.defScope = curScope;
	   function.nrOfFormalParams = params.size();
     curScope.assign(name.name, function);

    return null;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyWrite("def ");
    name.prettyPrint();
    Main.log.prettyWrite("(");

    for(AspName an : params) {
      an.prettyPrint();
    }

    Main.log.prettyWrite(")");
    Main.log.prettyWrite(":");
    body.prettyPrint();
  }
}

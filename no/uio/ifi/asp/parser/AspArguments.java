package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspPrimarySuffix {

  ArrayList<AspExpr> expressions = new ArrayList<>();

  AspArguments(int n) {
    super(n);
  }

  static AspArguments parse(Scanner s) {
    enterParser("arguments");

    AspArguments aa = new AspArguments(s.curLineNum());
    skip(s, leftParToken);

    while(s.curToken().kind != rightParToken) {
      aa.expressions.add(AspExpr.parse(s));
      if(s.curToken().kind == commaToken) skip(s, commaToken);
      else break;
    }
    skip(s, rightParToken);

    leaveParser("arguments");
    return aa;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeListValue v = new RuntimeListValue();

    for(int i = 0; i < expressions.size(); i++) {
      v.addValue(expressions.get(i).eval(curScope));
    }
    return v;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyWrite("(");

    for(int i = 0; i < expressions.size();i++) {
      expressions.get(i).prettyPrint();
      if(i == expressions.size()-1) break;
      else Main.log.prettyWrite(", ");
    }
    Main.log.prettyWrite(")");
  }
}

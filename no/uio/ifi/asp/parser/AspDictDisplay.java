package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspDictDisplay extends AspAtom {

  ArrayList<AspStringLiteral> stringLiterals = new ArrayList<>();
  ArrayList<AspExpr> expressions = new ArrayList<>();

  AspDictDisplay(int n) {
    super(n);
  }

  static AspDictDisplay parse(Scanner s) {
    enterParser("dict display");

    AspDictDisplay add = new AspDictDisplay(s.curLineNum());


    skip(s, leftBraceToken);
    while(s.curToken().kind != rightBraceToken) {
      add.stringLiterals.add(AspStringLiteral.parse(s));
      test(s, colonToken); skip(s, colonToken);
      add.expressions.add(AspExpr.parse(s));
      if(s.curToken().kind == commaToken) skip(s, commaToken);
    }
    skip(s, rightBraceToken);

    leaveParser("dict display");
    return add;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

    RuntimeDictValue v = new RuntimeDictValue();

    if(stringLiterals.isEmpty() && expressions.isEmpty()) return v;
    else {
      for(int i = 0; i < stringLiterals.size(); i++) {
        String val = stringLiterals.get(i).value;
        RuntimeValue expr = expressions.get(i).eval(curScope);
        v.addElement(val, expr);
      }
    }

    return v;
  }

  @Override
  void prettyPrint() {

    Main.log.prettyWrite("{");
    int index = 0;
    while(index < stringLiterals.size()) {
      try {
        stringLiterals.get(index).prettyPrint();
        Main.log.prettyWrite(": ");
        expressions.get(index).prettyPrint();
        index++;
      } catch(ArrayIndexOutOfBoundsException e) {}
    }
    Main.log.prettyWrite("}");
  }
}

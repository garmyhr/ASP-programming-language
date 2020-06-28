package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspInnerExpr extends AspAtom {

  AspExpr test;

  AspInnerExpr(int n) {
    super(n);
  }

  static AspInnerExpr parse(Scanner s) {
    enterParser("inner expr");
    AspInnerExpr aie = new AspInnerExpr(s.curLineNum());

    skip(s, leftParToken); aie.test = AspExpr.parse(s);

    test(s, rightParToken); skip(s, rightParToken);
    leaveParser("inner expr");
    return aie;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return test.eval(curScope);
  }

  @Override
  void prettyPrint() {
    Main.log.prettyWrite("(");
    test.prettyPrint();
    Main.log.prettyWrite(")");
  }
}

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExprStmt extends AspStmt {
  AspExpr test;

  AspExprStmt(int n) {
    super(n);
  }

  static AspExprStmt parse(Scanner s) {
    enterParser("expr stmt");

    AspExprStmt aes = new AspExprStmt(s.curLineNum());
    aes.test = AspExpr.parse(s);
    test(s, newLineToken); skip(s, newLineToken);

    leaveParser("expr stmt");
    return aes;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    trace("expr stmt");
    RuntimeValue v = test.eval(curScope);
    return v;
  }

  @Override
  void prettyPrint() {
    test.prettyPrint();
    Main.log.prettyWriteLn();
  }
}

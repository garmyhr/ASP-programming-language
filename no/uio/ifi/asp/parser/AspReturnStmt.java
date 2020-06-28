package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspReturnStmt extends AspStmt {
  AspExpr retVal;

  AspReturnStmt(int n) {
    super(n);
  }

  static AspReturnStmt parse(Scanner s) {
    enterParser("return stmt");

    AspReturnStmt ars = new AspReturnStmt(s.curLineNum());
    skip(s, returnToken); ars.retVal = AspExpr.parse(s);
    test(s, newLineToken); skip(s, newLineToken);

    leaveParser("return stmt");
    return ars;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = retVal.eval(curScope);
    trace("return " + v.showInfo());
    throw new RuntimeReturnValue(v, lineNum);
  }


  @Override
  void prettyPrint() {
    Main.log.prettyWrite("return ");
    retVal.prettyPrint();
    Main.log.prettyWriteLn();
  }
}

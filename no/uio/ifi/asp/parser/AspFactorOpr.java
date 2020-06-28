package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorOpr extends AspSyntax {
  TokenKind operator;

  AspFactorOpr(int n) {
    super(n);
  }

  static AspFactorOpr parse(Scanner s) {
    enterParser("factor opr");

    AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
    afo.operator = s.curToken().kind;
    skip(s, s.curToken().kind);

    leaveParser("factor opr");
    return afo;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }

    @Override
  void prettyPrint() {
    Main.log.prettyWrite(operator.toString());
  }
}

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom {
  boolean kind;

  AspBooleanLiteral(int n) {
    super(n);
  }

  static AspBooleanLiteral parse(Scanner s) {
    enterParser("boolean literal");

    AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
    TokenKind k = s.curToken().kind;
    switch(k) {
      case falseToken:
        abl.kind = false; break;
      case trueToken:
        abl.kind = true; break;
    }
    skip(s, s.curToken().kind);

    leaveParser("boolean literal");
    return abl;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return new RuntimeBoolValue(kind);
  }


    @Override
  void prettyPrint() {
    if(kind) Main.log.prettyWrite("True");
    else Main.log.prettyWrite("False");
  }
}

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom {
  Token floatVal;

  AspFloatLiteral(int n) {
    super(n);
  }

  static AspFloatLiteral parse(Scanner s) {
    enterParser("float literal");
    AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());
    afl.floatVal = s.curToken();
    skip(s, floatToken);
    leaveParser("float literal");
    return afl;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return new RuntimeFloatValue(floatVal.floatLit);
  }

  @Override
  void prettyPrint() {
    String s = "";
    s += floatVal.floatLit;
    Main.log.prettyWrite(s);
  }
}

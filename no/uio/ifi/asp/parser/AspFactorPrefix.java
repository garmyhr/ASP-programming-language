package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorPrefix extends AspSyntax {
  TokenKind prefix;

  AspFactorPrefix(int n) {
    super(n);
  }

  static AspFactorPrefix parse(Scanner s) {
    enterParser("factor prefix");

    AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
    afp.prefix = s.curToken().kind;
    skip(s, s.curToken().kind);
    leaveParser("factor prefix");

    return afp;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyWrite(prefix.toString());
  }
}

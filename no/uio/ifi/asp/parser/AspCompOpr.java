package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspCompOpr extends AspSyntax {

  TokenKind opr;

  AspCompOpr(int n) {
    super(n);
  }

  static AspCompOpr parse(Scanner s) {
    enterParser("comp opr");

    AspCompOpr aco = new AspCompOpr(s.curLineNum());
	  aco.opr = s.curToken().kind;
    //Safely skip with s.curToken().kind since Token is already tested by AspComparison
    skip(s, s.curToken().kind);

    leaveParser("comp opr");
    return aco;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyWrite(opr.toString());
  }
}

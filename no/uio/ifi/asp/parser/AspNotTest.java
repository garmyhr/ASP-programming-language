package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {

  AspComparison comparison;
  boolean not = false;

  AspNotTest(int n) {
    super(n);
  }

  static AspNotTest parse(Scanner s) {
    enterParser("not test");

    AspNotTest ant = new AspNotTest(s.curLineNum());
    if(s.curToken().kind == notToken) {
      ant.not = true;
      skip(s, notToken);
    }
    ant.comparison = AspComparison.parse(s);

    leaveParser("not test");
    return ant;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = comparison.eval(curScope);
    if(not) v = v.evalNot(comparison);
    return v;
  }

  @Override
  void prettyPrint() {
    if(not) {
      Main.log.prettyWrite("not ");
      comparison.prettyPrint();
    } else {
      comparison.prettyPrint();
    }
  }
}

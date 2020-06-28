package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspWhileStmt extends AspStmt {
  AspExpr test;
  AspSuite body;

  AspWhileStmt(int n) {
    super(n);
  }

  static AspWhileStmt parse(Scanner s) {
    enterParser("while stmt");

    AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
    skip(s, whileToken); aws.test = AspExpr.parse(s);
    skip(s, colonToken); aws.body = AspSuite.parse(s);

    leaveParser("while stmt");
    return aws;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    while(true) {
      RuntimeValue t = test.eval(curScope);
      if(!t.getBoolValue("while loop test", this)) break;
      trace("while True: ...");
      body.eval(curScope);
    }
    trace("while False");
    return null;
  }


  @Override
  void prettyPrint() {
    Main.log.prettyWrite("while ");
    test.prettyPrint();
    Main.log.prettyWrite(":");
    body.prettyPrint();
  }
}

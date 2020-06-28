package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {

  ArrayList<AspStmt> stmts = new ArrayList<>();

  AspSuite(int n) {
    super(n);
  }

  static AspSuite parse(Scanner s) {
    enterParser("suite");
    AspSuite as = new AspSuite(s.curLineNum());

    skip(s, newLineToken);
    skip(s, indentToken);

    while(s.curToken().kind != dedentToken) {
      as.stmts.add(AspStmt.parse(s));
    }

    skip(s, dedentToken);
    leaveParser("suite");
    return as;
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = stmts.get(0).eval(curScope);

    for(int i = 1; i < stmts.size(); i++) {
      v = stmts.get(i).eval(curScope);
    }

    return v;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyWriteLn();
    Main.log.prettyIndent();
    for(AspStmt stmt : stmts) {
      stmt.prettyPrint();
    }
    Main.log.prettyDedent();
  }
}

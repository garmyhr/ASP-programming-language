package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspStmt {

  ArrayList<AspExpr> expressions = new ArrayList<>();
  ArrayList<AspSuite> body = new ArrayList<>();

  AspIfStmt(int n) {
    super(n);
  }

  static AspIfStmt parse(Scanner s) {
    enterParser("if stmt");

    AspIfStmt ais = new AspIfStmt(s.curLineNum());
    skip(s, ifToken);

    while(true) {
    ais.expressions.add(AspExpr.parse(s));
    skip(s, colonToken);
    ais.body.add(AspSuite.parse(s));
      if(s.curToken().kind == elifToken) {
        skip(s, elifToken);
        continue;
      } else break;
    }

    if(s.curToken().kind == elseToken) {
      skip(s, elseToken);
      skip(s, colonToken);
      ais.body.add(AspSuite.parse(s));
    }

    leaveParser("if stmt");
    return ais;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

    for(int i = 0; i < expressions.size(); i++) {
      RuntimeValue v = expressions.get(i).eval(curScope);
      if(v.getBoolValue("if test", this)) {
        trace("if True");
        body.get(i).eval(curScope);
      }
    }
    if(body.size() > expressions.size()) {
      trace("else");
      body.get(body.size()-1).eval(curScope);
    }

    return null;
  }

  @Override
  void prettyPrint() {
    Main.log.prettyWrite("if ");

    int i = 0;
    int j = 0;

    while(i < expressions.size()) {
      if(i > 0) Main.log.prettyWrite("elif ");
      expressions.get(i).prettyPrint();
      Main.log.prettyWrite(":");
      if(j < body.size()) {
        body.get(j).prettyPrint();
        j++;
      }
      i++;
    }

    if(j < body.size()) {
      Main.log.prettyWrite("else");
      Main.log.prettyWrite(":");
      body.get(j).prettyPrint();
    }
  }
}

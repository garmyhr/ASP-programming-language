package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspStmt extends AspSyntax {

  static AspStmt as = null;

  AspStmt(int n) {
    super(n);
  }

  static AspStmt parse(Scanner s) {
    enterParser("stmt");

    switch(s.curToken().kind) {
      case ifToken:
	      as = AspIfStmt.parse(s); break;
	    case whileToken:
	      as = AspWhileStmt.parse(s); break;
	    case returnToken:
        as = AspReturnStmt.parse(s); break;
      case passToken:
        as = AspPassStmt.parse(s); break;
	    case defToken:
        as = AspFuncDef.parse(s); break;
      case forToken:
        as = AspForStmt.parse(s); break;
      case nameToken:
        if(s.anyEqualToken()) {
          as = AspAssignment.parse(s); break;
        } else {
          as = AspExprStmt.parse(s); break;
        }
    }

	leaveParser("stmt");
	return as;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return as.eval(curScope);
  }


    @Override
  void prettyPrint() {
    as.prettyPrint();
  }
}

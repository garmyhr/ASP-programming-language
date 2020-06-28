package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPassStmt extends AspStmt {

  AspPassStmt(int n) {
    super(n);
  }

  static AspPassStmt parse(Scanner s) {
    enterParser("pass stmt");

    AspPassStmt aps = new AspPassStmt(s.curLineNum());
    skip(s, passToken);
    test(s, newLineToken); skip(s, newLineToken);

    leaveParser("pass stmt");
    return aps;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    trace("pass");
    return null;
  }


  @Override
  void prettyPrint() {
    Main.log.prettyWriteLn("pass");
  }
}

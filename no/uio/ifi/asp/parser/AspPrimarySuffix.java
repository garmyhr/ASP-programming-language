package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspPrimarySuffix extends AspSyntax {

  static AspPrimarySuffix aps;

  AspPrimarySuffix(int n) {
    super(n);
  }

  static AspPrimarySuffix parse(Scanner s) {

    enterParser("primary suffix");

    aps = null;
    if(s.curToken().kind == leftParToken) {
      aps = AspArguments.parse(s);
    } else if(s.curToken().kind == leftBracketToken) {
      aps = AspSubscription.parse(s);
    } else {
      parserError("Expected ( or { bur found " + s.curToken().toString(), s.curLineNum());
    }

    leaveParser("primary suffix");
    return aps;
  }

  @Override
  void prettyPrint() {
    aps.prettyPrint();
  }
}

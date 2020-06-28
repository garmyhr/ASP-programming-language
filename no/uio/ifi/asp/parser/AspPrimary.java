package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {

  AspAtom atom;
  ArrayList<AspPrimarySuffix> suffixes = new ArrayList<>();

  AspPrimary(int n) {
    super(n);
  }

  static AspPrimary parse(Scanner s) {
    enterParser("primary");

    AspPrimary ap = new AspPrimary(s.curLineNum());
    ap.atom = AspAtom.parse(s);
    while(true) {
      if(s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken) {
        ap.suffixes.add(AspPrimarySuffix.parse(s));
      } else break;
    }

    leaveParser("primary");
    return ap;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = atom.eval(curScope);

    if(suffixes.isEmpty()) return v;

    if(suffixes.get(0) instanceof AspArguments) {
      RuntimeListValue args = (RuntimeListValue) suffixes.get(0).eval(curScope);
      RuntimeFunc func = (RuntimeFunc) v;
      trace("Call function " + ((AspName) atom).name + " with params " + args.toString());
      v = func.evalFuncCall(args.getValues(), this);
    } else {
      for(AspPrimarySuffix aps : suffixes) {
        if (aps instanceof AspSubscription) {
          RuntimeValue sub = aps.eval(curScope);
          v = v.evalSubscription(sub, this);
        } else RuntimeValue.runtimeError("Subscription error for: " + aps.toString(), this);
      }
    }
    return v;
  }

  @Override
  void prettyPrint() {
    atom.prettyPrint();

    for(AspPrimarySuffix suff : suffixes) {
      suff.prettyPrint();
    }
  }
}

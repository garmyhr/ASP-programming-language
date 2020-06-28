package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspComparison extends AspSyntax {

  ArrayList<AspTerm> terms = new ArrayList<>();
  ArrayList<AspCompOpr> comps = new ArrayList<>();

  AspComparison(int n) {
    super(n);
  }

  static AspComparison parse(Scanner s) {
    enterParser("comparison");

    AspComparison ac = new AspComparison(s.curLineNum());
    ac.terms.add(AspTerm.parse(s));
    while(true) {
      if(s.isCompOpr()) {
        ac.comps.add(AspCompOpr.parse(s));
        ac.terms.add(AspTerm.parse(s));
      } else break;
    }

    leaveParser("comparison");
    return ac;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = terms.get(0).eval(curScope);
	  RuntimeValue result = v;

    for(int i = 1; i < terms.size(); i++) {
      TokenKind k = comps.get(i-1).opr;
	    RuntimeValue nextValue = terms.get(i).eval(curScope);

      switch(k) {
        case lessToken:
          result = v.evalLess(nextValue, this);
		      v = nextValue; break;
        case greaterToken:
          result = v.evalGreater(nextValue, this);
		      v = nextValue; break;
        case doubleEqualToken:
          result = v.evalEqual(nextValue, this);
          v = nextValue; break;
        case greaterEqualToken:
          result = v.evalGreaterEqual(nextValue, this);
		      v = nextValue; break;
        case lessEqualToken:
          result = v.evalLessEqual(nextValue, this);
		      v = nextValue; break;
        case notEqualToken:
          result = v.evalNotEqual(nextValue, this);
		      v = nextValue; break;
        default:
          Main.panic("Illegal comparison operator: " + k);
      }
    }
    return result;
  }

  @Override
  void prettyPrint() {

  int i = 0;
  int j = 0;

  while(i < terms.size()) {
    terms.get(i).prettyPrint();
    if(j < comps.size()) {
      comps.get(j).prettyPrint();
      j++;
    }
    i++;
  }
 }
}

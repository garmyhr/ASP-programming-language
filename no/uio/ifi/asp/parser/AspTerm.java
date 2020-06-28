package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTerm extends AspSyntax {

  ArrayList<AspFactor> factorList = new ArrayList<>();
  ArrayList<AspTermOpr> termOprs = new ArrayList<>();

  AspTerm(int n) {
    super(n);
  }

  static AspTerm parse(Scanner s) {
    enterParser("term");

    AspTerm at = new AspTerm(s.curLineNum());
    while(true) {
      at.factorList.add(AspFactor.parse(s));
      if(s.isTermOpr()) {
        at.termOprs.add(AspTermOpr.parse(s));
      } else break;
    }

    leaveParser("term");
    return at;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = factorList.get(0).eval(curScope);

    for(int i = 1; i < factorList.size(); i++) {
      TokenKind k = termOprs.get(i-1).opr;
      switch(k) {
        case minusToken:
          v = v.evalSubtract(factorList.get(i).eval(curScope), this); break;
        case plusToken:
          v = v.evalAdd(factorList.get(i).eval(curScope), this); break;
        default:
          Main.panic("Illegal term operator: " + k);
      }
    }
    return v;
  }

  @Override
  void prettyPrint() {

   int i = 0;
   int j = 0;

   while(i < factorList.size()) {
     factorList.get(i).prettyPrint();
     if(j < termOprs.size()) {
       termOprs.get(j).prettyPrint();
       j++;
     }
     i++;
    }
  }
}

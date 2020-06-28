package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactor extends AspSyntax {

  ArrayList<AspFactorPrefix> factorPrefix = new ArrayList<>();
  ArrayList<AspFactorOpr> factorOpr = new ArrayList<>();
  ArrayList<AspPrimary> primary = new ArrayList<>();

  AspFactor(int n) {
    super(n);
  }

  static AspFactor parse(Scanner s) {

    enterParser("factor");
    AspFactor af = new AspFactor(s.curLineNum());

    while(true) {
      if(s.isFactorPrefix())  af.factorPrefix.add(AspFactorPrefix.parse(s));
      af.primary.add(AspPrimary.parse(s));
      if(s.isFactorOpr()) af.factorOpr.add(AspFactorOpr.parse(s));
      else break;
    }

    leaveParser("factor");
    return af;
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = primary.get(0).eval(curScope);

    if(!factorPrefix.isEmpty()) {
      TokenKind k = factorPrefix.get(0).prefix;
      switch(k) {
        case plusToken:
          v = v.evalPositive(this); break;
        case minusToken:
          v = v.evalNegate(this); break;
        default:
          Main.panic("Illegal factor prefix: " + k);
      }
    }

    for(int i = 1; i < primary.size(); i++) {
      TokenKind k = factorOpr.get(i-1).operator;
      switch(k) {
        case astToken:
          v = v.evalMultiply(primary.get(i).eval(curScope), this); break;
        case slashToken:
          v = v.evalDivide(primary.get(i).eval(curScope), this); break;
        case percentToken:
          v = v.evalModulo(primary.get(i).eval(curScope), this); break;
        case doubleSlashToken:
          v = v.evalIntDivide(primary.get(i).eval(curScope), this); break;
        default:
          Main.panic("Illegal factor operator: " + k);
      }
    }
    return v;
  }


  @Override
  void prettyPrint() {

    int i = 0;
    int j = 0;
    int k = 0;

    if(factorPrefix.isEmpty() && factorOpr.isEmpty()) {
      primary.get(0).prettyPrint();
    }

    else if(factorPrefix.isEmpty() && !factorOpr.isEmpty()) {
        while(i < primary.size()) {
          primary.get(i).prettyPrint();
          if(j < factorOpr.size()) {
            factorOpr.get(j).prettyPrint();
            j++;
          }
        i++;
      }
    }

    else {
      while(i < primary.size()) {
        if(k < factorPrefix.size()) {
          factorPrefix.get(k).prettyPrint();
          k++;
        }
        primary.get(i).prettyPrint();
        if(j < factorOpr.size()) {
          factorOpr.get(j).prettyPrint();
          j++;
        }
      i++;
     }
    }
  }
}

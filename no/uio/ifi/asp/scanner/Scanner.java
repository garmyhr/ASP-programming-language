package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


/*
 *  INF2100 - Prosjektoppgave i programmering
 *  Del 1
 *  @ Gard Myhre - gardsm@student.matnat.uio.no
 */

public class Scanner {
  private String AspFile;
  private LineNumberReader sourceFile = null;
  private ArrayList<Token> curLineTokens = new ArrayList<>();
  private Stack<Integer> indents = new Stack<>();
  private int numIndents = 0;
  private final int tabDist = 4;


  //Constructor for scanner.
  public Scanner(String fileName) {
    AspFile = fileName;
    numIndents = 1;
    indents.push(0);

    try {
      sourceFile = new LineNumberReader(
                   new InputStreamReader(
                   new FileInputStream(fileName),"UTF-8"));
                 } catch (IOException e) {
                   scannerError("Can't read from " + fileName);
                 }
  }

  //Creates error message for scanner and calls Main.error()
  private void scannerError(String message) {
    String m = "Asp scanner error";
    if (curLineNum() > 0) {
      m += " on line " + curLineNum();
      m += ": " + message;
    }
    Main.error(m);
  }

  //Returns first Token in curLineTokens
  //Does not remove element from curLineTokens
  public Token curToken() {
    while (curLineTokens.isEmpty()) {
      readNextLine();
    }
    return curLineTokens.get(0);
  }

  /*
   * Removes first Token in Tokenlist
   *
   * curToken() will now return the next Token in Tokenlist
  */
  public void readNextToken() {
    if(!curLineTokens.isEmpty()) curLineTokens.remove(0);
  }

  //Returns true if curLineTokens contains an equalToken
  public boolean anyEqualToken() {
    for (Token t : curLineTokens) {
      if(t.kind == equalToken) return true;
    }
    return false;
  }

  /**
   * Reads the next line in the sourcefile and creates tokens
   * Tokens are added to curLineTokens
   */
  private void readNextLine() {

    String line = null;

    try {
      line = sourceFile.readLine();
      if(line == null) {
        //To make sure we dedent all the way out before we end
        while(indents.peek() > 0) {
          indents.pop();
          curLineTokens.add(new Token(dedentToken, curLineNum()));
        }
        Token endOfFile = new Token(eofToken, curLineNum());
        curLineTokens.add(endOfFile);
        Main.log.noteToken(endOfFile);
        sourceFile.close();
        sourceFile = null;
        return;
      } else {
        Main.log.noteSourceLine(curLineNum(), line);
      }
    } catch (IOException e) {
      sourceFile = null;
      scannerError("Unspecified I/O error");
    }

    /**
     * Replace all leading tabs with appropriate number of whitespace
     * If line is only whitespace, or starts with a '#' go to next line
     */

    line = expandLeadingTabs(line);
    if(isEmpty(line)) {
      return;
    }

    //Determine indentation
    int curIndent = findIndent(line);

    if(curIndent > indents.peek()) {
      indents.push(curIndent);
      curLineTokens.add(new Token(indentToken, curLineNum()));
    } else {
      while(curIndent < indents.peek()) {
        indents.pop();
        curLineTokens.add(new Token(dedentToken, curLineNum()));
      }
    }

    if(curIndent != indents.peek()) {
      scannerError("Indentation error");
    }
    scanTokens(line);
    for(Token k : curLineTokens) {
      Main.log.noteToken(k);
    }
  }

  private void scanTokens(String line) {
    //Reads line char by char and determines tokens
    char c;
    boolean commentFound = false;

    for(int i = 0; i < line.length(); i++) {
      c = line.charAt(i);
      if(commentFound) break;
      switch(c) {

        //Delimiters
        case '(' :
          curLineTokens.add(new Token(leftParToken, curLineNum()));
          break;

        case ')' :
          curLineTokens.add(new Token(rightParToken, curLineNum()));
          break;

        case '{' :
          curLineTokens.add(new Token(leftBraceToken, curLineNum()));
          break;

        case '}' :
          curLineTokens.add(new Token(rightBraceToken, curLineNum()));
          break;

        case '[' :
          curLineTokens.add(new Token(leftBracketToken, curLineNum()));
          break;

        case ']' :
          curLineTokens.add(new Token(rightBracketToken, curLineNum()));
          break;

        case ':' :
          curLineTokens.add(new Token(colonToken, curLineNum()));
          break;

        case ',' :
          curLineTokens.add(new Token(commaToken, curLineNum()));
          break;

        case '=' :
          if(matchCharacter('=', (i+1), line)) {
            curLineTokens.add(new Token(doubleEqualToken, curLineNum()));
            i++; break;
          } else {
            curLineTokens.add(new Token(equalToken, curLineNum()));
            break;
          }

        case '!' :
          if(matchCharacter('=', (i+1), line)) {
            curLineTokens.add(new Token(notEqualToken, curLineNum()));
            i++; break;
          } else {
            scannerError("Single exclamation mark not allowed in Asp");
            break;
          }

        //Operators
        case '*' :
          curLineTokens.add(new Token(astToken, curLineNum()));
          break;

        case '>' :
          if(matchCharacter('=', (i+1), line)) {
            curLineTokens.add(new Token(greaterEqualToken, curLineNum()));
            i++; break;
          } else {
            curLineTokens.add(new Token(greaterToken, curLineNum()));
            break;
          }

        case '<' :
          if(matchCharacter('=', (i+1), line)) {
            curLineTokens.add(new Token(lessEqualToken, curLineNum()));
            i++; break;
          } else {
            curLineTokens.add(new Token(lessToken, curLineNum()));
            break;
          }

        case '-' :
          curLineTokens.add(new Token(minusToken, curLineNum()));
          break;

        case '+' :
          curLineTokens.add(new Token(plusToken, curLineNum()));
          break;

        case '%' :
          curLineTokens.add(new Token(percentToken, curLineNum()));
          break;

        case '/' :
          if(matchCharacter('/', (i+1), line)) {
            curLineTokens.add(new Token(doubleSlashToken, curLineNum()));
            i++; break;
          } else {
            curLineTokens.add(new Token(slashToken, curLineNum()));
            break;
          }

        //Names and literals
        case '"' :
          Token stringTok = createStringLit(line, i);
          curLineTokens.add(stringTok);
          i += stringTok.stringLit.length() + 1;
          if(i >= line.length()) {
            return;
          }

          break;

        case '\'' :
          Token stringTok1 = createStringLit(line, i);
          curLineTokens.add(stringTok1);
          i += stringTok1.stringLit.length() + 1;
          if(i >= line.length()) {
            return;
          }
          break;

        case '#' :
          commentFound = true;
          break;

        case ' ' :
          break;

        default :
          if(isDigit(c)) {
            Token numLit = createNumberLit(line, i);
            curLineTokens.add(numLit);
            int skip = 0;
            if(numLit.kind == integerToken) {
              skip = Long.toString(numLit.integerLit).length();
              //If single digit token, so we don't skip over char after number
              if(skip == 1) {
                break;
              } else {
                i += skip - 1;
                break;
              }
            } else {
              skip = Double.toString(numLit.floatLit).length()-1;
              i += skip;
              break;
            }
          } else if(isLetterAZ(c)) {

            Token nameTok = createNameToken(line, i);
            curLineTokens.add(nameTok);
            i += nameTok.name.length()-1;
            break;

          } else {
            scannerError("Unexpected char: " + c);
            break;
        }
      }
    }

    if(commentFound && curLineTokens.isEmpty()) {
      return;
    } else {
      curLineTokens.add(new Token(newLineToken, curLineNum()));
      return;
    }
  }

  /**
   * Method for creating name tokens
   *
   * @param line - the current line
   * @param from - index of first letter in the nameToken
   * @return A nameToken. Will return the correct token for reserved words
   *
  **/
  private Token createNameToken(String line, int from) {

    char[] charArray = line.toCharArray();
    int lineIndex = from;

    try {
      while(isLetterAZ(charArray[lineIndex]) || isDigit(charArray[lineIndex])) {
        lineIndex++;
      }
    } catch (IndexOutOfBoundsException e) {
      lineIndex = line.length();
    }

    Token toReturn = new Token(nameToken, curLineNum());
    toReturn.name = new String(charArray, from, (lineIndex-from));
    toReturn.checkResWords();
    return toReturn;
  }

  /**
   * A helper method for creating string literals.
   * Takes the line where '"' or '\'' is found and reads every char to ending quotationmark
   *
   * @param line - the entire line where a stringLit was found
   * @param from - the current character in line.
   *
   * @return Returns a stringToken containing the stringvalue found on line
  **/
  private Token createStringLit(String line, int from) {

    char[] charArray = line.toCharArray();
    char recognize = charArray[from];

    int letterIndex = from+1;

    try {
    while(charArray[letterIndex] != recognize) letterIndex++;
  } catch(IndexOutOfBoundsException e) {
    scannerError("Unterminated string");
  }

    String value = line.substring(from+1, letterIndex);
    Token tokenToReturn = new Token(stringToken, curLineNum());
    tokenToReturn.stringLit = value;
    return tokenToReturn;
  }


  /**
   *  A helper method for creating number literals
   *
   * @param line - the line where numberLit was found
   * @param from - where number is found in string
   *
   * @return a floatToken if the scanned number contains a '.'
   *         else an integerToken
  **/
  private Token createNumberLit(String line, int from) {

    Token intNum = null, floatNum = null;
    char[] charArray = line.toCharArray();
    int lineIndex = from;

    try {
      while(isDigit(charArray[lineIndex])) lineIndex++;
    } catch(IndexOutOfBoundsException e) {
      lineIndex = line.length();
    }

    if(matchCharacter('.', lineIndex, line)) {
      lineIndex++;
      try {
        while(isDigit(charArray[lineIndex])) lineIndex++;
      } catch(IndexOutOfBoundsException e) {
        lineIndex = charArray.length;
      }

      String value = new String(charArray, from, (lineIndex-from));
      floatNum = new Token(floatToken, curLineNum());
      floatNum.floatLit = Double.parseDouble(value);
      return floatNum;

    } else {

      if(lineIndex-from == 0) lineIndex++;
      String value = new String(charArray, from, (lineIndex-from));
      intNum = new Token(integerToken, curLineNum());
      intNum.integerLit = Integer.parseInt(value);
      return intNum;
    }
  }

  //Helper method for mathing two characters together
  private boolean matchCharacter(char expected, int place, String s) {
    if(place >= s.length()-1) return false;
    if(s.charAt(place) != expected) return false;
    return true;
  }

  //Returns true if s only contains whitespace, or if line starts with comment
  public boolean isEmpty(String s) {
    char[] charArray = s.toCharArray();

    for(char c : charArray) {
      if(c != ' ') {
        if(c == '#') {
          return true;
        } else {
          return false;
        }
      }
    }
    return true;
  }

  //Returns the current linenumber
  public int curLineNum() {
    return sourceFile!=null ? sourceFile.getLineNumber() : 0;
  }

  //Returns the number of blank spaces before a non-blank char
  private int findIndent(String s) {
    int indent = 0;

    while(indent < s.length() && s.charAt(indent) == ' ') indent++;
    return indent;
  }


  //Replaces all leading '\t' with blanks to a power of tabDist
  private String expandLeadingTabs(String s) {
    String newS = "";
    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (c == '\t') {

        do { newS += " ";
        } while (newS.length()%tabDist != 0);

      } else if (c == ' ') {
        newS += " ";
      } else {
        newS += s.substring(i);
        break;
      }
    }
    return newS;
  }

  //Returns true if char c is a letter
  private boolean isLetterAZ(char c) {
    return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
  }

  //Returns true if char c is a digit
  private boolean isDigit(char c) {
    return '0'<=c && c<='9';
  }

  public boolean isCompOpr() {
    TokenKind k = curToken().kind;
    if(k == lessToken ||
       k == greaterToken ||
       k == doubleEqualToken ||
       k == greaterEqualToken ||
       k == lessEqualToken ||
       k == notEqualToken) {
         return true;
    } else return false;
  }


  public boolean isFactorPrefix() {
    TokenKind k = curToken().kind;
    if(k == plusToken || k == minusToken) return true;
    else return false;
  }


  public boolean isFactorOpr() {
    TokenKind k = curToken().kind;
    if(k == astToken ||
       k == slashToken ||
       k == percentToken ||
       k == doubleSlashToken) {
         return true;
       } else return false;
  }


  public boolean isTermOpr() {
    TokenKind k = curToken().kind;
    if(k == plusToken || k == minusToken) return true;
    else return false;
  }
}

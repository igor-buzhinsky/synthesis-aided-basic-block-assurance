grammar ltl;

options {
  language = Java;
}

@header {
package basic_block_generator.generated;

import java.util.*;
import basic_block_generator.formula.*;
}

@parser::members {
}

unary_operator_sign returns[String value]
    : NOT { $value = "!"; }
    | NEXT { $value = "X"; }
    | GLOBALLY { $value = "G"; }
    | FUTURE { $value = "F"; }
    ;

binary_function_sign returns[String value]
    : UNTIL { $value = "U"; }
    | RELEASE { $value = "R"; }
    | WEAK_UNTIL { $value = "W"; }
    ;

constant returns[String value]
    : INT_CONST { $value = $INT_CONST.text; }
    | TRUE { $value = "TRUE"; }
    | FALSE  { $value = "FALSE"; }
    ;

proposition returns[LTLFormula f]
    : { String c = "TRUE"; boolean notEquals = false; }
      ID ((EQUALS | (NOT_EQUALS { notEquals = true; })) constant { c = $constant.value; })?
      { LTLFormula tmp = new Proposition($ID.text, c); $f = notEquals ? new UnaryOperator("!", tmp) : tmp; }
    ;

binary_function returns[LTLFormula f]
    : binary_function_sign '(' f1=formula ',' f2=formula ')'
      { $f = new BinaryFunction($binary_function_sign.value, $f1.f, $f2.f); }
    ;

atom returns[LTLFormula f]
    : proposition { $f = $proposition.f; }
    | binary_function { $f = $binary_function.f; }
    | '(' formula ')' { $f = $formula.f; }
    ;

unary_operator returns[LTLFormula f]
    : unary_operator_sign unary_operator { $f = new UnaryOperator($unary_operator_sign.value, $unary_operator.f); }
    | atom { $f = $atom.f; }
    ;

priority1_binary_operator returns[LTLFormula f]
    : f1=unary_operator { $f = $f1.f; }
      (AND f2=unary_operator { $f = new BinaryOperator("&", $f, $f2.f); })*
    ;

priority2_binary_operator returns[LTLFormula f]
    : f1=priority1_binary_operator { $f = $f1.f; }
      (OR f2=priority1_binary_operator { $f = new BinaryOperator("|", $f, $f2.f); })*
    ;

priority3_binary_operator returns[LTLFormula f]
    : f1=priority2_binary_operator { List<LTLFormula> formulas = new ArrayList<>(); formulas.add($f1.f); }
      (IMPLIES f2=priority2_binary_operator { formulas.add($f2.f); })*
      {
        $f = formulas.get(formulas.size() - 1);
        for (int i = formulas.size() - 2; i >= 0; i--) {
            $f = new BinaryOperator("->", formulas.get(i), $f);
        }
      }
    ;

priority4_binary_operator returns[LTLFormula f]
    : f1=priority3_binary_operator { $f = $f1.f; }
      (EQUIVALENT f2=priority3_binary_operator { $f = new BinaryOperator("<->", $f, $f2.f); })*
    ;

formula returns[LTLFormula f]
    : priority4_binary_operator { $f = $priority4_binary_operator.f; }
    ;

// operator sequences
WS : (' ' | '\t' | ('\r'? '\n'))+ -> channel(HIDDEN);

// keywords
GLOBALLY : 'G'; FUTURE : 'F'; UNTIL : 'U'; NEXT : 'X'; RELEASE : 'R'; WEAK_UNTIL : 'W';

// operators
AND : ('&&' | '&' | 'and' | 'AND');
OR : ('||' | '|' | 'or' | 'OR');
IMPLIES : ('->' | 'imply' | 'implies' | 'IMPLY' | 'IMPLIES');
NOT : ('!' | '~' | 'not' | 'NOT');
EQUIVALENT : '<->';

EQUALS : ('=' | '==');
NOT_EQUALS : '!=';

// constants
TRUE : ('TRUE' | 'true');
FALSE : ('FALSE' | 'false');
INT_CONST : '-'? ('0' | ('1'..'9' ('0'..'9')*));

// ids
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;
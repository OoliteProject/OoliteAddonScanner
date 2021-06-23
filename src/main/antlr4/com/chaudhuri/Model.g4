/** Optional javadoc style comment */
grammar Model;

options {
}

// import ;

tokens {
//    LP, RP
}

/* lexer grammars only
channels { // lexer only
    WHITESPACE_SHANNEL,
    COMMENTS_CHANNEL
}
*/

@actionName {
}

// Lexer rules
//INTEGER
//   : '0'..'9'+
//   ;
WS:                       Ws+             -> skip;
NUMBER
   : MINUS? '0'..'9'+ ('.' '0'..'9'*)? ('e' (PLUS | MINUS) '0'..'9'+)?
   | MINUS? '.' '0'..'9'+
   ;
STRING
   : '"' (~["] | '\\"')* '"' { setText(getText().substring(1, getText().length()-1)); }
   | NameChar ~[= , ; \p{White_Space}]*
   ;

// LP:                       '(';
// RP:                       ')';
LBRACE:                   '{';
RBRACE:                   '}';
// LBRACK:                   '[';
// RBRACK:                   ']';
SEMI:                     ';';
COMMA:                    ',';
//DOT:                      '.';
//STRUCTACCESS:             '->';
//AT:                       '@';
EQUALS:                     '=';
COLON:                    ':';
PLUS:                    '+';
MINUS:                    '-';
//NEWLINE:                  '\r'? '\n' ;

LINECOMMENT:              '//' ~[\r\n]*   -> skip;  // see also https://stackoverflow.com/questions/23976617/parsing-single-line-comments
LINECOMMENT2:             '#' ~[\r\n]*   -> skip;  // see also https://stackoverflow.com/questions/23976617/parsing-single-line-comments
BLOCKCOMMENT:              '/*' (BLOCKCOMMENT | ('*' ~'/') | ~'*')* '*/' -> skip;

fragment Ws: [ \r\n\t\u000C|];
fragment NameChar
   : NameStartChar
   | '0'..'9'
   | '_'
   | '\u00B7'
   | '\u0300'..'\u036F'
   | '\u203F'..'\u2040'
   | '(' | ')'
   ;
fragment NameStartChar
   : 'A'..'Z' | 'a'..'z'
   | '\u00C0'..'\u00D6'
   | '\u00D8'..'\u00F6'
   | '\u00F8'..'\u02FF'
   | '\u0370'..'\u037D'
   | '\u037F'..'\u1FFF'
   | '\u200C'..'\u200D'
   | '\u2070'..'\u218F'
   | '\u2C00'..'\u2FEF'
   | '\u3001'..'\uD7FF'
   | '\uF900'..'\uFDCF'
   | '\uFDF0'..'\uFFFD'
   ;

// parser rules
model: header vertexes faces textures names? normals? end;

header: nverts nfaces;
nverts: 'NVERTS' NUMBER;
nfaces: 'NFACES' NUMBER;

vertexes: 'VERTEX' vertex vertex vertex vertex*;
vertex: NUMBER COMMA? NUMBER COMMA? NUMBER;

faces: 'FACES' face+;
face: number COMMA? number COMMA? number COMMA? number COMMA? number COMMA? number COMMA? number COMMA? number COMMA? number COMMA? number;

textures: 'TEXTURES' texture+;
texture: (string | number) number number number number number number number number;

names: 'NAMES' number name+;
name: string;

normals: 'NORMALS' normal+;
normal: number number number;

end: 'END';

string: STRING;
number: NUMBER;

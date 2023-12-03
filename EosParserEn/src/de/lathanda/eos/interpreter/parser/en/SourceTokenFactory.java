package de.lathanda.eos.interpreter.parser.en;

import static de.lathanda.eos.interpreter.parser.en.ParserConstants.*;

import de.lathanda.eos.interpreter.parser.core.Format;
import de.lathanda.eos.interpreter.parser.core.SourceToken;

public class SourceTokenFactory {
	public static SourceToken create(Token t) {
		return new SourceToken(
			t.beginColumn,
			t.endColumn - t.beginColumn + 1,
			t.image,
			getTokenKind(t),
			t.kind == EOF
		);
	}
	private static Format getTokenKind(Token t) {
        switch (t.kind) {
        case EOF:
            return Format.IGNORE;
        case IMPORT:
        case END_IMPORT:
        case PROGRAM:
        case END_PROGRAM:
        case PROCEDURE:
        case END_PROCEDURE:
        case METHOD:
        case END_METHOD:
        case REPEAT:
        case TIMES:
        case FOREVER:
        case UNTIL:
        case END_REPEAT:
        case IF:
        case THEN:
        case ELSE:
        case END_IF:
        case WITH:
        case END_WITH:
        case WHILE:
        case DO:
        case END_WHILE:
        case RESULT:
        case NOT:
        case OR:
        case AND:
        case BREAKPOINT:
        case STOP:
        case CLASS:
        case END_CLASS:
        case EXTENDS:
        	return Format.KEYWORD;
        case TRUE:
        case FALSE:
        case YELLOW:
        case RED:
        case GREEN:
        case BLUE:
        case WHITE:
        case BLACK:
        case BROWN:
        case LIGHT_BLUE:
        case LIGHT_GREEN:
        case GRAY:
        case LIGHT_GRAY:
        case DASHED:
        case SOLID:
        case DOTTED:
        case DASHED_DOTTED:
        case INVISIBLE:
        case FILLED:
        case SHADED:
        case DARK_SHADED:
        case TRANSPARENT:
        case CENTER:
        case LEFT:
        case RIGHT:
        case TOP:
        case BOTTOM:                
        case INTEGER_LITERAL:
        case DECIMAL_LITERAL:
        case FLOATING_POINT_LITERAL:
        case EXPONENT:
        case STRING_LITERAL1:
        case STRING_LITERAL2:
        case COLOR_LITERAL_RGB:
        case COLOR_LITERAL_RGBA:
        	return Format.LITERAL;
        case SINGLE_LINE_COMMENT:
        case MULTI_LINE_COMMENT:
        case MULTI_LINE_COMMENT2:
        	return Format.COMMENT;
        default:
        	return Format.PLAIN;
        }
	}	
}

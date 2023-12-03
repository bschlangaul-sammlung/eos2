package de.lathanda.eos.parser.base;

import de.lathanda.eos.parser.core.Expression;

public final class ExpressionFactory {
	private ExpressionFactory() {}
	
	public static Expression multiply(Expression a, Expression b) {
		return new Product(a, b);
	}

	public static Expression divide(Expression a, Expression b) {
		return new Division(a, b);
	}

	public static Expression plus(Expression a, Expression b) {
		return new Sum(a, b);
	}

	public static Expression minus(Expression a, Expression b) {
		return new Difference(a, b);
	}

	public static Expression pow(Expression a, Expression b) {
		return new Power(a, b);
	}

	public static Expression not() {
		return new LogicalNot(a);
	}

	public static Expression negate(Expression a) {
		return new Negative(a);
	}

	public static Expression or(Expression a, Expression b) {
		return new LogicalOr(a, b);
	}

	public static Expression and(Expression a, Expression b) {
		return new LogicalAnd(a, b);
	}

	public static Expression equals(Expression a, Expression b) {
		return new IsEqual(a, b);
	}

	public static Expression unequals(Expression a, Expression b) {
		return new IsNotEqual(a, b);
	}

	public static Expression less(Expression a, Expression b) {
		return new IsLessThan(a, b);
	}

	public static Expression lessEquals(Expression a, Expression b) {
		return new IsLessEquals(a, b);
	}

	public static Expression greater(Expression a, Expression b) {
		return new IsGreaterThan(a, b);
	}

	public static Expression greaterEquals(Expression a, Expression b) {
		return new IsGreaterEquals(a, b);
	}
	
	

}

/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
*******************************************************************************/
package org.rascalmpl.library;

//This code was generated by Rascal API gen
import java.util.ArrayList;
import java.util.Iterator;

import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.imp.pdb.facts.*;

@SuppressWarnings("deprecation")
public class LinearProgramming {
	
	private final IValueFactory values;

	
	public LinearProgramming(IValueFactory values){
		super();
		this.values = values;
	}
	
	public static TypeStore typestore = new TypeStore(
			org.rascalmpl.values.errors.Factory.getStore(),
			org.rascalmpl.values.locations.Factory.getStore());

	private static TypeFactory tf = TypeFactory.getInstance();
	private static RascalTypeFactory rtf = RascalTypeFactory.getInstance();

	public static final Type ConstraintType = tf.abstractDataType(typestore,
			"ConstraintType");

	public static final Type ConstraintType_lessOrEqual = tf.constructor(
			typestore, ConstraintType, "lessOrEqual");
	public static final Type ConstraintType_equal = tf.constructor(typestore,
			ConstraintType, "equal");
	public static final Type ConstraintType_greaterOrEqual = tf.constructor(
			typestore, ConstraintType, "greaterOrEqual");

	public static final Type LinearEntry = tf.abstractDataType(typestore,
			"LinearEntry");

	public static final Type LinearEntry_coefficients = tf.constructor(
			typestore, LinearEntry, "coefficients", tf.listType(tf.realType()),
			"c");
	public static final Type LinearEntry_constant = tf.constructor(typestore,
			LinearEntry, "constant", tf.realType(), "con");

	public static final Type Solution = tf.abstractDataType(typestore,
			"Solution");

	public static final Type Solution_noSolFound = tf.constructor(typestore,
			Solution, "noSolFound");
	public static final Type Solution_sol = tf.constructor(typestore, Solution,
			"sol", tf.listType(tf.realType()), "sollution", tf.realType(),
			"fVal");

	public static final Type LinearObjectiveFunction = tf.abstractDataType(
			typestore, "LinearObjectiveFunction");

	public static final Type LinearObjectiveFunction_linearObjFun = tf
			.constructor(typestore, LinearObjectiveFunction, "linearObjFun",
					tf.listType(tf.realType()), "coefficients", tf.realType(),
					"constant");

	public static final Type Constraint = tf.abstractDataType(typestore,
			"Constraint");

	public static final Type Constraint_constraint = tf.constructor(typestore,
			Constraint, "constraint", LinearEntry, "lhs", ConstraintType,
			"conType", LinearEntry, "rhs");

	public static final Type GoalType = tf.abstractDataType(typestore,
			"GoalType");

	public static final Type GoalType_minimize = tf.constructor(typestore,
			GoalType, "minimize");
	public static final Type GoalType_maximize = tf.constructor(typestore,
			GoalType, "maximize");

	public static final Type LinearProblem = tf.abstractDataType(typestore,
			"LinearProblem");

	public static final Type LinearProblem_linProblem = tf.constructor(
			typestore, LinearProblem, "linProblem", GoalType, "goal",
			LinearObjectiveFunction, "f", tf.setType(Constraint),
			"constraints", tf.boolType(), "nonZero");

	public static IList LinearEntry_coefficients_c(IConstructor c) {
		return (IList) c.get(0);
	}

	public static double LinearEntry_constant_con(IConstructor c) {
		return ((IReal) c.get(0)).doubleValue();
	}

	public static IList Solution_sol_sollution(IConstructor c) {
		return (IList) c.get(0);
	}

	public static double Solution_sol_fVal(IConstructor c) {
		return ((IReal) c.get(1)).doubleValue();
	}

	public static IList LinearObjectiveFunction_linearObjFun_coefficients(
			IConstructor c) {
		return (IList) c.get(0);
	}

	public static double LinearObjectiveFunction_linearObjFun_constant(
			IConstructor c) {
		return ((IReal) c.get(1)).doubleValue();
	}

	public static IValue Constraint_constraint_lhs(IConstructor c) {
		return (IValue) c.get(0);
	}

	public static IValue Constraint_constraint_conType(IConstructor c) {
		return (IValue) c.get(1);
	}

	public static IValue Constraint_constraint_rhs(IConstructor c) {
		return (IValue) c.get(2);
	}

	public static IValue LinearProblem_linProblem_goal(IConstructor c) {
		return (IValue) c.get(0);
	}

	public static IValue LinearProblem_linProblem_f(IConstructor c) {
		return (IValue) c.get(1);
	}

	public static ISet LinearProblem_linProblem_constraints(IConstructor c) {
		return (ISet) c.get(2);
	}

	public static boolean LinearProblem_linProblem_nonZero(IConstructor c) {
		return ((IBool) c.get(3)).getValue();
	}



	public static TypeStore getStore() {
		return typestore;
	}

	// begin handwritten code

	private static double[] convertRealList(IList l) {
		double[] elems = new double[l.length()];
		for (int i = 0; i < l.length(); i++) {
			elems[i] = ((IReal) l.get(i)).doubleValue();
		}
		return elems;
	}

	private static IList convertToRealList(double[] l, IValueFactory vf) {
		IListWriter writer = vf.listWriter(tf.realType());
		for (int i = 0; i < l.length; i++) {
			writer.append(vf.real(l[i]));
		}
		return writer.done();
	}

	private static org.apache.commons.math.optimization.linear.LinearObjectiveFunction 
		convertLinObjFun(
			IConstructor c) {
		IList l = LinearObjectiveFunction_linearObjFun_coefficients(c);
		double[] coefficients = convertRealList(l);
		double constant = LinearObjectiveFunction_linearObjFun_constant(c);
		return new org.apache.commons.math.optimization.linear.LinearObjectiveFunction
				(coefficients, constant);
	}

	private static Relationship convertConstraintType(IConstructor c) {
		if (c.getConstructorType() == ConstraintType_lessOrEqual) {
			return Relationship.LEQ;
		} else if (c.getConstructorType() == ConstraintType_equal) {
			return Relationship.EQ;
		} else if (c.getConstructorType() == ConstraintType_greaterOrEqual) {
			return Relationship.GEQ;
		} else {
			throw new Error(
				"Cannot happen: Unkown constraint type for linear programming");
		}
	}

	private static org.apache.commons.math.optimization.GoalType convertGoalType(
			IConstructor c) {
		if (c.getConstructorType() == GoalType_maximize) {
			return org.apache.commons.math.optimization.GoalType.MAXIMIZE;
		} else if (c.getConstructorType() == GoalType_minimize) {
			return org.apache.commons.math.optimization.GoalType.MINIMIZE;
		} else {
			throw new Error(
					"Cannot happen: Unkown goal type for linear programming");
		}
	}

	private static LinearConstraint convertConstraint(IConstructor c, int nrVars) {
		double[] coeffientsLhs = new double[nrVars];
		double[] coeffientsRhs = coeffientsLhs;
		for(int i = 0 ; i < nrVars ; i++){
			coeffientsLhs[i] = 0.0;
		}
		double constLhs = 0;
		double constRhs = 0;
		IConstructor lhs = (IConstructor) Constraint_constraint_lhs(c);
		if (lhs.getConstructorType() == LinearEntry_coefficients) {
			coeffientsLhs = convertRealList(LinearEntry_coefficients_c(lhs));
		} else {
			constLhs = LinearEntry_constant_con(lhs);
		}
		IConstructor rhs = (IConstructor) Constraint_constraint_rhs(c);
		if (rhs.getConstructorType() == LinearEntry_coefficients) {
			coeffientsRhs = convertRealList(LinearEntry_coefficients_c(rhs));
		} else {
			constRhs = LinearEntry_constant_con(rhs);
		}
		Relationship r = convertConstraintType(
				(IConstructor) Constraint_constraint_conType(c));
		System.out.printf("Making constraint [");
		for(double d : coeffientsLhs){
			System.out.printf("%f ", d);
		}
		System.out.printf("],%f  ",constLhs);
		System.out.printf("Making constraint [");
		for(double d : coeffientsRhs){
			System.out.printf("%f ", d);
		}
		System.out.printf("],%f \n",constRhs);
		
		return new LinearConstraint(coeffientsLhs, constLhs, r, coeffientsRhs,
				constRhs);
	}

	public IValue optimizeR( IInteger nrVars,IConstructor c) {
		SimplexSolver solver = new SimplexSolver();
		ISet rconstraints = LinearProblem_linProblem_constraints(c);
		ArrayList<LinearConstraint> constraints = new ArrayList<LinearConstraint>(
				rconstraints.size());
		for (Iterator<IValue> i = rconstraints.iterator(); i.hasNext();) {
			constraints.add(convertConstraint((IConstructor) i.next(),nrVars.intValue()));
		}
		org.apache.commons.math.optimization.linear.LinearObjectiveFunction fun =
				convertLinObjFun((IConstructor) LinearProblem_linProblem_f(c));
		org.apache.commons.math.optimization.GoalType goal = 
				convertGoalType((IConstructor) LinearProblem_linProblem_goal(c));
		IValueFactory vf = values;
		boolean nonZero = LinearProblem_linProblem_nonZero(c);
		try {
			RealPointValuePair res = solver.optimize(fun, constraints, goal,
					nonZero);
			return vf.constructor(Solution_sol, 
					convertToRealList(res.getPoint(), vf),
					vf.real(res.getValue()));
		} catch (Exception e) {
			return vf.constructor(Solution_noSolFound);
		}

	}
}
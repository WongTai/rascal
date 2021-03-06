package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Apply extends Instruction {
	
	final String fuid;
	final int arity;
	
	public Apply(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.APPLY);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "APPLY " + fuid + ", " + arity + " [ " + codeblock.getFunctionIndex(fuid) + " ]"; }
	
	public void generate(){
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), arity);
	}

}

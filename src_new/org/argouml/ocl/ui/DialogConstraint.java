package org.argouml.ocl.ui;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;

public class DialogConstraint extends JDialog {

    ArgoConstraintEvaluation ace;

    public DialogConstraint(MModelElement me, JFrame parentFrame) {
      super(parentFrame, true);
      setTitle("Enter new OCL constraint");
      ace=new ArgoConstraintEvaluation(this, me);
      ace.setConstraint(me.getName(), "");
      getContentPane().add(ace);
      pack();
    }

    public DialogConstraint(MModelElement me, MConstraint cs, JFrame parentFrame) {
      super(parentFrame, true);
      setTitle("Enter new OCL constraint");
      ace=new ArgoConstraintEvaluation(this, me);
      ace.setConstraint(me.getName(), cs.getBody().getBody());
      getContentPane().add(ace);
      pack();
    }

    public String getResultingExpression() {
      return ace.getResultConstraint();
    }

	public String getConstraintName() {
	  return ace.getConstraintName();
    }

}

